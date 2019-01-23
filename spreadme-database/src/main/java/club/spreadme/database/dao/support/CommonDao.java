/*
 *  Copyright (c) 2019 Wangshuwei
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

package club.spreadme.database.dao.support;

import club.spreadme.database.bind.DaoProxyFactory;
import club.spreadme.database.core.executor.Executor;
import club.spreadme.database.core.executor.support.CachingExecutor;
import club.spreadme.database.core.executor.support.SimplExecutor;
import club.spreadme.database.core.grammar.Record;
import club.spreadme.database.core.resultset.support.BeanRowMapper;
import club.spreadme.database.core.resultset.support.RecordRowMapper;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.support.PrepareStatementBuilder;
import club.spreadme.database.core.statement.support.SimpleStatementBuilder;
import club.spreadme.database.core.statement.support.UpdateStatementCallback;
import club.spreadme.database.core.transaction.TransactionExecutor;
import club.spreadme.database.dao.ICommonDao;
import club.spreadme.database.metadata.ConcurMode;
import club.spreadme.database.metadata.SQLOptionType;
import club.spreadme.database.parser.SQLParser;
import club.spreadme.database.parser.grammar.SQLStatement;
import club.spreadme.database.parser.support.BeanSQLParser;
import club.spreadme.database.parser.support.RoutingSQLParser;
import club.spreadme.database.plugin.Interceptor;
import club.spreadme.lang.cache.Cache;

import javax.sql.DataSource;
import java.util.List;

/**
 * Common dao for database, you can use stream or async
 */
public class CommonDao extends AbstractDao {

    private TransactionExecutor transactionExecutor;

    private volatile static CommonDao commonDao;

    private CommonDao() {
        super();
    }

    public static CommonDao getInstance() {
        if (commonDao == null) {
            synchronized (CommonDao.class) {
                if (commonDao == null) {
                    commonDao = new CommonDao();
                }
            }
        }
        return commonDao;
    }

    public CommonDao use(DataSource dataSource) {
        this.dataSource = dataSource;
        this.executor = new SimplExecutor(this.dataSource);
        return commonDao;
    }

    public CommonDao use(Executor executor) {
        this.executor = executor;
        return commonDao;
    }

    @Override
    public ICommonDao use(Interceptor[] interceptors) {
        for (Interceptor interceptor : interceptors) {
            INTERCEPTOR_CHAIN.addInterceptor(interceptor);
        }
        return this;
    }

    @Override
    public ICommonDao use(Cache cache) {
        this.executor = new CachingExecutor(this.dataSource, true, cache);
        return this;
    }

    public <T> T getDao(Class<T> clazz) {
        return DaoProxyFactory.get(clazz, this.executor);
    }

    public TransactionExecutor getTransactionExecutor() {
        if (transactionExecutor == null) {
            transactionExecutor = new TransactionExecutor(this.dataSource);
        }
        return transactionExecutor;
    }

    public <T> T queryOne(String sql, Class<T> clazz) {
        return queryOne(sql, new BeanRowMapper<>(clazz));
    }

    public <T> T queryOne(String sql, Class<T> clazz, Object... objects) {
        return queryOne(sql, new BeanRowMapper<>(clazz), objects);
    }

    public Record queryOne(String sql) {
        return queryOne(sql, new RecordRowMapper());
    }

    public Record queryOne(String sql, Object... objects) {
        return queryOne(sql, new RecordRowMapper(), objects);
    }

    public <T> List<T> query(String sql, Class<T> clazz) {
        return query(sql, new BeanRowMapper<>(clazz));
    }

    public <T> List<T> query(String sql, Class<T> clazz, Object... objects) {
        return query(sql, new BeanRowMapper<>(clazz), objects);
    }

    public List<Record> query(String sql) {
        return query(sql, new RecordRowMapper());
    }

    public List<Record> query(String sql, Object... objects) {
        return query(sql, new RecordRowMapper(), objects);
    }

    public int execute(String sql) {
        StatementBuilder builder = getPorxyStatementBuilder(new SimpleStatementBuilder(sql, ConcurMode.UPDATABLE));
        return executor.execute(builder, new UpdateStatementCallback());
    }

    public int execute(String sql, Object... objects) {
        StatementBuilder builder = getPorxyStatementBuilder(new PrepareStatementBuilder(sql, objects, ConcurMode.UPDATABLE));
        return executor.execute(builder, new UpdateStatementCallback());
    }

    public int update(Object bean) {
        SQLParser sqlParser = new RoutingSQLParser(new BeanSQLParser(bean, SQLOptionType.UPDATE));
        SQLStatement sqlStatement = sqlParser.parse();
        return execute(sqlStatement.getSql(), sqlStatement.getValues());
    }

    public int insert(Object bean) {
        SQLParser sqlParser = new RoutingSQLParser(new BeanSQLParser(bean, SQLOptionType.INSERT));
        SQLStatement sqlStatement = sqlParser.parse();
        return execute(sqlStatement.getSql(), sqlStatement.getValues());
    }

    public int delete(Object bean) {
        SQLParser sqlParser = new RoutingSQLParser(new BeanSQLParser(bean, SQLOptionType.DELETE));
        SQLStatement sqlStatement = sqlParser.parse();
        return execute(sqlStatement.getSql(), sqlStatement.getValues());
    }

    public StreamDao withStream() {
        return new StreamDao(dataSource);
    }

    public AsyncDao withAsync() {
        return new AsyncDao(dataSource);
    }
}
