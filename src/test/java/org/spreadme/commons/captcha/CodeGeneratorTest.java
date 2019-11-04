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

package org.spreadme.commons.captcha;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.spreadme.commons.captcha.generator.MathCodeGenerator;
import org.spreadme.commons.captcha.support.LineCaptcha;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.IOUtil;

/**
 * @author shuwei.wang
 */
public class CodeGeneratorTest {

	private static final String FILE_NAME = "captcha.png";

	@Test
	public void testMathCodeGenerator() throws IOException {
		CodeGenerator generator = new MathCodeGenerator();
		CaptchaCode code = generator.generate();
		System.out.println(code);
		generator.verify(code, "100");

		Captcha captcha = new LineCaptcha(200, 50, 5, 20);
		byte[] image = captcha.create();

		try (ByteArrayInputStream in = new ByteArrayInputStream(image);
			 FileOutputStream out = new FileOutputStream(new File(ClassUtil.getClassPath() + FILE_NAME))) {
			IOUtil.copy(in, out);
		}
	}
}