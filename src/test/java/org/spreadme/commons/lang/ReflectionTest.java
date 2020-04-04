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

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

import org.junit.Test;
import org.spreadme.commons.system.SystemInfo;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.Console;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 */
public class ReflectionTest {

	private static final String JAVA_TEST_FILE_NAME = "CompilePerson.java";
	private static final String CLASS_NAME = "org.spreadme.commons.test.CompilePerson";
	private static final File JAVA_TEST_FILE =
			new File(ClassUtil.getClassPath() + SystemInfo.FILE_SEPARATOR + JAVA_TEST_FILE_NAME);

	@Test
	public void testCompile() throws Exception {
		try (FileInputStream in = new FileInputStream(JAVA_TEST_FILE)) {
			final String content = StringUtil.fromInputStream(in);
			Reflect.compile(CLASS_NAME, content).create("Tom", 27)
					.invoke("hello")
					.set("name", "Jack").set("age", 28)
					.fields()
					.forEach((key, value) -> Console.info("field name %s, value %s", key, value.get()));
		}
	}

	@Test
	public void testAnnotation() throws NoSuchMethodException {
		Method method = Reflect.ofClass(ReflectionTest.class).findMethod("testCompile");
		Annotate.Definition definition = Annotate.of(method, Test.class).definition();
		Console.info(definition);
	}
}
