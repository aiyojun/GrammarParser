package com.jpro.ast;

import com.jpro.ast.filter.*;

import java.util.stream.Collectors;

public final class SQLDecoder {
    public static String decode(Filter filter) {
        if (filter instanceof And)      return decode((And) filter);
        if (filter instanceof Or)       return decode((Or) filter);
        if (filter instanceof Eq)       return decode((Eq<?>) filter);
        if (filter instanceof Ne)       return decode((Ne<?>) filter);
        if (filter instanceof Gt)       return decode((Gt<?>) filter);
        if (filter instanceof Gte)      return decode((Gte<?>) filter);
        if (filter instanceof Lt)       return decode((Lt<?>) filter);
        if (filter instanceof Lte)      return decode((Lte<?>) filter);
        if (filter instanceof Between)  return decode((Between<?>) filter);
        if (filter instanceof In)       return decode((In<?>) filter);
        return "";
    }

    private static <T> String decode(Eq<T> filter) {
        return filter.getField() + "=" + (filter.getValue() instanceof String ?
                ("\"" + filter.getValue() + "\"") : filter.getValue());
    }

    private static <T> String decode(Ne<T> filter) {
        return filter.getField() + "!=" + (filter.getValue() instanceof String ?
                ("\"" + filter.getValue() + "\"") : filter.getValue());
    }

    private static <T> String decode(Gt<T> filter) {
        return filter.getField() + ">" + (filter.getValue() instanceof String ?
                ("\"" + filter.getValue() + "\"") : filter.getValue());
    }

    private static <T> String decode(Gte<T> filter) {
        return filter.getField() + ">=" + (filter.getValue() instanceof String ?
                ("\"" + filter.getValue() + "\"") : filter.getValue());
    }

    private static <T> String decode(Lt<T> filter) {
        return filter.getField() + "<" + (filter.getValue() instanceof String ?
                ("\"" + filter.getValue() + "\"") : filter.getValue());
    }

    private static <T> String decode(Lte<T> filter) {
        return filter.getField() + "<=" + (filter.getValue() instanceof String ?
                ("\"" + filter.getValue() + "\"") : filter.getValue());
    }

    private static <T> String decode(In<T> filter) {
        return filter.getField() + " IN ("
                + filter.getValues().stream()
                .map(o -> o instanceof String ? ("\"" + o + "\"") : o.toString())
                .collect(Collectors.joining(","))
                + ")";
    }

    private static <T> String decode(Between<T> filter) {
        return filter.getField()
                + " BETWEEN "
                + (filter.getMinValue() instanceof String ?
                ("\"" + filter.getMinValue() + "\"") : filter.getMinValue())
                + " AND "
                + (filter.getMaxValue() instanceof String ?
                ("\"" + filter.getMaxValue() + "\"") : filter.getMaxValue());
    }

    private static String decode(And filter) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < filter.getFilters().size(); i++) {
            if (filter.getFilters().get(i) instanceof Or) {
                s.append("(");
                s.append(decode(filter.getFilters().get(i)));
                s.append(")");
            } else {
                s.append(decode(filter.getFilters().get(i)));
            }
            if (i != filter.getFilters().size() - 1) {
                s.append(" AND ");
            }
        }
        return s.toString();
    }

    
    private static String decode(Or filter) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < filter.getFilters().size(); i++) {
            if (filter.getFilters().get(i) instanceof And) {
                s.append("(");
                s.append(decode(filter.getFilters().get(i)));
                s.append(")");
            } else {
                s.append(decode(filter.getFilters().get(i)));
            }
            if (i != filter.getFilters().size() - 1) {
                s.append(" OR ");
            }
        }
        return s.toString();
    }
}
