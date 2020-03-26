/*
 * Copyright [3/26/20 5:28 PM] [shuwei.wang (c) wswill@foxmail.com]
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.spreadme.commons.lang.MimeResource;
import org.spreadme.commons.lang.MimeType;

/**
 * LocalMimeResource
 * @author shuwei.wang
 */
public class LocalMimeResource implements MimeResource {

	private String path;
	private MimeType mimeType;
	private File file;

	public LocalMimeResource(String path, MimeType mimeType) {
		this.path = path;
		this.file = new File(path);
		this.mimeType = mimeType;
	}

	@Override
	public MimeType getMimeType() {
		return this.mimeType;
	}

	@Override
	public String getName() {
		return this.file.getName();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.file);
	}

	@Override
	public String toString() {
		return "LocalMimeResource{" +
				"path='" + path + '\'' +
				", mimeType=" + mimeType +
				", file=" + file +
				'}';
	}
}
