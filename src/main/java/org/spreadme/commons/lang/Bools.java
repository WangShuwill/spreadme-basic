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
 * boolean util
 * @author shuwei.wang
 */
public abstract class Bools {

	public static boolean isTure(Boolean bool) {
		return Boolean.TRUE.equals(bool);
	}

	public static boolean isNotTure(Boolean bool) {
		return !isTure(bool);
	}

	public static boolean isFalse(Boolean bool) {
		return Boolean.FALSE.equals(bool);
	}

	public static boolean isNotFalse(Boolean bool) {
		return !isFalse(bool);
	}
}
