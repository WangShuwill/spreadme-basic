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

package org.spreadme.commons.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * image util
 * @author shuwei.wang
 */
public class ImageUtil {

	private static final String FORMAT_NAME = "png";

	/**
	 * 随机生成颜色
	 *
	 * @return Color
	 */
	public static Color randomColor() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
	}

	/**
	 * Color对象转16进制表示，例如#fcf6d6
	 *
	 * @param color {@link Color}
	 * @return 16进制的颜色值，例如#fcf6d6
	 * @since 4.1.14
	 */
	public static String toHex(Color color) {
		String R = Integer.toHexString(color.getRed());
		R = R.length() < 2 ? ('0' + R) : R;
		String G = Integer.toHexString(color.getGreen());
		G = G.length() < 2 ? ('0' + G) : G;
		String B = Integer.toHexString(color.getBlue());
		B = B.length() < 2 ? ('0' + B) : B;
		return '#' + R + G + B;
	}

	/**
	 * 16进制的颜色值转换为Color对象，例如#fcf6d6
	 *
	 * @param hex 16进制的颜色值，例如#fcf6d6
	 * @return {@link Color}
	 * @since 4.1.14
	 */
	public static Color hexToColor(String hex) {
		return new Color(Integer.parseInt(StringUtil.trimStart(hex, "#"), 16));
	}

	public static BufferedImage drawText(String text, int width, int height, Font font, Color color, int transparency) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		image = graphics.getDeviceConfiguration().createCompatibleImage(width, height, transparency);
		graphics = image.createGraphics();
		drawText(text, graphics, width, height, font, color);
		graphics.dispose();
		return image;
	}

	public static void drawText(String text, Graphics2D graphics, int width, int height, Font font, Color color) {
		graphics.setFont(font);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		FontMetrics metrics = graphics.getFontMetrics();
		int midY = (height - metrics.getHeight()) / 2 + metrics.getAscent();

		final int len = text.length();
		int charWidth = width / len;
		for (int i = 0; i < len; i++) {
			if (color == null) {
				// 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
				graphics.setColor(randomColor());
			}
			graphics.setColor(color);
			graphics.drawString(String.valueOf(text.charAt(i)), i * charWidth, midY);
		}
	}
}
