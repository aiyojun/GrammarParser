package com.jpro.ast.filter;

public class Ne implements Filter {
    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    private String field;
    private Object value;

    public Ne(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + field + "\":{\"$ne\":" + (value instanceof String ? ("\"" + value + "\"") : value.toString()) + "}";
    }
}
