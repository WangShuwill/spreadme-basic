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
import java.awt.image.BufferedImage;

import org.spreadme.commons.captcha.Captcha;
import org.spreadme.commons.captcha.CaptchaCode;
import org.spreadme.commons.captcha.CodeGenerator;
import org.spreadme.commons.captcha.generator.RandomCodeGenerator;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.ImageUtil;

/**
 * abstract captcha
 * @author shuwei.wang
 */
public abstract class AbstractCaptcha implements Captcha {

	private static final String FONT_PATH = ClassUtil.getClassPath() + "Glober.otf";

	protected int width; // 图片的宽度

	protected int height; // 图片的高度

	protected Font font; // 字体

	protected Color color = Color.BLUE; //颜色

	protected CaptchaCode code; // 验证码

	protected CodeGenerator generator; // 验证码生成器

	public AbstractCaptcha(int width, int height, CodeGenerator generator) {
		this.width = width;
		this.height = height;
		this.generator = generator;
		this.font = new Font(Font.SANS_SERIF, Font.PLAIN, (int) (this.height * 0.75));
	}

	public AbstractCaptcha(int width, int height, int length) {
		this(width, height, new RandomCodeGenerator(length));
	}

	@Override
	public BufferedImage create() {
		// 生成验证码
		this.code = this.generator.generate();
		// 绘制图像
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		image = graphics.getDeviceConfiguration().createCompatibleImage(this.width, this.height, Transparency.TRANSLUCENT);
		graphics = image.createGraphics();
		drawText(code.getCode(), graphics, this.color);
		// 混淆图像
		confuseImage(graphics);
		graphics.dispose();
		return image;
	}

	protected abstract void confuseImage(Graphics2D graphics);

	private Graphics2D drawText(String code, Graphics2D graphics, Color color) {
		graphics.setFont(this.font);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		FontMetrics metrics = graphics.getFontMetrics();
		int midY = (height - metrics.getHeight()) / 2 + metrics.getAscent();

		final int len = code.length();
		int charWidth = width / len;
		for (int i = 0; i < len; i++) {
			if (color == null) {
				graphics.setColor(ImageUtil.randomColor());
			}
			graphics.setColor(color);
			if (i == 0) {
				graphics.drawString(String.valueOf(code.charAt(i)), charWidth / 2, midY);
			}
			else {
				graphics.drawString(String.valueOf(code.charAt(i)), i * charWidth + (charWidth / 2), midY);
			}
		}
		return graphics;
	}

	@Override
	public CaptchaCode getCode() {
		return this.code;
	}

	@Override
	public boolean verify(String input) {
		return generator.verify(this.code, input);
	}

	@Override
	public void setGenerator(CodeGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void setColor(String hexColor) {
		this.color = ImageUtil.hexToColor(hexColor);
	}

}
