/*
 * Copyright (c) 2018 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.spreadme.database.core.transaction;

import club.spreadme.database.core.resource.ConnectionHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Wangshuwei
 * @since 2018-6-23
 */
public class TransactionSynchronizationManager {

    private static ThreadLocal<ConnectionHolder> connectionHolderPool = new ThreadLocal<>();

    public static Connection getConnection(DataSource dataSource) throws SQLException {
        return getConnectionHolder().getConnection(dataSource);
    }

    public static void removeConnection(DataSource dataSource) {
        getConnectionHolder().removeConnection(dataSource);
    }

    public static ConnectionHolder getConnectionHolder() {
        ConnectionHolder connectionHolder = connectionHolderPool.get();
        if (connectionHolder == null) {
            connectionHolder = new ConnectionHolder();
            connectionHolderPool.set(connectionHolder);
        }
        return connectionHolder;
    }

    public static void clear() {
        connectionHolderPool.remove();
    }
}
