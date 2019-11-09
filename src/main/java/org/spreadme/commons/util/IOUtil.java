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

package org.spreadme.commons.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.spreadme.commons.lang.FileWriteMode;
import org.spreadme.commons.lang.LineIterator;

/**
 * io util
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class IOUtil {

	private static final int EOF = -1;

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 16;

	/**
	 * copy inputsteam to outputstream
	 *
	 * @param in inputsteam
	 * @param out outputstream
	 * @throws IOException IOException
	 */
	public static void copy(final InputStream in, final OutputStream out) throws IOException {
		final ReadableByteChannel readableChannel = Channels.newChannel(in);
		final WritableByteChannel writableChannl = Channels.newChannel(out);
		copy(readableChannel, writableChannl);
	}

	/**
	 * copy ReadableByteChannel to WritableByteChannel
	 *
	 * @param readableChannel ReadableByteChannel
	 * @param writableChannl WritableByteChannel
	 * @throws IOException IOException
	 */
	public static void copy(final ReadableByteChannel readableChannel, final WritableByteChannel writableChannl) throws IOException {
		final ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
		while (readableChannel.read(byteBuffer) != EOF) {
			byteBuffer.flip();
			writableChannl.write(byteBuffer);
			byteBuffer.compact();
		}
		byteBuffer.flip();
		while (byteBuffer.hasRemaining()) {
			writableChannl.write(byteBuffer);
		}
	}

	/**
	 * copy file
	 *
	 * @param srcPath src path
	 * @param destPath dest path
	 * @throws IOException IOException
	 */
	public static void copyFile(final String srcPath, final String destPath) throws IOException {
		File dest = new File(destPath);
		if (!dest.exists()) {
			dest = FileUtil.createFile(destPath, true);
		}
		copyFile(new File(srcPath), dest);
	}

	/**
	 * copy file
	 *
	 * @param src src file
	 * @param dest dest file
	 * @throws IOException IOException
	 */
	public static void copyFile(final File src, final File dest) throws IOException {
		if (dest.exists() && dest.isDirectory()) {
			throw new IOException("Destination '" + dest + "' exists but is a directory");
		}
		Path srcPath = src.toPath();
		Path destPath = dest.toPath();
		Files.copy(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * intputstream to byte array
	 *
	 * @param input InputStream
	 * @return byte array
	 * @throws IOException IOException
	 */
	public static byte[] toByteArray(InputStream input) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		copy(input, bos);
		return bos.toByteArray();
	}

	/**
	 * intputstream to file
	 *
	 * @param in InputStream
	 * @param file File
	 * @throws IOException IOException
	 */
	public static void toFile(InputStream in, File file) throws IOException {
		try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
			copy(in, out);
		}
	}

	/**
	 * intputstream to file
	 *
	 * @param in InputStream
	 * @param path file path
	 * @throws IOException IOException
	 */
	public static void toFile(InputStream in, String path) throws IOException {
		File file = FileUtil.createFile(path, true);
		toFile(in, file);
	}

	/**
	 * read lines from reader
	 *
	 * @param input Reader
	 * @return lines
	 * @throws IOException IOException
	 */
	public static List<String> readLines(final Reader input) throws IOException {
		final BufferedReader reader = new BufferedReader(input);
		final List<String> lines = new ArrayList<>();
		String line = reader.readLine();
		while (line != null) {
			lines.add(line);
			line = reader.readLine();
		}
		return lines;
	}

	/**
	 * read lines from reader
	 *
	 * @param input InputStream
	 * @param charset Charset {@link java.nio.charset.StandardCharsets}
	 * @return lines
	 * @throws IOException IOException
	 */
	public static List<String> readLines(final InputStream input, final Charset charset) throws IOException {
		final InputStreamReader reader = new InputStreamReader(input, charset);
		return readLines(reader);
	}

	/**
	 * line iterator
	 *
	 * @param input InputStream
	 * @param charset Charset {@link java.nio.charset.StandardCharsets}
	 * @return LineIterator {@link LineIterator}
	 */
	public static LineIterator lineIterator(final InputStream input, final Charset charset) {
		return new LineIterator(new InputStreamReader(input, charset));
	}

	/**
	 * line iterator
	 *
	 * @param reader Reader
	 * @return LineIterator {@link LineIterator}
	 */
	public static LineIterator lineIterator(final Reader reader) {
		return new LineIterator(reader);
	}

	/**
	 * zip files
	 *
	 * @param zip zip file
	 * @param files files
	 * @throws IOException IOException
	 */
	public static void zipFiles(File zip, List<File> files) throws IOException {
		try (WritableByteChannel writableChann = Channels.newChannel(new FileOutputStream(zip))) {
			Pipe pipe = Pipe.open();
			CompletableFuture.runAsync(() -> zipTask(pipe, files));
			ReadableByteChannel readableChann = pipe.source();
			copy(readableChann, writableChann);
		}
	}

	private static void zipTask(Pipe pipe, List<File> files) {
		try (ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(pipe.sink()));
			 WritableByteChannel writableChann = Channels.newChannel(zos)) {

			for (File file : files) {
				doZip(zos, file, writableChann, StringUtil.EMPTY);
			}
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex.getMessage(), ex);
		}
	}

	private static void doZip(ZipOutputStream zos, File file, WritableByteChannel writableChann, String base) throws IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			base = StringUtil.isBlank(base) ? file.getName() + File.separator : base + File.separator;
			if (files != null) {
				for (File item : files) {
					doZip(zos, item, writableChann, base + item.getName());
				}
			}
		}
		else {
			base = StringUtil.isBlank(base) ? file.getName() : base;
			zos.putNextEntry(new ZipEntry(base));
			FileChannel fileChann = new FileInputStream(new File(file.getAbsolutePath())).getChannel();
			fileChann.transferTo(0, fileChann.size(), writableChann);
			fileChann.close();
		}
	}

	/**
	 * append text to file
	 *
	 * @param text text
	 * @param file file
	 * @param mode file write mode {@link FileWriteMode}
	 * @throws IOException IOException
	 */
	public static void append(CharSequence text, File file, FileWriteMode mode) throws IOException {
		boolean append = mode == FileWriteMode.APPEND;
		try (FileWriter writer = new FileWriter(file, append);
			 BufferedWriter bWriter = new BufferedWriter(writer)) {
			bWriter.append(text);
		}
	}

	/**
	 * close closeable resource
	 *
	 * @param resources Closeable resource
	 */
	public static void close(Closeable... resources) {
		if (resources == null) {
			return;
		}
		try {
			for (Closeable resource : resources) {
				if (resource != null) {
					resource.close();
				}
			}
		}
		catch (IOException ignore) {

		}
	}

}
