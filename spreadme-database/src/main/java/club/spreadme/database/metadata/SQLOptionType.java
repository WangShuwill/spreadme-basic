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

package club.spreadme.database.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * The database option type
 *
 * @author Wangshuwei
 * @since 2018-8-1
 */
public enum SQLOptionType {

    QUERY, UPDATE, INSERT, DELETE;

    private static final Map<String, SQLOptionType> mappings = new HashMap<>(4);

    static {
        for (SQLOptionType sqlOptionType : values()) {
            mappings.put(sqlOptionType.name(), sqlOptionType);
        }
    }

    public static SQLOptionType resolve(String sqlOptionType) {
        return sqlOptionType == null ? null : mappings.get(sqlOptionType);
    }

    public boolean matches(String sqlOptionType) {
        return this == resolve(sqlOptionType);
    }
}
