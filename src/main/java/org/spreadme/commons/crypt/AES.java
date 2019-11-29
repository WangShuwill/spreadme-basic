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

import org.spreadme.commons.util.IOUtil;

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

	public static void encrypt(InputStream in, OutputStream out, byte[] key) throws Exception{
		SecretKeySpec keySpec = getKeySpec(key);
		Cipher cipher = CIPHER.get();
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(keySpec.getEncoded()));
		IOUtil.copy(new CipherInputStream(in, cipher), out);
	}

	public static void decrypt(InputStream in, OutputStream out, byte[] key) throws Exception{
		SecretKeySpec keySpec = getKeySpec(key);
		Cipher cipher = CIPHER.get();
		cipher.init(Cipher.DECRYPT_MODE, getKeySpec(key), new IvParameterSpec(keySpec.getEncoded()));
		IOUtil.copy(in, new CipherOutputStream(out, cipher));
	}

	private static SecretKeySpec getKeySpec(byte[] key) {
		if (key.length != 16) {
			key = KeyGen.extendKey(key);
		}
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, Algorithm.AES.getValue());
		byte[] encoded = secretKeySpec.getEncoded();
		return new SecretKeySpec(encoded, Algorithm.AES.getValue());
	}
}
