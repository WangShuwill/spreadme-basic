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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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
	 * @param charset Charset
	 * @return lines
	 * @throws IOException IOException
	 */
	public static List<String> readLines(final InputStream input, final Charset charset) throws IOException {
		final InputStreamReader reader = new InputStreamReader(input, charset);
		return readLines(reader);
	}

	/**
	 * close closeable resource
	 *
	 * @param resources Closeable resource
	 */
	public static void close(Closeable... resources) {
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

	/**
	 * get temp dir
	 *
	 * @return temp dir
	 */
	public static File getTempDir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

}
