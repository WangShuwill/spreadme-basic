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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * ClassUtil
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class ClassUtil {

	private static final String MAIN_METHOD_NAME = "main";

	/**
	 * get the thread context class loader
	 *
	 * @return ClassLoader
	 */
	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassUtil.class.getClassLoader();
			if (classLoader == null) {
				classLoader = ClassLoader.getSystemClassLoader();
			}
		}
		return classLoader;
	}

	public static String getClassPath() {
		return new File(Objects.requireNonNull(getClassLoader().getResource(StringUtil.EMPTY)).getPath()).getPath();
	}

	public static InputStream getResourceAsStream(String resource) {
		return getClassLoader().getResourceAsStream(resource);
	}

	public static Set<String> getClassPaths(String packageName, boolean isDecode) {
		String packagePath = packageName.replace(StringUtil.DOT, StringUtil.SLASH);
		final Set<String> paths = new HashSet<>();
		try {
			Enumeration<URL> resources = getClassLoader().getResources(packagePath);
			while (resources.hasMoreElements()) {
				String path = resources.nextElement().getPath();
				paths.add(isDecode ? URLDecoder.decode(path, StandardCharsets.UTF_8.name()) : path);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return paths;
	}

	/**
	 * 获取含有main方法的类
	 *
	 * @return main方法的类
	 */
	public static Class<?> deduceMainClass() {
		try {
			StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
			for (StackTraceElement traceElement : stackTrace) {
				if (MAIN_METHOD_NAME.equals(traceElement.getMethodName())) {
					return Class.forName(traceElement.getClassName());
				}
			}
		}
		catch (ClassNotFoundException ignored) {

		}
		return null;
	}
}
