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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.JDBCType;
import java.util.Date;

public final class TypeHandlerRegiatrar {

    private static final Cache<JDBCType, TypeHandler<?>> JDBC_TYPE_HANDLER_CACHE = new ConcurrentMapCache<>("JDBC_TYPE_HANDLER_CACHE");
    private static final Cache<Class<?>, TypeHandler<?>> ALL_TYPE_HANDLER_CACHE = new ConcurrentMapCache<>("ALL_TYPE_HANDLER_CACHE");

    // default typehandler regiatar
    static {
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.BOOLEAN, new BooleanTypeHandler());
        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.BIT, new BooleanTypeHandler());

        JDBC_TYPE_HANDLER_CACHE.put(JDBCType.NCLOB, new NClobTypeHandler());
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

    // default class regiatar
    static {
        ALL_TYPE_HANDLER_CACHE.put(Boolean.class, new BooleanTypeHandler());
        ALL_TYPE_HANDLER_CACHE.put(boolean.class, new BooleanTypeHandler());

        ALL_TYPE_HANDLER_CACHE.put(Byte.class, new ByteTypeHandler());
        ALL_TYPE_HANDLER_CACHE.put(byte.class, new ByteTypeHandler());

        ALL_TYPE_HANDLER_CACHE.put(Short.class, new ShortTypeHandler());
        ALL_TYPE_HANDLER_CACHE.put(short.class, new ShortTypeHandler());

        ALL_TYPE_HANDLER_CACHE.put(Integer.class, new IntegerTypeHandler());
        ALL_TYPE_HANDLER_CACHE.put(int.class, new IntegerTypeHandler());

        ALL_TYPE_HANDLER_CACHE.put(Long.class, new LongTypeHandler());
        ALL_TYPE_HANDLER_CACHE.put(long.class, new LongTypeHandler());

        ALL_TYPE_HANDLER_CACHE.put(Float.class, new FloatTypeHandler());
        ALL_TYPE_HANDLER_CACHE.put(float.class, new FloatTypeHandler());

        ALL_TYPE_HANDLER_CACHE.put(Double.class, new DateTypeHandler());
        ALL_TYPE_HANDLER_CACHE.put(double.class, new DateTypeHandler());

        ALL_TYPE_HANDLER_CACHE.put(String.class, new StringTypeHandler());
        ALL_TYPE_HANDLER_CACHE.put(BigInteger.class, new BigIntegerTypeHandler());
        ALL_TYPE_HANDLER_CACHE.put(BigDecimal.class, new BigDecimalTypeHandler());

        ALL_TYPE_HANDLER_CACHE.put(byte[].class, new ByteArrayTypeHandler());
        ALL_TYPE_HANDLER_CACHE.put(Byte[].class, new ByteArrayTypeHandler());

        ALL_TYPE_HANDLER_CACHE.put(Date.class, new DateTypeHandler());
    }

    public static void registerTypeHandler(JDBCType jdbcType, TypeHandler<?> typeHandler) {
        JDBC_TYPE_HANDLER_CACHE.put(jdbcType, typeHandler);
    }

    public static void registerClass(Class<?> clazz, TypeHandler<?> typeHandler){
        ALL_TYPE_HANDLER_CACHE.put(clazz, typeHandler);
    }

    public static TypeHandler<?> getTypeHandler(JDBCType jdbcType) {
        return JDBC_TYPE_HANDLER_CACHE.get(jdbcType);
    }

    public static TypeHandler<?> getTypeHandler(Class<?> clazz){
        return ALL_TYPE_HANDLER_CACHE.get(clazz);
    }
}
