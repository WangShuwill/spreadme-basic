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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * string util
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class StringUtil {

	public static final String SPACE = " ";

	public static final String EMPTY = "";

	public static final String LF = "\n";

	public static final String CR = "\r";

	public static final String DOT = ".";

	public static final String SLASH = "/";

	public static String STRINGS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	/**
	 * Convert an array of 8 bit characters into a string.
	 *
	 * @param bytes 8 bit characters.
	 * @return resulting String.
	 */
	public static String fromByteArray(byte[] bytes) {
		return new String(asCharArray(bytes));
	}

	/**
	 * Convert InputStream into a string.
	 *
	 * @param input InputStream
	 * @return resulting String.
	 */
	public static String fromInputStream(InputStream input) {
		return new BufferedReader(new InputStreamReader(input))
				.lines()
				.parallel()
				.collect(Collectors.joining(System.lineSeparator()));
	}

	/**
	 * Do a simple conversion of an array of 8 bit characters into a string.
	 *
	 * @param bytes 8 bit characters.
	 * @return resulting String.
	 */
	public static char[] asCharArray(byte[] bytes) {
		char[] chars = new char[bytes.length];
		for (int i = 0; i != chars.length; i++) {
			chars[i] = (char) (bytes[i] & 0xff);
		}
		return chars;
	}

	public static boolean isNotBlank(final CharSequence charSequence) {
		return !isBlank(charSequence);
	}

	public static boolean isBlank(final CharSequence charSequence) {
		int len;
		if (charSequence == null || (len = charSequence.length()) == 0) {
			return true;
		}
		for (int i = 0; i < len; i++) {
			if (!Character.isWhitespace(charSequence.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String trimAll(final String source) {
		if (isBlank(source)) {
			return source;
		}
		int len = source.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = source.charAt(i);
			if (!Character.isWhitespace(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String trimStart(final String source) {
		if (isBlank(source)) {
			return source;
		}
		StringBuilder sb = new StringBuilder(source);
		while (sb.length() > 1 && Character.isWhitespace(sb.charAt(0))) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

	public static String trimEnd(final String source) {
		if (isBlank(source)) {
			return source;
		}
		StringBuilder sb = new StringBuilder(source);
		while (sb.length() > 1 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static String trimStart(final String source, final char startChar) {
		if (isBlank(source)) {
			return source;
		}
		StringBuilder sb = new StringBuilder(source);
		while (sb.length() > 1 && sb.charAt(0) == startChar) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

	public static String trimEnd(final String source, final char endChar) {
		if (isBlank(source)) {
			return source;
		}
		StringBuilder sb = new StringBuilder(source);
		while (sb.length() > 1 && sb.charAt(sb.length() - 1) == endChar) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static String trimStart(final String source, final String startStr) {
		if (isBlank(source) || isBlank(startStr)) {
			return source;
		}
		int len = startStr.length();
		StringBuilder sb = new StringBuilder(source);
		for (int i = 0; i < len; i++) {
			if (len <= sb.length() && sb.charAt(0) == startStr.charAt(i)) {
				sb.deleteCharAt(0);
			}
		}
		return sb.toString();
	}

	public static String trimEnd(final String source, final String endStr) {
		if (isBlank(source) || isBlank(endStr)) {
			return source;
		}
		int len = endStr.length();
		StringBuilder sb = new StringBuilder(source);
		for (int i = len; i > 0; i--) {
			if (len <= sb.length() && sb.charAt(sb.length() - 1) == endStr.charAt(i - 1)) {
				sb.deleteCharAt(sb.length() - 1);
			}
		}
		return sb.toString();
	}

	public static String toUpper(final String source, Integer... indexs) {
		if (isBlank(source) || indexs.length < 1) {
			return source;
		}
		char[] chars = source.toCharArray();
		if (indexs.length > chars.length) {
			return source;
		}
		for (Integer index : indexs) {
			chars[index] -= 32;
		}
		return new String(chars);
	}

	public static String randomString(int length) {
		if (length < 1) {
			return null;
		}
		ThreadLocalRandom random = ThreadLocalRandom.current();
		StringBuilder strings = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(STRINGS.length());
			strings.append(STRINGS.charAt(index));
		}
		return strings.toString();
	}

}
