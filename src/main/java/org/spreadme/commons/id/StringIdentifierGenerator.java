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
 * String Identifier Generator
 * @author shuwei.wang
 */
public interface StringIdentifierGenerator extends IdentifierGenerator<String> {

	/**
	 * Constant representing unlimited identifier length, returned by {@link #maxLength()}
	 * when there is no upper bound to the length of an identifier in the sequence
	 */
	int INFINITE_MAX_LENGTH = -1;

	/**
	 * Gets the next identifier in the sequence.
	 *
	 * @return the next String identifier in sequence
	 */
	String nextStringIdentifier();

	/**
	 * Returns the maximum length (number or characters) for an identifier
	 * from this sequence.
	 *
	 * @return the maximum identifier length, or {@link #INFINITE_MAX_LENGTH} if there is no upper bound
	 */
	long maxLength();

	/**
	 * Returns the minimum length (number of characters) for an identifier
	 * from this sequence.
	 *
	 * @return the minimum identifier length
	 */
	long minLength();
}
