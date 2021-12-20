package com.jpro.ast.filter;

public class Lte implements Filter {
    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    private String field;
    private Object value;

    public Lte(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + field + "\":{\"$lte\":" + (value instanceof String ? ("\"" + value + "\"") : value.toString()) + "}";
    }
}

