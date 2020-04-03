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

package org.spreadme.commons.codec;

import java.util.Arrays;

import org.junit.Test;
import org.spreadme.commons.lang.Assert;
import org.spreadme.commons.lang.Charsets;
import org.spreadme.commons.util.Console;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public class CodecTest {

	private static final String plainText = StringUtil.randomString(10);

	@Test
	public void testHex() {
		String hex = Hex.toHexString(plainText.getBytes(Charsets.UTF_8));
		Console.info("原文为 %s, Hex编码为 %s", plainText, hex);
		byte[] originData = Hex.decode(hex);
		Assert.isTrue(Arrays.equals(plainText.getBytes(Charsets.UTF_8), originData), "hex编码失败");
	}

	@Test
	public void testBase64() {
		String base64 = Base64.toBase64String(plainText.getBytes(Charsets.UTF_8));
		Console.info("原文为 %s, Base65编码为 %s", plainText, base64);
		byte[] originData = Base64.decode(base64);
		Assert.isTrue(Arrays.equals(plainText.getBytes(Charsets.UTF_8), originData), "base64编码失败");
	}
}
