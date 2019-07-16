/*
 * Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package club.spreadme.core.cache.support;

import club.spreadme.core.cache.CacheClient;
import club.spreadme.core.cache.ValueLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LocalCacheClient<K, V> implements CacheClient<K, V> {

    private static final Map<Object, ValueWrapper<Object>> CACHE = new ConcurrentHashMap<>(256);

    @Override
    public void put(K key, V value) {
        CACHE.put(key, new ValueWrapper<>(value, System.currentTimeMillis()));
    }

    @Override
    public void put(K key, V value, int expiredtime, TimeUnit timeUnit) {
        long timemills = timeUnit.toMillis(expiredtime);
        ValueWrapper<Object> valueWrapper = new ValueWrapper<>(value, System.currentTimeMillis());
        valueWrapper.setExpiredtime(timemills);
        CACHE.put(key, valueWrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(K key) {
        ValueWrapper<Object> valueWrapper = CACHE.get(key);
        if (valueWrapper == null) {
            return null;
        }
        if (valueWrapper.getExpiredtime() == 0L) {
            return (V) valueWrapper.getValue();
        }
        else {
            long expiredtime = valueWrapper.getExpiredtime();
            long expiredTimestamp = valueWrapper.getTimestamp() + expiredtime;
            if (expiredTimestamp < System.currentTimeMillis()) {
                CACHE.remove(key);
                return null;
            }
        }
        return (V) valueWrapper.getValue();
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
            CACHE.put(key, new ValueWrapper<>(value, System.currentTimeMillis()));
        }
        return value;
    }

    @Override
    public void remove(K key) {
        CACHE.remove(key);
    }

    @Override
    public void clear() {
        CACHE.clear();
    }
}
