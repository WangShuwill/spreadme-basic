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
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.UUID;

import org.junit.Test;
import org.spreadme.commons.http.client.HttpClient;
import org.spreadme.commons.http.client.HttpMessageReader;
import org.spreadme.commons.http.client.HttpMessageWriter;
import org.spreadme.commons.lang.LocalMimeResource;
import org.spreadme.commons.lang.MimeResource;
import org.spreadme.commons.lang.MimeType;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 */
public class HttpClientTest {

	@Test
	public void testClient() {
		String url = "http://app176.qiyuesuo.net/callback/category/draft";
		HttpMessageWriter writer = (out) -> {
			String paramaters = "t";
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out));
			bufferedWriter.write(paramaters);
			bufferedWriter.close();
		};
		HttpMessageReader<String> reader = response -> {
			System.out.println(response.getHeader());
			return StringUtil.fromInputStream(response.getBody());
		};
		HttpClient httpClient = new HttpClient();
		String result = httpClient.execute(url, HttpMethod.POST, HttpHeader.DEFAULT, writer, reader);
		System.out.println(result);
	}

	@Test
	public void testPostFile() {
		final String url = "http://10.147.18.20:8090/word/topdf";
		final String path = "/Users/wangshuwei/Downloads/documents/电子数据存证报告-完整版（需求完整版）190916 .docx";

		MimeResource resource = new LocalMimeResource(path, MimeType.DOCX);
		HttpParam param = new HttpParam().add("file", resource);
		HttpClient httpClient = new HttpClient();
		httpClient.post(url, param, HttpHeader.DEFAULT, r -> {
			IOUtil.toFile(r.getBody(), ClassUtil.getClassPath() + File.separator + UUID.randomUUID().toString() + ".pdf");
			return true;
		});
	}
}
