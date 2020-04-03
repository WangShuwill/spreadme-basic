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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.spreadme.commons.codec.Base64;
import org.spreadme.commons.codec.Hex;
import org.spreadme.commons.lang.Assert;
import org.spreadme.commons.lang.Charsets;
import org.spreadme.commons.util.Console;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.Concurrents;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public class CryptTest {

	private static final String TEST_FILE_NAME = "CORE_TEST_FILE_ONE.txt";

	private File testFile;

	private byte[] publicKey;

	private byte[] privateKey;

	@Before
	public void init() throws Exception {
		testFile = new File(ClassUtil.getClassPath() + File.separator + TEST_FILE_NAME);
		KeyPair keyPair = RSA.getKeyPair();
		publicKey = RSA.getPublicKey(keyPair);
		privateKey = RSA.getPrivateKey(keyPair);
	}

	@Test
	public void fileHashTest() throws Exception {
		try (FileInputStream in = new FileInputStream(testFile)) {
			byte[] hash = Digest.get(in, Algorithm.MD5);
			Console.info("%s文件MD5值 %s", testFile.getName(), Hex.toHexString(hash));
		}
	}

	@Test
	public void concurrentDigestTest() throws Exception {
		final String plainText = StringUtil.randomString(10);
		final byte[] data = plainText.getBytes(Charsets.UTF_8);
		final int poolSize = 10;
		final Set<String> hashSet = Collections.synchronizedSet(new HashSet<>());

		ExecutorService executor = Executors.newFixedThreadPool(poolSize);
		Concurrents.startAll(poolSize, () -> {
			try {
				String hash = Hex.toHexString(Digest.get(new ByteArrayInputStream(data), Algorithm.MD5));
				Console.info("%s的MD5值为 %s", plainText, hash);
				hashSet.add(hash);
			}
			catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
		}, executor);

		Assert.isTrue(hashSet.size() == 1, "多线程摘要测试失败");
	}

	@Test
	public void aesTest() throws Exception {
		final String data = StringUtil.randomString(10);
		Concurrents.startAll(10, () -> {
			byte[] key = AES.generateKey();
			byte[] encrypt = AES.encrypt(data.getBytes(), key, true);
			byte[] origin = AES.decrypt(encrypt, key, true);
			String result = String.format("The key is {%s}, the encrty is {%s}, the origin is {%s}",
					Hex.toHexString(key), Hex.toHexString(encrypt), StringUtil.fromByteArray(origin));
			Console.info(result);
			return result;
		});
	}

	@Test
	public void testRSA() throws Exception {
		final String data = StringUtil.randomString(10);
		byte[] text = data.getBytes(StandardCharsets.UTF_8);
		byte[] encryptData = RSA.encryptByPublicKey(text, publicKey);
		Console.info("RSA加密数据 %s", Base64.toBase64String(encryptData));
		byte[] rawData = RSA.decryptByPrivateKey(encryptData, privateKey);
		Console.info("RSA解密数据 %s", new String(rawData));

		byte[] signData = RSA.sign(text, privateKey, Algorithm.SHA256withRSA);
		Console.info("SHA256withRSA签名数据 %s", Base64.toBase64String(signData));
		Console.info("SHA256withRSA验签是否成功 %s", RSA.verify(text, publicKey, signData, Algorithm.SHA256withRSA));
	}


	@Test
	public void testAESFile() throws Exception {
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(testFile))) {

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] key = AES.generateKey();
			InputStream cipherIn = AES.encrypt(in, key, true);
			IOUtil.copy(cipherIn, out);
			Console.info("%s文件AES加密数据 %s", testFile.getName(), Hex.toHexString(out.toByteArray()));

			ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
			ByteArrayOutputStream outer = new ByteArrayOutputStream();
			OutputStream cipherOut = AES.decrypt(outer, key, true);
			IOUtil.copy(bis, cipherOut);
			Console.info("%s文件AES解密数据 %s", testFile.getName(), new String(outer.toByteArray()));
		}
	}
}
