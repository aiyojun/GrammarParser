package com.jpro;

import com.jpro.ast.MongoDecoder;
import com.jpro.ast.SQLDecoder;
import com.jpro.ast.filter.Filter;
import com.jpro.parser.GrammarError;
import com.jpro.parser.SQLParser;
import org.bson.Document;
import org.junit.Test;

public class FilterDemo {
    @Test
    public void parseSQL() {
        SQLParser parser = new SQLParser();
        try {
            String sql = "a>12 and (b < 'haha' or c between 34 and 43 and d > '2020-01-01 08:00:00' or e in (1,2,3));";
            Filter filter = parser.parseWhere(sql);
            String sql_ = SQLDecoder.decode(filter);
            Document document = MongoDecoder.decode(filter);
            System.out.println(sql);
            System.out.println(sql_);
            System.out.println(document.toJson());
        } catch (GrammarError e) {
            e.printStackTrace();
        }
    }
}
