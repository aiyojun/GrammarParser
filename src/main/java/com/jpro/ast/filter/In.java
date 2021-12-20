package com.jpro.ast.filter;

import java.util.List;
import java.util.stream.Collectors;

public class In implements Filter {
    private String field;
    private List<Object> values;

    public In(String field, List<Object> values) {
        this.field = field;
        this.values = values;
    }

    public String getField() {
        return field;
    }

    public List<Object> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "\"" + field + "\":{\"$in\":["
                + values.stream()
                .map(o -> (o instanceof String ? ("\"" + o + "\"") : o.toString()))
                .collect(Collectors.joining(","))
                + "]}";
    }
}
