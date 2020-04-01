/*
 * Copyright [4/1/20 10:58 PM] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.reflect;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;
import org.spreadme.commons.lang.Compile;
import org.spreadme.commons.lang.CompileOptions;
import org.spreadme.commons.system.SystemInfo;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 */
public class CompileTest {

	private static final String JAVA_TEST_FILE_NAME = "CompileTest.java";
	private static final File JAVA_TEST_FILE =
			new File(ClassUtil.getClassPath() + SystemInfo.FILE_SEPARATOR + JAVA_TEST_FILE_NAME);

	@Test
	public void testCompile() throws Exception {
		try (FileInputStream in = new FileInputStream(JAVA_TEST_FILE)){
			final String content = StringUtil.fromInputStream(in);
			final String className = "org.spreadme.commons.test.TestCompileMain";
			Compile.compile(className, content, new CompileOptions());
		}
	}
}
