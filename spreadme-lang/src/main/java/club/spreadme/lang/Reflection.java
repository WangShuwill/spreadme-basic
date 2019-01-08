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

package club.spreadme.lang;

import club.spreadme.lang.reflection.AnnotationDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * reflection util
 *
 * @author wswei
 * @since 2018
 */
public abstract class Reflection {

    public static final List<?> PRIMARYTYPE = Arrays.asList(
            Byte.class, Short.class, Integer.class,
            Long.class, Double.class, Float.class,
            BigDecimal.class, BigInteger.class, String.class, Date.class
    );

    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = findField(obj.getClass(), fieldName);
        if (field != null) {
            setFieldValue(field, obj, value);
        }
    }

    public static void setFieldValue(Field field, Object obj, Object value) {
        try {
            makeAccessible(field, true);
            field.set(obj, value);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object getFieldValue(Object obj, Field field) {
        try {
            return field.get(obj);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFieldName(Field field) {
        return field.getName();
    }

    public static void makeAccessible(Field field, boolean isAccessible) {
        Assert.notNull(field, "field is not null");
        field.setAccessible(isAccessible);
    }

    public static boolean isPrimaryField(Field field) {
        return isPrimaryType(field.getType());
    }

    public static boolean isPrimaryType(Class<?> type) {
        return PRIMARYTYPE.contains(type);
    }

    public static Map<String, Object> parseBeanToMap(Object bean) {
        Map<String, Object> properties = new HashMap<>();
        for (Field field : getDeclareFields(bean.getClass())) {
            if (isPrimaryField(field)) {
                makeAccessible(field, true);
                String name = getFieldName(field);
                Object value = getFieldValue(bean, field);
                properties.put(name, value);
            }
        }
        return properties;
    }

    public static Field findFieldAccessible(Class<?> clazz, String name) {
        return findField(clazz, name, true);
    }

    public static Field findField(Class<?> clazz, String name, boolean isAccessible) {
        Field field = findField(clazz, name);
        makeAccessible(field, isAccessible);
        return field;
    }

    /**
     * get field from class
     *
     * @param clazz the Class
     * @param name  name of field
     * @return Field
     */
    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, null);
    }

    /**
     * get filed from Class
     *
     * @param clazz the Class
     * @param name  Name of field
     * @param type  Class type of field
     * @return Field
     */
    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        Assert.notNull(clazz, "Class must be not null");
        Field[] fields = getDeclareFields(clazz);
        for (Field field : fields) {
            if (ObjectUtil.equals(name, field.getName()) && (type == null || ObjectUtil.equals(type, field.getType()))) {
                return field;
            }
        }
        return null;
    }

    /**
     * @param clazz Class
     * @return Array of field
     */
    public static Field[] getDeclareFields(Class<?> clazz) {
        Assert.notNull(clazz, "Class must be not null");
        return clazz.getDeclaredFields();
    }

    public static void doWithField(Class<?> clazz, FieldHandler fieldHandler) {
        doWithField(clazz, fieldHandler, null);
    }

    public static void doWithField(Class<?> clazz, FieldHandler fieldHandler, FieldFilter fieldFilter) {
        Field[] fields = getDeclareFields(clazz);
        for (Field field : fields) {
            if (fieldFilter != null && !fieldFilter.match(field)) {
                continue;
            }
            try {
                fieldHandler.handle(field);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FunctionalInterface
    public interface FieldHandler {
        void handle(Field field) throws Exception;
    }

    @FunctionalInterface
    public interface FieldFilter {
        boolean match(Field field);
    }

    public static Method findMethod(Class<?> clazz, String name) {
        return findMethod(clazz, name, new Class<?>[0]);
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must be not null");
        Method[] methods = clazz.isInterface() ? clazz.getMethods() : getDeclareMethods(clazz);
        for (Method method : methods) {
            if (ObjectUtil.equals(name, method.getName()) &&
                    (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                return method;
            }
        }
        return null;
    }

    public static Method[] getDeclareMethods(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }

    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, new Object[0]);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void doWithMethod(Class<?> clazz, MethodHandler methodHandler, MethodFilter methodFilter) {
        Method[] methods = getDeclareMethods(clazz);
        for (Method method : methods) {
            if (methodFilter != null && !methodFilter.match(method)) {
                continue;
            }
            try {
                methodHandler.handle(method);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FunctionalInterface
    public interface MethodHandler {
        void handle(Method method) throws Exception;
    }

    @FunctionalInterface
    public interface MethodFilter {
        boolean match(Method method);
    }

    public static Object getAnnotationAttriValue(AnnotatedElement annotatedElement, Class<? extends Annotation> annoClass, String key) {
        Annotation annotation = getAnnotation(annotatedElement, annoClass);
        return getAnnotationAttriValue(annotation, key);
    }

    public static Map<String, Object> getAnnotationAttributes(AnnotatedElement annotatedElement, Class<? extends Annotation> annoClass) {
        Annotation annotation = getAnnotation(annotatedElement, annoClass);
        return getAnnotationAttributes(annotation);
    }

    public static Object getAnnotationAttriValue(Annotation annotation, String key) {
        Map<String, Object> attributes = getAnnotationAttributes(annotation);
        if (attributes != null && attributes.size() > 1) {
            return attributes.get(key);
        }
        return null;
    }

    public static Map<String, Object> getAnnotationAttributes(Annotation annotation) {
        return getAnnotationDefinition(annotation).getAttributes();
    }

    @SuppressWarnings("unchecked")
    public static AnnotationDefinition getAnnotationDefinition(Annotation annotation) {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        Field[] fields = getDeclareFields(invocationHandler.getClass());
        for (Field field : fields) {
            makeAccessible(field, true);
            if (ObjectUtil.equals(field.getType(), Class.class)) {
                annotationDefinition.setType((Class) getFieldValue(invocationHandler, field));
            }
            else if (ObjectUtil.equals(field.getType(), Map.class)) {
                annotationDefinition.setAttributes((Map<String, Object>) getFieldValue(invocationHandler, field));
            }
        }
        return annotationDefinition;
    }

    public static <T extends Annotation> T getAnnotation(Object obj, Class<T> annoClass) {
        return getAnnotation(obj.getClass(), annoClass);
    }

    public static <T extends Annotation> T getAnnotation(AnnotatedElement annotatedElement, Class<T> annoClass) {
        return annotatedElement.getAnnotation(annoClass);
    }

    public static Annotation[] getAnnotations(AnnotatedElement annotatedElement) {
        return annotatedElement.getAnnotations();
    }

    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }
}
