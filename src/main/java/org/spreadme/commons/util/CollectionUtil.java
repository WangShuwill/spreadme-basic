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

	/**
	 * 判断集合是否为空（null或者没有元素）
	 *
	 * @param collection Collection
	 * @return 是否为空
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 判断集合是否不为空（不为null并且有元素）
	 *
	 * @param collection Collection
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	/**
	 * 可变参数转化为List
	 *
	 * @param data data
	 * @param <E> E
	 * @return List
	 */
	@SafeVarargs
	public static <E> List<E> toList(E... data) {
		if (data == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(Arrays.asList(data));
	}

	/**
	 * Properties to Map
	 *
	 * @param props Properties
	 * @return Map
	 */
	public static Map<String, Object> toMap(@Nullable Properties props) {
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
	
	public static <E> Collection<E> addAll(Collection<E> all, Collection<E> sub) {
		if (isNotEmpty(sub)) {
			all.addAll(sub);
		}
		return all;
	}

	@SafeVarargs
	public static <E> Collection<E> addAll(Collection<E> all, E... values){
		if(values == null || values.length == 0){
			return all;
		}
		all.addAll(Arrays.asList(values));
		return all;
	}
}
