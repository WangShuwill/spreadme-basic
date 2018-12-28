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

package club.spreadme.database.core.statement;

import club.spreadme.database.core.aware.SQLProvider;
import club.spreadme.database.core.grammar.StatementConfig;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * build wrapped statement
 *
 * @author wswei
 * @see club.spreadme.database.core.aware.SQLProvider
 * @see club.spreadme.database.core.aware.Aware
 * @since 2018.12.24
 */
public interface StatementBuilder extends SQLProvider {

    /**
     * set a connection for build
     *
     * @param connection Connection
     * @see java.sql.Connection
     */
    void setConnection(Connection connection);

    /**
     * set a sql statement for build
     *
     * @param sql sql
     */
    void setSql(String sql);

    /**
     * build by connection,sql,Statementconfgi...
     *
     * @param config Statement config
     * @return wrapped statement
     * @throws SQLException sql exception
     * @see club.spreadme.database.core.statement.WrappedStatement
     * @see club.spreadme.database.core.grammar.StatementConfig
     */
    WrappedStatement build(StatementConfig config) throws SQLException;

}
