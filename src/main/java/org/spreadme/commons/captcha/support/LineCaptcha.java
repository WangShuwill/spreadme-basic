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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import org.spreadme.commons.util.ImageUtil;

/**
 * LineCaptcha
 * @author shuwei.wang
 */
public class LineCaptcha extends AbstractCaptcha {

	public LineCaptcha(int width, int height, int codeCount, int lineCount) {
		super(width, height, codeCount, lineCount);
	}

	public LineCaptcha(int width, int height) {
		this(width, height, 5, 100);
	}

	@Override
	protected byte[] createImage(String code) {
		// 图像buffer
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		g = image.createGraphics();
		ImageUtil.drawText(code, g, this.width, this.height, this.font, Color.BLUE);
		// 干扰线
		drawInterfere(g);
		g.dispose();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", out);
		}
		catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return out.toByteArray();
	}

	private void drawInterfere(Graphics2D g) {
		final ThreadLocalRandom random = ThreadLocalRandom.current();
		// 干扰线
		for (int i = 0; i < this.interfereCount; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs + random.nextInt(width / 8);
			int ye = ys + random.nextInt(height / 8);
			g.setColor(ImageUtil.randomColor());
			g.drawLine(xs, ys, xe, ye);
		}
	}
}
