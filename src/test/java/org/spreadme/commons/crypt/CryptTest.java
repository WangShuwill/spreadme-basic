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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.spreadme.commons.codec.Base64;
import org.spreadme.commons.codec.Hex;
import org.spreadme.commons.lang.Console;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.Concurrents;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public class CryptTest {

	private static final String TEST_FILE_NAME = "CORE_TEST_FILE_ONE.txt";

	private File testFile = null;

	private byte[] publicKey;

	private byte[] privateKey;

	@Before
	public void init() throws Exception {
		testFile = new File(ClassUtil.getClassPath() + File.separator + TEST_FILE_NAME);
		KeyPair keyPair = RSA.getKeyPair();
		publicKey = RSA.getPublicKey(keyPair);
		privateKey = RSA.getPrivateKey(keyPair);

		Console.info("The publickey is %s", Hex.toHexString(publicKey));
		Console.info("The privatekey is %s", Hex.toHexString(privateKey));
	}

	@Test
	public void hashTest() throws Exception {
		try (FileInputStream in = new FileInputStream(testFile)) {
			byte[] hash = Hash.get(in, Algorithm.MD5);
			Console.info(Hex.toHexString(hash));
		}
	}

	@Test
	public void md5Test() throws Exception {
		final String data = "test";

		final int THREAD_POOL_SIZE = 100;
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

		Concurrents.startAllTaskInOnce(THREAD_POOL_SIZE, () -> {
			try {
				Console.info(Hex.toHexString(Hash.get(new ByteArrayInputStream(data.getBytes()), Algorithm.MD5)));
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}, executor);
	}

	@Test
	public void aesTest() throws Exception {
		Concurrents.startAllTaskInOnce(10, () -> {
			final String data = "test";
			byte[] key = AES.generateKey();
			byte[] encrypt = AES.encrypt(data.getBytes(), key);
			byte[] origin = AES.decrypt(encrypt, key);
			String result = String.format("The key is {%s}, the encrty is {%s}, the origin is {%s}",
					Hex.toHexString(key), Hex.toHexString(encrypt), StringUtil.fromByteArray(origin));
			Console.info(result);
			return result;
		});

	}

	@Test
	public void testRSA() throws Exception {
		byte[] text = "wswei".getBytes(StandardCharsets.UTF_8);
		byte[] encryptData = RSA.encryptByPublicKey(text, publicKey);
		Console.info(Base64.toBase64String(encryptData));
		byte[] rawData = RSA.decryptByPrivateKey(encryptData, privateKey);
		Console.info(new String(rawData));

		byte[] signData = RSA.sign(text, privateKey, Algorithm.SHA256withRSA);
		Console.info(Base64.toBase64String(signData));
		Console.info(RSA.verify(text, publicKey, signData, Algorithm.SHA256withRSA));
	}

	@Test
	public void testBCrypt() {
		final String pwd = "1234";
		Console.info(BCrypt.hashpw(pwd, BCrypt.gensalt()));
		Console.info(BCrypt.hashpw(pwd, BCrypt.gensalt()));
		Console.info(BCrypt.checkpw(pwd, "$2a$10$bA2.WOmsYX/9To0w5BkaueORBgQPTuvS8MxR6YKTbTk948/5GlfkS"));
		Console.info(XOR.encrypt(pwd));
	}

	@Test
	public void testKeyGenerator() throws InterruptedException {
		Concurrents.startAllTaskInOnce(10, () -> {
			byte[] result = AES.generateKey();
			Console.info(Hex.toHexString(result));
			return result;
		});
	}

	@Test
	public void testDes() throws Exception {
		final String text = "wswei";
		byte[] key = AES.generateKey();
		byte[] encrypt = DES.encrypt(text.getBytes(), key);
		byte[] origin = DES.decrypt(encrypt, key);
		Console.info("The key is {%s}, the encrty is {%s}, the origin is {%s}",
				Hex.toHexString(key),
				Hex.toHexString(encrypt),
				StringUtil.fromByteArray(origin));
	}

	@Test
	public void testAESFile() throws Exception{
		try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(testFile));
			ByteArrayOutputStream out = new ByteArrayOutputStream()){

			byte[] key = AES.generateKey();
			AES.encrypt(in, out, key);
			Console.info(Hex.toHexString(out.toByteArray()));

			ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			AES.decrypt(bis, bos, key);
			Console.info(StringUtil.fromByteArray(bos.toByteArray()));
		}
	}
}
