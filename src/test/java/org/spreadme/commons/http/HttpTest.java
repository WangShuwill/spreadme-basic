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
import java.io.IOException;
import java.util.UUID;

import org.junit.Test;
import org.spreadme.commons.http.client.HttpClient;
import org.spreadme.commons.http.client.HttpMessageReader;
import org.spreadme.commons.http.useragent.Browser;
import org.spreadme.commons.http.useragent.Platform;
import org.spreadme.commons.http.useragent.UserAgent;
import org.spreadme.commons.io.resources.LocalResource;
import org.spreadme.commons.io.resources.Resource;
import org.spreadme.commons.lang.ContentType;
import org.spreadme.commons.util.ClassUtil;
import org.spreadme.commons.util.Console;
import org.spreadme.commons.util.IOUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * @author shuwei.wang
 */
public class HttpTest {

	private static final String TEST_FILE_NAME_ONE = "CORE_TEST_FILE_ONE.txt";
	private static final String POST_FILE_PATH = ClassUtil.getClassPath() + File.separator + TEST_FILE_NAME_ONE;
	private static final String LINK = "http://app16.qiyuesuo.net/callback/category/draft";

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
	public void testHttpGet() throws Exception{
		HttpMessageReader<String> reader = response -> {
			Console.info(response.getHeader());
			return StringUtil.fromInputStream(response.getBody());
		};
		String url = "https://silkroad.csdn.net/api/v2/assemble/list/channel/search_hot_word?channel_name=pc_hot_word&size=10&platform=pc&imei=10_37287632040-1591001737819-905873&toolbarSearchExt={\\\"landingWord\\\":[],\\\"queryWord\\\":\\\"\\\",\\\"tag\\\":[\\\"X509\\\",\\\"国密证书\\\",\\\"java\\\",\\\"Android\\\"],\\\"title\\\":\\\"采用bouncycastle库解析国密证书\\\"}";
		HttpClient httpClient = HttpClient.builder().encodable(true).build();
		Console.info(httpClient.get(url, null, null, reader));
	}
	
	@Test
	public void testPostForm() throws IOException{
		HttpMessageReader<String> reader = response -> {
			Console.info(response.getHeader());
			return StringUtil.fromInputStream(response.getBody());
		};
		HttpClient httpClient = HttpClient.builder().build();
		String result = httpClient.post(LINK, new HttpParam().add("key", "t"), HttpHeader.DEFAULT, reader);
		Console.info("Http client request %s, and response %s", LINK, result);
	}

	@Test
	public void testPostFile() throws IOException{
		Resource resource = new LocalResource(POST_FILE_PATH, ContentType.docx);
		HttpParam param = new HttpParam().add("file", resource);
		HttpClient httpClient = HttpClient.builder().connectTimeout(10).build();
		httpClient.post(LINK, param, HttpHeader.DEFAULT, r -> {
			IOUtil.toFile(r.getBody(), ClassUtil.getClassPath() + File.separator + UUID.randomUUID().toString() + ".pdf");
			return true;
		});
	}
}
