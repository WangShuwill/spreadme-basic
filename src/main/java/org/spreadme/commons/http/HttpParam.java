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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.spreadme.commons.io.resources.Resource;

/**
 * Http Parameter
 * @author shuwei.wang
 */
public class HttpParam {

	private boolean isMultiPart = false;
	private Map<String, String> commonParams = new LinkedHashMap<>();
	private Map<String, Resource> multipart = new LinkedHashMap<>();

	public HttpParam() {
	}

	public HttpParam(Map<String, String> params) {
		if(params != null) {
			this.commonParams.putAll(params);
		}
	}

	public HttpParam add(String key, Object value) {
		if (value instanceof Resource) {
			this.isMultiPart = true;
			this.multipart.put(key, (Resource) value);
		}
		else {
			this.commonParams.put(key, String.valueOf(value));
		}
		return this;
	}
	
	public HttpParam addAll(Map<String, String> params) {
		this.commonParams.putAll(params);
		return this;
	}
	
	public String getQuery(boolean encode, Charset charset) {
		return this.commonParams.entrySet().stream().map(e -> {
			try {
				String key = encode ? URLEncoder.encode(e.getKey(), charset.name()) : e.getKey();
				String value = encode ? URLEncoder.encode(e.getValue(), charset.name()) : e.getValue(); 
				return key + "=" + value;
			} catch (UnsupportedEncodingException ex) {
				throw new IllegalArgumentException(ex);
			}
			
		}).collect(Collectors.joining("&"));
	}
	
	public boolean isMultiPart() {
		return this.isMultiPart;
	}

	public Map<String, String> getParams() {
		return commonParams;
	}

	public Map<String, Resource> getMultipart() {
		return multipart;
	}

	@Override
	public String toString() {
		return "HttpParam{" +
				"isMultiPart=" + isMultiPart +
				", params=" + commonParams +
				", multipart=" + multipart +
				'}';
	}
}
