/*
 * Copyright [3/26/20 2:51 PM] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.http;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.spreadme.commons.lang.AbstractResource;
import org.spreadme.commons.util.StringUtil;

/**
 * Http Parameter
 * @author shuwei.wang
 */
public class HttpParam {

	private boolean isMultiPart = false;
	private Map<String, String> params = new LinkedHashMap<>();
	private Map<String, AbstractResource> multipart = new LinkedHashMap<>();

	public HttpParam() {
	}

	public HttpParam(Map<String, String> params) {
		this.params.putAll(params);
	}

	public HttpParam add(String key, Object value) {
		if (value instanceof AbstractResource) {
			this.isMultiPart = true;
			this.multipart.put(key, (AbstractResource) value);
		}
		else {
			this.params.put(key, String.valueOf(value));
		}
		return this;
	}

	public boolean isMultiPart() {
		return this.isMultiPart;
	}

	public String getQueryString() {
		return params.entrySet().stream()
				.filter(e -> StringUtil.isNotBlank(e.getKey()))
				.map(e -> e.getKey() + "=" + StringUtil.noneNullString(e.getValue()))
				.collect(Collectors.joining("&"));
	}

	public Map<String, String> getParams() {
		return params;
	}

	public Map<String, AbstractResource> getMultipart() {
		return multipart;
	}

	@Override
	public String toString() {
		return "HttpParam{" +
				"isMultiPart=" + isMultiPart +
				", params=" + params +
				", multipart=" + multipart +
				'}';
	}
}
