/*
 * Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.spreadme.basic.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public abstract class IOUtil {

	private static final int EOF = -1;

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	/**
	 *
	 * @param input InputStream
	 * @return byte array
	 * @throws IOException IOException
	 */
	private static byte[] toByteArray(InputStream input) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		copy(input, bos);
		return bos.toByteArray();
	}

	/**
	 *
	 * @param input InputStream
	 * @param output OutputStream
	 * @return content length
	 * @throws IOException IOException
	 */
	public static long copy(final InputStream input, final OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static void close(Closeable... resources) {
		if (resources != null) {
			try {
				for (Closeable resource : resources) {
					resource.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void compress(File srcFile, File dstFile) throws IOException {
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Zip file " + srcFile.getPath() + "not found");
		}
		try (FileOutputStream fos = new FileOutputStream(dstFile);
			 CheckedOutputStream cos = new CheckedOutputStream(fos, new CRC32());
			 ZipOutputStream zos = new ZipOutputStream(cos)) {
			String baseDir = "";
			compress(srcFile, zos, baseDir);
		}
	}

	public static void decompress(File zipFile, File dstFile) throws IOException {
		if (!dstFile.exists()) {
			dstFile.mkdirs();
		}
		try (ZipFile zip = new ZipFile(zipFile)) {
			Enumeration enumeration = zip.entries();
			while (enumeration.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
				String zipEntryName = zipEntry.getName();
				String outPath = (dstFile.getPath() + "/" + zipEntryName).replaceAll("\\*", "/");
				//判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf("/")));
				if (!file.exists()) {
					file.mkdirs();
				}
				//判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (new File(outPath).isDirectory()) {
					continue;
				}
				try (InputStream input = zip.getInputStream(zipEntry);
					 OutputStream output = new FileOutputStream(outPath)) {
					copy(input, output);
				}
			}
		}
	}

	private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
		if (file.isDirectory()) {
			compressDirectory(file, zipOut, baseDir);
		}
		else {
			compressFile(file, zipOut, baseDir);
		}
	}

	private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				compress(file, zipOut, baseDir + dir.getName() + "/");
			}
		}
	}

	private static void compressFile(File file, ZipOutputStream zipOutput, String baseDir) throws IOException {
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			ZipEntry zipEntry = new ZipEntry(baseDir + file.getName());
			zipOutput.putNextEntry(zipEntry);
			copy(bis, zipOutput);
		}
	}

	public static File createFile(String path, boolean isFile) throws IOException {
		path = path.replaceAll("\\*", "/");
		File file = new File(path);
		if(!file.exists()){
			if(isFile){
				String directoryPath = path.substring(0, path.lastIndexOf("/"));
				File directoryFile = new File(directoryPath);
				if(!directoryFile.exists()){
					directoryFile.mkdirs();
				}
				file.createNewFile();
				return file;
			}
			else{
				file.mkdirs();
				return file;
			}
		}
		return file;
	}
}
