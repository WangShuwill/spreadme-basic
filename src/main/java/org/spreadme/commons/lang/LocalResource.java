/*
 * Copyright [3/27/20 8:40 PM] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.commons.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * LocalResource
 * @author shuwei.wang
 */
public class LocalResource extends AbstractResource {

	private String path;
	private ContentType mimeType;
	private File file;

	public LocalResource(String path, ContentType mimeType) {
		this.path = path;
		this.file = new File(path);
		this.mimeType = mimeType;
	}

	@Override
	public ContentType getContentType() {
		return this.mimeType;
	}

	@Override
	public String getName() {
		return this.file.getName();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return Files.newInputStream(Paths.get(this.path));
	}

	@Override
	public boolean exsit() {
		return this.file != null && this.file.exists();
	}

	@Override
	public String toString() {
		return "LocalResource{" +
				"path='" + path + '\'' +
				", mimeType=" + mimeType +
				'}';
	}
}
