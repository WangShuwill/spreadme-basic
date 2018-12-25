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

import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.wrapper.WrappedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * the generic class to build wrapped statement
 *
 * @author wswei
 * @see club.spreadme.database.core.statement.StatementBuilder
 * @see club.spreadme.database.core.aware.SQLProvider
 * @see club.spreadme.database.core.aware.Aware
 * @since 2018.12.24
 */
public abstract class AbstractStatementBuilder implements StatementBuilder {

    protected Connection connection;
    protected String sql;
    protected Object[] args;

    public AbstractStatementBuilder(Connection connection, String sql, Object[] args) {
        this.connection = connection;
        this.sql = sql;
        this.args = args;
    }

    @Override
    public WrappedStatement build() throws SQLException {
        Statement statement = createStatement();
        return new WrappedStatement(statement, sql, args, connection);
    }

    public abstract Statement createStatement() throws SQLException;

    @Override
    public String getSql() {
        return sql;
    }
}
