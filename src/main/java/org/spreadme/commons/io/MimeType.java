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

package org.spreadme.commons.io;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public enum MimeType {

	PDF("application/pdf"),
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
	DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
	DOC("application/msword"),
	WPS("application/msword"),
	XLS("application/vnd.ms-excel"),
	PPT("application/vnd.ms-powerpoint"),
	PNG("image/png"),
	WEBP("image/webp"),
	TIFF("image/tiff"),
	XML("application/xml"),
	XHT("application/xhtml+xml"),
	XHTML("application/xhtml+xml"),
	HTM("text/html"),
	HTML("text/html"),
	TXT("text/plain"),
	JPG("image/jpeg"),
	JPEG("image/jpeg"),
	GIF("image/gif"),
	AVI("audio/basic"),
	ZIP("application/zip"),
	RTF("application/rtf");

	private static Map<String, MimeType> map = new HashMap<>(32);

	static {
		for (MimeType mimeType : MimeType.values()) {
			map.put(mimeType.name(), mimeType);
		}
	}

	private String value;

	MimeType(String value) {
		this.value = value;
	}

	public String getMimeType() {
		return value;
	}

	public void setMimeType(String value) {
		this.value = value;
	}

	/**
	 * get file extension name by file name
	 * @param fileName file name
	 * @return MimeType
	 */
	public static MimeType getByFileName(String fileName) {
		if (fileName == null) {
			return null;
		}
		int extIndex = fileName.lastIndexOf(fileName);
		if (extIndex > 0 && extIndex + 1 < fileName.length()) {
			String ext = fileName.substring(extIndex).toLowerCase();
			return valueOf(ext);
		}
		return null;
	}

	public static MimeType getByFileType(FileType fileType) {
		return map.get(fileType.name());
	}
}
