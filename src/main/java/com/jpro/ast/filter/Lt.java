package com.jpro.ast.filter;

public class Lt implements Filter {
    private String field;

    private Object value;

    public Lt(String field, Object value) {
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
        return "\"" + field + "\":{\"$lt\":"
                + (value instanceof String ? ("\"" + value + "\"") : value.toString())
                + "}";
    }
}
