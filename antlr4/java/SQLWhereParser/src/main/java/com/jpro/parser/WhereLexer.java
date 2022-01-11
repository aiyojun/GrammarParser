// Generated from /opt/jqs/Where.g4 by ANTLR 4.9.1
package com.jpro.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class WhereLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "TK_QUOTE_LEFT", "TK_QUOTE_RIGHT", "TK_OPERATOR", "TK_VALUE", "TK_AND", 
			"TK_OR", "COMMA", "OVER", "QUO_LEFT", "QUO_RIGHT", "KEY_OR", "KEY_AND", 
			"VALUE", "KEY_BOOL", "KEY_TRUE", "KEY_FALSE", "KEY_IN", "KEY_BETWEEN", 
			"KEY_LIKE", "VAL_STR", "VAL_NUM", "FIELD", "OP_EQ", "OP_NEQ", "OP_GT", 
			"OP_GTE", "OP_LT", "OP_LTE", "OPERATOR"
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


	public WhereLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Where.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2 \u0100\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\3\2\3\2\3"+
		"\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\6\7M\n\7\r\7\16\7N\3\7\3\7"+
		"\3\b\6\bT\n\b\r\b\16\bU\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r"+
		"\3\r\3\r\3\r\5\rf\n\r\3\16\3\16\3\16\3\16\3\16\3\16\5\16n\n\16\3\17\3"+
		"\17\3\17\5\17s\n\17\3\20\3\20\5\20w\n\20\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u0085\n\21\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u0096\n\22\3\23"+
		"\3\23\3\23\3\23\5\23\u009c\n\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u00ac\n\24\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\5\25\u00b6\n\25\3\26\3\26\3\26\3\26\7\26\u00bc\n\26\f"+
		"\26\16\26\u00bf\13\26\3\26\3\26\3\26\3\26\3\26\7\26\u00c6\n\26\f\26\16"+
		"\26\u00c9\13\26\3\26\5\26\u00cc\n\26\3\27\5\27\u00cf\n\27\3\27\6\27\u00d2"+
		"\n\27\r\27\16\27\u00d3\3\27\5\27\u00d7\n\27\3\27\6\27\u00da\n\27\r\27"+
		"\16\27\u00db\3\30\6\30\u00df\n\30\r\30\16\30\u00e0\3\30\7\30\u00e4\n\30"+
		"\f\30\16\30\u00e7\13\30\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3"+
		"\34\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u00ff"+
		"\n\37\2\2 \3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"+
		"\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67"+
		"\359\36;\37= \3\2\13\4\2\13\13\"\"\3\2$$\3\2))\3\2//\3\2\62;\5\2C\\c|"+
		"\u4e02\u9fa7\6\2C\\aac|\u4e02\u9fa7\3\2@@\3\2>>\2\u011e\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2"+
		"\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2"+
		"\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2"+
		"\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2"+
		"\2\3?\3\2\2\2\5C\3\2\2\2\7E\3\2\2\2\tG\3\2\2\2\13I\3\2\2\2\rL\3\2\2\2"+
		"\17S\3\2\2\2\21Y\3\2\2\2\23[\3\2\2\2\25]\3\2\2\2\27_\3\2\2\2\31e\3\2\2"+
		"\2\33m\3\2\2\2\35r\3\2\2\2\37v\3\2\2\2!\u0084\3\2\2\2#\u0095\3\2\2\2%"+
		"\u009b\3\2\2\2\'\u00ab\3\2\2\2)\u00b5\3\2\2\2+\u00cb\3\2\2\2-\u00ce\3"+
		"\2\2\2/\u00de\3\2\2\2\61\u00e8\3\2\2\2\63\u00ea\3\2\2\2\65\u00ed\3\2\2"+
		"\2\67\u00ef\3\2\2\29\u00f2\3\2\2\2;\u00f4\3\2\2\2=\u00fe\3\2\2\2?@\t\2"+
		"\2\2@A\3\2\2\2AB\b\2\2\2B\4\3\2\2\2CD\5\25\13\2D\6\3\2\2\2EF\5\27\f\2"+
		"F\b\3\2\2\2GH\5=\37\2H\n\3\2\2\2IJ\5\35\17\2J\f\3\2\2\2KM\7\"\2\2LK\3"+
		"\2\2\2MN\3\2\2\2NL\3\2\2\2NO\3\2\2\2OP\3\2\2\2PQ\5\33\16\2Q\16\3\2\2\2"+
		"RT\7\"\2\2SR\3\2\2\2TU\3\2\2\2US\3\2\2\2UV\3\2\2\2VW\3\2\2\2WX\5\31\r"+
		"\2X\20\3\2\2\2YZ\7.\2\2Z\22\3\2\2\2[\\\7=\2\2\\\24\3\2\2\2]^\7*\2\2^\26"+
		"\3\2\2\2_`\7+\2\2`\30\3\2\2\2ab\7q\2\2bf\7t\2\2cd\7Q\2\2df\7T\2\2ea\3"+
		"\2\2\2ec\3\2\2\2f\32\3\2\2\2gh\7c\2\2hi\7p\2\2in\7f\2\2jk\7C\2\2kl\7P"+
		"\2\2ln\7F\2\2mg\3\2\2\2mj\3\2\2\2n\34\3\2\2\2os\5+\26\2ps\5-\27\2qs\5"+
		"\37\20\2ro\3\2\2\2rp\3\2\2\2rq\3\2\2\2s\36\3\2\2\2tw\5!\21\2uw\5#\22\2"+
		"vt\3\2\2\2vu\3\2\2\2w \3\2\2\2xy\7v\2\2yz\7t\2\2z{\7w\2\2{\u0085\7g\2"+
		"\2|}\7V\2\2}~\7t\2\2~\177\7w\2\2\177\u0085\7g\2\2\u0080\u0081\7V\2\2\u0081"+
		"\u0082\7T\2\2\u0082\u0083\7W\2\2\u0083\u0085\7G\2\2\u0084x\3\2\2\2\u0084"+
		"|\3\2\2\2\u0084\u0080\3\2\2\2\u0085\"\3\2\2\2\u0086\u0087\7h\2\2\u0087"+
		"\u0088\7c\2\2\u0088\u0089\7n\2\2\u0089\u008a\7u\2\2\u008a\u0096\7g\2\2"+
		"\u008b\u008c\7H\2\2\u008c\u008d\7c\2\2\u008d\u008e\7n\2\2\u008e\u008f"+
		"\7u\2\2\u008f\u0096\7g\2\2\u0090\u0091\7H\2\2\u0091\u0092\7C\2\2\u0092"+
		"\u0093\7N\2\2\u0093\u0094\7U\2\2\u0094\u0096\7G\2\2\u0095\u0086\3\2\2"+
		"\2\u0095\u008b\3\2\2\2\u0095\u0090\3\2\2\2\u0096$\3\2\2\2\u0097\u0098"+
		"\7k\2\2\u0098\u009c\7p\2\2\u0099\u009a\7K\2\2\u009a\u009c\7P\2\2\u009b"+
		"\u0097\3\2\2\2\u009b\u0099\3\2\2\2\u009c&\3\2\2\2\u009d\u009e\7d\2\2\u009e"+
		"\u009f\7g\2\2\u009f\u00a0\7v\2\2\u00a0\u00a1\7y\2\2\u00a1\u00a2\7g\2\2"+
		"\u00a2\u00a3\7g\2\2\u00a3\u00ac\7p\2\2\u00a4\u00a5\7D\2\2\u00a5\u00a6"+
		"\7G\2\2\u00a6\u00a7\7V\2\2\u00a7\u00a8\7Y\2\2\u00a8\u00a9\7G\2\2\u00a9"+
		"\u00aa\7G\2\2\u00aa\u00ac\7P\2\2\u00ab\u009d\3\2\2\2\u00ab\u00a4\3\2\2"+
		"\2\u00ac(\3\2\2\2\u00ad\u00ae\7n\2\2\u00ae\u00af\7k\2\2\u00af\u00b0\7"+
		"m\2\2\u00b0\u00b6\7g\2\2\u00b1\u00b2\7N\2\2\u00b2\u00b3\7K\2\2\u00b3\u00b4"+
		"\7M\2\2\u00b4\u00b6\7G\2\2\u00b5\u00ad\3\2\2\2\u00b5\u00b1\3\2\2\2\u00b6"+
		"*\3\2\2\2\u00b7\u00bd\7$\2\2\u00b8\u00bc\n\3\2\2\u00b9\u00ba\7^\2\2\u00ba"+
		"\u00bc\7$\2\2\u00bb\u00b8\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bc\u00bf\3\2"+
		"\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00c0\3\2\2\2\u00bf"+
		"\u00bd\3\2\2\2\u00c0\u00cc\7$\2\2\u00c1\u00c7\7)\2\2\u00c2\u00c6\n\4\2"+
		"\2\u00c3\u00c4\7^\2\2\u00c4\u00c6\7)\2\2\u00c5\u00c2\3\2\2\2\u00c5\u00c3"+
		"\3\2\2\2\u00c6\u00c9\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8"+
		"\u00ca\3\2\2\2\u00c9\u00c7\3\2\2\2\u00ca\u00cc\7)\2\2\u00cb\u00b7\3\2"+
		"\2\2\u00cb\u00c1\3\2\2\2\u00cc,\3\2\2\2\u00cd\u00cf\t\5\2\2\u00ce\u00cd"+
		"\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d6\3\2\2\2\u00d0\u00d2\t\6\2\2\u00d1"+
		"\u00d0\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d1\3\2\2\2\u00d3\u00d4\3\2"+
		"\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d7\7\60\2\2\u00d6\u00d1\3\2\2\2\u00d6"+
		"\u00d7\3\2\2\2\u00d7\u00d9\3\2\2\2\u00d8\u00da\t\6\2\2\u00d9\u00d8\3\2"+
		"\2\2\u00da\u00db\3\2\2\2\u00db\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc"+
		".\3\2\2\2\u00dd\u00df\t\7\2\2\u00de\u00dd\3\2\2\2\u00df\u00e0\3\2\2\2"+
		"\u00e0\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e5\3\2\2\2\u00e2\u00e4"+
		"\t\b\2\2\u00e3\u00e2\3\2\2\2\u00e4\u00e7\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e5"+
		"\u00e6\3\2\2\2\u00e6\60\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e8\u00e9\7?\2\2"+
		"\u00e9\62\3\2\2\2\u00ea\u00eb\7#\2\2\u00eb\u00ec\7?\2\2\u00ec\64\3\2\2"+
		"\2\u00ed\u00ee\t\t\2\2\u00ee\66\3\2\2\2\u00ef\u00f0\7@\2\2\u00f0\u00f1"+
		"\7?\2\2\u00f18\3\2\2\2\u00f2\u00f3\t\n\2\2\u00f3:\3\2\2\2\u00f4\u00f5"+
		"\7>\2\2\u00f5\u00f6\7?\2\2\u00f6<\3\2\2\2\u00f7\u00ff\5\61\31\2\u00f8"+
		"\u00ff\5\63\32\2\u00f9\u00ff\5\65\33\2\u00fa\u00ff\5\67\34\2\u00fb\u00ff"+
		"\59\35\2\u00fc\u00ff\5;\36\2\u00fd\u00ff\5)\25\2\u00fe\u00f7\3\2\2\2\u00fe"+
		"\u00f8\3\2\2\2\u00fe\u00f9\3\2\2\2\u00fe\u00fa\3\2\2\2\u00fe\u00fb\3\2"+
		"\2\2\u00fe\u00fc\3\2\2\2\u00fe\u00fd\3\2\2\2\u00ff>\3\2\2\2\32\2NUemr"+
		"v\u0084\u0095\u009b\u00ab\u00b5\u00bb\u00bd\u00c5\u00c7\u00cb\u00ce\u00d3"+
		"\u00d6\u00db\u00e0\u00e5\u00fe\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}