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

package club.spreadme.security.auth.support;


import club.spreadme.security.auth.AuthenticatedToken;
import club.spreadme.security.auth.AuthorizingRealm;
import club.spreadme.security.auth.CredentialsToken;
import club.spreadme.security.auth.exception.AuthenticationException;

public abstract class UserAuthRealm implements AuthorizingRealm<UserPrincipal> {

	@Override
	public AuthenticatedToken<UserPrincipal> getAuthenticatedToken(CredentialsToken<?> credentialsToken) throws AuthenticationException {
		UsernamePasswordToken userToken = (UsernamePasswordToken) credentialsToken;
		return createAuthenticatedToken(userToken);
	}

	private UserPrincipal getUserPrincipal(String username) {
		return (UserPrincipal) () -> "username";
	}

	protected abstract AuthenticatedToken<UserPrincipal> createAuthenticatedToken(UsernamePasswordToken token);

}
