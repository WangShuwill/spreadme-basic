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

package org.spreadme.commons.digest;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.spreadme.commons.codec.Hex;
import org.spreadme.commons.lang.Assert;
import org.spreadme.commons.lang.Charsets;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.Concurrents;
import org.spreadme.commons.util.Console;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public class DigestTest {

	private static final String TEST_FILE_NAME = "CORE_TEST_FILE_ONE.txt";

	private File testFile;

	@Before
	public void init() {
		testFile = new File(ClassUtil.getClassPath() + File.separator + TEST_FILE_NAME);
	}

	@Test
	public void fileHashTest() throws Exception {
		try (FileInputStream in = new FileInputStream(testFile)) {
			byte[] hash = Digest.get(in, Digest.Algorithm.MD5);
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
				String hash = Digest.toHexString(data, Digest.Algorithm.SHA256);
				Console.info("%s的SHA256值为 %s", plainText, hash);
				hashSet.add(hash);
			}
			catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
		}, executor);

		Assert.isTrue(hashSet.size() == 1, "多线程摘要测试失败");
	}

}
