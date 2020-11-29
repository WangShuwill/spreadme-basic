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
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.spreadme.commons.http.HttpHeader;
import org.spreadme.commons.http.HttpMethod;
import org.spreadme.commons.http.HttpParam;
import org.spreadme.commons.http.HttpUrl;
import org.spreadme.commons.lang.Charsets;
import org.spreadme.commons.lang.ContentType;
import org.spreadme.commons.lang.Protocol;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * Http Client
 * @author shuwei.wang
 */
public class HttpClient {

	private int connectTimeout;
	private int readTimeout;
	private boolean encodable;
	private Charset charset;
	private Proxy proxy;
	private HttpsVerifier verifier;

	private HttpClient(int connectTimeout, int readTimeout, boolean encodable, Charset charset, Proxy proxy, HttpsVerifier verifier) {
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.encodable = encodable;
		this.charset = charset;
		this.proxy = proxy;
		this.verifier = verifier;
	}

	public <T> T execute(final URL url, HttpMethod method, HttpHeader header,
			HttpMessageWriter messageWriter, HttpMessageReader<T> messageReader) throws IOException{
		
		if (!Protocol.isHttp(url.getProtocol())) {
			throw new MalformedURLException("Only http & https protocols supported");
		}
		HttpClientRequest request = null;
		HttpClientResponse response = null;
		try {
			HttpClientRequestFactory requestFactory = new DefaultHttpClientRequestFactory();
			requestFactory.setConnectTimeout(this.connectTimeout);
			requestFactory.setReadTimeout(this.readTimeout);
			requestFactory.setProxy(this.proxy);
			requestFactory.setVerifier(this.verifier);
			if(header == null) {
				header = HttpHeader.DEFAULT;
			}
			request = requestFactory.createRequest(url, method, header);
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
	
	public <T> T execute(final String url, HttpMethod method, HttpHeader header,
			HttpMessageWriter messageWriter, HttpMessageReader<T> messageReader) throws IOException{

		URL requestUrl = this.encodeToUrl(url);
		return this.execute(requestUrl, method, header, messageWriter, messageReader);
	}
	
	public <T> T get(String url, HttpParam param, HttpHeader header, HttpMessageReader<T> reader) throws IOException {
		URL requestUrl = this.encodeToUrl(url);
		HttpUrl httpUrl = HttpUrl.toHttpUrl(requestUrl);
		Map<String, String> queryParams = this.getQueryParams(requestUrl.getQuery());
		if (!queryParams.isEmpty()) {
			param = param == null ? new HttpParam(queryParams) : param.addAll(queryParams);
		}
		url = httpUrl.toString() + "?" + param.getQuery(encodable, this.charset);
		return execute(url, HttpMethod.GET, header, null, reader);
	}

	public <T> T post(String url, HttpParam param, HttpHeader header, HttpMessageReader<T> reader)  throws IOException{
		if (param.isMultiPart()) {
			final String boundary = StringUtil.randomString(36);
			header.setContentType(ContentType.multipart.getType() + ";charset=" + charset + ";boundary=" + boundary);
			return this.execute(url, HttpMethod.POST, header, new FormHttpMessageWriter(param, charset, boundary), reader);
		}
		header.setContentType(ContentType.formurlencoded.getType() + ";charset=" + charset);
		byte[] content = param.getParams().isEmpty() ? new byte[0] : param.getQuery(this.encodable, this.charset).getBytes(Charsets.UTF_8);
		return this.execute(url, HttpMethod.POST, header, out -> out.write(content), reader);
	}
	
	private URL encodeToUrl(String url) throws IOException {
		URL requestUrl = new URL(url);
		String externalForm = requestUrl.toExternalForm();
		externalForm = externalForm.replace(" ", "%20");
		try {
			URI uri = new URI(externalForm);
			return new URL(uri.toASCIIString());
		} catch (URISyntaxException e) {
			return requestUrl;
		}
	}
	
	private Map<String, String> getQueryParams(String query) throws IOException{
        Map<String, String> params = new HashMap<>();
        if(StringUtil.isNotBlank(query)) {
        	for(String param : query.split("&")) {
        		String[] pair = param.split("=");
                String key = URLDecoder.decode(pair[0], this.charset.name()); 
                String value = pair.length > 1 ? URLDecoder.decode(pair[1], this.charset.name()) : "";
                params.put(key, value); 
        	}
        }
        return params;
	}

	public static class HttpClientBuilder {
		
		private int _connectTimeout = -1;
		private int _readTimeout = -1;
		private boolean _encodable = false;
		private Charset _charset = Charsets.UTF_8;
		private Proxy _proxy;
		private HttpsVerifier _verifier = new DefaultHttpsVerifier();
		
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
		
		public HttpClientBuilder encodable(boolean encodable) {
			this._encodable = encodable;
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
		
		public HttpClientBuilder httpsInitializer(HttpsVerifier verifier) {
			this._verifier = verifier;
			return this;
		}
		
		public HttpClient build() {
			return new HttpClient(this._connectTimeout, this._readTimeout, this._encodable, this._charset, this._proxy, this._verifier);
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

	public HttpsVerifier getVerifier() {
		return verifier;
	}

}
