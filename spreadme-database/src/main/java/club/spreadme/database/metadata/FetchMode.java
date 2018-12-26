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

public enum FetchMode {

    FORWARD(1000), REVERSE(1001), UNKNOWN(1002);

    private static final Map<Integer, FetchMode> mappings = new HashMap<>(4);

    static {
        for (FetchMode fetchMode : values()) {
            mappings.put(fetchMode.getValue(), fetchMode);
        }
    }

    public static FetchMode resolve(Integer mode) {
        return mode == null ? null : mappings.get(mode);
    }

    public boolean matches(Integer mode) {
        return this == resolve(mode);
    }

    private int value;

    FetchMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
