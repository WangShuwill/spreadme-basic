/*
 * Copyright [2/28/20 4:05 PM] [shuwei.wang (c) wswill@foxmail.com]
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

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URI;

import org.junit.Test;
import org.spreadme.commons.http.client.DefaultHttpClientRequestFactory;
import org.spreadme.commons.http.client.HttpClientRequest;
import org.spreadme.commons.http.client.HttpClientRequestFactory;
import org.spreadme.commons.http.client.HttpClientResponse;
import org.spreadme.commons.lang.Charsets;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 */
public class HttpClientTest {

	@Test
	public void testClient() throws Exception {
		HttpClientRequestFactory requestFactory = new DefaultHttpClientRequestFactory();
		URI url = new URI("http://app176.qiyuesuo.net/callback/category/draft");
		HttpClientRequest request = requestFactory.createRequest(url, HttpMethod.POST, HttpHeader.DEFAULT.setHeader(HeaderType.ACCEPT_ENCODING, Charsets.UTF_8.name()));
		String paramaters = "t";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(request.getBody()));
		writer.write(paramaters);
		writer.close();
		HttpClientResponse response = request.execute();
		System.out.println(response.getHeader());
		System.out.println(response.getStatusCode());
		System.out.println(response.getResponseMessage());
		String result = StringUtil.fromInputStream(response.getBody());
		System.out.println(result);
		response.close();
	}
}
