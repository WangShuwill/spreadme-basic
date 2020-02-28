/*
 * Copyright [2/28/20 3:41 PM] [shuwei.wang (c) wswill@foxmail.com]
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
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.spreadme.commons.http.HttpHeader;

/**
 * Default HttpClientRequest
 * @author shuwei.wang
 */
public class DefaultHttpClientRequest implements HttpClientRequest {

	private HttpHeader httpHeader;
	private HttpURLConnection connection;

	private OutputStream body;

	public DefaultHttpClientRequest(HttpHeader httpHeader, HttpURLConnection connection) {
		this.httpHeader = httpHeader;
		this.connection = connection;
	}

	@Override
	public HttpClientResponse execute() throws IOException {
		if (this.body != null) {
			this.body.close();
		}
		else {
			this.connection.connect();
			this.connection.getResponseCode();
		}
		return new DefaultHttpClientResponse(this.connection);
	}

	@Override
	public OutputStream getBody() throws IOException {
		if (this.body == null) {
			this.connection.connect();
			this.body = this.connection.getOutputStream();
		}
		return this.body;
	}

	@Override
	public HttpHeader getHeader() {
		return this.httpHeader;
	}

}
