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

public class InsertSQLBuilder extends AbstractSQLBuilder implements Serializable {

    private static final long serialVersionUID = -2049905797189201200L;

    private String tableName;
    private List<String> columns;
    private List<Object> values;

    public InsertSQLBuilder(String tableName) {
        this.tableName = tableName;
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public InsertSQLBuilder set(String column, Object value) {
        columns.add(column);
        values.add(value);
        return this;
    }

    @Override
    public StringBuilder build() {
        StringBuilder sql = new StringBuilder("insert into ").append(tableName).append(" (");
        appendList(sql, columns, "", ", ").append(") values (");
        appendList(sql, values, "", ", ").append(")");
        return sql;
    }
}
