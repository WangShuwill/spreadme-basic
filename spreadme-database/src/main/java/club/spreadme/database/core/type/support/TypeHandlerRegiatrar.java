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

package club.spreadme.database.core.type.support;

import club.spreadme.database.core.type.TypeHandler;
import club.spreadme.lang.cache.Cache;
import club.spreadme.lang.cache.support.ConcurrentMapCache;

import java.sql.JDBCType;

public final class TypeHandlerRegiatrar {

    private static final Cache<JDBCType, TypeHandler<?>> JDBC_TYPE_HANDLER_CACHE = new ConcurrentMapCache<>("JDBC_TYPE_HANDLER_CACHE");

    static {
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.BOOLEAN, new BooleanTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.BIT, new BooleanTypeHandler());

        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.TINYINT, new ByteTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.SMALLINT, new ShortTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.INTEGER, new IntegerTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.BIGINT, new LongTypeHandler());

        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.FLOAT, new FloatTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.DOUBLE, new DoubleTypeHandler());

        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.DECIMAL, new BigDecimalTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.NUMERIC, new BigIntegerTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.REAL, new BigDecimalTypeHandler());

        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.CHAR, new StringTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.VARCHAR, new StringTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.NCHAR, new StringTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.NVARCHAR, new StringTypeHandler());

        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.TIMESTAMP, new DateTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.DATE, new DateTypeHandler());
    }

    public static TypeHandler<?> getTypeReference(JDBCType jdbcType) {
        return JDBC_TYPE_HANDLER_CACHE.get(jdbcType);
    }
}
