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
public enum ConcurMode {

    READ_ONLY(1007), UPDATABLE(1008), UNKNOWN(-1);

    private static final Map<Integer, ConcurMode> mappings = new HashMap<>(3);

    static {
        for (ConcurMode concurMode : values()) {
            mappings.put(concurMode.getValue(), concurMode);
        }
    }

    public static ConcurMode resolve(Integer mode) {
        return mode == null ? UNKNOWN : mappings.get(mode);
    }

    public boolean matches(Integer mode) {
        return this == resolve(mode);
    }

    private int value;

    ConcurMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
