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

package org.spreadme.commons.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

import org.junit.Before;
import org.junit.Test;
import org.spreadme.commons.codec.Hex;
import org.spreadme.commons.crypt.Algorithm;
import org.spreadme.commons.crypt.Hash;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.IOUtil;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public class IOTest {

	private static final String TEST_FILE_NAME = "CORE_TEST_FILE.txt";

	private File testFile = null;

	@Before
	public void init() {
		testFile = new File(ClassUtil.getClassPath() + File.separator + TEST_FILE_NAME);
	}

	@Test
	public void testMessageDigestInputStream() throws Exception {
		MessageDigest digest = Hash.getMessageDigest(Algorithm.SHA256);
		try (FileInputStream in = new FileInputStream(testFile);
			 MessageDigestInputStream digestInput = new MessageDigestInputStream(in, digest);
			 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			IOUtil.copy(digestInput, out);
			System.out.println(Hex.toHexString(digest.digest()));
			System.out.println(Hash.toHashString(new ByteArrayInputStream(out.toByteArray()), Algorithm.SHA256));
		}
	}
}
