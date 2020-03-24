/*
 * Copyright [3/23/20 8:52 PM] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.spreadme.commons.util.IOUtil;

/**
 * http request param
 * @author shuwei.wang
 */
public class HttpParam {

	public static final String TWO_HYPHENS = "--";
	public static final String LINE_END = "\r\n";

	public static String buildFormData(final String boundary, final String key, final String value) {
		StringBuilder param = new StringBuilder();
		param.append(TWO_HYPHENS).append(boundary).append(LINE_END);
		param.append("Content-Disposition: form-data;")
				.append("name").append("=").append("\"").append(key).append("\"")
				.append(LINE_END);
		param.append("Content-Type: text/plain").append(LINE_END);
		param.append("Content-Lenght: ").append(value.length()).append(LINE_END);
		param.append(LINE_END).append(value).append(LINE_END);
		return param.toString();
	}

	public static void buildMultipart(final String boundary, final String key, final String filename,
			final InputStream input, final OutputStream output) throws IOException {

		StringBuilder param = new StringBuilder();
		param.append(LINE_END).append(TWO_HYPHENS).append(boundary).append(LINE_END);
		param.append("Content-Disposition: form-data;")
				.append("name").append("=").append("\"").append(key).append("\";")
				.append("filename").append("=").append("\"").append(filename).append("\"")
				.append(LINE_END);
		param.append("Content-Type: " + "file/*").append(LINE_END);
		param.append("Content-Lenght: " + "file/*").append(LINE_END).append(LINE_END);

		output.write(param.toString().getBytes());
		IOUtil.copy(input, output);
	}

	/**
	 * 写结束标记位
	 * @param boundary boundary
	 * @param output OutputStream
	 * @throws IOException IOException
	 */
	public static void buildEOF(final String boundary, final OutputStream output) throws IOException {
		byte[] endData = (LINE_END + TWO_HYPHENS + boundary + TWO_HYPHENS + LINE_END).getBytes();
		output.write(endData);
		output.flush();
	}

	//TODO
	public void write(OutputStream out){

	}
}
