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
import java.util.concurrent.ThreadLocalRandom;

import org.spreadme.commons.util.ImageUtil;

/**
 * LineCaptcha
 * @author shuwei.wang
 */
public class LineCaptcha extends AbstractCaptcha {

	private int lineCount;

	public LineCaptcha(int width, int height, int length, int lineCount) {
		super(width, height, length);
		this.lineCount = lineCount;
	}

	public LineCaptcha(int width, int height) {
		this(width, height, 5, 50);
	}

	@Override
	protected void confuseImage(Graphics2D graphics) {
		// 干扰线
		drawInterfere(graphics);
	}

	private void drawInterfere(Graphics2D g) {
		final ThreadLocalRandom random = ThreadLocalRandom.current();
		// 干扰线
		for (int i = 0; i < this.lineCount; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs + random.nextInt(width / 8);
			int ye = ys + random.nextInt(height / 8);
			g.setColor(ImageUtil.randomColor());
			g.drawLine(xs, ys, xe, ye);
		}
	}
}
