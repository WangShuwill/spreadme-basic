/*
 *    Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.spreadme.commons.http;

import java.io.File;
import java.util.UUID;

import org.junit.Test;
import org.spreadme.commons.http.client.HttpClient;
import org.spreadme.commons.http.client.HttpMessageReader;
import org.spreadme.commons.http.useragent.Browser;
import org.spreadme.commons.http.useragent.Platform;
import org.spreadme.commons.http.useragent.UserAgent;
import org.spreadme.commons.lang.LocalResource;
import org.spreadme.commons.util.Console;
import org.spreadme.commons.lang.AbstractResource;
import org.spreadme.commons.lang.ContentType;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 */
public class HttpTest {

	private static final String TEST_FILE_NAME_ONE = "CORE_TEST_FILE_ONE.txt";
	private static final String POST_FILE_PATH = ClassUtil.getClassPath() + File.separator + TEST_FILE_NAME_ONE;
	private static final String URL = "http://app176.qiyuesuo.net/callback/category/draft";

	@Test
	public void testUserAgent() {
		final String useragent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:70.0) Gecko/20100101 Firefox/70.0";
		Platform platform = UserAgent.getPlatform(useragent);
		Console.info("IsMobile: " + platform.isMobile());
		Console.info("Platfrom: " + platform.getName());

		Browser browser = UserAgent.getBrowser(useragent);
		Console.info("Browswe Name: " + browser.getName());
		Console.info("Browser Version: " + browser.getVersion());
	}

	@Test
	public void testPostForm() {
		HttpMessageReader<String> reader = response -> {
			Console.info(response.getHeader());
			return StringUtil.fromInputStream(response.getBody());
		};
		HttpClient httpClient = new HttpClient();
		String result = httpClient.post(URL, new HttpParam().add("key", "t"), HttpHeader.DEFAULT, reader);
		Console.info("Http client request %s, and response %s", URL, result);
	}

	public void testPostFile() {
		AbstractResource resource = new LocalResource(POST_FILE_PATH, ContentType.docx);
		HttpParam param = new HttpParam().add("file", resource);
		HttpClient httpClient = new HttpClient();
		httpClient.setConnectTimeout(10);
		httpClient.post(URL, param, HttpHeader.DEFAULT, r -> {
			IOUtil.toFile(r.getBody(), ClassUtil.getClassPath() + File.separator + UUID.randomUUID().toString() + ".pdf");
			return true;
		});
	}
}
