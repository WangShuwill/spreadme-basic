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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.spreadme.commons.http.HttpHeader;
import org.spreadme.commons.http.HttpMethod;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * Http Client
 * @author shuwei.wang
 */
public class HttpClient {

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
				T result = messageReader.reader(response.getBody());
				response.close();
				return result;
			}
			catch (IOException | URISyntaxException ex) {
				throw new IllegalStateException(ex);
			}

		});
	}

	public static <T> CompletableFuture<T> doGet(String url, Map<String, Object> params, HttpHeader header, HttpMessageReader<T> reader) {
		String param = params.entrySet().stream().map(e -> e.getValue() + "=" + e.getValue()).collect(Collectors.joining("&"));
		return execute(url + "?" + param, HttpMethod.GET, header, null, reader);
	}

	public static String doGet(String url, Map<String, Object> params) {
		CompletableFuture<String> future = doGet(url, params, null, in -> {
			try {
				return StringUtil.fromInputStream(in);
			}
			finally {
				IOUtil.close(in);
			}
		});
		try {
			return future.get();
		}
		catch (InterruptedException | ExecutionException ex) {
			throw new IllegalStateException(ex);
		}
	}
}
