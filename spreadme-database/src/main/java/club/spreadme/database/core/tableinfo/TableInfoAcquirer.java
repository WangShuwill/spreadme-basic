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

package club.spreadme.database.core.tableinfo;

import club.spreadme.database.core.executor.Executor;
import club.spreadme.database.core.grammar.TableInfo;
import club.spreadme.database.core.resource.ResourceHandler;
import club.spreadme.database.core.resultset.support.BeanRowMapper;
import club.spreadme.database.core.resultset.support.DefaultResultSetParser;
import club.spreadme.database.core.type.support.AbstractTypeHandler;
import club.spreadme.database.core.type.support.TypeHandlerRegiatrar;
import club.spreadme.database.exception.DAOMehtodException;
import club.spreadme.database.exception.DataBaseAccessException;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
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

    public TableInfoAcquirer use(Executor executor) {
        this.dataSource = executor.getDataSource();
        return tableInfoAcquirer;
    }

    public List<TableInfo> getTableInfo(String tableName) {
        return getTableInfo(tableName, "%");
    }

    public List<TableInfo> getTableInfo(String tableName, String columnName) {
        return getTableInfo("", "", tableName, columnName);
    }

    public List<TableInfo> getTableInfo(String catalog, String schema, String tableName, String columnName) {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = ResourceHandler.getConnection(dataSource);
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            return getTableInfo(databaseMetaData, catalog, schema, tableName, columnName);
        }
        catch (Exception ex) {
            throw new DataBaseAccessException(ex.getMessage());
        }
        finally {
            ResourceHandler.closeConnection(connection, dataSource);
        }
    }

    public List<TableInfo> getTableInfo(DatabaseMetaData databaseMetaData, String catalog, String schema, String tableName, String columnName) {
        ResultSet resultSet = null;
        try {
            resultSet = databaseMetaData.getColumns(catalog, schema, tableName, columnName);
            List<TableInfo> tableInfos = new DefaultResultSetParser<>(new BeanRowMapper<>(TableInfo.class)).parse(resultSet);
            tableInfos.forEach(tableInfo -> {
                AbstractTypeHandler<?> typeHandler = (AbstractTypeHandler<?>) TypeHandlerRegiatrar.getTypeHandler(JDBCType.valueOf(tableInfo.getData_type()));
                if (typeHandler == null) {
                    throw new DAOMehtodException("The JDBCType " + tableInfo.getType_name() + "are not support handle");
                }
                Type rawType = typeHandler.getRawType();
                tableInfo.setClazz((Class<?>) rawType);
            });
            return tableInfos;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new DataBaseAccessException(ex.getMessage());
        }
        finally {
            ResourceHandler.closeResultSet(resultSet);
        }
    }
}
