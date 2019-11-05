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

/**
 * math util
 * @author shuwei.wang
 */
public abstract class MathUtil {

	private static final String[] UNITS = new String[] {"", "K", "M", "G", "T", "P", "E"};

	public static String lengthToUnit(long length) {
		for (int i = 6; i > 0; i--) {
			// 1024 is for 1024 kb is 1 MB etc
			double step = Math.pow(1024, i);
			if (length > step) {
				return String.format("%3.1f%s", length / step, UNITS[i]);
			}
		}
		return Long.toString(length);
	}

	public static Number toNumber(Object num) {
		if (num == null) {
			return null;
		}
		if (num instanceof Number) {
			return (Number) num;
		}
		try {
			return parseNumber(String.valueOf(num));
		}
		catch (NumberFormatException nfe) {
			return null;
		}
	}

	protected static Number parseNumber(String value) throws NumberFormatException {
		if (!value.contains(StringUtil.DOT)) {
			long i = new Long(value);
			return i > Integer.MAX_VALUE || i < Integer.MIN_VALUE ? new Long(i) : new Integer((int) i);
		}
		else {
			return new Double(value);
		}
	}
}
