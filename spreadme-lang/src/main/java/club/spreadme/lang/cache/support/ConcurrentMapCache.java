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

package club.spreadme.lang.cache.support;

import club.spreadme.lang.Assert;
import club.spreadme.lang.cache.ValueWrapper;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapCache extends AbstractCache {

    private final String name;
    private final ConcurrentHashMap<Object, Object> store;

    public ConcurrentMapCache(String name, boolean allowNullValues) {
        this(name, new ConcurrentHashMap<>(256), allowNullValues);
    }

    public ConcurrentMapCache(String name, ConcurrentHashMap<Object, Object> store, boolean allowNullValues) {
        super(allowNullValues);
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
    protected Object lookup(Object key) {
        return this.store.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper storeValue = get(key);
        if (storeValue != null) {
            return (T) storeValue.get();
        }

        synchronized (this.store) {
            storeValue = get(key);
            if (storeValue != null) {
                return (T) storeValue.get();
            }

            T value;
            try {
                value = valueLoader.call();
            }
            catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
            return value;
        }
    }

    @Override
    public void put(Object key, Object value) {
        this.store.put(key, toStoreValue(value));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        this.store.putIfAbsent(key, toStoreValue(value));
        return toValueWrapper(value);
    }

    @Override
    public void remove(Object key) {
        this.store.remove(key);
    }

    @Override
    public void clear() {
        this.store.clear();
    }
}
