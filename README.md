# GrammarParser

A project of researching various grammar parser, including lex&amp;yacc(flex&amp;bison), antlr and custom solutions.

## Summary

As a backend developer, maybe you have written many kinds of parsers to parse many things, like SQL, DSL. This project was recorded many parsers i wrote at past. One of the most popular solutions is lex&amp;yacc. Maybe you were custom to use it, if you are a c/cpp developer. And Antlr is another good grammar parser, which can be used with many languages. However, both of them require you provide grammar files. After writing grammar files, you can compile them into codes.

This project has three parts:

- An antlr project for java to parse SQL-Where.
- A makefile project using lex&amp;yacc to parse AND/OR expression.
- A custom lightweight typescript project to help various grammar parsing in browser.

The module of antlr for java provides methods to convert SQL-Where strings to parser-tree(AST). Then you can transform AST to other kinds of DSL, and i provided a way of AST-to-Mongodb converting in project.

If you are interested in grammar parsing, you can spend sometimes in reading the project. I hope some of my code is useful for you. And if you have any questions, it's welcome to tell me.

## Typescript(browser)

A simple demo was provided:

```sql
show xxx when a = 12 and (b != -9.09 or c < -74) or (d = 43.00 and e != "hello \"oh")

```

<img src="browser/parser.png">


## lex&&yacc

Run the following commands to build:

```bash
cd GrammarParser
make
./expr     # run the 'expr' parser
make clean # remove build files
```

## Java SQL-Where parser

- Convert SQL-Where to Filter;
- Convert Filter to SQL;
- Convert Filter to MongoDB(Document).

SQL-Where like this:

```sql
age < 42 and (name != "simon" or work = 'writer')
```

Unit test:

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