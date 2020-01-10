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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.stream.Collectors;

import org.spreadme.commons.lang.Randoms;

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

	public static final String STRINGS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static final char[] CHARS = STRINGS.toCharArray();

	public static final int INDEX_NOT_FOUND = -1;

	private StringUtil() {

	}

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

	/**
	 * is it not Blank
	 *
	 * @param charSequence charSequence
	 * @return is it not Blank?
	 */
	public static boolean isNotBlank(final CharSequence charSequence) {
		return !isBlank(charSequence);
	}

	/**
	 * is it Blank
	 *
	 * @param charSequence charSequence
	 * @return is it Blank
	 */
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

	/**
	 * trim all whitespace
	 *
	 * @param source source
	 * @return trim all result
	 */
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

	/**
	 * trim whitespace at start of string
	 *
	 * @param source source
	 * @return trim start of string
	 */
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

	/**
	 * trim whitespace at end of string
	 *
	 * @param source source
	 * @return trim end of string
	 */
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

	@Deprecated
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

	/**
	 * 随机生成字符串
	 *
	 * @param length 长度
	 * @return 随机字符串
	 */
	public static String randomString(final int length) {
		return randomString(STRINGS, length);
	}

	/**
	 * 根据传入的字符串生成随机的字符串
	 *
	 * @param base 基本字符串
	 * @param length 长度
	 * @return 随机字符串
	 */
	public static String randomString(String base, final int length) {
		if (isBlank(base) || length < 1 || base.length() < length) {
			return null;
		}
		StringBuilder strings = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int index = Randoms.nextInt(base.length());
			strings.append(base.charAt(index));
		}
		return strings.toString();
	}

	/**
	 * repeat character with length
	 *
	 * @param c character
	 * @param length length
	 * @return result string
	 */
	public static String repeat(char c, int length) {
		StringBuilder builder = new StringBuilder();
		if (length < 1) {
			return builder.append(c).toString();
		}
		for (int i = 0; i < length; i++) {
			builder.append(c);
		}
		return builder.toString();
	}

	/**
	 * null string to empty string
	 *
	 * @param text text
	 * @return result
	 */
	public static String noneNullString(String text) {
		return text == null ? "" : text;
	}

	/**
	 * 替换字符串
	 *
	 * @param base 字符串
	 * @param oldPattern 被替换的字符串
	 * @param newPattern 新的字符串
	 * @return result
	 */
	public static String replace(String base, String oldPattern, String newPattern) {
		if (isBlank(base) || isBlank(oldPattern) || newPattern == null) {
			return base;
		}
		int index = base.indexOf(oldPattern);
		if (index == INDEX_NOT_FOUND) {
			return base;
		}
		// 初始化StringBuilder容量
		int capacity = base.length();
		if (newPattern.length() > oldPattern.length()) {
			capacity += 16;
		}
		StringBuilder sb = new StringBuilder(capacity);

		int pos = 0;
		int oldLen = oldPattern.length();
		while (index >= 0) {
			sb.append(base, pos, index);
			sb.append(newPattern);
			pos = index + oldLen;
			index = base.indexOf(oldPattern, pos);
		}

		// 追加剩余的字符串
		sb.append(base.substring(pos));
		return sb.toString();
	}

	/**
	 * delete the string form this string
	 *
	 * @param text text
	 * @param deleted will be deleted string
	 * @return result
	 */
	public static String delete(String text, String deleted) {
		return replace(text, deleted, EMPTY);
	}

	/**
	 * string to unicode
	 *
	 * @param string string
	 * @return unicode string
	 */
	public static String stringToUnicode(String string) {
		if (StringUtil.isBlank(string)) {
			return string;
		}
		StringBuilder unicode = new StringBuilder();
		for (int i = 0, l = string.length(); i < l; i++) {
			char c = string.charAt(i);
			unicode.append("\\u").append(Integer.toHexString(c));
		}
		return unicode.toString();
	}

	/**
	 * unicode to string
	 *
	 * @param unicode unicode string
	 * @return string
	 */
	public static String unicodeToString(String unicode) {
		StringBuilder string = new StringBuilder();
		String[] hex = unicode.split("\\\\u");
		for (int i = 1, l = hex.length; i < l; i++) {
			int data = Integer.parseInt(hex[i], 16);
			string.append((char) data);
		}
		return string.toString();
	}

	/**
	 * convert string list to array
	 *
	 * @param collection collection of string
	 * @return array of string
	 */
	public static String[] toStringArray(Collection<String> collection) {
		return collection.toArray(new String[0]);
	}

	/**
	 * Make a string representation of the exception.
	 * @param e The exception to stringify
	 * @return A string with exception name and call stack.
	 */
	public static String stringifyException(Throwable e) {
		StringWriter writer = new StringWriter();
		try (PrintWriter printer = new PrintWriter(writer)) {
			e.printStackTrace(printer);
		}
		return writer.toString();
	}
}
