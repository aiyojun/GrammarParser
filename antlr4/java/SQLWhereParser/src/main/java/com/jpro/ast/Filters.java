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
    public enum TYPES { STRING, BOOL, INT, DOUBLE }

    public static <T> Eq<T> eq(String field, T value) {
        return new Eq<>(field, value);
    }

    public static <T> Ne<T> ne(String field, T value) {
        return new Ne<>(field, value);
    }

    public static <T> Gt<T> gt(String field, T value) {
        return new Gt<>(field, value);
    }

    public static <T> Gte<T> gte(String field, T value) {
        return new Gte<>(field, value);
    }

    public static <T> Lt<T> lt(String field, T value) {
        return new Lt<>(field, value);
    }

    public static <T> Lte<T> lte(String field, T value) {
        return new Lte<>(field, value);
    }

    public static <T> Between<T> between(String field, T min, T max) {
        return new Between<>(field, min, max);
    }

    public static <T> In<T> in(String field, List<T> values) {
        return new In<>(field, values);
    }

    public static And and(List<Filter> Filters) {
        return new And(Filters);
    }

    public static And and(Filter... Filters) {
        return new And(Arrays.asList(Filters));
    }

    public static Or or(List<Filter> Filters) {
        return new Or(Filters);
    }

    public static Or or(Filter... Filters) {
        return new Or(Arrays.asList(Filters));
    }
}
