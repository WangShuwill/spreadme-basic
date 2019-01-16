/*
 *  Copyright (c) 2019 Wangshuwei
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package club.spreadme.lang.cache.support;

import club.spreadme.lang.cache.Cache;
import club.spreadme.lang.cache.ValueLoader;
import club.spreadme.lang.serializer.Serializer;

import java.util.concurrent.TimeUnit;

public abstract class AbstractCache<K, V> implements Cache<K, V> {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public V putIfAbsent(K key, V value) {
        return null;
    }

    @Override
    public void setSerializer(Serializer<V> serializer) {

    }

    @Override
    public void put(K key, V value, int expiredtime, TimeUnit timeUnit) {

    }

    @Override
    public void put(K key, V value, int expiredtime, TimeUnit timeUnit, Serializer<V> serializer) {

    }

    @Override
    public V get(K key, Serializer<V> serializer) {
        return null;
    }

    @Override
    public void put(K key, V value, Serializer<V> serializer) {

    }

    @Override
    public V get(K key, Cache<K, V> cache, ValueLoader<V> valueLoader) {
        return null;
    }

    @Override
    public V get(K key, Cache<K, V> cache, int expiredtime, TimeUnit timeUnit, ValueLoader<V> valueLoader, Serializer<V> serializer) {
        return null;
    }

    @Override
    public V get(K key, Cache<K, V> cache, ValueLoader<V> valueLoader, Serializer<V> serializer) {
        return null;
    }

    @Override
    public void remove(K key) {

    }

    @Override
    public void clear() {

    }
}
