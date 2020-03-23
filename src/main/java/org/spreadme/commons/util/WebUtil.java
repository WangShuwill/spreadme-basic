/*
 * Copyright [3/23/20 6:01 PM] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.commons.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spreadme.commons.lang.Charsets;

public abstract class WebUtil {

	public static String getHost(HttpServletRequest request) {
		int endIndex = request.getRequestURL().length() - request.getRequestURI().length();
		return request.getRequestURL().substring(0, endIndex);
	}

	/**
	 * 返回客户端的IP地址
	 *
	 * @param request 请求
	 * @return client ip
	 */
	public static String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

	public static void flush(byte[] bytes, HttpServletResponse response) throws IOException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			 OutputStream output = response.getOutputStream()) {

			IOUtil.copy(bis, output);
			output.flush();
		}
	}

	public static String encodedUrl(final HttpServletRequest request) throws IOException {
		// The returned URL contains a protocol, server name, port number, and server path, but it does not include
		// query string parameters.
		StringBuffer url = request.getRequestURL();
		if (StringUtil.isNotBlank(request.getQueryString()) && !"null".equals(request.getQueryString())) {
			url.append("?").append(request.getQueryString());
		}
		return encodedUrl(url);
	}

	public static String encodedUrl(StringBuffer url) throws IOException {
		return URLEncoder.encode(url.toString(), Charsets.UTF_8.name());
	}
}
