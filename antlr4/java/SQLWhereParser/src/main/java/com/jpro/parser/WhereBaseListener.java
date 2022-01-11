// Generated from /opt/jqs/Where.g4 by ANTLR 4.9.1
package com.jpro.parser;

import com.jpro.ast.filter.And;
import com.jpro.ast.filter.Filter;
import com.jpro.ast.Filters;
import com.jpro.ast.filter.Or;
import com.jpro.ds.Pair;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides an empty implementation of {@link WhereListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public class WhereBaseListener implements WhereListener {
	public void clear() { values.clear(); stack.clear(); key = ""; }

	private final List<Pair<Filters.TYPES, Object>> values = new ArrayList<>(8);

	private final List<Filter> stack = new ArrayList<>(128);

	private String key;

	public Filter getCondition() throws GrammarError {
		if (stack.size() != 1) {
			stack.forEach(System.out::println);
			throw new GrammarError("Parser internal error! " +
					"Fail to compose last condition!");
		}
		return stack.get(0);
	}

	private static Pair<Filters.TYPES, Object> getType(String value) throws GrammarError {
		if (value.charAt(0) == '\'' && value.charAt(value.length() - 1) == '\'') {
			return new Pair<>(Filters.TYPES.STRING, value.substring(1, value.length() - 1));
		} else if (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
			return new Pair<>(Filters.TYPES.STRING, value.substring(1, value.length() - 1));
		} else if (value.equalsIgnoreCase("true")) {
			return new Pair<>(Filters.TYPES.BOOL, true);
		} else if (value.equalsIgnoreCase("false")) {
			return new Pair<>(Filters.TYPES.BOOL, false);
		} else {
			try {
				if (value.contains(".")) {
					double d = Double.parseDouble(value);
					return new Pair<>(Filters.TYPES.DOUBLE, d);
				} else {
					int i = Integer.parseInt(value);
					return new Pair<>(Filters.TYPES.INT, i);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new GrammarError("Parser internal error! " +
						"Unknown type of value: " + value);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterProg(WhereParser.ProgContext ctx) {clear();}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitProg(WhereParser.ProgContext ctx) {}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExpr(WhereParser.ExprContext ctx) {

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExpr(WhereParser.ExprContext ctx) throws GrammarError {
		if (ctx.children.size() == 3 &&
				ctx.children.get(1).getText().trim().equalsIgnoreCase("or")) {
			Filter back  = stack.get(stack.size() - 1);
			Filter front = stack.get(stack.size() - 2);
			if (!(front instanceof Or) && !(back instanceof Or)) {
				stack.remove(stack.size() - 1);
				stack.remove(stack.size() - 1);
				stack.add(Filters.or(new ArrayList<Filter>(2) {{
					add(front);
					add(back);
				}}));
			} else if (front instanceof Or && !(back instanceof Or)) {
				((Or) front).add(back);
				stack.remove(stack.size() - 1);
			} else {
				throw new GrammarError("Parser internal error! No logic to handle ("
						+ front.getClass().getName() + ") and ("
						+  back.getClass().getName() + ")!");
			}
		}
	}

	@Override
	public void enterUnionItem(WhereParser.UnionItemContext ctx) {

	}

	@Override
	public void exitUnionItem(WhereParser.UnionItemContext ctx) throws GrammarError {
		if (ctx.children.size() == 3 &&
				ctx.children.get(1).getText().trim().equalsIgnoreCase("and")) {
			Filter back  = stack.get(stack.size() - 1);
			Filter front = stack.get(stack.size() - 2);
			if (!(front instanceof And) && !(back instanceof And)) {
				stack.remove(stack.size() - 1);
				stack.remove(stack.size() - 1);
				stack.add(Filters.and(new ArrayList<Filter>(2) {{
					add(front);
					add(back);
				}}));
			} else if (front instanceof And && !(back instanceof And)) {
				((And) front).add(back);
				stack.remove(stack.size() - 1);
			} else {
				throw new GrammarError("Parser internal error! No logic to handle ("
						+ front.getClass().getName() + ") and ("
						+  back.getClass().getName() + ")!");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExprWithQuote(WhereParser.ExprWithQuoteContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExprWithQuote(WhereParser.ExprWithQuoteContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExprItem(WhereParser.ExprItemContext ctx) {
		key = ""; values.clear();
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExprItem(WhereParser.ExprItemContext ctx) {}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAtomName(WhereParser.AtomNameContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAtomName(WhereParser.AtomNameContext ctx) {
		key = ctx.getText();
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAtomOp(WhereParser.AtomOpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAtomOp(WhereParser.AtomOpContext ctx) throws GrammarError {
		String s = ctx.getChild(0).getText();
		switch (s.toLowerCase()) {
			case ">":
				stack.add(Filters.gt(key, values.get(0).getValue()));
				break;
			case ">=":
				stack.add(Filters.gte(key, values.get(0).getValue()));
				break;
			case "<":
				stack.add(Filters.lt(key, values.get(0).getValue()));
				break;
			case "<=":
				stack.add(Filters.lte(key, values.get(0).getValue()));
				break;
			case "=":
				stack.add(Filters.eq(key, values.get(0).getValue()));
				break;
			case "!=":
				stack.add(Filters.ne(key, values.get(0).getValue()));
				break;
			case "between":
				stack.add(Filters.between(key, values.get(0).getValue(), values.get(1).getValue()));
				break;
			case "in":
				stack.add(Filters.in(key, values.stream().map(Pair::getValue).collect(Collectors.toList())));
				break;
			default:
				throw new GrammarError("Unknown operator: " + s);
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAtomList(WhereParser.AtomListContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAtomList(WhereParser.AtomListContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAtomValue(WhereParser.AtomValueContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAtomValue(WhereParser.AtomValueContext ctx) throws GrammarError {
		values.add(getType(ctx.getText()));
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitTerminal(TerminalNode node) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitErrorNode(ErrorNode node) { }
}