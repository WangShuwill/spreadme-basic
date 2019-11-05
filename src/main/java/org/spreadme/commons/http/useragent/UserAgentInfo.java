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

package org.spreadme.commons.http.useragent;

import java.util.regex.Pattern;

/**
 * useragent info
 * @author shuwei.wang
 */
public class UserAgentInfo {

	protected static final String NAME_UNKOWN = "UNKOWN";

	private final String name;

	private final Pattern pattern;

	public UserAgentInfo(String name, Pattern pattern) {
		this.name = name;
		this.pattern = pattern;
	}

	public UserAgentInfo(String name, String regex) {
		this(name, regex == null ? null : Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
	}

	public boolean isMatch(String content) {
		return pattern.matcher(content).find();
	}

	public String getName() {
		return name;
	}

	public Pattern getPattern() {
		return pattern;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UserAgentInfo other = (UserAgentInfo) obj;
		if (name == null) {
			return other.name == null;
		}
		else {
			return name.equals(other.name);
		}
	}

	@Override
	public String toString() {
		return "UserAgentInfo{" +
				"name='" + name + '\'' +
				'}';
	}
}
