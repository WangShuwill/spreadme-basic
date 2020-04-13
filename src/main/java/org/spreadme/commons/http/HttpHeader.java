/*
 * Copyright [2/28/20 2:22 PM] [shuwei.wang (c) wswill@foxmail.com]
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.spreadme.commons.lang.ContentType;

/**
 * Http Header
 * @author shuwei.wang
 */
public class HttpHeader implements Iterable<HttpHeader.HttpHeaderProperty> {

	private List<HttpHeaderProperty> properties = new ArrayList<>(32);

	public static HttpHeader DEFAULT = new HttpHeader();

	static {
		DEFAULT.setHeader(HeaderType.ACCEPT_ENCODING, "gzip,deflate");
		DEFAULT.setHeader(HeaderType.ACCEPT, String.join(",",
				ContentType.txt.getType(),
				ContentType.html.getType(),
				ContentType.json.getType()));
	}

	public HttpHeader setHeader(String key, String value) {
		properties.add(new HttpHeaderProperty(key, value));
		return this;
	}

	public HttpHeader setContentType(String contentType) {
		properties.add(new HttpHeaderProperty(HeaderType.CONTENT_TYPE, contentType));
		return this;
	}

	public Optional<HttpHeaderProperty> getHeader(String key) {
		return this.properties.stream().filter(p -> p.getKey().equals(key)).findAny();
	}

	@Override
	public Iterator<HttpHeaderProperty> iterator() {
		return properties.iterator();
	}

	public static class HttpHeaderProperty {

		private String key;
		private String value;

		public HttpHeaderProperty(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return key + " = " + value;
		}
	}

	@Override
	public String toString() {
		return properties.toString();
	}
}
