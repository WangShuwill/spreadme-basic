/*
 * Copyright [2/28/20 3:47 PM] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.commons.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import org.spreadme.commons.http.HttpHeader;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * Default HttpClientResponse
 * @author shuwei.wang
 */
public class DefaultHttpClientResponse implements HttpClientResponse {

	private HttpURLConnection connection;

	private InputStream inputStream;

	public DefaultHttpClientResponse(HttpURLConnection connection) {
		this.connection = connection;
	}

	@Override
	public InputStream getBody() throws IOException {
		InputStream errorStream = this.connection.getErrorStream();
		this.inputStream = (errorStream != null ? errorStream : this.connection.getInputStream());
		return this.inputStream;
	}

	@Override
	public int getStatusCode() throws IOException {
		return this.connection.getResponseCode();
	}

	@Override
	public String getResponseMessage() throws IOException {
		return this.connection.getResponseMessage();
	}

	@Override
	public HttpHeader getHeader() {
		HttpHeader httpHeader = new HttpHeader();
		Map<String, List<String>> headers = this.connection.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			if (StringUtil.isNotBlank(entry.getKey())) {
				httpHeader.setHeader(entry.getKey(), String.join(StringUtil.SPACE, entry.getValue()));
			}
		}
		return httpHeader;
	}

	@Override
	public void close() {
		IOUtil.close(this.inputStream);
		if (connection != null) {
			connection.disconnect();
		}
	}
}
