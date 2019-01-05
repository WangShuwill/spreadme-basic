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
import club.spreadme.database.core.executor.support.AsyncExecutor;
import club.spreadme.database.core.grammar.Record;
import club.spreadme.database.core.resultset.RowMapper;
import club.spreadme.database.core.resultset.support.BeanRowMapper;
import club.spreadme.database.core.resultset.support.DefaultResultSetParser;
import club.spreadme.database.core.resultset.support.RecordRowMapper;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.StatementCallback;
import club.spreadme.database.core.statement.support.AsyncStatementCallback;
import club.spreadme.database.core.statement.support.PrepareStatementBuilder;
import club.spreadme.database.core.statement.support.QueryStatementCallback;
import club.spreadme.database.core.statement.support.UpdateStatementCallback;
import club.spreadme.database.metadata.ConcurMode;
import club.spreadme.database.metadata.SQLOptionType;
import club.spreadme.database.parser.SQLParser;
import club.spreadme.database.parser.grammar.SQLStatement;
import club.spreadme.database.parser.support.BeanSQLParser;
import club.spreadme.database.parser.support.RoutingSQLParser;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncDao {

    private Executor executor;

    public AsyncDao(DataSource dataSource) {
        this.executor = new AsyncExecutor(dataSource);
    }

    public <T> CompletableFuture<List<T>> query(String sql, Class<T> clazz, Object... objects) {
        return query(new PrepareStatementBuilder(sql, objects, ConcurMode.READ_ONLY), new BeanRowMapper<>(clazz));
    }

    public CompletableFuture<List<Record>> query(String sql, Object... objects) {
        return query(new PrepareStatementBuilder(sql, objects, ConcurMode.READ_ONLY), new RecordRowMapper());
    }

    private <T> CompletableFuture<List<T>> query(StatementBuilder builder, RowMapper<T> rowMapper) {
        return query(builder, new QueryStatementCallback<>(new DefaultResultSetParser<>(rowMapper)));
    }

    private <T> CompletableFuture<T> query(StatementBuilder builder, StatementCallback<T> action) {
        return this.executor.execute(builder, new AsyncStatementCallback<>(action));
    }

    public CompletableFuture<Integer> execute(String sql, Object... objects) {
        return this.executor.execute(new PrepareStatementBuilder(sql, objects, ConcurMode.UPDATABLE),
                new AsyncStatementCallback<>(new UpdateStatementCallback()));
    }

    public CompletableFuture<Integer> update(Object bean) {
        SQLParser sqlParser = new RoutingSQLParser(new BeanSQLParser(bean, SQLOptionType.UPDATE));
        SQLStatement sqlStatement = sqlParser.parse();
        return execute(sqlStatement.getSql(), sqlStatement.getValues());
    }

    public CompletableFuture<Integer> insert(Object bean) {
        SQLParser sqlParser = new RoutingSQLParser(new BeanSQLParser(bean, SQLOptionType.INSERT));
        SQLStatement sqlStatement = sqlParser.parse();
        return execute(sqlStatement.getSql(), sqlStatement.getValues());
    }

    public CompletableFuture<Integer> delete(Object bean) {
        SQLParser sqlParser = new RoutingSQLParser(new BeanSQLParser(bean, SQLOptionType.DELETE));
        SQLStatement sqlStatement = sqlParser.parse();
        return execute(sqlStatement.getSql(), sqlStatement.getValues());
    }
}
