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

package club.spreadme.database.core.statement.support;

import club.spreadme.database.core.statement.WrappedStatement;
import club.spreadme.database.core.statement.wrapper.PrepareWrappedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchStatementBuilder extends AbstractStatementBuilder {

    private String sql;
    private Object[][] parameterss;

    public BatchStatementBuilder(final String sql, final Object[][] parameterss) {
        this.sql = sql;
        this.parameterss = parameterss;
    }

    @Override
    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public WrappedStatement doBuild(Statement statement) {
        return new PrepareWrappedStatement((PreparedStatement) statement);
    }

    @Override
    public Statement createStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        for (Object[] parameters : parameterss) {
            int parameterIndex = 1;
            for (Object parameter : parameters) {
                ps.setObject(parameterIndex, parameter);
                parameterIndex++;
            }
            ps.addBatch();
        }
        return ps;
    }

    @Override
    public String getSql() {
        return sql;
    }
}
