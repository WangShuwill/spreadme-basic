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

package org.spreadme.commons.lang;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.spreadme.commons.util.IOUtil;

/**
 * LineIterator
 * @author shuwei.wang
 */
public class LineIterator implements Iterator<String>, Closeable {

	private final BufferedReader bufferedReader;

	private String cacheLine;

	private boolean finished = false;

	public LineIterator(Reader reader) {
		if (reader == null) {
			throw new IllegalArgumentException("Reader must not be null");
		}
		if (reader instanceof BufferedReader) {
			this.bufferedReader = (BufferedReader) reader;
		}
		else {
			this.bufferedReader = new BufferedReader(reader);
		}
	}

	@Override
	public boolean hasNext() {
		if (cacheLine != null) {
			return true;
		}
		else if (finished) {
			return false;
		}
		else {
			try {
				while (true) {
					final String line = bufferedReader.readLine();
					if (line == null) {
						finished = true;
						return false;
					}
					else {
						cacheLine = line;
						return true;
					}
				}
			}
			catch (IOException ex) {
				IOUtil.close(this);
				throw new IllegalStateException(ex);
			}
		}
	}

	@Override
	public String next() {
		if (!hasNext()) {
			throw new NoSuchElementException("No more lines");
		}
		final String currentLine = cacheLine;
		cacheLine = null;
		return currentLine;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Remove unsupported on LineIterator");
	}

	@Override
	public void close() throws IOException {
		cacheLine = null;
		if (this.bufferedReader != null) {
			this.bufferedReader.close();
		}
	}
}
