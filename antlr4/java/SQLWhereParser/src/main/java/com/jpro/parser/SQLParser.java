package com.jpro.parser;

import com.jpro.ast.filter.Filter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * SQLParser
 * @author aiyo
 * The custom parser of SQL!
 * It just implements part of SQL statement.
 * If you want to parse the whole SQL,
 * you must construct grammar files by yourself.
 */
public class SQLParser {
    /**
     * Parse SQL-WHERE sub-statement,
     * like:
     *       x > 0 AND y BETWEEN 0 AND 100 OR z = 'ID0';
     * Don't forget semicolon at last!
     * @param   where string of WHERE
     * @return  the nest condition of and/or.
     */
    public Filter parseWhere(String where) throws GrammarError {
        // Antlr runtime help us finish the front work.
        // And we can't modify the building logic!
        // Therefore, we should construct lexer+parser manually everytime.
        WhereLexer lexer = new WhereLexer(CharStreams.fromString(where));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        WhereParser parser = new WhereParser(tokens);
        parser.addErrorListener(new GrammarErrorListener());
        WhereParser.ProgContext treeRoot = parser.prog();
        // Maybe we can reuse the listener!
        WhereBaseListener listener = new WhereBaseListener();
        // Main logic to travel the AST (tree)!
        ParseTreeWalker.DEFAULT.walk(listener, treeRoot);
        // Finally, take what we want!
        return listener.getCondition();
    }
}
