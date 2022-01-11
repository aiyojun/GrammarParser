package com.jpro.ast.filter;

import java.util.ArrayList;
import java.util.List;

public class And implements Filter {
    private List<Filter> Filters = new ArrayList<>(10);

    public And() {

    }

    public And(List<Filter> Filters) {
        this.Filters.addAll(Filters);
        this.unfold();
    }


    public List<Filter> getFilters() {
        return Filters;
    }

    public void add(Filter Filter) {
        if (Filter instanceof And) {
            And other = (And) Filter;
            Filters.addAll(other.Filters);
        } else {
            Filters.add(Filter);
        }
    }

    private int nestedAndSize() {
        int size = 0;
        for (Filter atom : Filters) {
            if (atom instanceof And) {
                size = ((And) atom).Filters.size();
                break;
            }
        }
        return size;
    }

    private void unfold() {
        int len;
        while ((len = nestedAndSize()) > 0) {
            List<Filter> replace = new ArrayList<>(
                    Filters.size() + len - 1);
            for (Filter atom : Filters) {
                if (atom instanceof And) {
                    replace.addAll(((And) atom).Filters);
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
            builder.append(Filter);
            if (index != Filters.size() - 1) {
                builder.append(",");
            }
            index++;
        }
        return builder.toString();
    }
}
