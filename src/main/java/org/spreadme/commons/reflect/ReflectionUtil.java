/*
 *    Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.spreadme.commons.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.spreadme.commons.reflect.model.AnnotationDefinition;
import org.spreadme.commons.util.Assert;
import org.spreadme.commons.util.FileUtil;

/**
 * ReflectionUtil
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class ReflectionUtil {

	private static List<String> primitiveNames;

	private static List<Class> primitiveTypes;

	private static List<String> primitiveDescriptors;

	static {
		primitiveNames = Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");
		primitiveTypes = Arrays.asList(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class);
		primitiveDescriptors = Arrays.asList("Z", "C", "B", "S", "I", "J", "F", "D", "V");
	}

	public static List<String> getPrimitiveNames() {
		return primitiveNames;
	}

	public static List<Class> getPrimitiveTypes() {
		return primitiveTypes;
	}

	public static List<String> getPrimitiveDescriptors() {
		return primitiveDescriptors;
	}

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

	public static Object getFieldValue(Object obj, String fieldName) {
		Field field = findField(obj.getClass(), fieldName);
		return field == null ? null : getFieldValue(obj, field);
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
		return primitiveTypes.contains(type);
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
			if (name.equalsIgnoreCase(field.getName()) && (type == null || type.equals(field.getType()))) {
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
			if (name.equals(method.getName()) &&
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
			if (field.getType().equals(Class.class)) {
				annotationDefinition.setType((Class) getFieldValue(invocationHandler, field));
			}
			else if (field.getType().equals(Map.class)) {
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

	public static Class<?> forName(String typeName, ClassLoader... classLoaders) {
		if (primitiveNames.contains(typeName)) {
			return primitiveTypes.get(primitiveNames.indexOf(typeName));
		}
		String type;
		if (typeName.contains("[")) {
			int i = typeName.indexOf("[");
			type = typeName.substring(0, i);
			String array = typeName.substring(i).replace("]", "");
			if (getPrimitiveNames().contains(type)) {
				type = getPrimitiveDescriptors().get(getPrimitiveNames().indexOf(type));
			}
			else {
				type = "L" + type + ";";
			}
			type = array + type;
		}
		else {
			type = typeName;
		}
		for (ClassLoader classLoader : classLoaders) {
			if (type.contains("[")) {
				try {
					return Class.forName(type, false, classLoader);
				}
				catch (ClassNotFoundException e) {
					throw new IllegalStateException(e.getMessage(), e);
				}
			}
			try {
				return classLoader.loadClass(type);
			}
			catch (ClassNotFoundException e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
		}
		return null;
	}

	public static List<String> scanTypeNames(String path) {
		return FileUtil.getFiles(path, file -> file.getName().endsWith(".class"))
				.stream()
				.map(item -> FileUtil.getRelativePath(path, item).replace("/", "."))
				.collect(Collectors.toList());
	}
}
