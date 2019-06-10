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


import club.spreadme.core.utils.Assert;

public class SecurityContextHolder {

    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> authUrlContext = new ThreadLocal<>();

    public static SecurityContext getContext() {
        SecurityContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
    }

    public static void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(context);
    }

    public static void clearContext() {
        contextHolder.remove();
    }

    public static SecurityContext createEmptyContext() {
        return new SecurityContext();
    }

    public static String getAuthUrlContext() {
        return authUrlContext.get();
    }

    public static void setAuthUrlContext(String url) {
        authUrlContext.set(url);
    }

    public static void clearAuthUrlContext() {
        authUrlContext.remove();
    }
}
