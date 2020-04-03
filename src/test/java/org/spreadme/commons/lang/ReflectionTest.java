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

package org.spreadme.commons.lang;

import java.lang.reflect.Method;

import org.junit.Test;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.Console;

/**
 * @author shuwei.wang
 */
public class ReflectionTest {

	@Test
	public void testScanTypeNames() {
		Reflect.scanTypeNames(ClassUtil.getClassPath()).forEach(Console::info);
	}

	@Test
	public void testAnnotation() throws NoSuchMethodException {
		Method method = Reflect.ofClass(ReflectionTest.class).findMethod("testScanTypeNames");
		Annotate.Definition definition = Annotate.of(method, Test.class).definition();
		Console.info(definition);
	}
}
