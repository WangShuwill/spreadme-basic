/*
 *  Copyright (c) 2018 Wangshuwei
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package club.spreadme.database.core.executor.support;

import club.spreadme.database.core.grammar.Record;
import club.spreadme.database.core.resultset.RowMapper;
import club.spreadme.database.core.resultset.support.RecordRowMapper;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.StatementConfig;
import club.spreadme.database.core.statement.WrappedStatement;
import club.spreadme.database.core.statement.support.PrepareStatementBuilder;
import club.spreadme.database.exception.DataBaseAccessException;
import club.spreadme.database.metadata.ConcurMode;
import club.spreadme.database.metadata.FetchMode;
import club.spreadme.database.util.JdbcUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamExecutor {

    private DataSource dataSource;

    public Stream<Record> query(String sql, Object... objects) {
        UncheckedCloseable closeable = null;
        try {
            StatementBuilder statementBuilder = new PrepareStatementBuilder(sql, objects, ConcurMode.READ_ONLY);
            Connection connection = JdbcUtil.getConnection(dataSource);
            StatementConfig config = new StatementConfig();
            config.setFetchSize(Integer.MIN_VALUE);
            config.setFetchDirection(FetchMode.REVERSE);
            WrappedStatement statement = statementBuilder.build(connection, config);

            ResultSet resultSet = statement.query();

            RowMapper<Record> rowMapper = new RecordRowMapper();

            return StreamSupport.stream(new Spliterators.AbstractSpliterator<Record>(Long.MAX_VALUE, Spliterator.ORDERED) {
                @Override
                public boolean tryAdvance(Consumer<? super Record> action) {
                    try {
                        if (!resultSet.next()) {
                            return false;
                        }
                        action.accept(rowMapper.mapping(resultSet));
                        return true;

                    } catch (Exception e) {
                        throw new DataBaseAccessException(e.getMessage());
                    }
                }
            }, false).onClose(closeable);

        } catch (Exception ex) {
            throw new DataBaseAccessException(ex.getMessage());
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    interface UncheckedCloseable extends Runnable, AutoCloseable {

        default void run() {
            try {
                close();
            } catch (Exception ex) {
                throw new DataBaseAccessException(ex.getMessage());
            }
        }

        static UncheckedCloseable wrap(AutoCloseable autoCloseable) {
            return autoCloseable::close;
        }

        default UncheckedCloseable nest(AutoCloseable autoCloseable) {
            return () -> {
                try (UncheckedCloseable uncheckedCloseable = this;) {
                    this.close();
                }
            };
        }
    }
}
