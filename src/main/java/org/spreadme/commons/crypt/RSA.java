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

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.spreadme.commons.codec.Base64;

/**
 * rsa
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class RSA {

	private static final int MAX_ENCRYPT_BLOCK = 117; //RSA最大加密明文大小

	private static final int MAX_DECRYPT_BLOCK = 128; //RSA最大解密密文大小

	private RSA() {

	}

	/**
	 * 生成密钥对
	 *
	 * @return 密钥对
	 * @throws NoSuchAlgorithmException {@link NoSuchAlgorithmException} NoSuchAlgorithmException
	 */
	public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
		return getKeyPair(2014);
	}

	/**
	 * 生成密钥对
	 *
	 * @param keysize key size
	 * @return 密钥对
	 * @throws NoSuchAlgorithmException {@link NoSuchAlgorithmException} NoSuchAlgorithmException
	 */
	public static KeyPair getKeyPair(int keysize) throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Algorithm.RSA.getValue());
		keyGen.initialize(keysize);
		return keyGen.generateKeyPair();
	}

	/**
	 * 从密钥对中获取公钥
	 *
	 * @param keyPair 密钥对
	 * @return BASE64编码的公钥
	 */
	public static String getPublicKey(KeyPair keyPair) {
		PublicKey publicKey = keyPair.getPublic();
		return Base64.toBase64String(publicKey.getEncoded());
	}

	/**
	 * 从密钥对中获取私钥
	 *
	 * @param keyPair 密钥对
	 * @return BASE64编码的私钥
	 */
	public static String getPrivateKey(KeyPair keyPair) {
		PrivateKey privateKey = keyPair.getPrivate();
		return Base64.toBase64String(privateKey.getEncoded());
	}

	/**
	 * 公钥加密
	 *
	 * @param data 待加密的数据
	 * @param publicKey 公钥
	 * @return 加密后的数据
	 * @throws Exception  Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		byte[] keybytes = Base64.decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keybytes);
		KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA.getValue());
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		return getDataByCipher(cipher, data, MAX_ENCRYPT_BLOCK);
	}

	/**
	 * 密钥解密
	 *
	 * @param cipherData 待解密的数据
	 * @param privateKey 密钥
	 * @return 解密后的数据
	 * @throws Exception Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] cipherData, String privateKey) throws Exception {
		byte[] keyBytes = Base64.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA.getValue());
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		return getDataByCipher(cipher, cipherData, MAX_DECRYPT_BLOCK);
	}

	private static byte[] getDataByCipher(Cipher cipher, byte[] data, int blockSize) throws Exception {
		int dataLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offset = 0;
		byte[] cache;
		int i = 0;
		while (dataLen - offset > 0) {
			if (dataLen - offset > blockSize) {
				cache = cipher.doFinal(data, offset, blockSize);
			}
			else {
				cache = cipher.doFinal(data, offset, dataLen - offset);
			}
			out.write(cache, 0, cache.length);
			i++;
			offset = i * blockSize;
		}
		byte[] rawdata = out.toByteArray();
		out.close();
		return rawdata;
	}
}
