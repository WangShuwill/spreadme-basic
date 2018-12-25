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

import club.spreadme.database.core.resultset.ResultSetParser;
import club.spreadme.database.core.statement.StatementCallback;
import club.spreadme.database.core.statement.wrapper.WrappedStatement;
import club.spreadme.database.metadata.SQLType;
import club.spreadme.database.util.JdbcUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryStatementCallback<T> implements StatementCallback<T> {

    private final ResultSetParser<T> resultSetParser;

    public QueryStatementCallback(ResultSetParser<T> resultSetParser) {
        this.resultSetParser = resultSetParser;
    }

    @Override
    public T executeStatement(WrappedStatement wrappedStatement) throws Exception {
        ResultSet resultSet = null;
        try {
            Statement statement = wrappedStatement.getStatement();
            String sql = wrappedStatement.getSql();
            resultSet = doExcute(statement, sql);
            return resultSetParser.parse(resultSet);
        } finally {
            JdbcUtil.closeResultSet(resultSet);
        }
    }

    @Override
    public SQLType getSqlOptionType() {
        return SQLType.QUERY;
    }

    protected ResultSet doExcute(Statement statement, String sql) throws SQLException {
        if (statement instanceof PreparedStatement) {
            return ((PreparedStatement) statement).executeQuery();
        } else {
            return statement.executeQuery(sql);
        }
    }

}
