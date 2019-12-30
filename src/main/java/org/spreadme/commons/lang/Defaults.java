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

package org.spreadme.commons.lang;

/**
 * Defaults
 * @author shuwei.wang
 */
public final class Defaults {

	private Defaults() {
	}

	private static final Double DOUBLE_DEFAULT = 0d;

	private static final Float FLOAT_DEFAULT = 0f;

	@SuppressWarnings("unchecked")
	public static <T> T defaultValue(Class<T> type) {
		if (type == boolean.class) {
			return (T) Boolean.FALSE;
		}
		else if (type == char.class) {
			return (T) Character.valueOf('\0');
		}
		else if (type == byte.class) {
			return (T) Byte.valueOf((byte) 0);
		}
		else if (type == short.class) {
			return (T) Short.valueOf((short) 0);
		}
		else if (type == int.class) {
			return (T) Integer.valueOf(0);
		}
		else if (type == long.class) {
			return (T) Long.valueOf(0L);
		}
		else if (type == float.class) {
			return (T) FLOAT_DEFAULT;
		}
		else if (type == double.class) {
			return (T) DOUBLE_DEFAULT;
		}
		else {
			return null;
		}
	}

	public static <T> T of(Class<T> clazz) {
		return defaultValue(clazz);
	}
}
