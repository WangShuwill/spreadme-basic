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

import java.io.InputStream;
import java.net.Proxy;
import java.net.URI;
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

	private int connectTimeout = -1;
	private int readTimeout = -1;
	private Proxy proxy;

	public <T> T execute(final String url, HttpMethod method, HttpHeader header,
			HttpMessageWriter messageWriter, HttpMessageReader<T> messageReader) {

		HttpClientResponse response = null;
		try {
			HttpClientRequestFactory requestFactory = new DefaultHttpClientRequestFactory();
			requestFactory.setConnectTimeout(this.connectTimeout);
			requestFactory.setReadTimeout(this.readTimeout);
			requestFactory.setProxy(this.proxy);
			HttpClientRequest request = requestFactory.createRequest(new URI(url), method, header);
			if (messageWriter != null) {
				messageWriter.write(request.getBody());
			}
			response = request.execute();
			return messageReader.reader(response);
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		finally {
			IOUtil.close(response);
		}
	}

	public <T> T get(String url, Map<String, String> params, HttpHeader header, HttpMessageReader<T> reader) {
		HttpParam param = new HttpParam(params);
		//TODO URLEcode
		return execute(url + "?" + param.getQueryString(), HttpMethod.GET, header, null, reader);
	}

	public String get(String url, Map<String, String> params) {
		return get(url, params, HttpHeader.DEFAULT, r -> {
			try (InputStream in = r.getBody()) {
				return StringUtil.fromInputStream(in);
			}
		});
	}

	public <T> T post(String url, HttpParam param, HttpHeader header, HttpMessageReader<T> reader) {
		final Charset charset = Charsets.UTF_8;
		if (param.isMultiPart()) {
			final String boundary = StringUtil.randomString(36);
			header.setContentType(ContentType.multipart.getType() + ";charset=" + charset + ";boundary=" + boundary);
			return execute(url, HttpMethod.POST, header, new FormHttpMessageWriter(param, charset, boundary), reader);
		}
		else {
			header.setContentType(ContentType.formurlencoded.getType() + ";charset=" + charset);
			byte[] content = param.getParams().isEmpty() ? new byte[0] : param.getQueryString().getBytes(Charsets.UTF_8);
			return execute(url, HttpMethod.POST, header, out -> out.write(content), reader);
		}
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}
}
