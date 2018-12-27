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

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public enum FetchDirection {

    FORWARD(ResultSet.FETCH_FORWARD), REVERSE(ResultSet.FETCH_REVERSE), UNKNOWN(ResultSet.FETCH_UNKNOWN);

    private static final Map<Integer, FetchDirection> mappings = new HashMap<>(3);

    static {
        for (FetchDirection fetchDirection : values()) {
            mappings.put(fetchDirection.getValue(), fetchDirection);
        }
    }

    public static FetchDirection resolve(Integer mode) {
        return mode == null ? null : mappings.get(mode);
    }

    public boolean matches(Integer mode) {
        return this == resolve(mode);
    }

    private int value;

    FetchDirection(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
