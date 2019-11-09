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

package org.spreadme.commons.id;

/**
 * Long Identifier Generator
 * @author shuwei.wang
 */
public interface LongIdentifierGenerator extends IdentifierGenerator<Long> {

	/**
	 * Gets the next identifier in the sequence.
	 *
	 * @return the next Long identifier in sequence
	 */
	Long nextLongIdentifier();

	/**
	 * Returns the maximum value of an identifier from this generator.
	 *
	 * @return the maximum identifier value
	 */
	Long maxValue();

	/**
	 * Returns the minimum value of an identifier from this generator.
	 *
	 * @return the minimum identifier value
	 */
	Long minValue();
}
