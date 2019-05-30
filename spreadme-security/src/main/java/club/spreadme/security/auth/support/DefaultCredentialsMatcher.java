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
import club.spreadme.security.auth.CredentialsMatcher;
import club.spreadme.security.auth.CredentialsToken;
import club.spreadme.security.auth.exception.AuthenticationException;

public class DefaultCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean match(CredentialsToken<?> credentials, AuthenticatedToken<?> token) throws AuthenticationException {
        return false;
    }

    @Override
    public boolean supports(Class<?> credentialsClass) {
        return false;
    }
}
