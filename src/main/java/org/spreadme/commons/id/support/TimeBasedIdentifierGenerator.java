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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * TimeBased dentifier Generator
 * @author shuwei.wang
 */
public class TimeBasedIdentifierGenerator extends AbstractStringIdentifierGenerator implements Serializable {

	private static final long serialVersionUID = -2982404908256163491L;

	private static final char[] padding;

	static {
		padding = new char[MAX_LONG_ALPHANUMERIC_VALUE_LENGTH];
		Arrays.fill(padding, '0');
	}

	private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

	private static long last = 0;

	private static long counter = 0;

	private final int postfixSize;

	private final long offset;

	public TimeBasedIdentifierGenerator(final int postfixSize, final long offset) {
		if (postfixSize < 0 || postfixSize > MAX_LONG_ALPHANUMERIC_VALUE_LENGTH) {
			throw new IllegalArgumentException("Invalid size for postfix");
		}
		this.postfixSize = postfixSize;
		this.offset = offset;
	}

	public TimeBasedIdentifierGenerator(final int postfixSize) {
		this(postfixSize, 0);
	}

	public TimeBasedIdentifierGenerator() {
		this(3);
	}

	@Override
	public String nextStringIdentifier() {
		long now;
		synchronized (this) {
			now = Calendar.getInstance(UTC).getTime().getTime();
			final long diff = now - last;
			if (diff > 0 || diff < -1000) {
				last = now;
				counter = 0;
			}
			else {
				if (diff != 0) {
					now = last; // ignore time shift
				}
				++counter;
			}
		}
		final String postfix = counter > 0 ? Long.toString(counter, ALPHA_NUMERIC_CHARSET_SIZE) : "";
		if (postfix.length() > postfixSize) {
			throw new IllegalStateException("The maximum number of identifiers in this millisecond has been reached");
		}
		long base = now - offset;
		long value = base < 0 ? base + Long.MAX_VALUE + 1 : base;
		final String time = Long.toString(value, ALPHA_NUMERIC_CHARSET_SIZE);
		final char[] buffer = new char[MAX_LONG_ALPHANUMERIC_VALUE_LENGTH + postfixSize];
		int i = 0;
		int maxPad = MAX_LONG_ALPHANUMERIC_VALUE_LENGTH - time.length();
		if (maxPad > 0) {
			System.arraycopy(padding, 0, buffer, 0, maxPad);
		}
		System.arraycopy(time.toCharArray(), 0, buffer, maxPad, time.length());
		if (base < 0) {
			// Representation of Long.MAX_VALUE starts with '1', negative 'base' means higher value
			// in time
			buffer[0] += 2;
		}
		i += time.length() + maxPad;
		if (postfixSize > 0) {
			maxPad = postfixSize - postfix.length();
			if (maxPad > 0) {
				System.arraycopy(padding, 0, buffer, i, maxPad);
				i += maxPad;
			}
			System.arraycopy(postfix.toCharArray(), 0, buffer, i, postfix.length());
		}
		return new String(buffer);
	}

	public long getMillisecondsFromId(final Object id, final long offset) {
		if (id instanceof String && id.toString().length() >= MAX_LONG_ALPHANUMERIC_VALUE_LENGTH) {
			final char[] buffer = new char[MAX_LONG_ALPHANUMERIC_VALUE_LENGTH];
			System.arraycopy(
					id.toString().toCharArray(), 0, buffer, 0, MAX_LONG_ALPHANUMERIC_VALUE_LENGTH);
			final boolean overflow = buffer[0] > '1';
			if (overflow) {
				buffer[0] -= 2;
			}
			long value = Long.parseLong(new String(buffer), ALPHA_NUMERIC_CHARSET_SIZE);
			if (overflow) {
				value -= Long.MAX_VALUE + 1;
			}
			return value + offset;
		}
		throw new IllegalArgumentException("'" + id + "' is not an id from this generator");
	}

	public long maxLength() {
		return MAX_LONG_ALPHANUMERIC_VALUE_LENGTH + postfixSize;
	}

	public long minLength() {
		return maxLength();
	}
}
