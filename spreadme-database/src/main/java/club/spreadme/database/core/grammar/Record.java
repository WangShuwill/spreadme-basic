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

package club.spreadme.database.core.grammar;

import club.spreadme.database.annotation.Table;
import club.spreadme.lang.Converter;
import club.spreadme.lang.Reflection;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Wangshuwei
 * @since 2018-6-23
 */
public class Record extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 2753780193138921L;

    private String tableName;
    private String primaryKeyName;

    public Record() {
        super();
    }

    public Record(int size) {
        super(size);
    }

    public Record(String tableName) {
        super();
        this.tableName = tableName;
    }

    public Record(Object object) {
        super();
        Table table = object.getClass().getAnnotation(Table.class);
        this.tableName = table.name();
        this.primaryKeyName = table.primarykey().toLowerCase();
        this.putAll(Reflection.parseBeanToMap(object));
    }

    public Record setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public Record setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
        return this;
    }

    public Record setPrimayKeyValue(String primaryKey, Object primaryValue){
        this.primaryKeyName = primaryKey;
        this.set(primaryKeyName,primaryValue);
        return this;
    }

    public Record set(String columnName, Object value) {
        put(columnName, value);
        return this;
    }

    public String getString(String columnName) {
        return Converter.toString(get(columnName));
    }

    public Date getDate(String columnName) {
        return Converter.toDate(get(columnName));
    }

    public Byte getByte(String columnName) {
        Object value = get(columnName);
        return Converter.toNumber(value.getClass(), Byte.class, (Number) value);
    }

    public Short getShort(String columnName) {
        Object value = get(columnName);
        return Converter.toNumber(value.getClass(), Short.class, (Number) value);
    }

    public Integer getInt(String columnName) {
        Object value = get(columnName);
        return Converter.toNumber(value.getClass(), Integer.class, (Number) value);
    }

    public Long getLong(String columnName) {
        Object value = get(columnName);
        return Converter.toNumber(value.getClass(), Long.class, (Number) value);
    }

    public Double getDouble(String columnName) {
        Object value = get(columnName);
        return Converter.toNumber(value.getClass(), Double.class, (Number) value);
    }

    public BigDecimal getBigDecimal(String columnName) {
        Object value = get(columnName);
        return Converter.toNumber(value.getClass(), BigDecimal.class, (Number) value);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
