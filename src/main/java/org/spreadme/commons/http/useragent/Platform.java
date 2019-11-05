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

/**
 * Platform
 * @author shuwei.wang
 */
public class Platform extends UserAgentInfo {

	public static final Platform UNKOWN = new Platform(NAME_UNKOWN, null);

	// 支持的移动平台类型
	private static final List<Platform> mobilePlatforms = new ArrayList<>(Arrays.asList(
			new Platform("Windows Phone", "windows (ce|phone|mobile)( os)?"),
			new Platform("iPad", "ipad"),
			new Platform("iPod", "ipod"),
			new Platform("iPhone", "iphone"),
			new Platform("Android", "android"),
			new Platform("Symbian", "symbian(os)?"),
			new Platform("Blackberry", "blackberry"))
	);

	// 支持的桌面平台类型
	private static final List<Platform> desktopPlatforms = new ArrayList<>(Arrays.asList(
			new Platform("Windows", "windows"),
			new Platform("Mac", "(macintosh|darwin)"),
			new Platform("Linux", "linux"),
			new Platform("Wii", "wii"),
			new Platform("Playstation", "playstation"),
			new Platform("Java", "java"))
	);

	public static final List<Platform> platforms;

	static {
		platforms = new ArrayList<>(13);
		platforms.addAll(mobilePlatforms);
		platforms.addAll(desktopPlatforms);
	}

	public Platform(String info, String regex) {
		super(info, regex);
	}

	/**
	 * 是否为移动平台
	 * @return 是否为移动平台
	 */
	public boolean isMobile() {
		return mobilePlatforms.contains(this);
	}

}
