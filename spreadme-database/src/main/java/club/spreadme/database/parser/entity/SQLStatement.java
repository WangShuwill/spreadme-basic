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

package club.spreadme.database.parser.entity;

import java.util.List;

public class SQLStatement {

    private String sql;
    private List<SQLParameter> sqlParameters;

    public SQLStatement() {
    }

    public SQLStatement(String sql, List<SQLParameter> sqlParameters) {
        this.sql = sql;
        this.sqlParameters = sqlParameters;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<SQLParameter> getSqlParameters() {
        return sqlParameters;
    }

    public void setSqlParameters(List<SQLParameter> sqlParameters) {
        this.sqlParameters = sqlParameters;
    }

    @Override
    public String toString() {
        return "SQLStatement{" +
                "sql='" + sql + '\'' +
                ", sqlParameters=" + sqlParameters +
                '}';
    }
}
