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

/**
 * Numeric Generator
 * @author shuwei.wang
 */
public class NumericGenerator extends AbstractStringIdentifierGenerator implements Serializable {

	private static final long serialVersionUID = -4354639364308124104L;

	/** Should the counter wrap. */
	private boolean wrapping;

	/** The counter. */
	private long count;

	public NumericGenerator(boolean wrap, long initialValue) {
		this.wrapping = wrap;
		this.count = initialValue;
	}

	@Override
	public String nextStringIdentifier() {
		long value;
		if (wrapping) {
			synchronized (this) {
				value = count++;
			}
		}
		else {
			synchronized (this) {
				if (count == Long.MAX_VALUE) {
					throw new IllegalStateException
							("The maximum number of identifiers has been reached");
				}
				value = count++;
			}
		}
		return Long.toString(value);
	}

	public long maxLength() {
		return AbstractStringIdentifierGenerator.MAX_LONG_NUMERIC_VALUE_LENGTH;
	}

	public long minLength() {
		return 1;
	}

}
