/*
 *    Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.spreadme.commons.system;

import java.util.HashMap;
import java.util.Map;

import org.spreadme.commons.util.StringUtil;


/**
 * arch type
 * @author shuwei.wang
 * @since 1.0.0
 */
public enum OSType {

	WINDOWS("windows"),
	LINUX("linux"),
	MAXOS("macos"),
	AIX("AIX"),
	HPUX("HP-UX"),
	OS400("OS/400"),
	IRIX("Irix"),
	FreeBSD("FreeBSD"),
	OpenBSD("OpenBSD"),
	NetBSD("NetBSD"),
	OS2("OS/2"),
	SOLARIS("Solaris"),
	SUNOS("SunOS"),
	UNKNOWN("UNKNOW");

	private static Map<String, OSType> types = new HashMap<>(4);

	static {
		for (OSType type : OSType.values()) {
			types.put(type.name, type);
		}
	}

	private String name;

	OSType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static OSType resolve(String arch) {
		for (Map.Entry<String, OSType> entry : types.entrySet()) {
			if (StringUtil.trimAll(arch).toUpperCase().contains(entry.getKey().toUpperCase())) {
				return entry.getValue();
			}
		}
		return UNKNOWN;
	}
}
