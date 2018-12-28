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

package club.spreadme.database.dao;

import club.spreadme.database.core.executor.Executor;
import club.spreadme.database.core.executor.support.SimplExecutor;
import club.spreadme.database.core.executor.support.StreamExecutor;
import club.spreadme.database.core.grammar.Record;
import club.spreadme.database.core.resultset.ResultSetParser;
import club.spreadme.database.core.resultset.RowMapper;
import club.spreadme.database.core.resultset.support.BeanRowMapper;
import club.spreadme.database.core.resultset.support.DefaultResultSetParser;
import club.spreadme.database.core.resultset.support.RecordRowMapper;
import club.spreadme.database.core.resultset.support.StreamResultSetParser;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.StatementCallback;
import club.spreadme.database.core.statement.support.*;
import club.spreadme.database.metadata.ConcurMode;
import club.spreadme.lang.Assert;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class CommonDao {

    private DataSource dataSource;
    private Executor executor;

    private volatile static CommonDao commonDao;

    private CommonDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.executor = new SimplExecutor(dataSource);
    }

    public static CommonDao getInstance(DataSource dataSource) {
        if (commonDao == null) {
            synchronized (CommonDao.class) {
                if (commonDao == null) {
                    commonDao = new CommonDao(dataSource);
                }
            }
        }
        return commonDao;
    }

    public <T> T queryOne(String sql, Class<T> clazz) {
        return queryOne(sql, new BeanRowMapper<>(clazz));
    }

    public <T> T queryOne(String sql, Class<T> clazz, Object... objects) {
        return queryOne(sql, new BeanRowMapper<>(clazz), objects);
    }

    public <T> Record queryOne(String sql) {
        return queryOne(sql, new RecordRowMapper());
    }

    public <T> Record queryOne(String sql, Object... objects) {
        return queryOne(sql, new RecordRowMapper(), objects);
    }

    public <T> T queryOne(String sql, RowMapper<T> rowMapper) {
        List<T> results = query(new SimpleStatementBuilder(sql, ConcurMode.READ_ONLY), new DefaultResultSetParser<>(rowMapper, 1));
        return results.iterator().hasNext() ? results.iterator().next() : null;
    }

    public <T> T queryOne(String sql, RowMapper<T> rowMapper, Object... objects) {
        List<T> results = query(new PrepareStatementBuilder(sql, objects, ConcurMode.READ_ONLY), new DefaultResultSetParser<>(rowMapper, 1));
        return results.iterator().hasNext() ? results.iterator().next() : null;
    }

    public <T> List<T> query(String sql, Class<T> clazz) {
        return query(sql, new BeanRowMapper<>(clazz));
    }

    public <T> List<T> query(String sql, Class<T> clazz, Object... objects) {
        return query(sql, new BeanRowMapper<>(clazz), objects);
    }

    public <T> List<Record> query(String sql) {
        return query(sql, new RecordRowMapper());
    }

    public <T> List<Record> query(String sql, Object... objects) {
        return query(sql, new RecordRowMapper(), objects);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return query(new SimpleStatementBuilder(sql, ConcurMode.READ_ONLY), rowMapper);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... objects) {
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

    public int update(String sql) {
        return executor.execute(new SimpleStatementBuilder(sql, ConcurMode.UPDATABLE), new UpdateStatementCallback());
    }

    public int update(String sql, Object... objects) {
        return executor.execute(new PrepareStatementBuilder(sql, objects, ConcurMode.UPDATABLE), new UpdateStatementCallback());
    }

    public StreamDao withStream() {
        return new StreamDao(dataSource);
    }

    public static class StreamDao {

        private DataSource dataSource;
        private Executor executor;

        public StreamDao(DataSource dataSource) {
            this.dataSource = dataSource;
            this.executor = new StreamExecutor(dataSource);
        }

        public <T> Stream<Record> query(String sql, Object... objects) {
            return query(new PrepareStatementBuilder(sql, objects, ConcurMode.READ_ONLY), new RecordRowMapper());
        }

        public <T> Stream<T> query(String sql, Class<T> clazz, Object... objects) {
            return query(new PrepareStatementBuilder(sql, objects, ConcurMode.READ_ONLY), new BeanRowMapper<>(clazz));
        }

        protected <T> Stream<T> query(StatementBuilder builder, RowMapper<T> rowMapper) {
            return query(builder, new StreamResultSetParser<>(rowMapper));
        }

        protected <T> Stream<T> query(StatementBuilder builder, StreamResultSetParser<T> resultSetParser) {
            return executor.execute(builder, new StreamQueryStatementCallback<>(resultSetParser));
        }
    }

    public AsyncDao withAsync() {
        return new AsyncDao();
    }

    public static class AsyncDao {

        public Future<List<Record>> query(String sql, Object... objects) {
            return query(new PrepareStatementBuilder(sql, objects, ConcurMode.READ_ONLY), new RecordRowMapper());
        }

        protected <T> Future<List<T>> query(StatementBuilder builder, RowMapper<T> rowMapper) {
            return query(builder, new QueryStatementCallback<>(new DefaultResultSetParser<>(rowMapper)));
        }

        protected <T> Future<T> query(StatementBuilder builder, StatementCallback<T> action) {
            return commonDao.executor.execute(builder, new AsyncStatementCallBack<>(Executors.newCachedThreadPool(), action));
        }

    }
}
