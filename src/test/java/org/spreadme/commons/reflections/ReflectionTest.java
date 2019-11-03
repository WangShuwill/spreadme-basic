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

package org.spreadme.commons.reflections;

import org.junit.Test;
import org.spreadme.commons.util.ClassUtil;

/**
 * @author shuwei.wang
 */
public class ReflectionTest {

	@Test
	public void testForName() {
		String typeName = this.getClass().getTypeName();
		Class<?> clazz = ReflectionUtil.forName(typeName, ClassUtil.getClassLoader());
		System.out.println(clazz);
	}

	@Test
	public void testGetClasses() {
		ReflectionUtil.scanTypeNames(ClassUtil.getClassPath()).forEach(System.out::println);
	}
}
