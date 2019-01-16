/*
 *  Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.spreadme.lang.cache;

import club.spreadme.lang.serializer.Serializer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public interface Cache<K, V> {

    Lock lock = new ReentrantLock();

    String getName();

    Object getNativeCache();

    V get(K key);

    default V get(K key, Class<V> type) {
        V value = get(key);
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return value;
    }

    void put(K key, V value);

    V putIfAbsent(K key, V value);

    void setSerializer(Serializer<V> serializer);

    void put(K key, V value, int expiredtime, TimeUnit timeUnit);

    void put(K key, V value, int expiredtime, TimeUnit timeUnit, Serializer<V> serializer);

    V get(K key, Serializer<V> serializer);

    void put(K key, V value, Serializer<V> serializer);

    V get(K key, Cache<K, V> cache, ValueLoader<V> valueLoader);

    V get(K key, Cache<K, V> cache, int expiredtime, TimeUnit timeUnit, ValueLoader<V> valueLoader, Serializer<V> serializer);

    V get(K key, Cache<K, V> cache, ValueLoader<V> valueLoader, Serializer<V> serializer);

    void remove(K key);

    void clear();
}
