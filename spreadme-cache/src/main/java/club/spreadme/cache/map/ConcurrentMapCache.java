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

package club.spreadme.cache.map;

import club.spreadme.cache.core.Cache;
import club.spreadme.cache.core.ValueLoader;
import club.spreadme.core.utils.Assert;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class ConcurrentMapCache<K, V> implements Cache<K, V> {

    private final String name;
    private final ConcurrentHashMap<K, V> store;

    public ConcurrentMapCache(String name) {
        this(name, new ConcurrentHashMap<>(256));
    }

    public ConcurrentMapCache(String name, ConcurrentHashMap<K, V> store) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(store, "Store must not be null");
        this.name = name;
        this.store = store;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.store;
    }

    @Override
    public V get(K key) {
        return this.store.get(key);
    }

    @Override
    public void put(K key, V value) {
        this.store.put(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        this.store.putIfAbsent(key, value);
        return value;
    }

    @Override
    public void remove(K key) {
        this.store.remove(key);
    }

    @Override
    public void clear() {
        this.store.clear();
    }

    @Override
    public V get(K key, Cache<K, V> cache, ValueLoader<V> valueLoader) {
        V value = cache.get(key);
        if (Objects.isNull(value)) {
            value = valueLoader.load();
            cache.put(key, value);
        }
        return value;
    }
}
