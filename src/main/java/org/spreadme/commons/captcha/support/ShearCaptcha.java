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

/**
 * Shear Captcha
 * @author shuwei.wang
 */
public class ShearCaptcha extends AbstractCaptcha {

	public ShearCaptcha(int width, int height, int length) {
		super(width, height, length);
	}

	public ShearCaptcha(int width, int height) {
		this(width, height, 5);
	}

	@Override
	protected void confuseImage(Graphics2D graphics) {
		//shear(graphics, width, height, Color.BLUE);
	}

	/**
	 * 扭曲
	 *
	 * @param g {@link Graphics}
	 * @param w1 w1
	 * @param h1 h1
	 * @param color 颜色
	 */
	private void shear(Graphics g, int w1, int h1, Color color) {
		shearX(g, w1, h1, color);
		shearY(g, w1, h1, color);
	}

	/**
	 * X坐标扭曲
	 *
	 * @param g {@link Graphics}
	 * @param w1 宽
	 * @param h1 高
	 * @param color 颜色
	 */
	private void shearX(Graphics g, int w1, int h1, Color color) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int period = random.nextInt(this.width);
		int frames = 1;
		int phase = random.nextInt(2);
		for (int i = 0; i < h1; i++) {
			double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
		}
	}

	/**
	 * Y坐标扭曲
	 *
	 * @param g {@link Graphics}
	 * @param w1 宽
	 * @param h1 高
	 * @param color 颜色
	 */
	private void shearY(Graphics g, int w1, int h1, Color color) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int period = random.nextInt(this.height >> 1);
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < w1; i++) {
			double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
		}

	}
}
