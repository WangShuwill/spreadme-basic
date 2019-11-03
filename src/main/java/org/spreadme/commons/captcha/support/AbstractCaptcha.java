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

package org.spreadme.commons.captcha.support;

import java.awt.*;

import org.spreadme.commons.captcha.Captcha;
import org.spreadme.commons.captcha.CaptchaCode;
import org.spreadme.commons.captcha.CodeGenerator;
import org.spreadme.commons.captcha.generator.RandomCodeGenerator;

/**
 * abstract captcha
 * @author shuwei.wang
 */
public abstract class AbstractCaptcha implements Captcha {

	protected int width; // 图片的宽度

	protected int height; // 图片的高度

	protected int interfereCount; // 验证码干扰元素个数

	protected Font font; // 字体

	protected CaptchaCode code; // 验证码

	protected byte[] imageBytes; // 验证码图片

	protected CodeGenerator generator; // 验证码生成器

	protected Color background; // 背景色

	protected AlphaComposite textAlpha; //文字透明度

	public AbstractCaptcha(int width, int height, CodeGenerator generator, int interfereCount) {
		this.width = width;
		this.height = height;
		this.generator = generator;
		this.interfereCount = interfereCount;
		// 字体高度设为验证码高度-2，留边距
		this.font = new Font(Font.SANS_SERIF, Font.PLAIN, (int) (this.height * 0.75));
	}

	public AbstractCaptcha(int width, int height, int codeCount, int interfereCount) {
		this(width, height, new RandomCodeGenerator(codeCount), interfereCount);
	}

	@Override
	public byte[] create() {
		this.code = this.generator.generate();
		return createImage(this.code.getCode());
	}

	protected abstract byte[] createImage(String code);

	@Override
	public String getCode() {
		return this.code.getCode();
	}

	@Override
	public boolean verify(String input) {
		return generator.verify(this.code, input);
	}
}
