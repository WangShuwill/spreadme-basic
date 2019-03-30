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

package club.spreadme.database.springboot;

import club.spreadme.database.plugin.Interceptor;
import club.spreadme.database.plugin.paginator.dialect.PaginationDialect;
import club.spreadme.lang.cache.Cache;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = SpreadDatabaseProperties.SPREADDATABASE_PREFIX)
public class SpreadDatabaseProperties {

    public static final String SPREADDATABASE_PREFIX = "spreaddatabase";

    // spreaddatabase settings
    private Class<? extends PaginationDialect>[] paginationDialects = new Class[]{};
    private Class<? extends Interceptor>[] interceptors = new Class[]{};
    private Class<? extends Cache>[] caches = new Class[]{};

    public Class<? extends PaginationDialect>[] getPaginationDialects() {
        return paginationDialects;
    }

    public void setPaginationDialects(Class<? extends PaginationDialect>[] paginationDialects) {
        this.paginationDialects = paginationDialects;
    }

    public Class<? extends Interceptor>[] getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(Class<? extends Interceptor>[] interceptors) {
        this.interceptors = interceptors;
    }

    public Class<? extends Cache>[] getCaches() {
        return caches;
    }

    public void setCaches(Class<? extends Cache>[] caches) {
        this.caches = caches;
    }

}
