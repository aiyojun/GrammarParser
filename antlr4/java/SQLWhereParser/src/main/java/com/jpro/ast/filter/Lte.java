package com.jpro.ast.filter;

public class Lte<T> implements Filter {
    private final String field;
    private final T value;

    public String getField() {
        return field;
    }

    public T getValue() {
        return value;
    }

    public Lte(String field, T value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + field + "\":{\"$lte\":" + (value instanceof String ? ("\"" + value + "\"") : value.toString()) + "}";
    }
}

