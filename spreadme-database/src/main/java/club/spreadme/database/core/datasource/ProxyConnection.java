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

import club.spreadme.database.exception.DataBaseAccessException;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

public class ProxyConnection implements InvocationHandler {

    private static final String CLOSEMETHODNAME = "close";
    private static final Class<?>[] ICONNECTION = new Class<?>[]{Connection.class};

    private final int hashCode;
    private final SpreadDataSource dataSource;
    private final Connection realConnection;
    private final Connection proxyConnection;

    private long createdTimestamp;
    private long lastUsedTimestamp;
    private long checkoutTimestamp;

    private boolean vaild;

    public ProxyConnection(SpreadDataSource dataSource, Connection connection) {
        this.hashCode = connection.hashCode();
        this.dataSource = dataSource;
        this.realConnection = connection;
        this.createdTimestamp = System.currentTimeMillis();
        this.lastUsedTimestamp = System.currentTimeMillis();
        this.vaild = true;
        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), ICONNECTION, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (CLOSEMETHODNAME.hashCode() == method.getName().hashCode() && CLOSEMETHODNAME.equals(methodName)) {
            dataSource.pushConnection(this);
            return null;
        }
        else {
            if (!Object.class.equals(method.getDeclaringClass())) {
                if (!vaild) {
                    throw new DataBaseAccessException("The connection is not valid");
                }
            }
            return method.invoke(realConnection, args);
        }
    }

    public void inValid() {
        this.vaild = false;
    }

    public boolean isVaild() {
        return vaild && realConnection != null && dataSource.pingConnection(this);
    }

    public int getRealConnectionHashCode() {
        return realConnection == null ? 0 : realConnection.hashCode();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getLastUsedTimestamp() {
        return lastUsedTimestamp;
    }

    public void setLastUsedTimestamp(long lastUsedTimestamp) {
        this.lastUsedTimestamp = lastUsedTimestamp;
    }

    public long getCheckoutTimestamp() {
        return System.currentTimeMillis() - createdTimestamp;
    }

    public void setCheckoutTimestamp(long checkoutTimestamp) {
        this.checkoutTimestamp = checkoutTimestamp;
    }

    public Connection getProxyConnection() {
        return proxyConnection;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProxyConnection) {
            return realConnection.hashCode() == ((ProxyConnection) obj).getRealConnection().hashCode();
        }
        else if (obj instanceof Connection) {
            return hashCode == obj.hashCode();
        }
        else {
            return false;
        }
    }
}
