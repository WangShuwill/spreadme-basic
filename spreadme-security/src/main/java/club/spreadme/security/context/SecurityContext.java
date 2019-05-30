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

import club.spreadme.security.auth.AuthenticatedToken;

import java.io.Serializable;

public class SecurityContext implements Serializable {

    private static final long serialVersionUID = 6813891629350241211L;

    private AuthenticatedToken<?> authenticationToken;

    public SecurityContext() {
    }

    public SecurityContext(AuthenticatedToken<?> authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public AuthenticatedToken<?> getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(AuthenticatedToken<?> authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authenticationToken == null) ? 0 : authenticationToken.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SecurityContext other = (SecurityContext) obj;
        if (authenticationToken == null) {
            return other.authenticationToken == null;
        }
        else {
            return authenticationToken.equals(other.authenticationToken);
        }
    }
}
