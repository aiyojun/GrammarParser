package com.jpro.ast.filter;

public class Between implements Filter {
    public String getField() {
        return field;
    }

    public Object getMinValue() {
        return minValue;
    }

    public Object getMaxValue() {
        return maxValue;
    }

    private String field;
    private Object minValue;
    private Object maxValue;

    public Between(String field, Object min, Object max) {
        this.field = field;
        this.minValue = min;
        this.maxValue = max;
    }

    @Override
    public String toString() {
        return "\"" + field + "\":{\"$gt\":"
                + (minValue instanceof String ? ("\"" + minValue + "\"") : minValue.toString())
                + ",\"$lte\":"
                + (maxValue instanceof String ? ("\"" + maxValue + "\"") : maxValue.toString())
                + "}";
    }
}
