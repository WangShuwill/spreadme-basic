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

package org.spreadme.commons.cache.support;

import java.util.Map;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.spreadme.commons.cache.CacheClient;

/**
 * 通过ConcurrentHashMap实现的本地缓存 Simple
 * @author shuwei.wang
 */
public class LocalCacheClient<K, V> implements CacheClient<K, V> {

	private final Map<K, ValueWrapper<V>> POOL = new ConcurrentHashMap<>(256);

	@Override
	public void put(K key, V value) {
		POOL.put(key, new ValueWrapper<>(value));
	}

	@Override
	public void put(K key, V value, int timeout, TimeUnit timeUnit) {
		ValueWrapper<V> valueWrapper = new ValueWrapper<>(value);
		POOL.put(key, valueWrapper);
		valueWrapper.schedule(new TimerTask() {
			@Override
			public void run() {
				POOL.remove(key);
			}
		}, timeUnit.toMillis(timeout));
	}

	@Override
	public V get(K key) {
		if (key == null) {
			return null;
		}
		ValueWrapper<V> valueWrapper = POOL.get(key);
		return valueWrapper != null ? valueWrapper.getValue() : null;
	}

	@Override
	public V get(K key, Class<V> type) {
		V value = get(key);
		if (value != null && type != null && !type.isInstance(value)) {
			throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
		}
		return value;
	}


	@Override
	public boolean remove(K key) {
		Object value = POOL.remove(key);
		return Objects.isNull(value);
	}

	@Override
	public void clear() {
		POOL.clear();
	}

}
