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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.spreadme.commons.lang.Nullable;

/**
 * collection util
 * @author shuwei.wang
 */
public abstract class CollectionUtil {

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isNotEmpty(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	@SafeVarargs
	public static <E> List<E> toList(E... data) {
		if (data == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(Arrays.asList(data));
	}

	public static Map<String, Object> propertiesIntoMap(@Nullable Properties props) {
		Map<String, Object> map = new HashMap<>();
		if (props != null) {
			for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
				String key = (String) en.nextElement();
				Object value = props.get(key);
				if (value == null) {
					value = props.getProperty(key);
				}
				map.put(key, value);
			}
		}
		return map;
	}

}
