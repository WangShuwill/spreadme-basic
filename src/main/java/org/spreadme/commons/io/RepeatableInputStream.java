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

import java.io.IOException;
import java.io.InputStream;

/**
 * RepeatableInputStream
 * @author shuwei.wang
 * @since 1.0.0
 */
public class RepeatableInputStream extends InputStream implements InputStreamWrapper {

	private InputStream is;

	private int bufferSize;

	private int bufferOffset = 0;

	private long bytesReadPastMark = 0;

	private byte[] buffer;

	/**
	 * Creates a repeatable input stream based on another input stream.
	 *
	 * @param inputStream
	 * an input stream to wrap. The data read from the wrapped input stream is buffered as it is
	 * read, up to the buffer limit specified.
	 * @param bufferSize
	 * the number of bytes buffered by this class.
	 */
	public RepeatableInputStream(InputStream inputStream, int bufferSize) {
		if (inputStream == null) {
			throw new IllegalArgumentException("InputStream cannot be null");
		}
		this.is = inputStream;
		this.bufferSize = bufferSize;
		this.buffer = new byte[this.bufferSize];
	}

	public RepeatableInputStream(InputStream inputStream) {
		this(inputStream, 131072);
	}

	/**
	 * Resets the input stream to the beginning by pointing the buffer offset to the beginning of the
	 * available data buffer.
	 *
	 * @throws IOException
	 * when the available buffer size has been exceeded, in which case the input stream data cannot
	 * be repeated.
	 */
	@Override
	public void reset() throws IOException {
		if (bytesReadPastMark <= bufferSize) {
			bufferOffset = 0;
		}
		else {
			throw new IOException("Input stream cannot be reset as " + this.bytesReadPastMark + " bytes have been written, exceeding the available buffer size of " + this.bufferSize);
		}
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	/**
	 * This method can only be used while less data has been read from the input
	 * stream than fits into the buffer. The readLimit parameter is ignored entirely.
	 */
	@Override
	public synchronized void mark(int readlimit) {
		if (bytesReadPastMark <= bufferSize && buffer != null) {
			// Clear buffer of already-read data to make more space.
			// it is safe to cast bytesReadPastMark to an int because it is known to be less than bufferSize, which is an int
			byte[] newBuffer = new byte[this.bufferSize];
			System.arraycopy(buffer, bufferOffset, newBuffer, 0, (int) (bytesReadPastMark - bufferOffset));
			this.buffer = newBuffer;
			this.bytesReadPastMark -= bufferOffset;
			this.bufferOffset = 0;
		}
		else {
			// If mark is called after the buffer was already exceeded, create a new buffer.
			this.bufferOffset = 0;
			this.bytesReadPastMark = 0;
			this.buffer = new byte[this.bufferSize];
		}
	}

	@Override
	public int available() throws IOException {
		return is.available();
	}

	@Override
	public void close() throws IOException {
		is.close();
	}

	@Override
	public int read(byte[] out, int outOffset, int outLength) throws IOException {
		byte[] tmp = new byte[outLength];
		// Check whether we already have buffered data.
		if (bufferOffset < bytesReadPastMark && buffer != null) {
			// Data is being repeated, so read from buffer instead of wrapped input stream.
			int bytesFromBuffer = tmp.length;
			if (bufferOffset + bytesFromBuffer > bytesReadPastMark) {
				bytesFromBuffer = (int) bytesReadPastMark - bufferOffset;
			}
			// Write to output.
			System.arraycopy(buffer, bufferOffset, out, outOffset, bytesFromBuffer);
			bufferOffset += bytesFromBuffer;
			return bytesFromBuffer;
		}

		// Read data from input stream.
		int count = is.read(tmp);
		if (count <= 0) {
			return count;
		}
		// Fill the buffer with data, as long as we won't exceed its capacity.
		if (bytesReadPastMark + count <= bufferSize) {
			System.arraycopy(tmp, 0, buffer, (int) bytesReadPastMark, count);
			bufferOffset += count;
		}
		else if (buffer != null) {
			// We have exceeded the buffer capacity, after which point it is of no use. Free the memory.
			buffer = null;
		}
		// Write to output byte array.
		System.arraycopy(tmp, 0, out, outOffset, count);
		bytesReadPastMark += count;
		return count;
	}

	@Override
	public int read() throws IOException {
		byte[] tmp = new byte[1];
		int count = read(tmp);
		if (count != -1) {
			return tmp[0];
		}
		else {
			return count;
		}
	}

	@Override
	public InputStream getWrappedInputStream() {
		return is;
	}

}
