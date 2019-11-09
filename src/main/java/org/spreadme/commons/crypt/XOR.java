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

package org.spreadme.commons.crypt;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 异或运算加解密
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class XOR {

	private static Charset charset = StandardCharsets.UTF_8;

	private static byte[] keyBytes = {'(', 'X', 'O', 'R', '!', '@', '#', '$', 'K', 'E', 'Y', '%', '^', '&', '*', ')'};

	private XOR() {

	}

	/**
	 * @param data data
	 * @return result
	 */
	private static String crypto(String data) {
		if (data == null) {
			return null;
		}
		byte[] bs = data.getBytes(charset);
		int index = 0;
		for (int i = 0, size = bs.length; i < size; i++) {
			bs[i] = (byte) (bs[i] ^ keyBytes[index++]);
			if (index >= keyBytes.length) {
				index = 0;
			}
		}
		return new String(bs, charset);
	}

	/**
	 * @param data data
	 * @return result
	 */
	public static String encrypt(String data) {
		return crypto(data);
	}

	/**
	 * @param data data
	 * @return result
	 */
	public static String decrypt(String data) {
		return crypto(data);
	}
}
