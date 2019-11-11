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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.spreadme.commons.codec.Hex;
import org.spreadme.commons.crypt.Algorithm;
import org.spreadme.commons.crypt.Hash;
import org.spreadme.commons.lang.Charsets;
import org.spreadme.commons.lang.LineIterator;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.CollectionUtil;
import org.spreadme.commons.util.FileUtil;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public class IOTest {

	private static final String TEST_FILE_NAME_ONE = "CORE_TEST_FILE_ONE.txt";

	private static final String TEST_FILE_NAME_TWO = "CORE_TEST_FILE_ONE.txt";

	private static final String ZIP_FILE = "jre6.zip";

	private File testFileOne = null;

	private File testFileTwo = null;

	@Before
	public void init() {
		testFileOne = new File(ClassUtil.getClassPath() + File.separator + TEST_FILE_NAME_ONE);
		testFileTwo = new File(ClassUtil.getClassPath() + File.separator + TEST_FILE_NAME_ONE);
	}

	@Test
	public void testMessageDigestInputStream() throws Exception {
		MessageDigest digest = Hash.getMessageDigest(Algorithm.SHA256);
		try (FileInputStream in = new FileInputStream(testFileOne);
			 MessageDigestInputStream digestInput = new MessageDigestInputStream(in, digest);
			 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			IOUtil.copy(digestInput, out);
			System.out.println(Hex.toHexString(digest.digest()));
			System.out.println(Hash.toHexString(new ByteArrayInputStream(out.toByteArray()), Algorithm.SHA256));
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

	@Test
	public void testUnzipFiles() throws IOException {
		File zipFile = new File(ClassUtil.getClassPath() + File.separator + ZIP_FILE);
		try (FileInputStream in = new FileInputStream(zipFile)) {
			IOUtil.unzip(in, new File(ClassUtil.getClassPath()), Charsets.UTF_8);
		}
	}

	@Test
	public void testMultiInputStream() throws IOException {
		List<FileInputStream> ins = CollectionUtil.toList(testFileOne, testFileTwo)
				.stream()
				.map(IOTest::toFileInputStream)
				.collect(Collectors.toList());
		try (MultiInputStream in = new MultiInputStream(ins.iterator())) {
			System.out.println(StringUtil.fromInputStream(in));
		}
	}

	private static FileInputStream toFileInputStream(File file) {
		try {
			return new FileInputStream(file);
		}
		catch (FileNotFoundException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
}
