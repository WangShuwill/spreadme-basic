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

package club.spreadme.database.bind;

import club.spreadme.database.bind.support.MethodSignature;
import club.spreadme.database.bind.support.SQLCommand;
import club.spreadme.database.core.executor.Executor;

/**
 * database option
 *
 * @author wswei
 */
public interface DatabaseOption {

    /**
     * query option for database
     *
     * @param methodSignature MethodSignature
     * @param sqlCommand      SQLCommond
     * @param executor        Executer
     * @return query result
     */
    Object query(MethodSignature methodSignature, SQLCommand sqlCommand, Executor executor);

    /**
     * update delete insert for database
     *
     * @param methodSignature MethodSignature
     * @param sqlCommand      SQLCommond
     * @param executor        Executor
     * @return update result
     */
    Object update(MethodSignature methodSignature, SQLCommand sqlCommand, Executor executor);

}
