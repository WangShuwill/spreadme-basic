/*
 * Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package club.spreadme.security.cookie;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import club.spreadme.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CookieUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CookieUtil.class);

	public static void addCookie(HttpServletResponse response, String domain, String name, String value, boolean secure) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		if (StringUtil.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		cookie.setHttpOnly(true);
		if (secure) {
			cookie.setSecure(true);
		}
		LOGGER.info(cookie.toString());
		response.addCookie(cookie);
	}

	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (Objects.isNull(cookies)) {
			return null;
		}
		List<Cookie> needCookies = Arrays.stream(cookies)
				.filter(cookie -> Objects.equals(cookie.getName(), name))
				.collect(Collectors.toList());

		if (!needCookies.isEmpty() && needCookies.get(0) != null) {
			return needCookies.get(0);
		}

		return null;
	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie cookie = getCookie(request, name);
		if (cookie != null) {
			cookie.setMaxAge(0);
			cookie.setValue(null);
			response.addCookie(cookie);
		}
	}

}
