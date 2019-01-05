/*
 *  Copyright (c) 2018 Wangshuwei
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

package club.spreadme.database.core.executor.support;

import club.spreadme.database.core.cache.Cache;
import club.spreadme.database.core.cache.CacheKey;
import club.spreadme.database.core.executor.Executor;
import club.spreadme.database.core.grammar.StatementConfig;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.StatementCallback;
import club.spreadme.database.metadata.ConcurMode;

import javax.sql.DataSource;

public class CachingExecutor extends AbstractExecutor {

    private final DataSource dataSource;
    private final boolean isUseCache;
    private final Cache cache;
    private final Executor executor;

    public CachingExecutor(DataSource dataSource, boolean isUseCache, Cache cache) {
        this.dataSource = dataSource;
        this.isUseCache = isUseCache;
        this.cache = cache;
        this.executor = new SimplExecutor(dataSource);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T doExecute(StatementBuilder builder, StatementCallback<T> action, StatementConfig config) {
        if (!isUseCache || cache == null) {
            return cachingExecute(builder, action, config);
        }
        if (!ConcurMode.READ_ONLY.equals(action.getCouncurMode())) {
            cache.clear();
            return cachingExecute(builder, action, config);

        }
        else {
            CacheKey cacheKey = builder.createCachekey();

            Object cacheObject = cache.get(cacheKey);
            if (cacheObject != null) {
                return (T) cacheObject;
            }
            else {
                T object = cachingExecute(builder, action, config);
                cache.put(cacheKey, object);
                return object;
            }
        }

    }

    private <T> T cachingExecute(StatementBuilder builder, StatementCallback<T> action, StatementConfig config) {
        executor.setStatementConfig(config);
        return executor.execute(builder, action);
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }
}
