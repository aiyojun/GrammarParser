package com.jpro.ast.filter;

public class Gte implements Filter {
    private String field;

    private Object value;

    public Gte(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + field + "\":{\"$gte\":"
                + (value instanceof String ? ("\"" + value + "\"") : value.toString())
                + "}";
    }
}
