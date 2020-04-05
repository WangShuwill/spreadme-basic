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

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.spreadme.commons.captcha.support.CurvesCaptcha;
import org.spreadme.commons.captcha.support.LineCaptcha;
import org.spreadme.commons.lang.ImageFormats;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.Console;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.ImageUtil;

/**
 * @author shuwei.wang
 */
public class CodeGeneratorTest {

	@Test
	public void testMathCodeGenerator() throws IOException {
		CaptchaCode lineCode = LineCaptcha.of(200, 50).create();
		Console.info(lineCode);
		IOUtil.toFile(
				ImageUtil.toBytes(lineCode.getImage(), ImageFormats.PNG),
				ClassUtil.getClassPath() + File.separator + lineCode.getText() + "." + ImageFormats.PNG.getName()
		);

		CaptchaCode curvesCode = CurvesCaptcha.of(200, 50).create();
		Console.info(curvesCode);
		IOUtil.toFile(
				ImageUtil.toBytes(curvesCode.getImage(), ImageFormats.PNG),
				ClassUtil.getClassPath() + File.separator + curvesCode.getText() + "." + ImageFormats.PNG.getName()
		);
	}
}
