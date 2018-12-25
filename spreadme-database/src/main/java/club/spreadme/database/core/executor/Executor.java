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

package club.spreadme.database.core.executor;

import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.StatementCallback;

import javax.sql.DataSource;

/**
 * The interface of base options for database
 *
 * @author Wangshuwei
 * @since 2018-7-31
 */
public interface Executor {

    /**
     * @param builder statement builder
     * @param action  statement callback
     * @return parsed resultset
     * @see club.spreadme.database.core.statement.StatementBuilder
     * @see club.spreadme.database.core.statement.StatementCallback
     */
    <T> T execute(StatementBuilder builder, StatementCallback<T> action);

    DataSource getDataSourcec();
}
