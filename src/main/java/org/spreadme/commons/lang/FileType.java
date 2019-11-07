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

package org.spreadme.commons.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * file type
 * @author shuwei.wang
 * @since 1.0.0
 */
public enum FileType {

	JPEG("FFD8FF"),
	PNG("89504E47"),
	GIF("47494638"),
	TIFF("49492A00"),
	BMP("424D"),
	DWG("41433130"),
	PSD("38425053"),
	RTF("7B5C727466"),
	XML("3C3F786D6C"),
	HTML("68746D6C3E"),
	EML("44656C69766572792D646174653A"),
	DBX("CFAD12FEC5FD746F"),
	PST("2142444E"),
	XLS_DOC("D0CF11E0"),
	MDB("5374616E64617264204A"),
	WPD("FF575043"),
	EPS("252150532D41646F6265"),
	PDF("255044462D312E"),
	QDF("AC9EBD8F"),
	PWL("E3828596"),
	ZIP("504B0304"),
	RAR("52617221"),
	WAV("57415645"),
	AVI("41564920"),
	RAM("2E7261FD"),
	RM("2E524D46"),
	MPG("000001BA"),
	MOV("6D6F6F76"),
	ASF("3026B2758E66CF11"),
	MID("4D546864"),
	TXT("");

	private static Map<String, FileType> map = new HashMap<>(32);

	static {
		for (FileType fileType : FileType.values()) {
			map.put(fileType.getValue(), fileType);
		}
	}

	private String value;

	FileType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static FileType resolve(String header) {
		for(Map.Entry<String, FileType> entry : map.entrySet()){
			if(header.toUpperCase().startsWith(header)){
				return entry.getValue();
			}
		}
		return FileType.TXT;
	}
}
