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


import club.spreadme.security.auth.CredentialsToken;

public class UsernamePasswordToken extends CredentialsToken<String> {

    private static final long serialVersionUID = -5256945614960074983L;

    private String username;

    public UsernamePasswordToken(String username, String password) {
        super(password);
        this.username = username;
    }

    @Override
    public String getPrincipal() {
        return this.username;
    }
}
