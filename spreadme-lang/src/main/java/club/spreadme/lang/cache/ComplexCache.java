package club.spreadme.lang.cache;

import club.spreadme.lang.serializer.Serializer;

import java.util.concurrent.TimeUnit;

public interface ComplexCache<K,V> extends Cache<K,V> {

    void setSerializer(Serializer<V> serializer);

    void put(K key, V value, int expiredtime, TimeUnit timeUnit);

    void put(K key, V value, int expiredtime, TimeUnit timeUnit, Serializer<V> serializer);

    V get(K key, Serializer<V> serializer);

    void put(K key, V value, Serializer<V> serializer);

    V get(K key, Cache<K, V> cache, int expiredtime, TimeUnit timeUnit, ValueLoader<V> valueLoader, Serializer<V> serializer);

    V get(K key, Cache<K, V> cache, ValueLoader<V> valueLoader, Serializer<V> serializer);
}
