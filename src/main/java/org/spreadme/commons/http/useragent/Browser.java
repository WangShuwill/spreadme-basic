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

package org.spreadme.commons.http.useragent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spreadme.commons.util.StringUtil;

/**
 * Browser
 * @author shuwei.wang
 */
public class Browser extends UserAgentInfo {

	public final static Browser UNKOWN = new Browser(NAME_UNKOWN, null, null);

	private final static String OTHER_VERSION_REGEX = "[\\/ ]([\\d\\w\\.\\-]+)";

	private Pattern versionPattern;

	private String version;

	public Browser(String name, String regex, String versionRegex) {
		super(name, regex);
		if (OTHER_VERSION_REGEX.equals(versionRegex)) {
			versionRegex = name + versionRegex;
		}
		if (StringUtil.isNotBlank(versionRegex)) {
			this.versionPattern = Pattern.compile(versionRegex, Pattern.CASE_INSENSITIVE);
		}
	}

	public static final List<Browser> browers = new ArrayList<>(Arrays.asList(//
			new Browser("MSEdge", "Edge", "edge\\/([\\d\\w\\.\\-]+)"),
			new Browser("Chrome", "chrome", "chrome\\/([\\d\\w\\.\\-]+)"),
			new Browser("Firefox", "firefox", OTHER_VERSION_REGEX),
			new Browser("IEMobile", "iemobile", OTHER_VERSION_REGEX),
			new Browser("Safari", "safari", "version\\/([\\d\\w\\.\\-]+)"),
			new Browser("Opera", "opera", OTHER_VERSION_REGEX),
			new Browser("Konqueror", "konqueror", OTHER_VERSION_REGEX),
			new Browser("PS3", "playstation 3", "([\\d\\w\\.\\-]+)\\)\\s*$"),
			new Browser("PSP", "playstation portable", "([\\d\\w\\.\\-]+)\\)?\\s*$"),
			new Browser("Lotus", "lotus.notes", "Lotus-Notes\\/([\\w.]+)"),
			new Browser("Thunderbird", "thunderbird", OTHER_VERSION_REGEX),
			new Browser("Netscape", "netscape", OTHER_VERSION_REGEX),
			new Browser("Seamonkey", "seamonkey", OTHER_VERSION_REGEX),
			new Browser("Outlook", "microsoft.outlook", OTHER_VERSION_REGEX),
			new Browser("Evolution", "evolution", OTHER_VERSION_REGEX),
			new Browser("MSIE", "msie", "msie ([\\d\\w\\.\\-]+)"),
			new Browser("MSIE11", "rv:11", "rv:([\\d\\w\\.\\-]+)"),
			new Browser("Gabble", "Gabble", "Gabble\\/([\\d\\w\\.\\-]+)"),
			new Browser("Yammer Desktop", "AdobeAir", "([\\d\\w\\.\\-]+)\\/Yammer"),
			new Browser("Yammer Mobile", "Yammer[\\s]+([\\d\\w\\.\\-]+)", "Yammer[\\s]+([\\d\\w\\.\\-]+)"),
			new Browser("Apache HTTP Client", "Apache\\\\-HttpClient", "Apache\\-HttpClient\\/([\\d\\w\\.\\-]+)"),
			new Browser("BlackBerry", "BlackBerry", "BlackBerry[\\d]+\\/([\\d\\w\\.\\-]+)"))
	);

	public void setVersion(String userAgent) {
		final Matcher matcher = versionPattern.matcher(userAgent);
		if (matcher.find()) {
			this.version = matcher.group(1);
		}
		else {
			this.version = NAME_UNKOWN;
		}
	}

	public String getVersion() {
		return version;
	}

	public boolean isMobile() {
		return "PSP".equals(this.getName());
	}
}
