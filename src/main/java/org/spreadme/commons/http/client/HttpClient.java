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
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.spreadme.commons.http.HeaderType;
import org.spreadme.commons.http.HttpHeader;
import org.spreadme.commons.http.HttpMethod;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * Http Client
 * @author shuwei.wang
 */
public class HttpClient {

	public static final String TWO_HYPHENS = "--";
	public static final String LINE_END = "\r\n";

	public static <T> CompletableFuture<T> execute(final String url, HttpMethod method, HttpHeader header,
			HttpMessageWriter messageWriter, HttpMessageReader<T> messageReader) {

		return CompletableFuture.supplyAsync(() -> {
			try {
				HttpClientRequestFactory requestFactory = new DefaultHttpClientRequestFactory();
				HttpClientRequest request = requestFactory.createRequest(new URI(url), method, header);
				if (messageWriter != null) {
					messageWriter.write(request.getBody());
				}
				HttpClientResponse response = request.execute();
				response.getHeader();
				T result = messageReader.reader(response);
				response.close();
				return result;
			}
			catch (IOException | URISyntaxException ex) {
				throw new IllegalStateException(ex);
			}

		});
	}

	public static <T> CompletableFuture<T> doGet(String url, Map<String, Object> params, HttpHeader header, HttpMessageReader<T> reader) {
		String param = params.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
		return execute(url + "?" + param, HttpMethod.GET, header, null, reader);
	}

	public static String doGet(String url, Map<String, Object> params) {
		CompletableFuture<String> future = doGet(url, params, HttpHeader.DEFAULT, r -> {
			try (InputStream in = r.getBody()) {
				return StringUtil.fromInputStream(in);
			}
		});
		try {
			return future.get();
		}
		catch (InterruptedException | ExecutionException ex) {
			throw new IllegalStateException(ex);
		}
	}

	public String buildFormData(final String boundary, final String key, final String value) {
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

	public void buildMultipart(final String boundary, final String key, final String filename,
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
	public void buildEOF(final String boundary, final OutputStream output) throws IOException {
		byte[] endData = (LINE_END + TWO_HYPHENS + boundary + TWO_HYPHENS + LINE_END).getBytes();
		output.write(endData);
		output.flush();
	}

	public static boolean isGzip(HttpHeader httpHeader) {
		Optional<HttpHeader.HttpHeaderProperty> property = httpHeader.getHeader(HeaderType.CONTENT_ENCODING);
		return property.isPresent() && StringUtil.isNotBlank(property.get().getValue())
				&& property.get().getValue().contains("gzip");
	}
}
