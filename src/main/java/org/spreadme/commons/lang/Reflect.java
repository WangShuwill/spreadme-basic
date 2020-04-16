/*
 * Copyright [4/2/20 9:26 AM] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.lang;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.spreadme.commons.util.CollectionUtil;

/**
 * Reflect
 * @author shuwei.wang
 */
public class Reflect {

	// 基本类型
	private static final List<Class> primaryTypes;
	// 包装类型
	private static final List<Class> wrapTypes;

	static {
		List<Class> primaries = CollectionUtil.toList(
				boolean.class, char.class, byte.class, short.class,
				int.class, long.class, float.class, double.class, void.class
		);
		List<Class> wrapes = CollectionUtil.toList(
				Boolean.class, Character.class, Byte.class, Short.class,
				Integer.class, Long.class, Float.class, Double.class, Void.class
		);
		primaryTypes = Collections.unmodifiableList(primaries);
		wrapTypes = Collections.unmodifiableList(wrapes);
	}

	// the type of wrapped object
	private final Class<?> type;
	// the wrapped object
	private final Object object;

	private Reflect(Class<?> type, Object object) {
		this.type = type;
		this.object = object;
	}

	private Reflect(Class<?> type) {
		this(type, type);
	}

	/**
	 * create Reflect by type and object
	 * @param type type of object
	 * @param object object
	 * @return Reflect
	 */
	public static Reflect of(Class<?> type, Object object) {
		return new Reflect(type, object);
	}

	/**
	 * create Reflect by object
	 * @param object object
	 * @return Reflect
	 */
	public static Reflect of(Object object) {
		return new Reflect(object == null ? Object.class : object.getClass(), object);
	}

	/**
	 * create Reflect by constructor function
	 * @param constructor constructor
	 * @param args constructor arguments
	 * @return Reflect
	 */
	public static Reflect of(Constructor<?> constructor, Object... args) {
		try {
			return of(constructor.getDeclaringClass(),
					Objects.requireNonNull(accessible(constructor)).newInstance(args));
		}
		catch (Exception e) {
			throw new ReflectException(e);
		}
	}

	public static Reflect ofClass(Class<?> type) {
		return new Reflect(type);
	}

	public static Reflect ofClass(String className, ClassLoader classLoader) {
		return new Reflect(forName(className, classLoader));
	}

	public static Reflect ofClass(String className) {
		return new Reflect(forName(className));
	}

	public Reflect create(Object... args) {
		Class<?>[] types = types(args);
		try {
			Constructor<?> constructor = type().getDeclaredConstructor(types);
			return of(constructor, args);
		}
		catch (NoSuchMethodException e) {
			for (Constructor<?> constructor : type().getDeclaredConstructors()) {
				if (match(constructor.getParameterTypes(), types)) {
					return of(constructor, args);
				}
			}
			throw new ReflectException(e);
		}
	}

	public Reflect create() {
		return this.create(new Object[0]);
	}

	public Reflect invoke(String methodName, Object... args) {
		Class<?>[] types = types(args);
		try {
			Method method = findMethod(methodName, types);
			return invoke(method, this.object, args);
		}
		catch (NoSuchMethodException e) {
			try {
				Method method = findSimilarMethod(methodName, types);
				return invoke(method, this.object, args);
			}
			catch (NoSuchMethodException e1) {
				throw new ReflectException(e);
			}
		}
	}

	public Reflect field(String fieldName) {
		try {
			Field field = this.findField(fieldName);
			return of(field.getType(), field.get(this.object));
		}
		catch (Exception e) {
			throw new ReflectException(e);
		}
	}

	public Map<String, Reflect> fields() {
		Map<String, Reflect> result = new LinkedHashMap<>();
		Class<?> t = this.type();
		do {
			for (Field field : t.getDeclaredFields()) {
				if (type != object ^ Modifier.isStatic(field.getModifiers())) {
					String name = field.getName();
					if (!result.containsKey(name)) {
						result.put(name, this.field(name));
					}
				}
			}
			t = t.getSuperclass();
		}
		while (t != null);
		return result;
	}

	public Reflect set(String fieldName, Object value) {
		try {
			Field field = this.findField(fieldName);
			if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
				try {
					Field modifiersField = Field.class.getDeclaredField("modifiers");
					modifiersField.setAccessible(true);
					modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
				}
				catch (NoSuchFieldException ignore) {
				}
			}
			field.set(this.object, value);
			return this;
		}
		catch (Exception e) {
			throw new ReflectException(e);
		}
	}

	public Field findField(String fieldName) throws NoSuchFieldException {
		Class<?> t = this.type();
		try {
			return accessible(t.getField(fieldName));
		}
		catch (NoSuchFieldException ex) {
			do {
				try {
					return accessible(t.getDeclaredField(fieldName));
				}
				catch (NoSuchFieldException ignore) {

				}
				t = t.getSuperclass();
			}
			while (t != null);
			throw new NoSuchFieldException();
		}
	}

	public Method findMethod(String methodName, Class<?>... types) throws NoSuchMethodException {
		Class<?> t = this.type();
		try {
			return t.getDeclaredMethod(methodName, types);
		}
		catch (NoSuchMethodException e) {
			do {
				try {
					return t.getDeclaredMethod(methodName, types);
				}
				catch (NoSuchMethodException ignore) {
				}
				t = t.getSuperclass();
			}
			while (t != null);
			throw new NoSuchMethodException();
		}
	}

	public Method findSimilarMethod(String methodName, Class<?>... types) throws NoSuchMethodException {
		Class<?> t = this.type();
		for (Method method : t.getMethods()) {
			if (isSimilarSignature(method, methodName, types)) {
				return method;
			}
		}

		do {
			for (Method method : t.getDeclaredMethods()) {
				if (isSimilarSignature(method, methodName, types)) {
					return method;
				}
			}
			t = t.getSuperclass();
		}
		while (t != null);
		throw new NoSuchMethodException("No similar method " + methodName + " with params " + Arrays.toString(types) + " could be found on type " + type() + ".");
	}

	/**
	 * is similar method signature
	 * match the parameter types by primary types and wraped types
	 * @param method Method
	 * @param desiredMethodName desired method name
	 * @param desiredParamTypes desired parameter types
	 * @return is similar method
	 */
	private boolean isSimilarSignature(Method method, String desiredMethodName, Class<?>[] desiredParamTypes) {
		return method.getName().equals(desiredMethodName) && match(method.getParameterTypes(), desiredParamTypes);
	}

	/**
	 * match the declared types and actual types
	 * match the primary types and wraped types
	 * @param declaredTypes declared types
	 * @param actualTypes actual types
	 * @return is matched
	 */
	private boolean match(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
		if (declaredTypes.length == actualTypes.length) {
			for (int i = 0; i < actualTypes.length; i++) {
				if (actualTypes[i] == Null.class) {
					continue;
				}
				if (getWrapType(declaredTypes[i]).isAssignableFrom(getWrapType(actualTypes[i]))) {
					continue;
				}
				return false;
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public <T> T get() {
		return (T) this.object;
	}

	public <T> T get(String fieldName) {
		return this.field(fieldName).get();
	}

	public Class<?> type() {
		return this.type;
	}

	@Override
	public String toString() {
		return "Reflect{" +
				"type=" + type +
				", object=" + object +
				'}';
	}

	public static Reflect compile(String name, String content, CompileOptions options) throws CompileException {
		return Reflect.ofClass(Compile.compile(name, content, options));
	}

	public static Reflect compile(String name, String content) throws CompileException {
		return Reflect.ofClass(Compile.compile(name, content, new CompileOptions()));
	}

	public static Reflect invoke(Method method, Object object, Object... args) {
		try {
			accessible(method);
			if (method.getReturnType() == void.class) {
				method.invoke(object, args);
				return of(object);
			}
			else {
				return of(method.invoke(object, args));
			}
		}
		catch (Exception e) {
			throw new ReflectException(e);
		}
	}

	/**
	 * make the accessible object accessible
	 * @param accessible AccessibleObject {@link AccessibleObject}
	 * @return The argument object rendered accessible
	 */
	public static <T extends AccessibleObject> T accessible(T accessible) {
		if (Objects.isNull(accessible)) {
			return null;
		}
		if (accessible instanceof Member) {
			Member member = (Member) accessible;
			if (Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
				return accessible;
			}
		}
		if (!accessible.isAccessible()) {
			accessible.setAccessible(true);
		}
		return accessible;
	}

	public static Class<?>[] types(Object... values) {
		if (values == null) {
			return new Class[0];
		}
		Class<?>[] result = new Class[values.length];
		for (int i = 0; i < values.length; i++) {
			Object value = values[i];
			result[i] = value == null ? Null.class : value.getClass();
		}
		return result;
	}

	public static Class<?> forName(String className) {
		try {
			return Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			throw new ReflectException(e);
		}
	}

	public static Class<?> forName(String className, ClassLoader classLoader) {
		try {
			return Class.forName(className, true, classLoader);
		}
		catch (ClassNotFoundException e) {
			throw new ReflectException(e);
		}
	}

	public static Class<?> getWrapType(Class<?> type) {
		if (type == null) {
			return null;
		}
		else if (type.isPrimitive()) {
			int index = primaryTypes.indexOf(type);
			if (index != -1) {
				type = wrapTypes.get(index);
			}
		}
		return type;
	}

	public static Class getSuperClassGenricType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		return getClassByType(params, index);
	}

	public static Class getInterfaceGenericType(Class clazz, int index) {
		Type[] types = clazz.getGenericInterfaces();
		ParameterizedType parameterizedType = null;
		for (Type type : types) {
			if (type instanceof ParameterizedType) {
				parameterizedType = (ParameterizedType) type;
				break;
			}
		}
		if (parameterizedType == null) {
			return Object.class;
		}
		Type[] params = parameterizedType.getActualTypeArguments();
		return getClassByType(params, index);
	}

	private static Class getClassByType(Type[] params, int index) {
		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}

	public static <T> T initValue(Class<T> type) {
		return Defaults.of(type);
	}

	public static class Null {
	}
}
