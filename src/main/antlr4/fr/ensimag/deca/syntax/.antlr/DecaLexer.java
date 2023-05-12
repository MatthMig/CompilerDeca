// Generated from /user/2/.base/pacotteg/home/Projet_GL/src/main/antlr4/fr/ensimag/deca/syntax/DecaLexer.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DecaLexer extends AbstractDecaLexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OBRACE=1, CBRACE=2, SEMI=3, COMMA=4, EQUALS=5, PRINT=6, OPARENT=7, CPARENT=8, 
		PRINTLN=9, IDENT=10, FLOAT=11, INT=12, SPACE=13, COMMENTS=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"OBRACE", "CBRACE", "SEMI", "COMMA", "EQUALS", "PRINT", "OPARENT", "CPARENT", 
			"PRINTLN", "DIGIT", "LETTER", "IDENT", "NUM", "SIGN", "EXP", "FLOATSUF", 
			"DEC", "FLOATDEC", "HEXLETTER", "DIGITHEX", "HEXPREF", "NUMHEX", "FLOATHEX", 
			"FLOAT", "POSITIVE_DIGIT", "INT", "SPACE", "LINE_COMMENT", "MULTI_COMMENT", 
			"COMMENTS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "';'", "','", "'='", "'print'", "'('", "')'", "'println'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "OBRACE", "CBRACE", "SEMI", "COMMA", "EQUALS", "PRINT", "OPARENT", 
			"CPARENT", "PRINTLN", "IDENT", "FLOAT", "INT", "SPACE", "COMMENTS"
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




	public DecaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DecaLexer.g4"; }

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

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 26:
			SPACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 29:
			COMMENTS_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void SPACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:

			      skip();
			   
			break;
		}
	}
	private void COMMENTS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:

			      skip();
			   
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20\u00d1\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\3\2"+
		"\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3"+
		"\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\5\rb"+
		"\n\r\3\r\3\r\3\r\7\rg\n\r\f\r\16\rj\13\r\3\16\6\16m\n\16\r\16\16\16n\3"+
		"\17\5\17r\n\17\3\20\3\20\3\20\3\20\3\21\5\21y\n\21\3\22\3\22\3\22\3\22"+
		"\3\23\3\23\3\23\3\23\5\23\u0083\n\23\3\23\3\23\3\24\3\24\3\25\3\25\5\25"+
		"\u008b\n\25\3\26\3\26\3\26\3\26\5\26\u0091\n\26\3\27\6\27\u0094\n\27\r"+
		"\27\16\27\u0095\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31"+
		"\5\31\u00a3\n\31\3\32\3\32\3\33\3\33\3\33\7\33\u00aa\n\33\f\33\16\33\u00ad"+
		"\13\33\5\33\u00af\n\33\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\7\35\u00b9"+
		"\n\35\f\35\16\35\u00bc\13\35\3\35\3\35\3\36\3\36\3\36\3\36\7\36\u00c4"+
		"\n\36\f\36\16\36\u00c7\13\36\3\36\3\36\3\36\3\37\3\37\5\37\u00ce\n\37"+
		"\3\37\3\37\4\u00ba\u00c5\2 \3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\2\27\2\31\f\33\2\35\2\37\2!\2#\2%\2\'\2)\2+\2-\2/\2\61\r\63\2\65\16\67"+
		"\179\2;\2=\20\3\2\n\4\2C\\c|\4\2&&aa\4\2--//\4\2GGgg\4\2HHhh\4\2CHch\4"+
		"\2RRrr\5\2\13\f\17\17\"\"\2\u00d2\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2"+
		"\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
		"\2\2\2\2\31\3\2\2\2\2\61\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\2=\3\2\2\2"+
		"\3?\3\2\2\2\5A\3\2\2\2\7C\3\2\2\2\tE\3\2\2\2\13G\3\2\2\2\rI\3\2\2\2\17"+
		"O\3\2\2\2\21Q\3\2\2\2\23S\3\2\2\2\25[\3\2\2\2\27]\3\2\2\2\31a\3\2\2\2"+
		"\33l\3\2\2\2\35q\3\2\2\2\37s\3\2\2\2!x\3\2\2\2#z\3\2\2\2%\u0082\3\2\2"+
		"\2\'\u0086\3\2\2\2)\u008a\3\2\2\2+\u0090\3\2\2\2-\u0093\3\2\2\2/\u0097"+
		"\3\2\2\2\61\u00a2\3\2\2\2\63\u00a4\3\2\2\2\65\u00ae\3\2\2\2\67\u00b0\3"+
		"\2\2\29\u00b3\3\2\2\2;\u00bf\3\2\2\2=\u00cd\3\2\2\2?@\7}\2\2@\4\3\2\2"+
		"\2AB\7\177\2\2B\6\3\2\2\2CD\7=\2\2D\b\3\2\2\2EF\7.\2\2F\n\3\2\2\2GH\7"+
		"?\2\2H\f\3\2\2\2IJ\7r\2\2JK\7t\2\2KL\7k\2\2LM\7p\2\2MN\7v\2\2N\16\3\2"+
		"\2\2OP\7*\2\2P\20\3\2\2\2QR\7+\2\2R\22\3\2\2\2ST\7r\2\2TU\7t\2\2UV\7k"+
		"\2\2VW\7p\2\2WX\7v\2\2XY\7n\2\2YZ\7p\2\2Z\24\3\2\2\2[\\\4\62;\2\\\26\3"+
		"\2\2\2]^\t\2\2\2^\30\3\2\2\2_b\5\27\f\2`b\t\3\2\2a_\3\2\2\2a`\3\2\2\2"+
		"bh\3\2\2\2cg\5\27\f\2dg\t\3\2\2eg\5\25\13\2fc\3\2\2\2fd\3\2\2\2fe\3\2"+
		"\2\2gj\3\2\2\2hf\3\2\2\2hi\3\2\2\2i\32\3\2\2\2jh\3\2\2\2km\5\25\13\2l"+
		"k\3\2\2\2mn\3\2\2\2nl\3\2\2\2no\3\2\2\2o\34\3\2\2\2pr\t\4\2\2qp\3\2\2"+
		"\2qr\3\2\2\2r\36\3\2\2\2st\t\5\2\2tu\5\35\17\2uv\5\33\16\2v \3\2\2\2w"+
		"y\t\6\2\2xw\3\2\2\2xy\3\2\2\2y\"\3\2\2\2z{\5\33\16\2{|\7\60\2\2|}\5\33"+
		"\16\2}$\3\2\2\2~\u0083\5#\22\2\177\u0080\5#\22\2\u0080\u0081\5\37\20\2"+
		"\u0081\u0083\3\2\2\2\u0082~\3\2\2\2\u0082\177\3\2\2\2\u0083\u0084\3\2"+
		"\2\2\u0084\u0085\5!\21\2\u0085&\3\2\2\2\u0086\u0087\t\7\2\2\u0087(\3\2"+
		"\2\2\u0088\u008b\5\25\13\2\u0089\u008b\5\'\24\2\u008a\u0088\3\2\2\2\u008a"+
		"\u0089\3\2\2\2\u008b*\3\2\2\2\u008c\u008d\7\62\2\2\u008d\u0091\7z\2\2"+
		"\u008e\u008f\7\62\2\2\u008f\u0091\7Z\2\2\u0090\u008c\3\2\2\2\u0090\u008e"+
		"\3\2\2\2\u0091,\3\2\2\2\u0092\u0094\5)\25\2\u0093\u0092\3\2\2\2\u0094"+
		"\u0095\3\2\2\2\u0095\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096.\3\2\2\2"+
		"\u0097\u0098\5+\26\2\u0098\u0099\5-\27\2\u0099\u009a\7\60\2\2\u009a\u009b"+
		"\5-\27\2\u009b\u009c\t\b\2\2\u009c\u009d\5\35\17\2\u009d\u009e\5\33\16"+
		"\2\u009e\u009f\5!\21\2\u009f\60\3\2\2\2\u00a0\u00a3\5/\30\2\u00a1\u00a3"+
		"\5%\23\2\u00a2\u00a0\3\2\2\2\u00a2\u00a1\3\2\2\2\u00a3\62\3\2\2\2\u00a4"+
		"\u00a5\4\63;\2\u00a5\64\3\2\2\2\u00a6\u00af\7\62\2\2\u00a7\u00ab\5\63"+
		"\32\2\u00a8\u00aa\5\25\13\2\u00a9\u00a8\3\2\2\2\u00aa\u00ad\3\2\2\2\u00ab"+
		"\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00af\3\2\2\2\u00ad\u00ab\3\2"+
		"\2\2\u00ae\u00a6\3\2\2\2\u00ae\u00a7\3\2\2\2\u00af\66\3\2\2\2\u00b0\u00b1"+
		"\t\t\2\2\u00b1\u00b2\b\34\2\2\u00b28\3\2\2\2\u00b3\u00b4\7\61\2\2\u00b4"+
		"\u00b5\7\61\2\2\u00b5\u00ba\3\2\2\2\u00b6\u00b9\13\2\2\2\u00b7\u00b9\7"+
		"\f\2\2\u00b8\u00b6\3\2\2\2\u00b8\u00b7\3\2\2\2\u00b9\u00bc\3\2\2\2\u00ba"+
		"\u00bb\3\2\2\2\u00ba\u00b8\3\2\2\2\u00bb\u00bd\3\2\2\2\u00bc\u00ba\3\2"+
		"\2\2\u00bd\u00be\7\f\2\2\u00be:\3\2\2\2\u00bf\u00c0\7\61\2\2\u00c0\u00c1"+
		"\7,\2\2\u00c1\u00c5\3\2\2\2\u00c2\u00c4\13\2\2\2\u00c3\u00c2\3\2\2\2\u00c4"+
		"\u00c7\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c6\u00c8\3\2"+
		"\2\2\u00c7\u00c5\3\2\2\2\u00c8\u00c9\7,\2\2\u00c9\u00ca\7\61\2\2\u00ca"+
		"<\3\2\2\2\u00cb\u00ce\59\35\2\u00cc\u00ce\5;\36\2\u00cd\u00cb\3\2\2\2"+
		"\u00cd\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0\b\37\3\2\u00d0>\3"+
		"\2\2\2\24\2afhnqx\u0082\u008a\u0090\u0095\u00a2\u00ab\u00ae\u00b8\u00ba"+
		"\u00c5\u00cd\4\3\34\2\3\37\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}