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
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.spreadme.commons.lang.ImageFormats;
import org.spreadme.commons.lang.Randoms;

/**
 * image util
 * @author shuwei.wang
 */
public abstract class ImageUtil {

	private static final String FORMAT_NAME = "png";

	/**
	 * 随机生成颜色
	 *
	 * @return Color
	 */
	public static Color randomColor() {
		return new Color(Randoms.nextInt(255), Randoms.nextInt(255), Randoms.nextInt(255));
	}

	/**
	 * Color对象转16进制表示，例如#fcf6d6
	 *
	 * @param color {@link Color}
	 * @return 16进制的颜色值，例如#fcf6d6
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
	 */
	public static Color hexToColor(String hex) {
		return new Color(Integer.parseInt(hex.replace("#", StringUtil.EMPTY), 16));
	}

	public static byte[] toBytes(BufferedImage image, ImageFormats format) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, format.getName(), out);
		return out.toByteArray();
	}

	/**
	 * 文字转图片
	 *
	 * @param text 文字
	 * @param font 字体
	 * @param color 颜色
	 * @return BufferedImage
	 */
	public static BufferedImage toImage(String text, Font font, Color color) {
		BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		Graphics fontGraphics = image.getGraphics();
		fontGraphics.setFont(font);
		int width = fontGraphics.getFontMetrics().stringWidth(text);
		int height = font.getSize();

		BufferedImage trueImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = trueImage.createGraphics();
		trueImage = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		graphics = trueImage.createGraphics();
		graphics.setColor(color);
		graphics.setFont(font);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		FontMetrics fm = graphics.getFontMetrics(font);
		int ascent = fm.getAscent();
		int descent = fm.getDescent();
		int x = (width - fontGraphics.getFontMetrics().stringWidth(text)) / 2;
		int y = (height - (ascent + descent)) / 2 + ascent;
		graphics.drawString(text, x, y);
		graphics.dispose();
		return trueImage;
	}
}
