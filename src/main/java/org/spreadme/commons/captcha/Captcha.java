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

import java.awt.*;

import org.spreadme.commons.captcha.code.CodeGenerator;

/**
 * 验证码
 * @author shuwei.wang
 */
public interface Captcha {

	/**
	 * 创建验证码图像
	 *
	 * @return captcha code {@link CaptchaCode}
	 */
	CaptchaCode create();

	/**
	 * 设置代码生成器
	 *
	 * @param generator CodeGenerator
	 * @return Captcha
	 */
	Captcha generator(CodeGenerator generator);

	/**
	 * 设置颜色
	 *
	 * @param hexColor hex color
	 * @return Captcha
	 */
	Captcha color(String hexColor);

	/**
	 * 设置字体
	 *
	 * @param font font
	 * @return Captcha
	 */
	Captcha font(Font font);
}
