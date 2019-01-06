/*
 *  Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.spreadme.database.dao.support;

import club.spreadme.database.core.executor.Executor;
import club.spreadme.database.core.executor.support.StreamExecutor;
import club.spreadme.database.core.grammar.Record;
import club.spreadme.database.core.grammar.StatementConfig;
import club.spreadme.database.core.resultset.RowMapper;
import club.spreadme.database.core.resultset.support.BeanRowMapper;
import club.spreadme.database.core.resultset.support.RecordRowMapper;
import club.spreadme.database.core.resultset.support.StreamResultSetParser;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.support.PrepareStatementBuilder;
import club.spreadme.database.core.statement.support.StreamQueryStatementCallback;
import club.spreadme.database.metadata.ConcurMode;
import club.spreadme.database.metadata.FetchDirection;

import javax.sql.DataSource;
import java.util.stream.Stream;

public class StreamDao {

    private Executor executor;

    public StreamDao(DataSource dataSource) {
        this.executor = new StreamExecutor(dataSource);
    }

    public StreamDao fetchSize(int fetchSize) {
        StatementConfig statementConfig = new StatementConfig();
        statementConfig.setFetchDirection(FetchDirection.REVERSE);
        statementConfig.setFetchSize(fetchSize);
        this.executor.setStatementConfig(statementConfig);
        return this;
    }

    public <T> Stream<Record> query(String sql, Object... objects) {
        return query(new PrepareStatementBuilder(sql, objects, ConcurMode.READ_ONLY), new RecordRowMapper());
    }

    public <T> Stream<T> query(String sql, Class<T> clazz, Object... objects) {
        return query(new PrepareStatementBuilder(sql, objects, ConcurMode.READ_ONLY), new BeanRowMapper<>(clazz));
    }

    private <T> Stream<T> query(StatementBuilder builder, RowMapper<T> rowMapper) {
        return query(builder, new StreamResultSetParser<>(rowMapper));
    }

    private <T> Stream<T> query(StatementBuilder builder, StreamResultSetParser<T> resultSetParser) {
        return executor.execute(builder, new StreamQueryStatementCallback<>(resultSetParser));
    }

}
