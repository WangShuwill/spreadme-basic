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

import org.junit.Test;
import org.spreadme.commons.http.useragent.Browser;
import org.spreadme.commons.http.useragent.Platform;
import org.spreadme.commons.http.useragent.UserAgent;

/**
 * @author shuwei.wang
 */
public class HttpTest {

	@Test
	public void testUserAgent() {
		final String useragent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:70.0) Gecko/20100101 Firefox/70.0";
		Platform platform = UserAgent.getPlatform(useragent);
		System.out.println("IsMobile: " + platform.isMobile());
		System.out.println("Platfrom: " + platform.getName());

		Browser browser = UserAgent.getBrowser(useragent);
		System.out.println("Browswe Name: " + browser.getName());
		System.out.println("Browser Version: " + browser.getVersion());
	}
}
