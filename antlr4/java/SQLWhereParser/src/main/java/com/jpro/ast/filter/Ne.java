package com.jpro.ast.filter;

public class Ne<T> implements Filter {
    private final String field;
    private final T value;

    public String getField() {
        return field;
    }

    public T getValue() {
        return value;
    }

    public Ne(String field, T value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + field + "\":{\"$ne\":" + (value instanceof String ? ("\"" + value + "\"") : value.toString()) + "}";
    }
}

