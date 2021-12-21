package com.jpro.ast.filter;

import java.util.ArrayList;
import java.util.List;

public class Or implements Filter {
    private List<Filter> Filters = new ArrayList<>(10);

    public Or() {

    }

    public Or(List<Filter> Filters) {
        this.Filters.addAll(Filters);
        this.unfold();
    }

    public List<Filter> getFilters() {
        return Filters;
    }

    public void add(Filter Filter) {
        Filters.add(Filter);
    }

    private int nestedOrSize() {
        int size = 0;
        for (Filter atom : Filters) {
            if (atom instanceof Or) {
                size = ((Or) atom).Filters.size();
                break;
            }
        }
        return size;
    }

    private void unfold() {
        int len;
        while ((len = nestedOrSize()) > 0) {
            List<Filter> replace = new ArrayList<>(
                    Filters.size() + len - 1);
            for (Filter atom : Filters) {
                if (atom instanceof Or) {
                    replace.addAll(((Or) atom).Filters);
                } else {
                    replace.add(atom);
                }
            }
            Filters = replace;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Filter Filter : Filters) {
            builder.append("{");
            builder.append(Filter);
            builder.append("}");
            if (index != Filters.size() - 1) {
                builder.append(",");
            }
            index++;
        }
        return "\"$or\":[" + builder + "]";
    }
}

