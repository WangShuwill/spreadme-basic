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

package club.spreadme.security.session;

import club.spreadme.security.auth.AuthenticatedToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SessionManager {

	int DEFAULT_SESSION_TIMEOUT = 60 * 60;

	void add(HttpServletRequest request, HttpServletResponse response, AuthenticatedToken<?> token);

	AuthenticatedToken<?> load(HttpServletRequest request);

	void remove(HttpServletRequest request, HttpServletResponse response, AuthenticatedToken<?> token);

}
