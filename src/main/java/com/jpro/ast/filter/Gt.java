package com.jpro.ast.filter;

public class Gt implements Filter {
    private String field;

    private Object value;

    public Gt(String field, Object value) {
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
        return "\"" + field + "\":{\"$gt\":"
                + (value instanceof String ? ("\"" + value + "\"") : value.toString())
                + "}";
    }
}
