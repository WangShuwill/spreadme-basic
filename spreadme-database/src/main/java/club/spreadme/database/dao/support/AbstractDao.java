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

import club.spreadme.database.bind.DaoProxyFactory;
import club.spreadme.database.core.executor.Executor;
import club.spreadme.database.core.resultset.ResultSetParser;
import club.spreadme.database.core.resultset.RowMapper;
import club.spreadme.database.core.resultset.support.DefaultResultSetParser;
import club.spreadme.database.core.statement.support.PrepareStatementBuilder;
import club.spreadme.database.core.statement.support.QueryStatementCallback;
import club.spreadme.database.core.statement.support.SimpleStatementBuilder;
import club.spreadme.database.metadata.ConcurMode;
import club.spreadme.lang.Assert;

import javax.sql.DataSource;
import java.util.List;

public abstract class AbstractDao {

    protected DataSource dataSource;
    protected Executor executor;

    public AbstractDao() {

    }

    protected <T> T newInstance(Class<T> clazz) {
        return new DaoProxyFactory<>(clazz).newInstance(executor);
    }

    protected <T> T queryOne(String sql, RowMapper<T> rowMapper) {
        List<T> results = query(new SimpleStatementBuilder(sql, ConcurMode.READ_ONLY), new DefaultResultSetParser<>(rowMapper, 1));
        return results.iterator().hasNext() ? results.iterator().next() : null;
    }

    protected <T> T queryOne(String sql, RowMapper<T> rowMapper, Object... objects) {
        List<T> results = query(new PrepareStatementBuilder(sql, objects, ConcurMode.READ_ONLY), new DefaultResultSetParser<>(rowMapper, 1));
        return results.iterator().hasNext() ? results.iterator().next() : null;
    }

    protected <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return query(new SimpleStatementBuilder(sql, ConcurMode.READ_ONLY), rowMapper);
    }

    protected <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... objects) {
        return query(new PrepareStatementBuilder(sql, objects, ConcurMode.READ_ONLY), rowMapper);
    }

    protected <T> List<T> query(SimpleStatementBuilder builder, final RowMapper<T> rowMapper) {
        Assert.notNull(rowMapper, "RowMapper must not be null");
        return query(builder, new DefaultResultSetParser<>(rowMapper));
    }

    protected <T> List<T> query(PrepareStatementBuilder builder, final RowMapper<T> rowMapper) {
        Assert.notNull(rowMapper, "RowMapper must not be null");
        return query(builder, new DefaultResultSetParser<>(rowMapper));
    }

    protected <T> T query(SimpleStatementBuilder builder, final ResultSetParser<T> parser) {
        Assert.notNull(parser, "ResultSetParser must not be null");
        return executor.execute(builder, new QueryStatementCallback<>(parser));
    }

    protected <T> T query(PrepareStatementBuilder builder, final ResultSetParser<T> parser) {
        Assert.notNull(parser, "ResultSetParser must not be null");
        return executor.execute(builder, new QueryStatementCallback<>(parser));
    }
}
