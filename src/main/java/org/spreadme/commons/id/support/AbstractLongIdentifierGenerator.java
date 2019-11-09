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

import org.spreadme.commons.id.LongIdentifierGenerator;

/**
 * Abstract LongI dentifier Generator
 * @author shuwei.wang
 */
public abstract class AbstractLongIdentifierGenerator implements LongIdentifierGenerator {

	protected AbstractLongIdentifierGenerator() {

	}

	@Override
	public abstract Long nextLongIdentifier();

	@Override
	public Long maxValue() {
		return Long.MAX_VALUE;
	}

	@Override
	public Long minValue() {
		return Long.MIN_VALUE;
	}

	@Override
	public Long nextIdentifier() {
		return this.nextLongIdentifier();
	}
}
