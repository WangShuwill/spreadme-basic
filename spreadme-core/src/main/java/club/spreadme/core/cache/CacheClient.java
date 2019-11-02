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

package club.spreadme.core.cache;

import java.util.concurrent.TimeUnit;

public interface CacheClient<K, V> {

    void put(K key, V value);

    void put(K key, V value, int timeout, TimeUnit timeUnit);

    V get(K key);

    V get(K key, ValueLoader<V> valueLoader);

    V get(K key, ValueLoader<V> valueLoader, int timeout, TimeUnit timeUnit);

    V get(K key, Class<V> type);

    V get(K key, Class<V> type, ValueLoader<V> valueLoader);

    V get(K key, Class<V> type, ValueLoader<V> valueLoader, int timeout, TimeUnit timeUnit);

    boolean remove(K key);

    void clear();
}
