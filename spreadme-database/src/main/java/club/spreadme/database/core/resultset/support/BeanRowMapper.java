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

package club.spreadme.database.core.resultset.support;

import club.spreadme.database.core.resource.ResourceHandler;
import club.spreadme.database.core.resultset.RowMapper;
import club.spreadme.database.core.type.TypeHandler;
import club.spreadme.database.core.type.support.TypeHandlerRegiatrar;
import club.spreadme.database.exception.DataBaseAccessException;
import club.spreadme.lang.Reflection;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * The class use reflection map resultset to POJO,,String,Number,Date
 *
 * @author Wangshuwei
 * @since 2018-6-21
 */
public class BeanRowMapper<T> implements RowMapper<T> {

    private Class<T> clazz;

    public BeanRowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T mapping(ResultSet rs) throws Exception {
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();

        if (Reflection.isPrimaryType(clazz)) {
            if (columnCount > 1) {
                throw new DataBaseAccessException("Too much parameter from " + clazz.getName());
            }
            TypeHandler typeHandler = TypeHandlerRegiatrar.getTypeHandler(clazz);
            if (typeHandler == null) {
                throw new DataBaseAccessException("Not find typehandler for " + clazz);
            }
            return (T) typeHandler.getResult(rs, 1);

        }
        else {
            T entity = clazz.newInstance();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = ResourceHandler.getColumnName(resultSetMetaData, i);
                Field field = Reflection.findField(clazz, columnName);
                if (field != null) {
                    Reflection.makeAccessible(field, true);
                    TypeHandler typeHandler = TypeHandlerRegiatrar.getTypeHandler(field.getType());
                    if (typeHandler == null) {
                        throw new DataBaseAccessException("Not find typehandler for " + field.getType());
                    }
                    Reflection.setFieldValue(entity, columnName, typeHandler.getResult(rs, i));
                }
            }
            return entity;
        }

    }
}
