// Generated from /opt/jqs/Where.g4 by ANTLR 4.9.1
package com.jpro.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class WhereParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, TK_QUOTE_LEFT=2, TK_QUOTE_RIGHT=3, TK_OPERATOR=4, TK_VALUE=5, TK_AND=6, 
		TK_OR=7, COMMA=8, OVER=9, QUO_LEFT=10, QUO_RIGHT=11, KEY_OR=12, KEY_AND=13, 
		VALUE=14, KEY_BOOL=15, KEY_TRUE=16, KEY_FALSE=17, KEY_IN=18, KEY_BETWEEN=19, 
		KEY_LIKE=20, VAL_STR=21, VAL_NUM=22, FIELD=23, OP_EQ=24, OP_NEQ=25, OP_GT=26, 
		OP_GTE=27, OP_LT=28, OP_LTE=29, OPERATOR=30;
	public static final int
		RULE_prog = 0, RULE_expr = 1, RULE_unionItem = 2, RULE_exprWithQuote = 3, 
		RULE_exprItem = 4, RULE_atomName = 5, RULE_atomOp = 6, RULE_atomList = 7, 
		RULE_atomValue = 8;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "expr", "unionItem", "exprWithQuote", "exprItem", "atomName", 
			"atomOp", "atomList", "atomValue"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "','", "';'", "'('", 
			"')'", null, null, null, null, null, null, null, null, null, null, null, 
			null, "'='", "'!='", null, "'>='", null, "'<='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "TK_QUOTE_LEFT", "TK_QUOTE_RIGHT", "TK_OPERATOR", "TK_VALUE", 
			"TK_AND", "TK_OR", "COMMA", "OVER", "QUO_LEFT", "QUO_RIGHT", "KEY_OR", 
			"KEY_AND", "VALUE", "KEY_BOOL", "KEY_TRUE", "KEY_FALSE", "KEY_IN", "KEY_BETWEEN", 
			"KEY_LIKE", "VAL_STR", "VAL_NUM", "FIELD", "OP_EQ", "OP_NEQ", "OP_GT", 
			"OP_GTE", "OP_LT", "OP_LTE", "OPERATOR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Where.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public WhereParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode OVER() { return getToken(WhereParser.OVER, 0); }
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).exitProg(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			expr(0);
			setState(19);
			match(OVER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public UnionItemContext unionItem() {
			return getRuleContext(UnionItemContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode TK_OR() { return getToken(WhereParser.TK_OR, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) {
				try {
					((WhereListener)listener).exitExpr(this);
				} catch (GrammarError grammarError) {
					grammarError.printStackTrace();
				}
			}
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(22);
			unionItem(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(29);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_expr);
					setState(24);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(25);
					match(TK_OR);
					setState(26);
					unionItem(0);
					}
					}
				}
				setState(31);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class UnionItemContext extends ParserRuleContext {
		public ExprItemContext exprItem() {
			return getRuleContext(ExprItemContext.class,0);
		}
		public UnionItemContext unionItem() {
			return getRuleContext(UnionItemContext.class,0);
		}
		public TerminalNode TK_AND() { return getToken(WhereParser.TK_AND, 0); }
		public UnionItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).enterUnionItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) {
				try {
					((WhereListener)listener).exitUnionItem(this);
				} catch (GrammarError grammarError) {
					grammarError.printStackTrace();
				}
			}
		}
	}

	public final UnionItemContext unionItem() throws RecognitionException {
		return unionItem(0);
	}

	private UnionItemContext unionItem(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		UnionItemContext _localctx = new UnionItemContext(_ctx, _parentState);
		UnionItemContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_unionItem, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(33);
			exprItem();
			}
			_ctx.stop = _input.LT(-1);
			setState(40);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new UnionItemContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_unionItem);
					setState(35);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(36);
					match(TK_AND);
					setState(37);
					exprItem();
					}
					}
				}
				setState(42);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExprWithQuoteContext extends ParserRuleContext {
		public TerminalNode TK_QUOTE_LEFT() { return getToken(WhereParser.TK_QUOTE_LEFT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode TK_QUOTE_RIGHT() { return getToken(WhereParser.TK_QUOTE_RIGHT, 0); }
		public ExprWithQuoteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprWithQuote; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).enterExprWithQuote(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).exitExprWithQuote(this);
		}
	}

	public final ExprWithQuoteContext exprWithQuote() throws RecognitionException {
		ExprWithQuoteContext _localctx = new ExprWithQuoteContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_exprWithQuote);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			match(TK_QUOTE_LEFT);
			setState(44);
			expr(0);
			setState(45);
			match(TK_QUOTE_RIGHT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprItemContext extends ParserRuleContext {
		public AtomNameContext atomName() {
			return getRuleContext(AtomNameContext.class,0);
		}
		public AtomOpContext atomOp() {
			return getRuleContext(AtomOpContext.class,0);
		}
		public ExprWithQuoteContext exprWithQuote() {
			return getRuleContext(ExprWithQuoteContext.class,0);
		}
		public ExprItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).enterExprItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).exitExprItem(this);
		}
	}

	public final ExprItemContext exprItem() throws RecognitionException {
		ExprItemContext _localctx = new ExprItemContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_exprItem);
		try {
			setState(51);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FIELD:
				enterOuterAlt(_localctx, 1);
				{
				setState(47);
				atomName();
				setState(48);
				atomOp();
				}
				break;
			case TK_QUOTE_LEFT:
				enterOuterAlt(_localctx, 2);
				{
				setState(50);
				exprWithQuote();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomNameContext extends ParserRuleContext {
		public TerminalNode FIELD() { return getToken(WhereParser.FIELD, 0); }
		public AtomNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).enterAtomName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).exitAtomName(this);
		}
	}

	public final AtomNameContext atomName() throws RecognitionException {
		AtomNameContext _localctx = new AtomNameContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_atomName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53);
			match(FIELD);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomOpContext extends ParserRuleContext {
		public TerminalNode TK_OPERATOR() { return getToken(WhereParser.TK_OPERATOR, 0); }
		public List<AtomValueContext> atomValue() {
			return getRuleContexts(AtomValueContext.class);
		}
		public AtomValueContext atomValue(int i) {
			return getRuleContext(AtomValueContext.class,i);
		}
		public TerminalNode KEY_BETWEEN() { return getToken(WhereParser.KEY_BETWEEN, 0); }
		public TerminalNode TK_AND() { return getToken(WhereParser.TK_AND, 0); }
		public TerminalNode KEY_IN() { return getToken(WhereParser.KEY_IN, 0); }
		public TerminalNode TK_QUOTE_LEFT() { return getToken(WhereParser.TK_QUOTE_LEFT, 0); }
		public AtomListContext atomList() {
			return getRuleContext(AtomListContext.class,0);
		}
		public TerminalNode TK_QUOTE_RIGHT() { return getToken(WhereParser.TK_QUOTE_RIGHT, 0); }
		public AtomOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).enterAtomOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) {
				try {
					((WhereListener)listener).exitAtomOp(this);
				} catch (GrammarError grammarError) {
					grammarError.printStackTrace();
				}
			}
		}
	}

	public final AtomOpContext atomOp() throws RecognitionException {
		AtomOpContext _localctx = new AtomOpContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_atomOp);
		try {
			setState(67);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TK_OPERATOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(55);
				match(TK_OPERATOR);
				setState(56);
				atomValue();
				}
				break;
			case KEY_BETWEEN:
				enterOuterAlt(_localctx, 2);
				{
				setState(57);
				match(KEY_BETWEEN);
				setState(58);
				atomValue();
				setState(59);
				match(TK_AND);
				setState(60);
				atomValue();
				}
				break;
			case KEY_IN:
				enterOuterAlt(_localctx, 3);
				{
				setState(62);
				match(KEY_IN);
				setState(63);
				match(TK_QUOTE_LEFT);
				setState(64);
				atomList(0);
				setState(65);
				match(TK_QUOTE_RIGHT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomListContext extends ParserRuleContext {
		public AtomValueContext atomValue() {
			return getRuleContext(AtomValueContext.class,0);
		}
		public AtomListContext atomList() {
			return getRuleContext(AtomListContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(WhereParser.COMMA, 0); }
		public AtomListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).enterAtomList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).exitAtomList(this);
		}
	}

	public final AtomListContext atomList() throws RecognitionException {
		return atomList(0);
	}

	private AtomListContext atomList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		AtomListContext _localctx = new AtomListContext(_ctx, _parentState);
		AtomListContext _prevctx = _localctx;
		int _startState = 14;
		enterRecursionRule(_localctx, 14, RULE_atomList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(70);
			atomValue();
			}
			_ctx.stop = _input.LT(-1);
			setState(77);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new AtomListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_atomList);
					setState(72);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(73);
					match(COMMA);
					setState(74);
					atomValue();
					}
					} 
				}
				setState(79);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class AtomValueContext extends ParserRuleContext {
		public TerminalNode TK_VALUE() { return getToken(WhereParser.TK_VALUE, 0); }
		public AtomValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) ((WhereListener)listener).enterAtomValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof WhereListener ) {
				try {
					((WhereListener)listener).exitAtomValue(this);
				} catch (GrammarError grammarError) {
					grammarError.printStackTrace();
				}
			}
		}
	}

	public final AtomValueContext atomValue() throws RecognitionException {
		AtomValueContext _localctx = new AtomValueContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_atomValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			match(TK_VALUE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return expr_sempred((ExprContext)_localctx, predIndex);
		case 2:
			return unionItem_sempred((UnionItemContext)_localctx, predIndex);
		case 7:
			return atomList_sempred((AtomListContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean unionItem_sempred(UnionItemContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean atomList_sempred(AtomListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3 U\4\2\t\2\4\3\t\3"+
		"\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\7\3\36\n\3\f\3\16\3!\13\3\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\7\4)\n\4\f\4\16\4,\13\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\5\6\66\n\6"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bF\n\b\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\7\tN\n\t\f\t\16\tQ\13\t\3\n\3\n\3\n\2\5\4\6\20\13"+
		"\2\4\6\b\n\f\16\20\22\2\2\2Q\2\24\3\2\2\2\4\27\3\2\2\2\6\"\3\2\2\2\b-"+
		"\3\2\2\2\n\65\3\2\2\2\f\67\3\2\2\2\16E\3\2\2\2\20G\3\2\2\2\22R\3\2\2\2"+
		"\24\25\5\4\3\2\25\26\7\13\2\2\26\3\3\2\2\2\27\30\b\3\1\2\30\31\5\6\4\2"+
		"\31\37\3\2\2\2\32\33\f\4\2\2\33\34\7\t\2\2\34\36\5\6\4\2\35\32\3\2\2\2"+
		"\36!\3\2\2\2\37\35\3\2\2\2\37 \3\2\2\2 \5\3\2\2\2!\37\3\2\2\2\"#\b\4\1"+
		"\2#$\5\n\6\2$*\3\2\2\2%&\f\4\2\2&\'\7\b\2\2\')\5\n\6\2(%\3\2\2\2),\3\2"+
		"\2\2*(\3\2\2\2*+\3\2\2\2+\7\3\2\2\2,*\3\2\2\2-.\7\4\2\2./\5\4\3\2/\60"+
		"\7\5\2\2\60\t\3\2\2\2\61\62\5\f\7\2\62\63\5\16\b\2\63\66\3\2\2\2\64\66"+
		"\5\b\5\2\65\61\3\2\2\2\65\64\3\2\2\2\66\13\3\2\2\2\678\7\31\2\28\r\3\2"+
		"\2\29:\7\6\2\2:F\5\22\n\2;<\7\25\2\2<=\5\22\n\2=>\7\b\2\2>?\5\22\n\2?"+
		"F\3\2\2\2@A\7\24\2\2AB\7\4\2\2BC\5\20\t\2CD\7\5\2\2DF\3\2\2\2E9\3\2\2"+
		"\2E;\3\2\2\2E@\3\2\2\2F\17\3\2\2\2GH\b\t\1\2HI\5\22\n\2IO\3\2\2\2JK\f"+
		"\4\2\2KL\7\n\2\2LN\5\22\n\2MJ\3\2\2\2NQ\3\2\2\2OM\3\2\2\2OP\3\2\2\2P\21"+
		"\3\2\2\2QO\3\2\2\2RS\7\7\2\2S\23\3\2\2\2\7\37*\65EO";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}