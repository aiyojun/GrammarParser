// Generated from /opt/jqs/Where.g4 by ANTLR 4.9.1
package com.jpro.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link WhereParser}.
 */
public interface WhereListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link WhereParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(WhereParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhereParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(WhereParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhereParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(WhereParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhereParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(WhereParser.ExprContext ctx) throws GrammarError;
	/**
	 * Enter a parse tree produced by {@link WhereParser#unionItem}.
	 * @param ctx the parse tree
	 */
	void enterUnionItem(WhereParser.UnionItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhereParser#unionItem}.
	 * @param ctx the parse tree
	 */
	void exitUnionItem(WhereParser.UnionItemContext ctx) throws GrammarError;
	/**
	 * Enter a parse tree produced by {@link WhereParser#exprWithQuote}.
	 * @param ctx the parse tree
	 */
	void enterExprWithQuote(WhereParser.ExprWithQuoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhereParser#exprWithQuote}.
	 * @param ctx the parse tree
	 */
	void exitExprWithQuote(WhereParser.ExprWithQuoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhereParser#exprItem}.
	 * @param ctx the parse tree
	 */
	void enterExprItem(WhereParser.ExprItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhereParser#exprItem}.
	 * @param ctx the parse tree
	 */
	void exitExprItem(WhereParser.ExprItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhereParser#atomName}.
	 * @param ctx the parse tree
	 */
	void enterAtomName(WhereParser.AtomNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhereParser#atomName}.
	 * @param ctx the parse tree
	 */
	void exitAtomName(WhereParser.AtomNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhereParser#atomOp}.
	 * @param ctx the parse tree
	 */
	void enterAtomOp(WhereParser.AtomOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhereParser#atomOp}.
	 * @param ctx the parse tree
	 */
	void exitAtomOp(WhereParser.AtomOpContext ctx) throws GrammarError;
	/**
	 * Enter a parse tree produced by {@link WhereParser#atomList}.
	 * @param ctx the parse tree
	 */
	void enterAtomList(WhereParser.AtomListContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhereParser#atomList}.
	 * @param ctx the parse tree
	 */
	void exitAtomList(WhereParser.AtomListContext ctx);
	/**
	 * Enter a parse tree produced by {@link WhereParser#atomValue}.
	 * @param ctx the parse tree
	 */
	void enterAtomValue(WhereParser.AtomValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link WhereParser#atomValue}.
	 * @param ctx the parse tree
	 */
	void exitAtomValue(WhereParser.AtomValueContext ctx) throws GrammarError;
}