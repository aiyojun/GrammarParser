package com.jpro.ast.filter;

public class Lt<T> implements Filter {
    private final String field;

    private final T value;

    public Lt(String field, T value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + field + "\":{\"$lt\":"
                + (value instanceof String ? ("\"" + value + "\"") : value.toString())
                + "}";
    }
}
