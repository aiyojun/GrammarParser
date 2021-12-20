package com.jpro.ast;

import com.jpro.ast.filter.*;
import com.jpro.parser.GrammarError;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DefaultBuilder
 * @author aiyo
 * This class help to build all kinds of {@code Filter}, quickly!
 */
public final class Filters {
    private Filters() {}

    public enum TYPES { STRING, BOOL, INT, DOUBLE }

    public static Eq eq(String field, Object value) {
        return new Eq(field, value);
    }

    public static Ne ne(String field, Object value) {
        return new Ne(field, value);
    }

    public static Gt gt(String field, Object value) {
        return new Gt(field, value);
    }

    public static Gte gte(String field, Object value) {
        return new Gte(field, value);
    }

    public static Lt lt(String field, Object value) {
        return new Lt(field, value);
    }

    public static Lte lte(String field, Object value) {
        return new Lte(field, value);
    }

    public static Between between(String field, Object min, Object max) {
        return new Between(field, min, max);
    }

    public static In in(String field, List<Object> values) {
        return new In(field, values);
    }

    public static And and(List<Filter> conditions) {
        return new And(conditions);
    }

    public static And and(Filter... conditions) {
        return new And(Arrays.asList(conditions));
    }

    public static Or or(List<Filter> conditions) {
        return new Or(conditions);
    }

    public static Or or(Filter... conditions) {
        return new Or(Arrays.asList(conditions));
    }

    public static String dumpsToSQL(Filter condition) throws GrammarError {
        if (condition instanceof And) {
            return dumpsToSQL((And) condition);
        } else if (condition instanceof Or) {
            return dumpsToSQL((Or) condition);
        } else if (condition instanceof Eq) {
            return dumpsToSQL((Eq) condition);
        } else if (condition instanceof Gt) {
            return dumpsToSQL((Gt) condition);
        } else if (condition instanceof Gte) {
            return dumpsToSQL((Gte) condition);
        } else if (condition instanceof Lt) {
            return dumpsToSQL((Lt) condition);
        } else if (condition instanceof Lte) {
            return dumpsToSQL((Lte) condition);
        } else if (condition instanceof Between) {
            return dumpsToSQL((Between) condition);
        } else if (condition instanceof In) {
            return dumpsToSQL((In) condition);
        } else {
            throw new GrammarError("Unknown condition type: " + condition);
        }
    }

    public static String dumpsToMongoDB(Filter condition) {
        return "{" + condition.toString() + "}";
    }

    private static String dumpsToSQL(Eq condition) {
        return condition.getField() + "=" + (condition.getValue() instanceof String ?
                ("\"" + condition.getValue() + "\"") : condition.getValue());
    }

    private static String dumpsToSQL(Ne condition) {
        return condition.getField() + "!=" + (condition.getValue() instanceof String ?
                ("\"" + condition.getValue() + "\"") : condition.getValue());
    }

    private static String dumpsToSQL(Gt condition) {
        return condition.getField() + ">" + (condition.getValue() instanceof String ?
                ("\"" + condition.getValue() + "\"") : condition.getValue());
    }

    private static String dumpsToSQL(Gte condition) {
        return condition.getField() + ">=" + (condition.getValue() instanceof String ?
                ("\"" + condition.getValue() + "\"") : condition.getValue());
    }

    private static String dumpsToSQL(Lt condition) {
        return condition.getField() + "<" + (condition.getValue() instanceof String ?
                ("\"" + condition.getValue() + "\"") : condition.getValue());
    }

    private static String dumpsToSQL(Lte condition) {
        return condition.getField() + "<=" + (condition.getValue() instanceof String ?
                ("\"" + condition.getValue() + "\"") : condition.getValue());
    }

    private static String dumpsToSQL(Between condition) {
        return condition.getField()
                + " BETWEEN "
                + (condition.getMinValue() instanceof String ?
                ("\"" + condition.getMinValue() + "\"") : condition.getMinValue())
                + " AND "
                + (condition.getMaxValue() instanceof String ?
                ("\"" + condition.getMaxValue() + "\"") : condition.getMaxValue());
    }

    private static String dumpsToSQL(In condition) {
        return condition.getField() + " IN ("
                + condition.getValues().stream()
                .map(o -> o instanceof String ? ("\"" + o + "\"") : o.toString())
                .collect(Collectors.joining(","))
                + ")";
    }

    private static String dumpsToSQL(And condition) throws GrammarError {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < condition.getConditions().size(); i++) {
            if (condition.getConditions().get(i) instanceof Or) {
                s.append("(");
                s.append(dumpsToSQL(condition.getConditions().get(i)));
                s.append(")");
            } else {
                s.append(dumpsToSQL(condition.getConditions().get(i)));
            }
            if (i != condition.getConditions().size() - 1) {
                s.append(" AND ");
            }
        }
        return s.toString();
    }

    private static String dumpsToSQL(Or condition) throws GrammarError {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < condition.getConditions().size(); i++) {
            if (condition.getConditions().get(i) instanceof And) {
                s.append("(");
                s.append(dumpsToSQL(condition.getConditions().get(i)));
                s.append(")");
            } else {
                s.append(dumpsToSQL(condition.getConditions().get(i)));
            }
            if (i != condition.getConditions().size() - 1) {
                s.append(" Or ");
            }
        }
        return s.toString();
    }
}
