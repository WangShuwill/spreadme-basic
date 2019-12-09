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
 * captcha code
 * @author shuwei.wang
 */
public class CaptchaCode {

	private BufferedImage image;

	private String text;

	private String value;

	public CaptchaCode() {

	}

	public CaptchaCode(final String text, final String value) {
		this.text = text;
		this.value = value;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public String getText() {
		return text;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "CaptchaCode{" +
				"text='" + text + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
