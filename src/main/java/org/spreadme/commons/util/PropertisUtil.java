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

package org.spreadme.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Propertis Util
 * @author shuwei.wang
 */
public abstract class PropertisUtil {

	private PropertisUtil() {

	}

	public static Map<Object, Object> loadProperties(InputStream in) {
		Properties properties = new Properties();
		Map<Object, Object> propertyMap = new HashMap<>();
		try {
			properties.load(in);
			Enumeration enumeration = properties.propertyNames();
			while (enumeration.hasMoreElements()) {
				Object key = enumeration.nextElement();
				Object value = properties.get(key);
				propertyMap.put(key, value);
			}

		}
		catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return propertyMap;
	}
}
