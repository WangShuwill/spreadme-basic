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

package club.spreadme.database.parser.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateSQLBuilder extends AbstractSQLBuilder implements Serializable {

    private static final long serialVersionUID = 2254784413448364529L;

    private String tableName;
    private List<String> sets;
    private List<String> wheres;

    public UpdateSQLBuilder(String tableName) {
        this.tableName = tableName;
        this.sets = new ArrayList<>();
        this.wheres = new ArrayList<>();
    }

    public UpdateSQLBuilder set(String expr) {
        sets.add(expr);
        return this;
    }

    public UpdateSQLBuilder where(String expr) {
        wheres.add(expr);
        return this;
    }

    @Override
    public StringBuilder build() {
        StringBuilder sql = new StringBuilder("update ").append(tableName);
        appendList(sql, sets, " set ", ", ");
        appendList(sql, wheres, " where ", " and ");
        return sql;
    }
}
