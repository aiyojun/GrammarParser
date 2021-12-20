package com.jpro.ast.filter;

import java.util.ArrayList;
import java.util.List;

public class Or implements Filter {
    private List<Filter> conditions = new ArrayList<>(10);

    public Or() {

    }

    public Or(List<Filter> conditions) {
        this.conditions.addAll(conditions);
        this.unfold();
    }

    public List<Filter> getConditions() {
        return conditions;
    }

    public void add(Filter condition) {
        conditions.add(condition);
    }

    private int nestedOrSize() {
        int size = 0;
        for (Filter atom : conditions) {
            if (atom instanceof Or) {
                size = ((Or) atom).conditions.size();
                break;
            }
        }
        return size;
    }

    private void unfold() {
        int len;
        while ((len = nestedOrSize()) > 0) {
            List<Filter> replace = new ArrayList<>(
                    conditions.size() + len - 1);
            for (Filter atom : conditions) {
                if (atom instanceof Or) {
                    replace.addAll(((Or) atom).conditions);
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
            builder.append("{");
            builder.append(condition);
            builder.append("}");
            if (index != conditions.size() - 1) {
                builder.append(",");
            }
            index++;
        }
        return "\"$or\":[" + builder + "]";
    }
}
