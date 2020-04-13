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

package org.spreadme.commons.cache;

import java.util.concurrent.TimeUnit;

/**
 * 缓存客户端
 * @author shuwei.wang
 * @since 1.0.0
 */
public interface CacheClient<K, V> {

	/**
	 * 放置缓存对象
	 *
	 * @param key 键
	 * @param value 值
	 */
	void put(K key, V value);

	/**
	 * 如果传入key对应的value已经存在，就返回存在的value，不进行替换。如果不存在，就添加key和value，返回null
	 *
	 * @param key 键
	 * @param value 值
	 */
	V putIfAbsent(K key, V value);

	/**
	 * 放置缓存对象，并且设置过期时间
	 *
	 * @param key 键
	 * @param value 值
	 * @param timeout 过期时间
	 * @param timeUnit 时间单位 {@link TimeUnit}
	 */
	void put(K key, V value, long timeout, TimeUnit timeUnit);

	/**
	 * 获取缓存对象
	 *
	 * @param key 键
	 * @return value
	 */
	V get(K key);

	/**
	 * 获取缓存对象，并且转化为对应的类型
	 *
	 * @param key 键
	 * @param type 类型
	 * @return 值
	 */
	default V get(K key, Class<V> type){
		V value = get(key);
		if (value != null && type != null && !type.isInstance(value)) {
			throw new IllegalStateException(String.format("Cached value is not of required type [%s]: %s", type.getName(), value));
		}
		return value;
	}


	/**
	 * 移除缓存对象
	 *
	 * @param key 键
	 * @return 是否移除成功
	 */
	boolean remove(K key);

	/**
	 * 清除缓存
	 */
	void clear();
}
