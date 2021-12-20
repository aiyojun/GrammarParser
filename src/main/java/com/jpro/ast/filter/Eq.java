package com.jpro.ast.filter;

public class Eq implements Filter {
    private String field;

    private Object value;

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    public Eq(String field, Object value) {
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
