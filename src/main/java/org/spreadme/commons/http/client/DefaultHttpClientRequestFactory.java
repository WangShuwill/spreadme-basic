/*
 * Copyright [2/28/20 3:14 PM] [shuwei.wang (c) wswill@foxmail.com]
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
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import org.spreadme.commons.http.HttpHeader;
import org.spreadme.commons.http.HttpMethod;
import org.spreadme.commons.http.SSLInitializer;

/**
 * Simple HttpClient Request Factory
 * @author shuwei.wang
 */
public class DefaultHttpClientRequestFactory implements HttpClientRequestFactory {

	private static final int DEFAULT_CHUNK_SIZE = 4096;

	private Proxy proxy;
	private int connectTimeout = -1;
	private int readTimeout = -1;

	@Override
	public HttpClientRequest createRequest(URI uri, HttpMethod httpMethod, HttpHeader httpHeader) throws IOException {
		HttpURLConnection connection = openConnection(uri.toURL(), this.proxy);
		prepareConnection(connection, httpMethod, httpHeader);
		return new DefaultHttpClientRequest(httpHeader, connection);
	}

	protected HttpURLConnection openConnection(URL url, Proxy proxy) throws IOException {
		URLConnection connection = proxy != null ? url.openConnection(proxy) : url.openConnection();
		if (!(connection instanceof HttpURLConnection)) {
			throw new IllegalStateException("HttpURLConnection required for [" + url + "] but got: " + connection);
		}
		return (HttpURLConnection) connection;
	}

	protected void prepareConnection(HttpURLConnection connection, HttpMethod httpMethod, HttpHeader httpHeader) throws IOException {
		if (connection instanceof HttpsURLConnection) {
			connection = new SSLInitializer().init((HttpsURLConnection) connection);
		}
		if (this.connectTimeout >= 0) {
			connection.setConnectTimeout(this.connectTimeout);
		}
		if (this.readTimeout >= 0) {
			connection.setReadTimeout(this.readTimeout);
		}
		connection.setDoInput(true);
		if (HttpMethod.GET.equals(httpMethod)) {
			connection.setInstanceFollowRedirects(true);
		}
		else {
			connection.setInstanceFollowRedirects(false);
		}

		if (HttpMethod.POST.equals(httpMethod) || HttpMethod.PUT.equals(httpMethod) ||
				HttpMethod.PATCH.equals(httpMethod) || HttpMethod.DELETE.equals(httpMethod)) {
			connection.setDoOutput(true);
		}
		else {
			connection.setDoOutput(false);
		}
		for (HttpHeader.HttpHeaderProperty property : httpHeader) {
			connection.setRequestProperty(property.getKey(), property.getValue());
		}
		connection.setRequestMethod(httpMethod.name());
	}

	@Override
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	@Override
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
}
