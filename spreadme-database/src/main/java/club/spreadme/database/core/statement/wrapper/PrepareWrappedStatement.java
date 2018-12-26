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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrepareWrappedStatement implements WrappedStatement {

    private final PreparedStatement preparedStatement;

    public PrepareWrappedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    @Override
    public ResultSet query() throws SQLException {
        return preparedStatement.executeQuery();
    }

    @Override
    public int update() throws SQLException {
        return preparedStatement.executeUpdate();
    }

    @Override
    public int[] batch() throws SQLException {
        return preparedStatement.executeBatch();
    }

    @Override
    public ConcurMode getConcurMode() throws SQLException {
        return ConcurMode.resolve(preparedStatement.getResultSetConcurrency());
    }

    @Override
    public void close() throws IOException {
        JdbcUtil.closeStatement(preparedStatement);
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
}
