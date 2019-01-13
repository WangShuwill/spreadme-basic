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

package club.spreadme.database.bind;

import club.spreadme.database.bind.support.MethodSignature;
import club.spreadme.database.bind.support.SQLCommand;
import club.spreadme.database.core.cache.PerpetualCache;
import club.spreadme.lang.cache.Cache;

import java.lang.reflect.Method;

/**
 * DaoMethod registrar, cache MethodSignature and SQLCommond
 *
 * @author wswei
 */
public interface DaoMethodRegiatrar {

    // MehthodSignature cache
    Cache<Method, MethodSignature> mscache = new PerpetualCache<>("MethodSignature");
    // SQLCommond cache
    Cache<Method, SQLCommand> sdcache = new PerpetualCache<>("SQLCommand");

    /**
     * register methodSignature to cache
     *
     * @param method          method
     * @param methodSignature method signature
     * @see club.spreadme.database.bind.support.MethodSignature
     */
    void register(Method method, MethodSignature methodSignature);

    /**
     * register sqlcommond to cache
     *
     * @param method     method
     * @param sqlCommand sqlCommond
     * @see club.spreadme.database.bind.support.SQLCommand
     */
    void register(Method method, SQLCommand sqlCommand);

    /**
     * get methodsignature from cache
     *
     * @param daoInterface dao interface
     * @param method       method
     * @param values       method values
     * @return MethodSignature
     * @see club.spreadme.database.bind.support.MethodSignature
     */
    MethodSignature getMethodSignature(Class<?> daoInterface, Method method, Object[] values);

    /**
     * get sqlcommond from cache
     *
     * @param method method
     * @return SQLCommond
     * @see club.spreadme.database.bind.support.SQLCommand
     */
    SQLCommand getSQLCommand(Method method);
}
