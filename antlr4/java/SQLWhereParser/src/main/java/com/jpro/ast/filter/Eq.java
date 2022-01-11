package com.jpro.ast.filter;

public class Eq<T> implements Filter {
    private final String field;

    private final T value;

    public String getField() {
        return field;
    }

    public T getValue() {
        return value;
    }

    public Eq(String field, T value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + field + "\":{\"$eq\":"
                + (value instanceof String ? ("\"" + value + "\"") : value.toString())
                + "}";
    }
}

