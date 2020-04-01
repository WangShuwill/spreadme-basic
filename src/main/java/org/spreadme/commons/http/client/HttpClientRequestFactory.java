/*
 * Copyright [2/28/20 3:12 PM] [shuwei.wang (c) wswill@foxmail.com]
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
import java.net.Proxy;
import java.net.URI;

import org.spreadme.commons.http.HttpHeader;
import org.spreadme.commons.http.HttpMethod;

/**
 * Http Client Request Factory
 * @author shuwei.wang
 */
public interface HttpClientRequestFactory {

	/**
	 * set proxy
	 * @param proxy proxy
	 */
	void setProxy(Proxy proxy);

	/**
	 * set the connect timeou
	 * @param connectTimeout connect time out
	 */
	void setConnectTimeout(int connectTimeout);

	/**
	 * set read timeout
	 * @param readTimeout read timeout
	 */
	void setReadTimeout(int readTimeout);

	/**
	 * Create a new {@link HttpClientRequest} for the specified URI and HTTP method.
	 * <p>The returned request can be written to, and then executed by calling
	 * {@link HttpClientRequest#execute()}.
	 * @param uri the URI to create a request for
	 * @param httpMethod the HTTP method to execute
	 * @param httpHeader the HTTP header
	 * @return the created request
	 * @throws IOException in case of I/O errors
	 */
	HttpClientRequest createRequest(URI uri, HttpMethod httpMethod, HttpHeader httpHeader) throws IOException;
}
