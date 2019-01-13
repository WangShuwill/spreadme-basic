/*
 *  Copyright (c) 2019 Wangshuwei
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package club.spreadme.util.web;

import club.spreadme.lang.StringUtil;

import javax.servlet.http.HttpServletRequest;

public abstract class WebUtil {

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtil.isNotBlank(ip) && (!"unKnown".equalsIgnoreCase(ip))) {
            int index = ip.indexOf(",");
            if (index != -1) {
                ip = ip.substring(0, index);
            }
        } else {
            ip = request.getHeader("X-Real-IP");
            if ((StringUtil.isBlank(ip)) || ("unKnown".equalsIgnoreCase(ip))) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if ((StringUtil.isBlank(ip)) || ("unKnown".equalsIgnoreCase(ip))) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if ((StringUtil.isBlank(ip)) || ("unknown".equalsIgnoreCase(ip))) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }
}
