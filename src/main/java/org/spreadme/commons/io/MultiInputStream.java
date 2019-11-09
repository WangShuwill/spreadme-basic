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
import java.util.Iterator;

/**
 * MultiInputStream
 * @author shuwei.wang
 */
public class MultiInputStream extends InputStream {

	private final Iterator<? extends InputStream> ins;

	private InputStream in;

	public MultiInputStream(Iterator<? extends InputStream> ins) throws IOException {
		this.ins = ins;
		advance();
	}

	@Override
	public int read() throws IOException {
		while (in != null) {
			int result = in.read();
			if (result != -1) {
				return result;
			}
			advance();
		}
		return -1;
	}

	@Override
	public int read(byte[] b) throws IOException {
		while (in != null) {
			int result = in.read(b);
			if (result != -1) {
				return result;
			}
			advance();
		}
		return -1;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		while (in != null) {
			int result = in.read(b, off, len);
			if (result != -1) {
				return result;
			}
			advance();
		}
		return -1;
	}

	@Override
	public long skip(long n) throws IOException {
		if (in == null || n <= 0) {
			return 0;
		}
		long result = in.skip(n);
		if (result != 0) {
			return result;
		}
		if (read() == -1) {
			return 0;
		}
		return 1 + in.skip(n - 1);
	}

	@Override
	public int available() throws IOException {
		if (in == null) {
			return 0;
		}
		return in.available();
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public void close() throws IOException {
		if (in != null) {
			try {
				in.close();
			}
			finally {
				in = null;
			}
		}
	}

	private void advance() throws IOException {
		close();
		if (ins.hasNext()) {
			in = ins.next();
		}
	}
}
