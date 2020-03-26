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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.spreadme.commons.http.client.HttpClient;
import org.spreadme.commons.http.client.HttpMessageReader;
import org.spreadme.commons.http.client.HttpMessageWriter;
import org.spreadme.commons.lang.MimeResource;
import org.spreadme.commons.lang.MimeType;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.FileUtil;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 */
public class HttpClientTest {

	@Test
	public void testClient() throws Exception {
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
	public void testPostFile() throws Exception {
		final String url = "http://192.168.50.103:8090/word/topdf";
		final String path = "/Users/wangshuwei/Downloads/documents/";

		final List<File> files = FileUtil.getFiles(path, file -> {
			String ext = FileUtil.getExtension(file.getName());
			return "doc".equals(ext) || "docx".equals(ext);
		});

		final int count = files.size();
		final AtomicInteger atomic = new AtomicInteger(count);
		final CountDownLatch downLatch = new CountDownLatch(count);

		ExecutorService executorService = Executors.newFixedThreadPool(count);
		for (int i = 0; i < count; i++) {
			executorService.submit(() -> {
				int index = atomic.decrementAndGet();
				System.out.println(index);
				MimeResource resource = new LocalMimeResource(files.get(index).getPath(), MimeType.DOCX);
				HttpParam param = new HttpParam().add("file", resource);
				HttpClient httpClient = new HttpClient();
				httpClient.post(url, param, HttpHeader.DEFAULT, r -> {
					IOUtil.toFile(r.getBody(), ClassUtil.getClassPath() + File.separator + UUID.randomUUID().toString() + ".pdf");
					return true;
				});
				downLatch.countDown();
			});
		}
		downLatch.await();
		executorService.shutdown();
	}
}
