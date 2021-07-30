# GrammarParser

Using lex&amp;yacc or flex&amp;bison build grammar parser.

## Summary

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