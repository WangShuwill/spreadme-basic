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

package org.spreadme.commons.captcha.code;

import org.spreadme.commons.captcha.CaptchaCode;
import org.spreadme.commons.lang.Randoms;
import org.spreadme.commons.util.StringUtil;

/**
 * math code code
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
		int n1 = Randoms.nextInt(limit);
		int n2 = Randoms.nextInt(limit);
		String operator = StringUtil.randomString(operators, 1);
		String builder = n1 + operator + n2 + "=";
		assert operator != null;
		return new CaptchaCode(builder, String.valueOf(calcu(operator, n1, n2)));
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
