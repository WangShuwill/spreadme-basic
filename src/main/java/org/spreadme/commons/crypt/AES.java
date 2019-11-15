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

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.spreadme.commons.codec.Hex;
import org.spreadme.commons.lang.Randoms;

/**
 * aes encrypt
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class AES {

	private static final ThreadLocal<Cipher> CIPHER;

	static {
		CIPHER = ThreadLocal.withInitial(() -> {
			try {
				return Cipher.getInstance(Algorithm.AES_CBC_PKCS5Padding.getValue());
			}
			catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		});
	}

	private AES() {

	}

	/**
	 * generate key
	 *
	 * @param length length of byte array
	 * @return hex key
	 */
	public static String generateKey(int length) {
		byte[] bytes = new byte[length];
		Randoms.getSecureRandom().nextBytes(bytes);
		return Hex.toHexString(bytes);
	}

	public static byte[] encrypt(byte[] rawData, byte[] key) throws Exception {
		//使用CBC模式，需要一个向量iv，可增加加密算法的强度
		SecretKeySpec keySpec = getKeySpec(key);
		CIPHER.get().init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(keySpec.getEncoded()));
		return CIPHER.get().doFinal(rawData);
	}

	public static byte[] decrypt(byte[] cipherData, byte[] key) throws Exception {
		//使用CBC模式，需要一个向量iv，可增加加密算法的强度
		SecretKeySpec keySpec = getKeySpec(key);
		CIPHER.get().init(Cipher.DECRYPT_MODE, getKeySpec(key), new IvParameterSpec(keySpec.getEncoded()));
		return CIPHER.get().doFinal(cipherData);
	}

	private static SecretKeySpec getKeySpec(byte[] key) {
		if (key.length != 16) {
			key = extendKey(key);
		}
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, Algorithm.AES.getValue());
		byte[] encoded = secretKeySpec.getEncoded();
		return new SecretKeySpec(encoded, Algorithm.AES.getValue());
	}

	private static byte[] extendKey(byte[] key) {
		key = Hash.toHexString(key, null, 0, Algorithm.MD5).getBytes();
		key = Arrays.copyOf(key, key.length / 2);
		return key;
	}
}
