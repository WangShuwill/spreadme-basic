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

package club.spreadme.security.context;

import java.security.Principal;

import club.spreadme.security.auth.AuthenticatedToken;
import club.spreadme.security.auth.AuthenticationToken;

public class SecurityUtils {

	public static <U extends Principal> U getPrincipal() {
		AuthenticationToken<?> token = SecurityContextHolder.getContext().getAuthenticationToken();

		if ((token != null)) {
			@SuppressWarnings("unchecked")
			AuthenticatedToken<U> info = (AuthenticatedToken<U>) token;
			return info.getPrincipal();
		}

		return null;
	}

	/**
	 * 获取当前用户Cache中对应的TokenId
	 */
	public static String getToken() {
		AuthenticationToken<?> authentication = SecurityContextHolder.getContext().getAuthenticationToken();

		if ((authentication != null)) {
			AuthenticatedToken<?> token = (AuthenticatedToken<?>) authentication;
			return token.getToken();
		}

		return null;
	}
}
