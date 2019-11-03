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

import java.util.concurrent.ThreadLocalRandom;

import org.spreadme.commons.captcha.CaptchaCode;
import org.spreadme.commons.captcha.CodeGenerator;
import org.spreadme.commons.util.StringUtil;

/**
 * math code generator
 * @author shuwei.wang
 */
public class MathCodeGenerator implements CodeGenerator {

	private static final String operators = "+-*";

	private int maxNumberLength;

	public MathCodeGenerator() {
		this(2);
	}

	public MathCodeGenerator(int maxNumberLength) {
		this.maxNumberLength = maxNumberLength;
	}

	@Override
	public CaptchaCode generate() {
		final int limit = getLimit();
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int n1 = random.nextInt(limit);
		int n2 = random.nextInt(limit);
		String operator = StringUtil.randomString(operators, 1);
		StringBuilder builder = new StringBuilder()
				.append(n1)
				.append(StringUtil.repeat(' ', 1))
				.append(operator)
				.append(StringUtil.repeat(' ', 1))
				.append(n2)
				.append(StringUtil.repeat(' ', 1))
				.append("=");
		return new CaptchaCode(builder.toString(), String.valueOf(calcu(operator, n1, n2)));
	}

	@Override
	public boolean verify(CaptchaCode code, String input) {
		return StringUtil.isNotBlank(input) && input.equals(code.getValue());
	}

	public int getLength() {
		return this.maxNumberLength * 2 + 2;
	}

	private int getLimit() {
		return Integer.valueOf("1" + StringUtil.repeat('0', this.maxNumberLength));
	}

	private int calcu(String operator, int n1, int n2) {
		switch (operator) {
			case "+":
				return n1 + n2;
			case "-":
				return n1 - n2;
			case "*":
				return n1 * n2;
			default:
				throw new IllegalArgumentException("Illegal operatoe");
		}
	}

}
