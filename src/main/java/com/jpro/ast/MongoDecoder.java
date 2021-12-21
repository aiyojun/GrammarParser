package com.jpro.ast;

import com.jpro.ast.filter.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MongoDecoder {
    public static Document decode(Filter filter) {
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
        return new Document();
    }

    private static <T> Document decode(Eq<T> filter) {
        return new Document().append(filter.getField(), filter.getValue());
    }

    private static <T> Document decode(Ne<T> filter) {
        return new Document().append(filter.getField(), new Document().append("$ne", filter.getValue()));
    }

    private static <T> Document decode(Gt<T> filter) {
        return new Document().append(filter.getField(), new Document().append("$gt", filter.getValue()));
    }

    private static <T> Document decode(Lt<T> filter) {
        return new Document().append(filter.getField(), new Document().append("$lt", filter.getValue()));
    }

    private static <T> Document decode(Gte<T> filter) {
        return new Document().append(filter.getField(), new Document().append("$gte", filter.getValue()));
    }

    private static <T> Document decode(Lte<T> filter) {
        return new Document().append(filter.getField(), new Document().append("$lte", filter.getValue()));
    }

    private static <T> Document decode(In<T> filter) {
        return new Document().append(filter.getField(), new Document().append("$in", filter.getValues()));
    }

    private static <T> Document decode(Between<T> filter) {
        return new Document().append(filter.getField(), new Document().append("$gt", filter.getMinValue()).append("$lte", filter.getMaxValue()));
    }

    private static Document decode(And filter) {
        Document andDoc = new Document();
        for (Filter sub : filter.getFilters()) {
            Document subDoc = decode(sub);
            if (subDoc == null) continue;
            for (Map.Entry<String, Object> every: subDoc.entrySet()) {
                andDoc.append(every.getKey(), every.getValue());
            }
        }
        return andDoc;
    }

    private static Document decode(Or filter) {
        List<Document> orDoc = new ArrayList<>(filter.getFilters().size());
        for (Filter sub : filter.getFilters()) {
            Document subDoc = decode(sub);
            if (subDoc == null) continue;
            orDoc.add(subDoc);
        }
        return new Document("$or", orDoc);
    }
}
