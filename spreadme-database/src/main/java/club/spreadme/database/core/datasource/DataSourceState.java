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

import java.util.LinkedList;
import java.util.List;

public class DataSourceState {

    protected SpreadDataSource spreadDataSource;

    protected final List<ProxyConnection> idleConnections = new LinkedList<>();
    protected final List<ProxyConnection> activeConnections = new LinkedList<>();

    public DataSourceState(SpreadDataSource dataSource) {
        this.spreadDataSource = dataSource;
    }

    public synchronized int getIdleConnectionCount() {
        return idleConnections.size();
    }

    public synchronized int getActiveConnectionCount() {
        return activeConnections.size();
    }

}
