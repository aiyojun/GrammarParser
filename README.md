# GrammarParser

Using lex&amp;yacc or flex&amp;bison build grammar parser.

## Summary

Recently, i'm learning lex&yacc, a tool which can help parse language syntax! Of cause, lex&yacc(or flex&yacc gnu's) only support C/C++. If you want to use grammar parser in other language, you can choose ANTLRv4.

Now, there is a simple grammar parser program in the project. It's a logic about judging 'AND', 'OR' expression, like SQL's WHERE sentense.

For example:

```sql
age < 42 and (name != "simon" or work = 'writer')
```

## Build

```bash
cd GrammarParser
make
./expr     # run the 'expr' parser
make clean # remove build files
```

## Java SQL parser

- Convert SQL-Where to Filter;
- Convert Filter to SQL;
- Convert Filter to MongoDB(Document).

```java
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
```