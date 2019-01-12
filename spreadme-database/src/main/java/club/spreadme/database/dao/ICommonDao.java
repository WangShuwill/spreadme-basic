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

package club.spreadme.database.dao;

import club.spreadme.database.core.executor.Executor;
import club.spreadme.database.core.grammar.Record;
import club.spreadme.database.core.transaction.TransactionExecutor;
import club.spreadme.database.dao.support.AsyncDao;
import club.spreadme.database.dao.support.StreamDao;
import club.spreadme.database.plugin.Interceptor;
import club.spreadme.lang.cache.Cache;

import javax.sql.DataSource;
import java.util.List;

public interface ICommonDao {

    ICommonDao use(DataSource dataSource);

    ICommonDao use(Executor executor);

    ICommonDao use(Interceptor[] interceptors);

    ICommonDao use(Cache cache);

    <T> T getDao(Class<T> clazz);

    TransactionExecutor getTransactionExecutor();

    <T> T queryOne(String sql, Class<T> clazz);

    <T> T queryOne(String sql, Class<T> clazz, Object... args);

    Record queryOne(String sql);

    Record queryOne(String sql, Object... args);

    <T> List<T> query(String sql, Class<T> clazz);

    <T> List<T> query(String sql, Class<T> clazz, Object... args);

    List<Record> query(String sql);

    List<Record> query(String sql, Object... args);

    int insert(Object bean);

    int delete(Object bean);

    int update(Object bean);

    int execute(String sql);

    int execute(String sql, Object... args);

    StreamDao withStream();

    AsyncDao withAsync();
}
