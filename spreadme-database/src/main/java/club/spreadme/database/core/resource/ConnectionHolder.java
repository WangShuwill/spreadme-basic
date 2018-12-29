/*
 *  Copyright (c) 2018 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.spreadme.database.core.resource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Datasoure as key,connection as value,save a connection to map
 *
 * @author Wangshuwei
 * @since 2018-6-23
 */
public class ConnectionHolder {

    private boolean isCommitOnly = false;
    private boolean isRollbackOnly = false;
    private boolean isTransactionActive = false;
    private Map<DataSource, Connection> connectionMap = new HashMap<>();

    public Connection getConnection(DataSource dataSource) throws SQLException {
        Connection connection = connectionMap.get(dataSource);
        if (connection == null || connection.isClosed()) {
            connection = dataSource.getConnection();
            connectionMap.put(dataSource, connection);
        }
        return connection;
    }

    public void removeConnection(DataSource dataSource) {
        connectionMap.remove(dataSource);
    }

    public void closeConnection() {

    }

    public boolean isTransactionActive() {
        return isTransactionActive;
    }

    public void setTransactionActive(boolean transactionActive) {
        isTransactionActive = transactionActive;
    }

    public boolean isCommitOnly() {
        return isCommitOnly;
    }

    public void setCommitOnly(boolean commitOnly) {
        isCommitOnly = commitOnly;
    }

    public boolean isRollbackOnly() {
        return isRollbackOnly;
    }

    public void setRollbackOnly(boolean rollbackOnly) {
        isRollbackOnly = rollbackOnly;
    }
}
