package com.jpro.ast.filter;

public class Between<T> implements Filter {
    private final String field;
    private final T minValue;
    private final T maxValue;

    public String getField() {
        return field;
    }

    public T getMinValue() {
        return minValue;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public Between(String field, T min, T max) {
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
