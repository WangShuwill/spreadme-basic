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

import java.awt.image.BufferedImage;

/**
 * 验证码
 * @author shuwei.wang
 */
public interface Captcha {

	/**
	 * 创建验证码图像
	 *
	 * @return captcha image {@link BufferedImage}
	 */
	BufferedImage create();

	/**
	 * 获取验证码
	 *
	 * @return captcha code {@link CaptchaCode}
	 */
	CaptchaCode getCode();

	/**
	 * 校验验证码
	 *
	 * @param code captcha code
	 * @param input 输入
	 * @return 校验是否成功
	 */
	boolean verify(CaptchaCode code, String input);

	/**
	 * 设置验证码生成器
	 *
	 * @param generator 验证码生成器 {@link CodeGenerator}
	 */
	void setGenerator(CodeGenerator generator);

	/**
	 * 设置颜色
	 *
	 * @param hexColor 十六进制颜色值
	 */
	void setColor(String hexColor);
}
