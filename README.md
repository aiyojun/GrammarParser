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