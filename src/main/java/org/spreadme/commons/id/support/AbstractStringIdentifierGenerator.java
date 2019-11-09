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

import org.spreadme.commons.id.StringIdentifierGenerator;

/**
 * Abstract String Identifier Generator
 * @author shuwei.wang
 */
public abstract class AbstractStringIdentifierGenerator implements StringIdentifierGenerator {

	protected static final int MAX_LONG_NUMERIC_VALUE_LENGTH = Long.toString(Long.MIN_VALUE).length();

	protected static final int ALPHA_NUMERIC_CHARSET_SIZE = 36;

	protected static final int MAX_LONG_ALPHANUMERIC_VALUE_LENGTH = Long.toString(Long.MAX_VALUE, ALPHA_NUMERIC_CHARSET_SIZE).length();

	protected static final int MAX_INT_NUMERIC_VALUE_LENGTH = Integer.toString(Integer.MIN_VALUE).length();

	protected static final int MAX_INT_ALPHANUMERIC_VALUE_LENGTH = Integer.toString(Integer.MAX_VALUE, ALPHA_NUMERIC_CHARSET_SIZE).length();

	protected static final int DEFAULT_ALPHANUMERIC_IDENTIFIER_SIZE = 15;

	protected AbstractStringIdentifierGenerator() {

	}

	@Override
	public abstract String nextStringIdentifier();

	@Override
	public long maxLength() {
		return INFINITE_MAX_LENGTH;
	}

	@Override
	public long minLength() {
		return 0;
	}

	@Override
	public String nextIdentifier() {
		return this.nextStringIdentifier();
	}
}
