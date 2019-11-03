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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.ImageUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 */
public class ImageTest {

	private static final String FILE_NAME = "captcha.png";

	@Test
	public void testDrawText() throws IOException {
		BufferedImage image = ImageUtil.drawText(StringUtil.randomString(6), 200, 50,
				new Font(Font.SANS_SERIF, Font.PLAIN, (int) (50 * 0.75)), Color.BLUE, Transparency.TRANSLUCENT);
		ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
		ImageIO.write(image, "png", imageBytes);
		try (ByteArrayInputStream in = new ByteArrayInputStream(imageBytes.toByteArray());
			 FileOutputStream out = new FileOutputStream(new File(ClassUtil.getClassPath() + FILE_NAME))) {
			IOUtil.copy(in, out);
		}
	}
}
