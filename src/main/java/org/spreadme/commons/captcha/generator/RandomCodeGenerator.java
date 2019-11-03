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

package org.spreadme.commons.captcha.generator;

import org.spreadme.commons.captcha.CaptchaCode;
import org.spreadme.commons.captcha.CodeGenerator;
import org.spreadme.commons.util.StringUtil;

/**
 * random code generator
 * @author shuwei.wang
 */
public class RandomCodeGenerator implements CodeGenerator {

	private final int legth;

	public RandomCodeGenerator(int legth) {
		this.legth = legth;
	}

	@Override
	public CaptchaCode generate() {
		String code = StringUtil.randomString(this.legth);
		return new CaptchaCode(code, code);
	}

	@Override
	public boolean verify(CaptchaCode code, String input) {
		return StringUtil.isNotBlank(input) && input.equalsIgnoreCase(code.getValue());
	}
}
