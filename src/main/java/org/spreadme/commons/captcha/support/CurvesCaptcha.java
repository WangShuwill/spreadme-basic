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
import org.spreadme.commons.captcha.code.RandomCodeGenerator;
import org.spreadme.commons.lang.Randoms;

/**
 * Shear Captcha
 * @author shuwei.wang
 */
public class CurvesCaptcha extends AbstractCaptcha {

	public CurvesCaptcha(int width, int height, int length) {
		super(width, height, new RandomCodeGenerator(length));
	}

	public CurvesCaptcha(int width, int height) {
		this(width, height, 5);
	}

	public static Captcha of(int width, int height, int length) {
		return new CurvesCaptcha(width, height, length);
	}

	public static Captcha of(int width, int height) {
		return new CurvesCaptcha(width, height);
	}

	@Override
	protected void confuseImage(Graphics2D graphics) {
		curve(graphics, width, height, color);
	}

	/**
	 * 扭曲
	 *
	 * @param g {@link Graphics}
	 * @param w1 w1
	 * @param h1 h1
	 * @param color 颜色
	 */
	private void curve(Graphics g, int w1, int h1, Color color) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		int cp = 4 + Randoms.nextInt(3);
		int[] xPoints = new int[cp];
		int[] yPoints = new int[cp];
		width -= 10;
		for (int i = 0; i < cp; i++) {
			xPoints[i] = 5 + (i * width) / (cp - 1);
			yPoints[i] = (int) (height * (Randoms.nextDouble() * 0.5 + 0.2));
		}
		int subsections = 6;
		int[] xPointsSpline = new int[(cp - 1) * subsections];
		int[] yPointsSpline = new int[(cp - 1) * subsections];
		for (int i = 0; i < cp - 1; i++) {
			double x0 = i > 0 ? xPoints[i - 1] : 2 * xPoints[i] - xPoints[i + 1];
			double x1 = xPoints[i];
			double x2 = xPoints[i + 1];
			double x3 = (i + 2 < cp) ? xPoints[i + 2] : 2 * xPoints[i + 1] - xPoints[i];
			double y0 = i > 0 ? yPoints[i - 1] : 2 * yPoints[i] - yPoints[i + 1];
			double y1 = yPoints[i];
			double y2 = yPoints[i + 1];
			double y3 = (i + 2 < cp) ? yPoints[i + 2] : 2 * yPoints[i + 1] - yPoints[i];
			for (int j = 0; j < subsections; j++) {
				xPointsSpline[i * subsections + j] = (int) catmullRomSpline(x0, x1, x2, x3, 1.0 / subsections * j);
				yPointsSpline[i * subsections + j] = (int) catmullRomSpline(y0, y1, y2, y3, 1.0 / subsections * j);
			}
		}
		for (int i = 0; i < xPointsSpline.length - 1; i++) {
			g2.setColor(color);
			float strokeMin = 2;
			float strokeMax = 4;
			g2.setStroke(new BasicStroke(strokeMin + (strokeMax - strokeMin) * Randoms.nextFloat()));
			g2.drawLine(xPointsSpline[i], yPointsSpline[i], xPointsSpline[i + 1], yPointsSpline[i + 1]);
		}
	}

	private double hermiteSpline(double x1, double a1, double x2, double a2, double t) {
		double t2 = t * t;
		double t3 = t2 * t;
		double b = -a2 - 2.0 * a1 - 3.0 * x1 + 3.0 * x2;
		double a = a2 + a1 + 2.0 * x1 - 2.0 * x2;
		return a * t3 + b * t2 + a1 * t + x1;
	}

	private double catmullRomSpline(double x0, double x1, double x2, double x3, double t) {
		double a1 = (x2 - x0) / 2;
		double a2 = (x3 - x1) / 2;
		return hermiteSpline(x1, a1, x2, a2, t);
	}
}
