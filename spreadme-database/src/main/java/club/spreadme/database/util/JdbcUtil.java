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
package club.spreadme.database.util;

import club.spreadme.database.core.resource.ConnectionHolder;
import club.spreadme.database.core.statement.WrappedStatement;
import club.spreadme.database.core.transaction.TransactionSynchronizationManager;
import club.spreadme.database.exception.DataBaseAccessException;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.*;

/**
 * @author Wangshuwei
 * @since 2018-6-21
 */
public abstract class JdbcUtil {

    private static DatabaseMetaData databaseMetaData;

    /**
     * Get a connection from datasource
     *
     * @param dataSource DataSource
     * @return Connection
     */
    public static Connection getConnection(DataSource dataSource) {
        try {
            // Transaction support,the current operation has begined transaction
            ConnectionHolder connectionHolder = TransactionSynchronizationManager.getConnectionHolder();
            if (connectionHolder != null && connectionHolder.isTransactionActive()) {
                Connection connection = connectionHolder.getConnection(dataSource);
                databaseMetaData = connection.getMetaData();
                return connection;
            }
            Connection connection = dataSource.getConnection();
            databaseMetaData = connection.getMetaData();
            return connection;
        }
        catch (SQLException e) {
            throw new DataBaseAccessException(e.getMessage());
        }
    }

    /**
     * close a connection from datasource
     *
     * @param connection Connection
     * @param dataSource DataSource
     */
    public static void closeConnection(Connection connection, DataSource dataSource) {
        try {
            doCloseConnection(connection, dataSource);
        }
        catch (SQLException ex) {
            throw new DataBaseAccessException(ex.getMessage());
        }
    }

    /**
     * close a connection from datasource
     *
     * @param connection Connection
     * @param dataSource DataSource
     * @throws SQLException SQLException
     */
    public static void doCloseConnection(Connection connection, DataSource dataSource) throws SQLException {
        if (connection == null) {
            return;
        }
        if (dataSource != null) {
            // get a connectionholder from threadlocal
            ConnectionHolder connectionHolder = TransactionSynchronizationManager.getConnectionHolder();
            // compare the connection to connection of connectionholder
            if (connectionHolder != null && connectionHolder.isTransactionActive()
                    && equals(connection, connectionHolder.getConnection(dataSource))) {
                connectionHolder.closeConnection();
                return;
            }
        }
        connection.close();
    }

    /**
     * close statement
     *
     * @param stmt statement
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (SQLException ex) {
                throw new DataBaseAccessException(ex.getMessage());
            }
        }
    }

    /**
     * close resultset
     *
     * @param rs resultset
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (SQLException ex) {
                throw new DataBaseAccessException(ex.getMessage());
            }
        }
    }

    public static void closeWrappedStatement(WrappedStatement wrappedStatement) {
        if (wrappedStatement != null) {
            try {
                wrappedStatement.close();
            }
            catch (Exception ex) {
                throw new DataBaseAccessException(ex.getMessage());
            }
        }
    }

    /**
     * get a column name from resultsetmetadata by index
     *
     * @param resultSetMetaData RsultsetMetaData
     * @param columnIndex       Column index
     * @return column name
     */
    public static String getColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) {
        try {
            return resultSetMetaData.getColumnLabel(columnIndex);
        }
        catch (SQLException ex) {
            throw new DataBaseAccessException(ex.getMessage());
        }
    }

    /**
     * get value from resultset by index
     *
     * @param rs    Resultset
     * @param index Index
     * @return value
     */
    public static Object getResultSetValue(ResultSet rs, int index) {
        try {
            return rs.getObject(index);
        }
        catch (SQLException ex) {
            throw new DataBaseAccessException(ex.getMessage());
        }
    }

    public static DatabaseMetaData getDataBaseMeteData() {
        return databaseMetaData;
    }

    protected static boolean equals(Connection sourceConnection, Connection destinationConnection) {
        //compatible the proxy connection of embed spreaddatasource
        if (Proxy.isProxyClass(sourceConnection.getClass())) {
            return sourceConnection.hashCode() == destinationConnection.hashCode();
        }
        return sourceConnection.equals(destinationConnection);
    }
}
