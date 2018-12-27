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

package club.spreadme.database.parser.grammar;

import java.util.Map;

public class SQLBean {

    private String taleName;
    private String primaryKeyName;
    private Map<String, Object> valueMap;

    public SQLBean(String taleName, String primaryKeyName, Map<String, Object> valueMap) {
        this.taleName = taleName;
        this.primaryKeyName = primaryKeyName;
        this.valueMap = valueMap;
    }

    public String getTaleName() {
        return taleName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    @Override
    public String toString() {
        return "SQLBean[" +
                "taleName='" + taleName + '\'' +
                ", primaryKeyName='" + primaryKeyName + '\'' +
                ", valueMap=" + valueMap +
                ']';
    }
}
