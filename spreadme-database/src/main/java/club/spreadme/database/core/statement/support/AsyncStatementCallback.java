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

import club.spreadme.database.core.statement.StatementCallback;
import club.spreadme.database.core.statement.WrappedStatement;
import club.spreadme.database.exception.DataBaseAccessException;
import club.spreadme.database.metadata.ConcurMode;
import club.spreadme.database.core.resource.ResourceHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.concurrent.CompletableFuture;

public class AsyncStatementCallback<T> implements StatementCallback<CompletableFuture<T>> {

    private DataSource dataSource;
    private Connection connection;

    private final StatementCallback<T> callback;

    public AsyncStatementCallback(StatementCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public CompletableFuture<T> executeStatement(WrappedStatement wrappedStatement) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return callback.executeStatement(wrappedStatement);
            }
            catch (Exception e) {
                throw new DataBaseAccessException(e.getMessage());
            }
            finally {
                ResourceHandler.closeWrappedStatement(wrappedStatement);
                ResourceHandler.closeConnection(connection, dataSource);
            }
        });
    }

    @Override
    public ConcurMode getCouncurMode() {
        return callback.getCouncurMode();
    }

    public void nest(DataSource dataSource, Connection connection) {
        this.dataSource = dataSource;
        this.connection = connection;
    }
}
