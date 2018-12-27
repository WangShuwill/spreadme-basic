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

package club.spreadme.database.core.resultset.support;

import club.spreadme.database.core.resultset.ResultSetParser;
import club.spreadme.database.core.resultset.RowMapper;
import club.spreadme.database.core.statement.WrappedStatement;
import club.spreadme.database.exception.DataBaseAccessException;
import club.spreadme.database.util.JdbcUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamResultSetParser<T> implements Spliterator<T>, ResultSetParser<Stream<T>> {

    private DataSource dataSource;
    private Connection connection;
    private WrappedStatement wrappedStatement;
    private ResultSet resultSet;

    private int splitSize = 128;

    private RowMapper<T> rowMapper;

    public StreamResultSetParser(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public Stream<T> parse(ResultSet resultSet) {
        this.resultSet = resultSet;
        return StreamSupport.stream(this, true).onClose(closeHandle());
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        closeReource();
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        int count = 0;
        Object[] items = new Object[splitSize];
        try {
            if (!resultSet.next()) {
                closeReource();
                return null;
            }
            do {
                items[count] = rowMapper.mapping(resultSet);
                count++;
            } while (count < splitSize && resultSet.next());

        } catch (Exception ex) {
            closeReource();
            throw new DataBaseAccessException(ex.getMessage());
        }
        return Spliterators.spliterator(items, count);
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return ORDERED;
    }

    public void nest(DataSource dataSource, Connection connection, WrappedStatement wrappedStatement) {
        this.dataSource = dataSource;
        this.connection = connection;
        this.wrappedStatement = wrappedStatement;
    }

    public int getSplitSize() {
        return splitSize;
    }

    public void setSplitSize(int splitSize) {
        this.splitSize = splitSize;
    }

    private Runnable closeHandle() {
        return this::closeReource;
    }

    private void closeReource() {
        JdbcUtil.closeResultSet(resultSet);
        JdbcUtil.closeWrappedStatement(wrappedStatement);
        JdbcUtil.closeConnection(connection, dataSource);
    }
}
