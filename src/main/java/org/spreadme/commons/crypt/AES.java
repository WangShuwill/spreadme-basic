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

import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * aes encrypt
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class AES {

	private static final ThreadLocal<Cipher> CIPHER_CBC;
	private static final ThreadLocal<Cipher> CIPHER_NORMAL;

	static {
		CIPHER_CBC = ThreadLocal.withInitial(() -> {
			try {
				return Cipher.getInstance(Algorithm.AES_CBC_PKCS5Padding.getValue());
			}
			catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		});

		CIPHER_NORMAL = ThreadLocal.withInitial(() -> {
			try {
				return Cipher.getInstance(Algorithm.AES.getValue());
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
	 * @return byte array of key
	 */
	public static byte[] generateKey() {
		try {
			return KeyGen.generateKey(128, Algorithm.AES);
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * encrypt data with aes
	 *
	 * @param rawData original data
	 * @param key secret key
	 * @param isUseCBC is use cbc mode
	 * @return encrypt data
	 * @throws Exception Exception
	 */
	public static byte[] encrypt(byte[] rawData, byte[] key, boolean isUseCBC) throws Exception {
		Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, key, isUseCBC);
		return cipher.doFinal(rawData);
	}

	/**
	 * decrypt data with aes
	 *
	 * @param cipherData encrypt data
	 * @param key secret key
	 * @param isUseCBC is use cbc mode
	 * @return original data
	 * @throws Exception Exception
	 */
	public static byte[] decrypt(byte[] cipherData, byte[] key, boolean isUseCBC) throws Exception {
		Cipher cipher = initCipher(Cipher.DECRYPT_MODE, key, isUseCBC);
		return cipher.doFinal(cipherData);
	}

	/**
	 * encrypt data by InputStream
	 *
	 * @param in InputStream
	 * @param key secret key
	 * @param isUseCBC is use cbc mode
	 * @return CipherInputStream
	 * @throws Exception Exception
	 */
	public static CipherInputStream encrypt(InputStream in, byte[] key, boolean isUseCBC) throws Exception {
		Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, key, isUseCBC);
		return new CipherInputStream(in, cipher);
	}

	/**
	 * decrypt data with InputStream
	 *
	 * @param out OutputStream
	 * @param key secret key
	 * @param isUseCBC is use cbc mode
	 * @return CipherOutputStream
	 * @throws Exception Exception
	 */
	public static CipherOutputStream decrypt(OutputStream out, byte[] key, boolean isUseCBC) throws Exception {
		Cipher cipher = initCipher(Cipher.DECRYPT_MODE, key, isUseCBC);
		return new CipherOutputStream(out, cipher);
	}

	/**
	 * generate SecretKeySpec
	 * @param key secret key byte array
	 * @return SecretKeySpec
	 */
	private static SecretKeySpec getKeySpec(byte[] key) {
		if (key.length != 16) {
			key = KeyGen.extendKey(key);
		}
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, Algorithm.AES.getValue());
		byte[] encoded = secretKeySpec.getEncoded();
		return new SecretKeySpec(encoded, Algorithm.AES.getValue());
	}

	/**
	 * init cipher
	 * @param mode cipher mode
	 * @param key secret key
	 * @param isUseCBC is use cbc mode
	 * @return Cipher
	 * @throws Exception Exception
	 */
	public static Cipher initCipher(int mode, byte[] key, boolean isUseCBC) throws Exception {
		SecretKeySpec keySpec = getKeySpec(key);
		if (isUseCBC) {
			Cipher cipher = CIPHER_CBC.get();
			//使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(mode, keySpec, new IvParameterSpec(keySpec.getEncoded()));
			return cipher;
		}
		else {
			Cipher cipher = CIPHER_NORMAL.get();
			cipher.init(mode, keySpec);
			return cipher;
		}
	}
}
