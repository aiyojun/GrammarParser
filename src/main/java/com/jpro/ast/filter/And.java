package com.jpro.ast.filter;

import java.util.ArrayList;
import java.util.List;

public class And implements Filter {
    private List<Filter> conditions = new ArrayList<>(10);

    public And() {

    }

    public And(List<Filter> conditions) {
        this.conditions.addAll(conditions);
        this.unfold();
    }


    public List<Filter> getConditions() {
        return conditions;
    }

    public void add(Filter condition) {
        if (condition instanceof And) {
            And other = (And) condition;
            conditions.addAll(other.conditions);
        } else {
            conditions.add(condition);
        }
    }

    private int nestedAndSize() {
        int size = 0;
        for (Filter atom : conditions) {
            if (atom instanceof And) {
                size = ((And) atom).conditions.size();
                break;
            }
        }
        return size;
    }

    private void unfold() {
        int len;
        while ((len = nestedAndSize()) > 0) {
            List<Filter> replace = new ArrayList<>(
                    conditions.size() + len - 1);
            for (Filter atom : conditions) {
                if (atom instanceof And) {
                    replace.addAll(((And) atom).conditions);
                } else {
                    replace.add(atom);
                }
            }
            conditions = replace;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Filter condition : conditions) {
            builder.append(condition);
            if (index != conditions.size() - 1) {
                builder.append(",");
            }
            index++;
        }
        return builder.toString();
    }
}
