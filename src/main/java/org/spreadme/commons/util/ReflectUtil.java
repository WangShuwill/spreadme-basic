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

package org.spreadme.commons.util;

import java.io.IOException;
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
import java.util.Objects;
import java.util.stream.Collectors;

import org.spreadme.commons.lang.Assert;

/**
 * ReflectUtil
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class ReflectUtil {

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
			field.setAccessible(true);
			field.set(obj, value);
		}
		catch (IllegalAccessException e) {
			throw new IllegalStateException(e.getMessage(), e);
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
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static String getFieldName(Field field) {
		return field.getName();
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
				field.setAccessible(true);
				String name = getFieldName(field);
				Object value = getFieldValue(bean, field);
				properties.put(name, value);
			}
		}
		return properties;
	}

	/**
	 * get filed from Class
	 *
	 * @param type the Class
	 * @param name  Name of field
	 * @return Field
	 */
	public static Field findField(Class<?> type, String name) {
		try {
			Field field = type.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		}
		catch (NoSuchFieldException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * @param clazz Class
	 * @return Array of field
	 */
	public static Field[] getDeclareFields(Class<?> clazz) {
		Assert.notNull(clazz, "Class must be not null");
		return clazz.getDeclaredFields();
	}

	public static void doWithField(Class<?> clazz, FieldHandler handler, FieldFilter filter) {
		Assert.notNull(handler, "MethodHandler is null");
		Assert.notNull(filter, "MethodFilter is null");
		Field[] fields = clazz.getDeclaredFields();
		CollectionUtil.toList(fields).stream().filter(filter::match).forEach(handler::handle);
	}

	@FunctionalInterface
	public interface FieldHandler {
		void handle(Field field);
	}

	@FunctionalInterface
	public interface FieldFilter {
		boolean match(Field field);
	}

	public static Method findMethod(Class<?> type, String name, Class<?>... paramTypes) {
		try {
			return type.getDeclaredMethod(name, paramTypes);
		}
		catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	public static Object invokeMethod(Method method, Object target, Object... args) {
		try {
			return method.invoke(target, args);
		}
		catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static void doWithMethod(Class<?> type, MethodHandler handler, MethodFilter filter) {
		Assert.notNull(handler, "MethodHandler is null");
		Assert.notNull(filter, "MethodFilter is null");
		Method[] methods = type.getMethods();
		CollectionUtil.toList(methods).stream().filter(filter::match).forEach(handler::handle);
	}

	@FunctionalInterface
	public interface MethodHandler {
		void handle(Method method);
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
	private static AnnotationDefinition getAnnotationDefinition(Annotation annotation) {
		AnnotationDefinition annotationDefinition = new AnnotationDefinition();
		InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
		Field[] fields = getDeclareFields(invocationHandler.getClass());
		for (Field field : fields) {
			field.setAccessible(true);
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
		try {
			return FileUtil.getFiles(path, file -> file.getName().endsWith(".class"))
					.stream()
					.map(item -> Objects.requireNonNull(FileUtil.getRelativePath(path, item)).replace("/", "."))
					.collect(Collectors.toList());
		}
		catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	static class AnnotationDefinition {

		private Class<? extends Annotation> type;

		private Map<String, Object> attributes;

		public AnnotationDefinition() {

		}

		public AnnotationDefinition(Class<? extends Annotation> type, Map<String, Object> attributes) {
			this.type = type;
			this.attributes = attributes;
		}

		public Class<? extends Annotation> getType() {
			return type;
		}

		public void setType(Class<? extends Annotation> type) {
			this.type = type;
		}

		public Map<String, Object> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, Object> attributes) {
			this.attributes = attributes;
		}
	}
}
