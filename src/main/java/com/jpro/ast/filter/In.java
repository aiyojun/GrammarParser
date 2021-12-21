package com.jpro.ast.filter;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class In<T> implements Filter, Iterable<T> {
    private final String field;
    private final List<T> values;

    public In(String field, List<T> values) {
        this.field = field;
        this.values = values;
    }

    public String getField() {
        return field;
    }

    public List<T> getValues() {
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

    @Override
    public Iterator<T> iterator() {
        return values.iterator();
    }
}
