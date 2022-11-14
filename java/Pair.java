package com.jpro;

public record Pair<T, K>(T key, K value) {
    public static <T, K> Pair<T, K> build(T key, K value) {
        return new Pair<>(key, value);
    }
}