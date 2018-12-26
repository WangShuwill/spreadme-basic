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

package club.spreadme.database.core.statement.wrapper;

import club.spreadme.database.core.statement.WrappedStatement;
import club.spreadme.database.metadata.ConcurMode;
import club.spreadme.database.util.JdbcUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SimpleWrappedStatement implements WrappedStatement {

    private final Statement statement;
    private final String sql;

    public SimpleWrappedStatement(Statement statement, String sql) {
        this.statement = statement;
        this.sql = sql;
    }

    @Override
    public ResultSet query() throws SQLException {
        return statement.executeQuery(sql);
    }

    @Override
    public int update() throws SQLException {
        return statement.executeUpdate(sql);
    }

    @Override
    public int[] batch() throws SQLException {
        return statement.executeBatch();
    }

    @Override
    public ConcurMode getConcurMode() throws SQLException {
        return ConcurMode.resolve(statement.getResultSetConcurrency());
    }

    @Override
    public void close() throws IOException {
        JdbcUtil.closeStatement(statement);
    }
}
