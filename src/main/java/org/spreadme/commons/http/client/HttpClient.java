/*
 * Copyright [3/23/20 10:05 PM] [shuwei.wang (c) wswill@foxmail.com]
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
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.spreadme.commons.http.HttpHeader;
import org.spreadme.commons.http.HttpMethod;
import org.spreadme.commons.http.HttpParam;
import org.spreadme.commons.lang.Charsets;
import org.spreadme.commons.lang.ContentType;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * Http Client
 * @author shuwei.wang
 */
public class HttpClient {

	private int connectTimeout;
	private int readTimeout;
	private Charset charset;
	private Proxy proxy;
	private HttpsInitializer httpsInitializer;

	private HttpClient(int connectTimeout, int readTimeout, Charset charset, Proxy proxy, HttpsInitializer httpsInitializer) {
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.charset = charset;
		this.proxy = proxy;
		this.httpsInitializer = httpsInitializer;
	}

	public <T> T execute(final String url, HttpMethod method, HttpHeader header,
			HttpMessageWriter messageWriter, HttpMessageReader<T> messageReader) throws IOException{

		HttpClientRequest request = null;
		HttpClientResponse response = null;
		try {
			HttpClientRequestFactory requestFactory = new DefaultHttpClientRequestFactory();
			requestFactory.setConnectTimeout(this.connectTimeout);
			requestFactory.setReadTimeout(this.readTimeout);
			requestFactory.setProxy(this.proxy);
			requestFactory.setHttpsInitializer(httpsInitializer);
			if(header == null) {
				header = HttpHeader.DEFAULT;
			}
			request = requestFactory.createRequest(new URL(url), method, header);
			if (messageWriter != null) {
				messageWriter.write(request.getBody());
			}
			response = request.execute();
			return messageReader.reader(response);
		}
		finally {
			IOUtil.close(request, response);
		}
	}

	public <T> T get(String url, Map<String, String> params, HttpHeader header, HttpMessageReader<T> reader) throws IOException{
		HttpParam param = new HttpParam(params);
		//TODO URLEcode
		return execute(url + "?" + param.getQueryString(), HttpMethod.GET, header, null, reader);
	}

	public String get(String url, Map<String, String> params)  throws IOException{
		return get(url, params, HttpHeader.DEFAULT, r -> {
			try (InputStream in = r.getBody()) {
				return StringUtil.fromInputStream(in);
			}
		});
	}

	public <T> T post(String url, HttpParam param, HttpHeader header, HttpMessageReader<T> reader)  throws IOException{
		if (param.isMultiPart()) {
			final String boundary = StringUtil.randomString(36);
			header.setContentType(ContentType.multipart.getType() + ";charset=" + charset + ";boundary=" + boundary);
			return execute(url, HttpMethod.POST, header, new FormHttpMessageWriter(param, charset, boundary), reader);
		}
		header.setContentType(ContentType.formurlencoded.getType() + ";charset=" + charset);
		byte[] content = param.getParams().isEmpty() ? new byte[0] : param.getQueryString().getBytes(Charsets.UTF_8);
		return execute(url, HttpMethod.POST, header, out -> out.write(content), reader);
	}

	public static class HttpClientBuilder {
		
		private int _connectTimeout = -1;
		private int _readTimeout = -1;
		private Charset _charset = Charsets.UTF_8;
		private Proxy _proxy;
		private HttpsInitializer _httpsInitializer;
		
		private HttpClientBuilder() {
		}
		
		public HttpClientBuilder connectTimeout(int connectTimeout) {
			this._connectTimeout = connectTimeout;
			return this;
		}
		
		public HttpClientBuilder readTimeout(int readTimeout) {
			this._readTimeout = readTimeout;
			return this;
		}
		
		public HttpClientBuilder charset(Charset charset) {
			this._charset = charset;
			return this;
		}
		
		public HttpClientBuilder proxy(Proxy proxy) {
			this._proxy = proxy;
			return this;
		}
		
		public HttpClientBuilder httpsInitializer(HttpsInitializer initializer) {
			this._httpsInitializer = initializer;
			return this;
		}
		
		public HttpClient build() {
			return new HttpClient(this._connectTimeout, this._readTimeout, this._charset, this._proxy, this._httpsInitializer);
		}
	}
	
	public static HttpClientBuilder builder() {
		return new HttpClientBuilder();
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public Charset getCharset() {
		return charset;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public HttpsInitializer getHttpsInitializer() {
		return httpsInitializer;
	}
	
}
