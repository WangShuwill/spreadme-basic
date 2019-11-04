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
import org.spreadme.commons.cache.ValueLoader;

/**
 * 通过ConcurrentHashMap实现的本地缓存
 * @author shuwei.wang
 */
public class LocalCacheClient<K, V> implements CacheClient<K, V> {

	private static final Map<Object, ValueWrapper<Object>> CACHE_MAP = new ConcurrentHashMap<>(256);

	@Override
	public void put(K key, V value) {
		CACHE_MAP.put(key, new ValueWrapper<>(value));
	}

	@Override
	public void put(K key, V value, int timeout, TimeUnit timeUnit) {
		ValueWrapper<Object> valueWrapper = new ValueWrapper<>(value);
		CACHE_MAP.put(key, valueWrapper);
		valueWrapper.schedule(new TimerTask() {
			@Override
			public void run() {
				CACHE_MAP.remove(key);
			}
		}, timeUnit.toMillis(timeout));
	}

	@Override
	@SuppressWarnings("unchecked")
	public V get(K key) {
		if (key == null) {
			return null;
		}
		ValueWrapper<V> valueWrapper = (ValueWrapper<V>) CACHE_MAP.get(key);
		return valueWrapper != null ? valueWrapper.getValue() : null;
	}

	@Override
	public V get(K key, ValueLoader<V> valueLoader) {
		V value = get(key);
		if (value == null) {
			value = valueLoader.load();
			CACHE_MAP.put(key, new ValueWrapper<>(value));
		}
		return value;
	}

	@Override
	public V get(K key, ValueLoader<V> valueLoader, int timeout, TimeUnit timeUnit) {
		V value = get(key);
		if (value == null) {
			value = valueLoader.load();
			this.put(key, value, timeout, timeUnit);
		}
		return value;
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
	public V get(K key, Class<V> type, ValueLoader<V> valueLoader) {
		V value = get(key, type);
		if (value == null) {
			value = valueLoader.load();
			this.put(key, value);

		}
		return value;
	}

	@Override
	public V get(K key, Class<V> type, ValueLoader<V> valueLoader, int timeout, TimeUnit timeUnit) {
		V value = get(key, type);
		if (value == null) {
			value = valueLoader.load();
			this.put(key, value, timeout, timeUnit);
		}
		return value;
	}

	@Override
	public boolean remove(K key) {
		Object value = CACHE_MAP.remove(key);
		return Objects.isNull(value);
	}

	@Override
	public void clear() {
		CACHE_MAP.clear();
	}

}
