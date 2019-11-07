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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.spreadme.commons.codec.Hex;
import org.spreadme.commons.crypt.Algorithm;
import org.spreadme.commons.crypt.Hash;
import org.spreadme.commons.lang.LineIterator;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.CollectionUtil;
import org.spreadme.commons.util.FileUtil;
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

	@Test
	public void testFiles() throws IOException {
		List<File> files = FileUtil.getFiles(ClassUtil.getClassPath());
		for (File file : files) {
			System.out.println(file);
		}
	}

	@Test
	public void testToFile() throws IOException {
		final String text = "wswei1\nwswei2\nwswei3";
		final String filePath = ClassUtil.getClassPath() + "/file/test.txt";
		IOUtil.toFile(new ByteArrayInputStream(text.getBytes()), filePath);
		final String destPath = ClassUtil.getClassPath() + "/cfile/test.txt";
		IOUtil.copyFile(filePath, destPath);
		try (FileInputStream in = new FileInputStream(new File(destPath))) {
			System.out.println(IOUtil.readLines(in, StandardCharsets.UTF_8));
		}
	}

	@Test
	public void testLineIterator() throws IOException {
		final String text = "wswei1\nwswei2\nwswei3";
		final String filePath = ClassUtil.getClassPath() + "/file/test.txt";
		IOUtil.toFile(new ByteArrayInputStream(text.getBytes()), filePath);
		try (FileInputStream in = new FileInputStream(new File(filePath))) {
			LineIterator iterator = IOUtil.lineIterator(in, StandardCharsets.UTF_8);
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
			}
		}
	}

	@Test
	public void testZipFiles() throws IOException {
		File f1 = new File("/Users/wangshuwei/Downloads/jre/jre6");
		File f2 = new File("/Users/wangshuwei/Downloads/jre/jre7");
		File f3 = new File("/Users/wangshuwei/Downloads/jre/jre8");
		List<File> files = CollectionUtil.toList(f1, f2, f3);
		File zipFile = new File(ClassUtil.getClassPath() + "text.zip");
		IOUtil.zipFiles(zipFile, files);
	}
}
