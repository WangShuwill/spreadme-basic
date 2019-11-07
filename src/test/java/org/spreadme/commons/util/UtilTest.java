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

import java.util.Arrays;

import org.junit.Test;
import org.spreadme.commons.lang.Randoms;

/**
 * @author shuwei.wang
 */
public class UtilTest {

	@Test
	public void testStringUtil() {
		System.out.println(Arrays.toString(Randoms.nextBytes(3)));
		for (int i = 0; i < 100; i++) {
			System.out.println(StringUtil.randomString(6));
			System.out.println(StringUtil.randomString("-+*", 1));
		}
	}
}
