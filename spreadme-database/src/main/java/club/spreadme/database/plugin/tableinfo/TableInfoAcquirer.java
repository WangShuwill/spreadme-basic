/*
 *  Copyright (c) 2019 Wangshuwei
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

package club.spreadme.database.plugin.tableinfo;

import club.spreadme.database.core.resource.ResourceHandler;
import club.spreadme.database.core.resultset.support.BeanRowMapper;
import club.spreadme.database.core.resultset.support.DefaultResultSetParser;
import club.spreadme.database.exception.DataBaseAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

public class TableInfoAcquirer {

    private DataSource dataSource;

    private volatile static TableInfoAcquirer tableInfoAcquirer;

    private TableInfoAcquirer() {

    }

    public static TableInfoAcquirer getInstance() {
        if (tableInfoAcquirer == null) {
            synchronized (TableInfoAcquirer.class) {
                if (tableInfoAcquirer == null) {
                    tableInfoAcquirer = new TableInfoAcquirer();
                }
            }
        }
        return tableInfoAcquirer;
    }

    public TableInfoAcquirer use(DataSource dataSource) {
        this.dataSource = dataSource;
        return tableInfoAcquirer;
    }

    public List<TableInfo> getTableInfo(String catalog, String schema, String tableName, String columnName) {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = ResourceHandler.getConnection(dataSource);
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            resultSet = databaseMetaData.getColumns(catalog, schema, tableName, columnName);
            return new DefaultResultSetParser<>(new BeanRowMapper<>(TableInfo.class)).parse(resultSet);
        }
        catch (Exception ex) {
            throw new DataBaseAccessException(ex.getMessage());
        }
        finally {
            ResourceHandler.closeResultSet(resultSet);
            ResourceHandler.closeConnection(connection, dataSource);
        }
    }
}
