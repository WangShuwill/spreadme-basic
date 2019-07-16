/*
 * Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package club.spreadme.core.utils;

import club.spreadme.core.exception.ConvertException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ConverteUtil {

    public static <T> T toNumber(final Class<?> sourceType, final Class<T> targetType, final Number value) {
        // if it is number,return
        if (targetType.equals(value.getClass())) {
            return targetType.cast(value);
        }
        // Bytes
        if (targetType.equals(Byte.class)) {
            if (value.longValue() > Byte.MAX_VALUE || value.longValue() < Byte.MIN_VALUE) {
                throw new ConvertException(value + " out of range for byte");
            }
            return targetType.cast(value.byteValue());
        }
        // Short
        if (targetType.equals(Short.class)) {
            if (value.longValue() > Short.MAX_VALUE || value.longValue() < Short.MIN_VALUE) {
                throw new ConvertException(value + " out of range for byte");
            }
            return targetType.cast(value.shortValue());
        }
        //Intager
        if (targetType.equals(Integer.class)) {
            if (value.longValue() > Integer.MAX_VALUE || value.longValue() < Integer.MIN_VALUE) {
                throw new ConvertException(value + " out of range for byte");
            }
            return targetType.cast(value.intValue());
        }
        // Long
        if (targetType.equals(Long.class)) {
            return targetType.cast(value.longValue());
        }
        //Float
        if (targetType.equals(Float.class)) {
            if (value.doubleValue() > Float.MAX_VALUE) {
                throw new ConvertException(value + " out of range for byte");
            }
            return targetType.cast(value.floatValue());
        }
        // Double
        if (targetType.equals(Double.class)) {
            return targetType.cast(value.doubleValue());
        }
        // BigDecimal
        if (targetType.equals(BigDecimal.class)) {
            if (value instanceof Float || value instanceof Double) {
                return targetType.cast(new BigDecimal(value.toString()));
            }
            else if (value instanceof BigInteger) {
                return targetType.cast(new BigDecimal((BigInteger) value));
            }
            else if (value instanceof BigDecimal) {
                return targetType.cast(new BigDecimal(value.toString()));
            }
            else {
                return targetType.cast(value.longValue());
            }

        }
        // BigInteger
        if (targetType.equals(BigInteger.class)) {
            if (value instanceof BigDecimal) {
                return targetType.cast(((BigDecimal) value).toBigInteger());
            }
            else {
                return targetType.cast(value.longValue());
            }
        }

        throw new ConvertException("Can not convert this type");
    }

    public static String toString(final Object object) {
        if (object == null) {
            throw new ConvertException("Null can not convert to string");
        }
        if (object instanceof String) {
            return (String) object;
        }
        return object.toString();
    }

    public static Date toDate(final Object object) {
        if (object == null) {
            throw new ConvertException("Null can not convert to date");
        }
        if (object instanceof Date) {
            return (Date) object;
        }
        try {
            return new SimpleDateFormat().parse(object.toString());
        }
        catch (ParseException ex) {
            throw new ConvertException(ex.getMessage());
        }
    }

}
