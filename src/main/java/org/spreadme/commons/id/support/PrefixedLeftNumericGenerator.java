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

package org.spreadme.commons.id.support;

/**
 * Prefixed Left Padded Numeric Generator
 * @author shuwei.wang
 */
public class PrefixedLeftNumericGenerator extends AbstractStringIdentifierGenerator {

	/** Prefix. */
	private final String prefix;

	/** Should the counter wrap. */
	private boolean wrap;

	/** The counter. */
	private char[] count;

	/** '9' char. */
	private static final char NINE_CHAR = '9';

	public PrefixedLeftNumericGenerator(String prefix, boolean wrap, int size) {
		super();

		if (prefix == null) {
			throw new NullPointerException("prefix must not be null");
		}
		if (size < 1) {
			throw new IllegalArgumentException("size must be at least one");
		}
		if (size <= prefix.length()) {
			throw new IllegalArgumentException("size less prefix length must be at least one");
		}
		this.wrap = wrap;
		this.prefix = prefix;

		int countLength = size - prefix.length();
		this.count = new char[countLength];
		for (int i = 0; i < countLength; i++) {
			count[i] = '0';
		}
	}

	public String getPrefix() {
		return prefix;
	}

	public long maxLength() {
		return this.count.length + prefix.length();
	}

	public long minLength() {
		return this.count.length + prefix.length();
	}

	public int getSize() {
		return this.count.length + prefix.length();
	}

	public boolean isWrap() {
		return wrap;
	}

	public void setWrap(boolean wrap) {
		this.wrap = wrap;
	}

	public String nextStringIdentifier() {
		for (int i = count.length - 1; i >= 0; i--) {
			if (count[i] == NINE_CHAR) {
				count[i] = '0';
				if (i == 0 && !wrap) {
					throw new IllegalStateException("The maximum number of identifiers has been reached");
				}
			}
			else {
				count[i]++;
				i = -1;
			}
		}

		return prefix + String.valueOf(count);
	}
}
