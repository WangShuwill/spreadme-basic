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

public class DeleteSQLBuilder extends AbstractSQLBuilder implements Serializable {

    private static final long serialVersionUID = -8232686904288788218L;

    private String tableName;
    private List<String> wheres;

    public DeleteSQLBuilder(String tableName) {
        this.tableName = tableName;
        this.wheres = new ArrayList<>();
    }

    @Override
    public StringBuilder build() {
        StringBuilder sql = new StringBuilder("delete from ").append(tableName);
        appendList(sql, wheres, " where ", " and ");
        return sql;
    }

    public DeleteSQLBuilder where(String expr) {
        wheres.add(expr);
        return this;
    }

}
