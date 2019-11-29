/*
 * Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.crypt;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.spreadme.commons.util.IOUtil;

/**
 * DES
 * @author shuwei.wang
 */
public abstract class DES {

	private static final ThreadLocal<Cipher> CIPHER;

	static {
		CIPHER = ThreadLocal.withInitial(() -> {
			try {
				return Cipher.getInstance(Algorithm.DES_CBC_PKCS5Padding.getValue());
			}
			catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		});
	}

	private DES() {
	}

	/**
	 * generate key
	 *
	 * @return byte array of key
	 */
	public static byte[] generateKey() {
		try {
			return KeyGen.generateKey(128, Algorithm.DES);
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static byte[] encrypt(byte[] rawData, byte[] key) throws Exception {
		SecretKey secretKey = getKeySpec(key);
		CIPHER.get().init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(secretKey.getEncoded()));
		return CIPHER.get().doFinal(rawData);
	}

	public static byte[] decrypt(byte[] cipherData, byte[] key) throws Exception {
		SecretKey secretKey = getKeySpec(key);
		CIPHER.get().init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(secretKey.getEncoded()));
		return CIPHER.get().doFinal(cipherData);
	}

	public static void encrypt(InputStream in, OutputStream out, byte[] key) throws Exception{
		SecretKey secretKey = getKeySpec(key);
		Cipher cipher = CIPHER.get();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(secretKey.getEncoded()));
		IOUtil.copy(new CipherInputStream(in, cipher), out);
	}

	public static void decrypt(InputStream in, OutputStream out, byte[] key) throws Exception{
		SecretKey secretKey = getKeySpec(key);
		Cipher cipher = CIPHER.get();
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(secretKey.getEncoded()));
		IOUtil.copy(in, new CipherOutputStream(out, cipher));
	}

	private static SecretKey getKeySpec(byte[] key) throws Exception {
		DESKeySpec keySpec = new DESKeySpec(key);
		SecretKeyFactory factory = SecretKeyFactory.getInstance(Algorithm.DES.getValue());
		return factory.generateSecret(keySpec);
	}
}
