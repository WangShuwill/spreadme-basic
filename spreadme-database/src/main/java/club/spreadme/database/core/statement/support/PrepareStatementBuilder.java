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

public class PrepareStatementBuilder extends AbstractStatementBuilder {

    private final String sql;
    private final Object[] parameters;

    public PrepareStatementBuilder(String sql, Object[] parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    @Override
    public WrappedStatement doBuild(Statement statement) throws SQLException {
        return new PrepareWrappedStatement((PreparedStatement) statement);
    }

    @Override
    public Statement createStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        int parameterIndex = 1;
        for (Object parameter : parameters) {
            ps.setObject(parameterIndex, parameter);
            parameterIndex++;
        }
        return ps;
    }

    @Override
    public String getSql() {
        return sql;
    }
}
