/*
 * Copyright (c) 2018 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.spreadme.database.core.datasource;

import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * A simple datasource
 *
 * @author Wangshuwei
 * @since 2018-08-23
 */
public class SpreadDataSource implements DataSource {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SpreadDataSource.class);

    private String driver;
    private String url;
    private String username;
    private String password;

    private int maximumActiveConnections = 4;
    private int maximumIdleConnections = 2;
    private int maimumCheckoutTime = 2 * 1000;
    private int poolTimeToWait = 2 * 1000;

    private final DataSourceState state = new DataSourceState(this);

    public SpreadDataSource() {

    }

    public SpreadDataSource(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public SpreadDataSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return popConnection(username, password).getProxyConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection(username, password).getProxyConnection();
    }

    @Override
    public void setLoginTimeout(int loginTimeout) {
        DriverManager.setLoginTimeout(loginTimeout);
    }

    @Override
    public int getLoginTimeout() {
        return DriverManager.getLoginTimeout();
    }

    /**
     * create new connection
     *
     * @return connection
     * @throws SQLException sql exception
     */
    protected Connection createConnection() throws SQLException {
        if (driver != null) {
            try {
                Class.forName(driver);
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return DriverManager.getConnection(url, username, password);
    }

    protected void pushConnection(ProxyConnection proxyConnection) throws SQLException {
        synchronized (state) {
            // remove active connection from activeconnections
            state.activeConnections.remove(proxyConnection);
            if (proxyConnection.isVaild()) {
                if (state.idleConnections.size() < maximumIdleConnections) {
                    ProxyConnection newProxyConnection = new ProxyConnection(this, proxyConnection.getRealConnection());
                    // return connection to idleconnections
                    state.idleConnections.add(newProxyConnection);
                    newProxyConnection.setCreatedTimestamp(System.currentTimeMillis());
                    newProxyConnection.setLastUsedTimestamp(System.currentTimeMillis());
                    proxyConnection.inValid();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Return a connection {} to idleconnections", newProxyConnection.getRealConnectionHashCode());
                    }
                    state.notifyAll();
                }
                else {
                    proxyConnection.getRealConnection().close();
                    proxyConnection.inValid();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Close the connection {}", proxyConnection.getRealConnectionHashCode());
                    }
                }
            }
            else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("a invalid connection {} can not return idleconnections", proxyConnection.getRealConnectionHashCode());
                }
            }
        }
    }

    protected ProxyConnection popConnection(String username, String password) throws SQLException {
        boolean iswait = false;
        ProxyConnection proxyConnection = null;

        while (proxyConnection == null) {
            synchronized (state) {
                // if idelconnection is not empty, get a proxyconnection from index zero
                if (!state.idleConnections.isEmpty()) {
                    proxyConnection = state.idleConnections.remove(0);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("get a connection {} from idleconnections", proxyConnection.getRealConnectionHashCode());
                    }
                }
                else {
                    // if active connection count less than maximum active connection count,create new connection
                    if (state.activeConnections.size() < maximumActiveConnections) {
                        proxyConnection = new ProxyConnection(this, createConnection());
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("create a new connection {}", proxyConnection.getRealConnectionHashCode());
                        }
                    }
                    else {
                        ProxyConnection oldActiveProxyConnection = state.activeConnections.get(0);
                        long checkoutTimestamp = oldActiveProxyConnection.getCheckoutTimestamp();
                        // deal orverdue connection
                        if (checkoutTimestamp > maimumCheckoutTime) {
                            state.activeConnections.remove(oldActiveProxyConnection);
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Remove the orverdue connection {} from activeconnections", oldActiveProxyConnection.getRealConnectionHashCode());
                            }
                            // create new proxyconnection from old connection
                            proxyConnection = new ProxyConnection(this, oldActiveProxyConnection.getRealConnection());
                            proxyConnection.setCreatedTimestamp(oldActiveProxyConnection.getCreatedTimestamp());
                            proxyConnection.setLastUsedTimestamp(oldActiveProxyConnection.getLastUsedTimestamp());
                            // invalid the old connection
                            oldActiveProxyConnection.inValid();
                        }
                        else {
                            try {
                                if (!iswait) {
                                    iswait = true;
                                }
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug("Waiting as long as {} milliseconds for connection", poolTimeToWait);
                                }
                                state.wait(poolTimeToWait);
                            }
                            catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                }
                // add connection to activeconnections
                if (proxyConnection != null) {
                    if (proxyConnection.isVaild()) {
                        proxyConnection.setLastUsedTimestamp(System.currentTimeMillis());
                        proxyConnection.setCheckoutTimestamp(System.currentTimeMillis());
                        state.activeConnections.add(proxyConnection);
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Add connection {} to activeconnections", proxyConnection.getRealConnectionHashCode());
                        }
                    }
                }
            }
        }

        if (proxyConnection == null) {
            throw new SQLException("The spreadDatasource return null connection");
        }

        return proxyConnection;
    }

    // ping the connection is close
    protected boolean pingConnection(ProxyConnection proxyConnection) {
        boolean result;

        try {
            result = !proxyConnection.getRealConnection().isClosed();
        }
        catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The connection {} is bad : {}", proxyConnection.getRealConnectionHashCode(), e.getMessage());
            }
            result = false;
        }

        return result;
    }

    public SpreadDataSource setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public SpreadDataSource setUrl(String url) {
        this.url = url;
        return this;
    }

    public SpreadDataSource setUsername(String username) {
        this.username = username;
        return this;
    }

    public SpreadDataSource setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getMaximumActiveConnections() {
        return maximumActiveConnections;
    }

    public SpreadDataSource setMaximumActiveConnections(int maximumActiveConnections) {
        this.maximumActiveConnections = maximumActiveConnections;
        return this;
    }

    public int getMaximumIdleConnections() {
        return maximumIdleConnections;
    }

    public SpreadDataSource setMaximumIdleConnections(int maximumIdleConnections) {
        this.maximumIdleConnections = maximumIdleConnections;
        return this;
    }

    public int getMaimumCheckoutTime() {
        return maimumCheckoutTime;
    }

    public SpreadDataSource setMaimumCheckoutTime(int maimumCheckoutTime) {
        this.maimumCheckoutTime = maimumCheckoutTime;
        return this;
    }

    public int getPoolTimeToWait() {
        return poolTimeToWait;
    }

    public void setPoolTimeToWait(int poolTimeToWait) {
        this.poolTimeToWait = poolTimeToWait;
    }

    @Override
    public PrintWriter getLogWriter() {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter logWriter) {
        DriverManager.setLogWriter(logWriter);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + " is not a wrapper");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }

    @Override
    public Logger getParentLogger() {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

}
