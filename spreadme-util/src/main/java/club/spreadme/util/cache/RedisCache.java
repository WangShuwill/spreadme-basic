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
import club.spreadme.lang.cache.SpreadCache;
import club.spreadme.lang.cache.ValueLoader;
import club.spreadme.lang.serializer.Serializer;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

public class RedisCache implements SpreadCache<String, Object> {

    private final Jedis jedis;
    private Serializer<Object> serializer;

    public RedisCache(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public void setSerializer(Serializer<Object> serializer) {
        this.serializer = serializer;
    }

    @Override
    public Object get(String key, Serializer<Object> serializer) {
        byte[] bytes = jedis.get(key.getBytes());
        return serializer.deSerialize(bytes);
    }

    @Override
    public void put(String key, Object value, Serializer<Object> serializer) {
        byte[] bytes = serializer.serialize(value);
        jedis.set(key.getBytes(), bytes);
    }

    @Override
    public Object get(String key, Cache<String, Object> cache, ValueLoader<Object> valueLoader) {
        Object result = cache.get(key);
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
    public void put(String key, Object value, int expiredtime, TimeUnit timeUnit, Serializer<Object> serializer) {
        byte[] bytes = serializer.serialize(value);
        jedis.set(key.getBytes(), bytes);
        jedis.expireAt(key, timeUnit.toMillis(expiredtime));
    }

    @Override
    public void put(String key, Object value, int expiredtime, TimeUnit timeUnit) {
        byte[] bytes = serializer.serialize(value);
        jedis.set(key.getBytes(), bytes);
        jedis.expireAt(key, timeUnit.toMillis(expiredtime));
    }

    @Override
    public Object get(String key, SpreadCache<String, Object> cache, int expiredtime, TimeUnit timeUnit, ValueLoader<Object> valueLoader, Serializer<Object> serializer) {
        Object result = cache.get(key);
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
    public Object get(String key, SpreadCache<String, Object> cache, ValueLoader<Object> valueLoader, Serializer<Object> serializer) {
        Object result = cache.get(key);
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
        return jedis.clientGetname();
    }

    @Override
    public Object getNativeCache() {
        return jedis;
    }

    @Override
    public Object get(String key) {
        byte[] bytes = jedis.get(key.getBytes());
        return serializer.deSerialize(bytes);
    }

    @Override
    public void put(String key, Object value) {
        byte[] bytes = serializer.serialize(value);
        jedis.set(key.getBytes(), bytes);
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        if (jedis.exists(key)) {
            return get(key);
        }
        else {
            put(key, value);
        }
        return value;
    }

    @Override
    public void remove(String key) {
        jedis.del(key);
    }

    @Override
    public void clear() {
        jedis.flushDB();
    }
}
