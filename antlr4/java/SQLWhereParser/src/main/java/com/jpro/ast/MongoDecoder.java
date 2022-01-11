package com.jpro.ast;

import com.jpro.ast.filter.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MongoDecoder {
    public enum FunctionType { NONE, COUNT, SUM, AVG, MIN, MAX }

    public static class Selected {
        private String field;
        private FunctionType function;
        private String alias;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public FunctionType getFunction() {
            return function;
        }

        public void setFunction(FunctionType function) {
            this.function = function;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }
    }

    public enum OrderType { ASC, DESC}

    public static class Ordered {
        private String field;
        private OrderType order;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public OrderType getOrder() {
            return order;
        }

        public void setOrder(OrderType order) {
            this.order = order;
        }
    }

    public static MongoIterable<Document> execute(
            MongoCollection<Document> collection,
            List<Selected> selects,
            Filter filters,
            List<String> groups,
            List<Ordered> orders,
            Long batchSize, Long offset) {
        Bson filter = null;
        if (filters != null) filter = MongoDecoder.decode(filters);
        boolean hasAlias = false;
        Document groupFun = new Document();
        Document project = new Document().append("_id", 0);
        Document projectDoc = new Document().append("_id", 0);
        for (Selected queryField : selects) {
            project.append(queryField.getAlias(), "$" + queryField.getField());
            if (!queryField.getField().equals(queryField.getAlias())) hasAlias = true;
            switch (queryField.getFunction()) {
                case NONE:
                    break;
                case COUNT:
                    groupFun.append(queryField.getAlias(), new Document().append("$sum", 1));
                    projectDoc.append(queryField.getAlias(), "$" + queryField.getAlias());
                    break;
                case SUM:
                    groupFun.append(queryField.getAlias(), new Document().append("$sum", "$" + queryField.getField()));
                    projectDoc.append(queryField.getAlias(), "$" + queryField.getAlias());
                    break;
                case MIN:
                    groupFun.append(queryField.getAlias(), new Document().append("$min", "$" + queryField.getField()));
                    projectDoc.append(queryField.getAlias(), "$" + queryField.getAlias());
                    break;
                case MAX:
                    groupFun.append(queryField.getAlias(), new Document().append("$max", "$" + queryField.getField()));
                    projectDoc.append(queryField.getAlias(), "$" + queryField.getAlias());
                    break;
                case AVG:
                    groupFun.append(queryField.getAlias(), new Document().append("$avg", "$" + queryField.getField()));
                    projectDoc.append(queryField.getAlias(), "$" + queryField.getAlias());
                    break;
                default:
            }
        }
        Document sortDoc = new Document();
        if (orders != null && !orders.isEmpty()) {
            for (Ordered orderField : orders) {
                sortDoc.append(orderField.getField(), orderField.getOrder() == OrderType.DESC ? -1 : 1);
            }
        }
        Document skipDoc = offset != null ? new Document().append("$skip", offset.intValue()) : null;
        if (groups == null || groups.isEmpty()) {
            if (groupFun.isEmpty()) { // common query
                if (hasAlias) {
                    return aggregatePipeline(collection, filter, null, project, sortDoc, skipDoc, batchSize);
                } else { // normal
                    Document projection = new Document().append("_id", 0);
                    selects.forEach(q -> projection.append(q.getField(), 1));
                    Long skipN = offset;
                    return find(collection, filter, projection, sortDoc, skipN, batchSize);
                }
            } else { // aggregation
                groupFun.append("_id", null);
                return aggregatePipeline(collection, filter, groupFun, projectDoc, sortDoc, skipDoc, batchSize);
            }
        } else if (groups.size() == 1) {
            groupFun.append("_id", "$" + groups.get(0));
            projectDoc.append(groups.get(0), "$_id");
            return aggregatePipeline(collection, filter, groupFun, projectDoc, sortDoc, skipDoc, batchSize);
        } else {
            Document groupDoc = new Document();
            for (String groupField : groups) {
                groupDoc.append(groupField, "$" + groupField);
                projectDoc.append(groupField, "$_id." + groupField);
            }
            groupFun.append("_id", groupDoc);
            return aggregatePipeline(collection, filter, groupFun, projectDoc, sortDoc, skipDoc, batchSize);
        }
    }

    private static FindIterable<Document> find(MongoCollection<Document> collection, Bson filter, Bson projection, Bson sort, Long skip, Long size) {
        System.out.println("[Find] filter: " + filter + "; projection: " + projection + "; sort: " + sort + "; skip: " + skip + "; size: " + size);
        FindIterable<Document> findIterable;
        findIterable = filter != null ? collection.find(filter) : collection.find();
        findIterable = projection != null && (!(projection instanceof Document) || !((Document) projection).isEmpty()) ? findIterable.projection(projection) : findIterable;
        findIterable = sort != null && (!(sort instanceof Document) || !((Document) sort).isEmpty()) ? findIterable.sort(sort) : findIterable;
        findIterable = skip != null ? findIterable.skip(skip.intValue()) : findIterable;
        findIterable = size != null ? findIterable.batchSize(size.intValue()) : findIterable;
        return findIterable;
    }

    private static AggregateIterable<Document> aggregatePipeline(MongoCollection<Document> collection, Bson filter, Bson group, Bson project, Bson sort, Bson skip, Long size) {
        System.out.println("[Aggregate] filter: " + filter + "; group: " + group + "; project: " + project + "; sort: " + sort + "; skip: " + skip + "; size: " + size);
        List<Bson> pipeline = new ArrayList<>(4);
        if (filter != null) pipeline.add(new Document("$match", filter));
        if (group != null && (!(group instanceof Document) || !((Document) group).isEmpty())) pipeline.add(new Document("$group", group));
        if (project != null && (!(project instanceof Document) || !((Document) project).isEmpty())) pipeline.add(new Document("$project", project));
        if (sort != null &&(!(sort instanceof Document) || !((Document) sort).isEmpty())) pipeline.add(new Document("$sort", sort));
        if (skip != null &&(!(skip instanceof Document) || !((Document) skip).isEmpty())) pipeline.add(new Document("$skip", skip));
        return size == null ? collection.aggregate(pipeline) : collection.aggregate(pipeline).batchSize(size.intValue());
    }

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
