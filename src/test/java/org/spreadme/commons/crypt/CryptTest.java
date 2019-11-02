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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;

import org.junit.Before;
import org.junit.Test;
import org.spreadme.commons.codec.Hex;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public class CryptTest {

	private static final String TEST_FILE_NAME = "CORE_TEST_FILE.txt";

	private File testFile = null;

	private String publicKey;

	private String privateKey;


	@Before
	public void init() throws Exception {
		testFile = new File(ClassUtil.getClassPath() + File.separator + TEST_FILE_NAME);
		KeyPair keyPair = RSA.getKeyPair();
		publicKey = RSA.getPublicKey(keyPair);
		privateKey = RSA.getPrivateKey(keyPair);

		System.out.println("publickey: " + publicKey);
		System.out.println("privatekey: " + privateKey);
	}

	@Test
	public void hashTest() throws Exception {
		try (FileInputStream in = new FileInputStream(testFile)) {
			byte[] hash = Hash.get(in, Algorithm.MD5);
			System.out.println(Hex.toHexString(hash));
		}
	}

	@Test
	public void md5Test() {
		final String data = "test";
		System.out.println(Hex.toHexString(Hash.get(new ByteArrayInputStream(data.getBytes()), Algorithm.MD5)));
	}

	@Test
	public void aesTest() throws Exception {
		String key = AES.generateKey();
		System.out.println(key);
		String data = "test";
		byte[] encrypt = AES.encrypt(data.getBytes(), key.getBytes());
		System.out.println(StringUtil.fromByteArray(encrypt));
		byte[] origin = AES.decrypt(encrypt, key.getBytes());
		System.out.println(StringUtil.fromByteArray(origin));
	}

	@Test
	public void testRSA() throws Exception {
		byte[] text = "wswei".getBytes(StandardCharsets.UTF_8);
		byte[] encryptData = RSA.encryptByPublicKey(text, publicKey);
		System.out.println(Base64.getEncoder().encodeToString(encryptData));
		byte[] rawData = RSA.decryptByPrivateKey(encryptData, privateKey);
		System.out.println(new String(rawData));
	}
}
