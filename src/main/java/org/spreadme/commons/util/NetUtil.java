/*
 * Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.util;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * net util
 * @author shuwei.wang
 */
public abstract class NetUtil {

	private NetUtil() {

	}

	/**
	 * 获取URL中的域名
	 *
	 * @param link URL
	 * @return 域名
	 */
	public static String getHostByUrl(String link) {
		try {
			URL url = new URL(link);
			return url.getHost();
		}
		catch (MalformedURLException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * 根据域名获取IP地址
	 *
	 * @param domainName 域名
	 * @return IP地址
	 */
	public static String getIpByDomain(String domainName) {
		try {
			return InetAddress.getByName(domainName).getHostAddress();
		}
		catch (UnknownHostException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * isReachable
	 *
	 * @param host host
	 * @param timeout timeout
	 * @return isReachable
	 */
	@Deprecated
	public static boolean isReachable(String host, int timeout) {
		try {
			InetAddress inetAddress = InetAddress.getByName(host);
			return inetAddress.isReachable(timeout);
		}
		catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * can connect the url
	 *
	 * @param url url
	 * @param timeout timeout
	 * @return is connected
	 */
	public static boolean isConnected(String url, int timeout) {
		HttpURLConnection connection = null;
		try {
			URL u = new URL(url);
			connection = (HttpURLConnection) u.openConnection();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
		finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
