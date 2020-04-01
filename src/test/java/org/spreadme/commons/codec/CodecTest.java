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
import org.spreadme.commons.lang.Console;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public class CodecTest {

	@Test
	public void testHex() {
		final byte[] data = new byte[] {1, 3, 2, 9, 12};
		String hexStr = Hex.toHexString(data);
		Console.info(hexStr);
		byte[] originData = Hex.decode(hexStr);
		Console.info(Arrays.equals(data, originData));
	}

	@Test
	public void testBase64() {
		final String data = "test";
		String base64Str = Base64.toBase64String(data.getBytes());
		Console.info(base64Str);
		byte[] originData = Base64.decode(base64Str);
		Console.info(Arrays.equals(originData, data.getBytes()));
	}
}
