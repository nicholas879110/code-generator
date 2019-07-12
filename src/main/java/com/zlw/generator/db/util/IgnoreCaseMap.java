package com.zlw.generator.db.util;

import java.util.HashMap;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/15
 *
 */
public class IgnoreCaseMap <K, V> extends HashMap<K, V> {

    public IgnoreCaseMap() {
    }

    public IgnoreCaseMap(K key, V v) {
        this.put(key, v);
    }

    public IgnoreCaseMap linkPut(K key, V value) {
        this.put(key, value);
        return this;
    }

    @Override
    public V get(Object key) {
        return super.get(key instanceof String?key.toString().toLowerCase():key);
    }

    @Override
    public V put(K key, V value) {
        return super.put(/*key instanceof String?key.toString().toLowerCase():*/key, value);
    }
}
