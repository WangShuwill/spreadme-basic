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

package club.spreadme.database.core.aware;

import club.spreadme.database.metadata.SQLType;

/**
 * sql option type aware
 *
 * @author wswei
 * @see club.spreadme.database.core.aware.Aware
 * @since 2018.12.24
 */
public interface SQLOptionTypeAware extends Aware {

    /**
     * get sql option type
     *
     * @return sql option type
     * @see SQLType
     */
    SQLType getSqlOptionType();

}
