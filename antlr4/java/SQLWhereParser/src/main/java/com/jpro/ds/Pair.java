package com.jpro.ds;

import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V> {
    private K k;

    private V v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getKey() {
        return k;
    }

    public V getValue() {
        return v;
    }

    public V setValue(V value) {
        v = value;
        return v;
    }

    @Override
    public boolean equals(Object o) {
        try {
            return o instanceof Pair && ((Pair<K, V>) o).getKey().equals(k) && ((Pair<K, V>) o).getValue().equals(v);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
