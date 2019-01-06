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

package club.spreadme.database.exception;

import club.spreadme.database.core.aware.SQLProvider;
import club.spreadme.database.plugin.Plugin;

import java.lang.reflect.Proxy;

/**
 * @author Wangshuwei
 * @since 2018-6-21
 */
public class DataBaseAccessException extends RuntimeException {

    private static final long serialVersionUID = -2723030345746130999L;

    public DataBaseAccessException(String reason) {
        super(reason);
    }

    public DataBaseAccessException(SQLProvider sqlProvider, Throwable ex) {
        super(getErrorSql(sqlProvider), ex);
    }

    protected static String getErrorSql(Object object) {
        Object target = object;
        if (Proxy.isProxyClass(object.getClass())) {
            // compatible the plugin proxy
            target = Plugin.getTarget(object);
        }
        SQLProvider sqlProvider = target instanceof SQLProvider ? (SQLProvider) target : null;
        return sqlProvider == null ? "" : sqlProvider.getSql();
    }
}
