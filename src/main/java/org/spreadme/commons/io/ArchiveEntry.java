/*
 * Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.io;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Archive Entry
 * @author shuwei.wang
 */
public class ArchiveEntry implements Serializable {

	private static final long serialVersionUID = 2016951958576096734L;

	private String name;
	private InputStream in;

	public ArchiveEntry() {

	}

	public ArchiveEntry(String name, InputStream in) {
		this.name = name;
		this.in = in;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}
}
