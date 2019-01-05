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

package club.spreadme.database.core.executor.support;

import club.spreadme.database.core.grammar.StatementConfig;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.StatementCallback;
import club.spreadme.database.core.statement.WrappedStatement;
import club.spreadme.database.exception.DataBaseAccessException;
import club.spreadme.database.util.JdbcUtil;
import club.spreadme.lang.Assert;

import javax.sql.DataSource;
import java.sql.Connection;

public class SimplExecutor extends AbstractExecutor {

    private final DataSource dataSource;

    public SimplExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected <T> T doExecute(StatementBuilder builder, StatementCallback<T> action, StatementConfig config) {
        Assert.notNull(builder, "StatementBuilder must be not null");
        Assert.notNull(action, "StatementCallback must be not null");
        Connection connection = null;
        WrappedStatement wrappedStatement = null;
        try {
            connection = JdbcUtil.getConnection(dataSource);
            builder.setConnection(connection);
            wrappedStatement = builder.build(config);
            return action.executeStatement(wrappedStatement);
        }
        catch (Exception ex) {
            String errorSql = builder.getSql();
            throw new DataBaseAccessException(errorSql, ex);
        }
        finally {
            JdbcUtil.closeWrappedStatement(wrappedStatement);
            JdbcUtil.closeConnection(connection, dataSource);
        }
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
