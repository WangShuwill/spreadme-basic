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

import club.spreadme.lang.Assert;
import club.spreadme.lang.cache.Cache;
import club.spreadme.lang.cache.ValueWrapper;
import club.spreadme.lang.cache.support.SimpleValueWrapper;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;

import java.util.concurrent.Callable;

public class EhcacheCache implements Cache {

    private final Ehcache ehCache;

    public EhcacheCache(Ehcache ehCache) {
        Assert.notNull(ehCache, "Ehcache must bu not null");
        Status status = ehCache.getStatus();
        if (!Status.STATUS_ALIVE.equals(status)) {
            throw new IllegalArgumentException("The current ehcache is not alive " + status.toString());
        }
        this.ehCache = ehCache;
    }

    @Override
    public final String getName() {
        return this.ehCache.getName();
    }

    @Override
    public final Ehcache getNativeCache() {
        return this.ehCache;
    }

    @Override
    public ValueWrapper get(Object key) {
        Element element = lookup(key);
        return toValueWrapper(element);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        Element element = this.ehCache.get(key);
        Object value = element != null ? element.getObjectValue() : null;
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Callable<T> valueLoader) {
        Element element = lookup(key);
        if (element != null) {
            return (T) element.getObjectValue();
        }
        else {
            this.ehCache.acquireWriteLockOnKey(key);
            try {
                element = lookup(key);
                if (element != null) {
                    return (T) element.getObjectValue();
                }
                else {
                    return loadValue(key, valueLoader);
                }
            }
            finally {
                this.ehCache.releaseWriteLockOnKey(key);
            }
        }
    }

    @Override
    public void put(Object key, Object value) {
        this.ehCache.put(new Element(key, value));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Element exsitingElement = this.ehCache.putIfAbsent(new Element(key, value));
        return toValueWrapper(exsitingElement);
    }

    @Override
    public void remove(Object key) {
        this.ehCache.remove(key);
    }

    @Override
    public void clear() {
        this.ehCache.removeAll();
    }

    private <T> T loadValue(Object key, Callable<T> valueLoader) {
        T value;
        try {
            value = valueLoader.call();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return value;
    }

    private ValueWrapper toValueWrapper(Element element) {
        return element != null ? new SimpleValueWrapper(element) : null;
    }

    private Element lookup(Object key) {
        return this.ehCache.get(key);
    }

}
