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

import club.spreadme.database.core.grammar.StatementConfig;
import club.spreadme.database.core.grammar.StatementInfo;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.WrappedStatement;
import club.spreadme.database.exception.DataBaseAccessException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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

    private Connection connection;
    private StatementInfo statementInfo;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setValues(Object[] values) {

    }

    @Override
    public Object[] getValues() {
        return new Object[0];
    }

    @Override
    public WrappedStatement build(StatementConfig statementConfig) throws SQLException {
        Statement statement = createStatement(connection);
        prepare(statement, statementConfig);
        return doBuild(statement);
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public DatabaseMetaData getDatabaseMetaData() {
        try {
            return connection.getMetaData();
        }
        catch (SQLException ex) {
            throw new DataBaseAccessException(ex.getMessage());
        }
    }

    @Override
    public StatementInfo getStatementInfo() {
        return this.statementInfo;
    }

    public abstract WrappedStatement doBuild(Statement statement) throws SQLException;

    public abstract Statement createStatement(Connection connection) throws SQLException;

    private void prepare(Statement statement, StatementConfig config) throws SQLException {
        if (config != null) {
            if (config.getQueryTimeout() != null) {
                statement.setQueryTimeout(config.getQueryTimeout());
            }
            if (config.getFetchSize() != null) {
                statement.setFetchSize(config.getFetchSize());
            }
            if (config.getFetchDirection() != null) {
                statement.setFetchDirection(config.getFetchDirection().getValue());
            }
        }
    }
}
