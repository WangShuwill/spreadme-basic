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

package club.spreadme.security.session.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import club.spreadme.core.codec.Id;
import club.spreadme.core.utils.StringUtil;
import club.spreadme.security.auth.AuthenticatedToken;
import club.spreadme.security.cookie.CookieUtil;
import club.spreadme.security.session.SessionManager;

public class CookieSessionManager implements SessionManager {

	private static final Map<String, AuthenticatedToken<?>> TOKEN_CACHE = new ConcurrentHashMap<>(256);

	private static final String SESSION_ID_NAME = "SPREADME-ID";

	@Override
	public void add(HttpServletRequest request, HttpServletResponse response, AuthenticatedToken<?> token) {
		String sessionId = Id.UUID.generate();
		token.setToken(sessionId);
		if (request.isSecure()) {
			CookieUtil.addCookie(response, null, SESSION_ID_NAME, sessionId, true);
		}
		else {
			CookieUtil.addCookie(response, null, SESSION_ID_NAME, sessionId, false);
		}
		TOKEN_CACHE.put(sessionId, token);
	}

	@Override
	public AuthenticatedToken<?> load(HttpServletRequest request) {
		Cookie cookie = CookieUtil.getCookie(request, SESSION_ID_NAME);
		if (cookie == null || StringUtil.isBlank(cookie.getValue())) {
			return null;
		}
		return TOKEN_CACHE.get(cookie.getValue());
	}

	@Override
	public void remove(HttpServletRequest request, HttpServletResponse response, AuthenticatedToken<?> token) {
		CookieUtil.deleteCookie(request, response, SESSION_ID_NAME);
	}

}
