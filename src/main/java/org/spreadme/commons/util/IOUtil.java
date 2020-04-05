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
import java.io.ByteArrayInputStream;
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
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.spreadme.commons.io.FastByteArrayOutputStream;
import org.spreadme.commons.io.RepeatableInputStream;
import org.spreadme.commons.lang.LineIterator;
import org.spreadme.commons.lang.Resource;
import org.spreadme.commons.lang.WriteMode;

/**
 * io util
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class IOUtil {

	private static final int EOF = -1;

	// buffer size used for reading and writing
	private static final int DEFAULT_BUFFER_SIZE = 8192;

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
	 * @param readableChan ReadableByteChannel
	 * @param writableChan WritableByteChannel
	 * @throws IOException IOException
	 */
	public static void copy(final ReadableByteChannel readableChan, final WritableByteChannel writableChan) throws IOException {
		final ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
		while (readableChan.read(byteBuffer) != EOF) {
			byteBuffer.flip();
			writableChan.write(byteBuffer);
			byteBuffer.compact();
		}
		byteBuffer.flip();
		while (byteBuffer.hasRemaining()) {
			writableChan.write(byteBuffer);
		}
	}

	/**
	 * copy file
	 *
	 * @param srcPath src path
	 * @param destPath dest path
	 * @throws IOException IOException
	 */
	public static void copy(final String srcPath, final String destPath) throws IOException {
		File dest = new File(destPath);
		if (!dest.exists()) {
			dest = FileUtil.createFile(destPath, true);
		}
		copy(new File(srcPath), dest);
	}

	/**
	 * copy file
	 *
	 * @param src src file
	 * @param dest dest file
	 * @throws IOException IOException
	 */
	public static void copy(final File src, final File dest) throws IOException {
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
		final FastByteArrayOutputStream bos = new FastByteArrayOutputStream();
		copy(input, bos);
		return bos.toByteArray();
	}

	/**
	 * string to file
	 * @param content content
	 * @param file file
	 * @throws IOException IOException
	 */
	public static void toFile(String content, File file) throws IOException {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(content);
		}
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
	 * write bytes to file
	 * @param bytes bytes
	 * @param path file path
	 * @throws IOException IOException
	 */
	public static void toFile(byte[] bytes, String path) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		toFile(in, path);
	}

	/**
	 * form path to inputstream
	 *
	 * @param path path
	 * @param openOptions open options {@link OpenOption}
	 * @return InputStream
	 * @throws IOException IOException
	 */
	public static InputStream toInputstream(String path, OpenOption... openOptions) throws IOException {
		return Files.newInputStream(Paths.get(path), openOptions);
	}

	/**
	 * form file to inputstream
	 *
	 * @param file File
	 * @param openOptions open options {@link OpenOption}
	 * @return Inputstream
	 * @throws IOException IOException
	 */
	public static InputStream toInputstream(File file, OpenOption... openOptions) throws IOException {
		return Files.newInputStream(file.toPath(), openOptions);
	}

	/**
	 * change inputstream to repeatable
	 *
	 * @param inputStream InputStream
	 * @return RepeatableInputStream {@link RepeatableInputStream}
	 */
	public static RepeatableInputStream toRepeatable(InputStream inputStream) {
		return new RepeatableInputStream(inputStream);
	}

	/**
	 *  form path to outputStream
	 *
	 * @param path path
	 * @param openOptions open options {@link OpenOption}
	 * @return OutputStream
	 * @throws IOException IOException
	 */
	public static OutputStream toOutputstream(String path, OpenOption... openOptions) throws IOException {
		return Files.newOutputStream(Paths.get(path), openOptions);
	}

	/**
	 * form file to outputStream
	 *
	 * @param file file
	 * @param openOptions open options {@link OpenOption}
	 * @return OutputStream
	 * @throws IOException IOException
	 */
	public static OutputStream toOutputstream(File file, OpenOption... openOptions) throws IOException {
		return Files.newOutputStream(file.toPath(), openOptions);
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
		zipFiles(files, new FileOutputStream(zip));
	}

	/**
	 * zip files
	 *
	 * @param files files
	 * @param out OutputStream
	 * @throws IOException IOException
	 */
	public static void zipFiles(List<File> files, OutputStream out) throws IOException {
		try (WritableByteChannel writableChann = Channels.newChannel(new BufferedOutputStream(out))) {
			Pipe pipe = Pipe.open();
			CompletableFuture.runAsync(() -> zipTask(pipe, files));
			ReadableByteChannel readableChann = pipe.source();
			copy(readableChann, writableChann);
		}
	}

	/**
	 * zip Resource
	 *
	 * @param entries list of Resource {@link Resource}
	 * @param out OutputStream
	 * @throws IOException IOException
	 */
	public static void zipResouces(final List<Resource> entries, OutputStream out) throws IOException {
		try (WritableByteChannel writableChann = Channels.newChannel(out)) {
			Pipe pipe = Pipe.open();
			CompletableFuture.runAsync(() -> {
				try (ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(pipe.sink()));
					 WritableByteChannel writableChannl = Channels.newChannel(zos)) {
					for (Resource entry : entries) {
						zos.putNextEntry(new ZipEntry(entry.getName()));
						final ReadableByteChannel readableChannel = Channels.newChannel(entry.getInputStream());
						IOUtil.copy(readableChannel, writableChannl);
						readableChannel.close();
					}
				}
				catch (IOException ex) {
					throw new IllegalStateException(ex.getMessage(), ex);
				}
			});
			ReadableByteChannel readableChann = pipe.source();
			copy(readableChann, writableChann);
		}
	}

	/**
	 * zip task
	 *
	 * @param pipe pipe
	 * @param files files
	 */
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

	/**
	 * realy zip task
	 *
	 * @param zos ZipOutputStream
	 * @param file File
	 * @param writableChann WritableByteChannel
	 * @param base base
	 * @throws IOException IOException
	 */
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
	 * 解压zip文件
	 *
	 * @param in InputStream
	 * @param destDir Destination Dir
	 * @param charset Charset
	 * @throws IOException IOException
	 */
	public static void unzip(InputStream in, File destDir, Charset charset) throws IOException {
		try (ZipInputStream zis = new ZipInputStream(in, charset)) {
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				File destFile = new File(destDir, zipEntry.getName());
				if (zipEntry.isDirectory() && !destFile.exists()) {
					FileUtil.createFile(destFile.getPath(), false);
					zipEntry = zis.getNextEntry();
					continue;
				}
				String destDirPath = destDir.getCanonicalPath();
				String destFilePath = destFile.getCanonicalPath();
				if (!destFilePath.startsWith(destDirPath + File.separator)) {
					throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
				}

				try (FileOutputStream fos = new FileOutputStream(destFile);
					 BufferedOutputStream bos = new BufferedOutputStream(fos)) {
					IOUtil.copy(zis, bos);
				}
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
		}
	}

	/**
	 * append text to file
	 *
	 * @param text text
	 * @param file file
	 * @param mode file write mode {@link WriteMode}
	 * @throws IOException IOException
	 */
	public static void append(CharSequence text, File file, WriteMode mode) throws IOException {
		boolean append = mode == WriteMode.APPEND;
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
		if (resources == null) return;
		try {
			for (Closeable resource : resources) {
				if (resource != null) resource.close();
			}
		}
		catch (IOException ignore) {
		}
	}

}
