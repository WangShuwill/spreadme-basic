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

package club.spreadme.util.cache;

import club.spreadme.lang.cache.Cache;
import club.spreadme.lang.cache.ValueLoader;
import club.spreadme.lang.cache.support.AbstractCache;
import club.spreadme.lang.serializer.Serializer;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class RedisCache<K extends String, V> extends AbstractCache<K, V> {

    private final RedisCommands<byte[], byte[]> redisCommands;
    private Serializer<V> serializer;

    public RedisCache(RedisCommands<byte[], byte[]> redisCommands) {
        this.redisCommands = redisCommands;
    }

    @Override
    public void setSerializer(Serializer<V> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void put(K key, V value, int expiredtime, TimeUnit timeUnit) {
        byte[] bytes = serializer.serialize(value);
        redisCommands.setex(key.getBytes(), timeUnit.toSeconds(expiredtime), bytes);
    }

    @Override
    public void put(K key, V value, int expiredtime, TimeUnit timeUnit, Serializer<V> serializer) {
        byte[] bytes = serializer.serialize(value);
        redisCommands.setex(key.getBytes(), timeUnit.toSeconds(expiredtime), bytes);
    }

    @Override
    public V get(K key, Serializer<V> serializer) {
        byte[] bytes = redisCommands.get(key.getBytes());
        return serializer.deSerialize(bytes);
    }

    @Override
    public void put(K key, V value, Serializer<V> serializer) {
        byte[] bytes = serializer.serialize(value);
        redisCommands.set(key.getBytes(), bytes);
    }

    @Override
    public V get(K key, Cache<K, V> cache, ValueLoader<V> valueLoader) {
        V result = cache.get(key);
        if (result == null) {
            if (lock.tryLock()) {
                try {
                    result = valueLoader.load();
                    cache.put(key, result);
                }
                finally {
                    lock.unlock();
                }
            }
            else {
                result = cache.get(key);
                if (result == null) {
                    return get(key, cache, valueLoader);
                }
            }
        }
        return result;
    }

    @Override
    public V get(K key, Cache<K, V> cache, int expiredtime, TimeUnit timeUnit, ValueLoader<V> valueLoader, Serializer<V> serializer) {
        V result = cache.get(key);
        if (result == null) {
            if (lock.tryLock()) {
                try {
                    result = valueLoader.load();
                    cache.put(key, result, expiredtime, timeUnit, serializer);
                }
                finally {
                    lock.unlock();
                }
            }
            else {
                result = cache.get(key);
                if (result == null) {
                    return get(key, cache, expiredtime, timeUnit, valueLoader, serializer);
                }
            }
        }
        return result;
    }

    @Override
    public V get(K key, Cache<K, V> cache, ValueLoader<V> valueLoader, Serializer<V> serializer) {
        V result = cache.get(key, serializer);
        if (result == null) {
            if (lock.tryLock()) {
                try {
                    result = valueLoader.load();
                    cache.put(key, result, serializer);
                }
                finally {
                    lock.unlock();
                }
            }
            else {
                result = cache.get(key);
                if (result == null) {
                    return get(key, cache, valueLoader, serializer);
                }
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return Arrays.toString(redisCommands.clientGetname());
    }

    @Override
    public Object getNativeCache() {
        return redisCommands;
    }

    @Override
    public V get(K key) {
        byte[] bytes = redisCommands.get(key.getBytes());
        return serializer.deSerialize(bytes);
    }

    @Override
    public void put(K key, V value) {
        byte[] bytes = serializer.serialize(value);
        redisCommands.set(key.getBytes(), bytes);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        if (redisCommands.exists(key.getBytes()) > 0) {
            return get(key);
        }
        else {
            put(key, value);
        }
        return value;
    }

    @Override
    public void remove(K key) {
        redisCommands.del(key.getBytes());
    }

    @Override
    public void clear() {
        redisCommands.flushdb();
    }
}
