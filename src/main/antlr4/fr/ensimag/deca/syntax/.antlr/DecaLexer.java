// Generated from /user/3/telliere/Documents/P3/Projet_GL/Projet_GL/src/main/antlr4/fr/ensimag/deca/syntax/DecaLexer.g4 by ANTLR 4.9.2
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
		PRINTLN=9, IDENT=10, INT=11, FLOAT=12, STRING=13, SPACE=14, COMMENTS=15;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"OBRACE", "CBRACE", "SEMI", "COMMA", "EQUALS", "PRINT", "OPARENT", "CPARENT", 
			"PRINTLN", "DIGIT", "LETTER", "IDENT", "POSITIVE_DIGIT", "INT", "NUM", 
			"SIGN", "EXP", "FLOATSUF", "DEC", "FLOATDEC", "HEXLETTER", "DIGITHEX", 
			"HEXPREF", "NUMHEX", "FLOATHEX", "FLOAT", "QUOTES", "SINGLE_QUOTE", "STRING", 
			"SPACE", "LINE_COMMENT", "MULTI_COMMENT", "COMMENTS"
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
			"CPARENT", "PRINTLN", "IDENT", "INT", "FLOAT", "STRING", "SPACE", "COMMENTS"
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
		case 29:
			SPACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 32:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\21\u00ef\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3"+
		"\f\3\f\3\r\3\r\5\rh\n\r\3\r\3\r\3\r\7\rm\n\r\f\r\16\rp\13\r\3\16\3\16"+
		"\3\17\3\17\3\17\7\17w\n\17\f\17\16\17z\13\17\5\17|\n\17\3\20\6\20\177"+
		"\n\20\r\20\16\20\u0080\3\21\5\21\u0084\n\21\3\22\3\22\3\22\3\22\3\23\5"+
		"\23\u008b\n\23\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\5\25\u0095\n\25"+
		"\3\25\3\25\3\26\3\26\3\27\3\27\5\27\u009d\n\27\3\30\3\30\3\30\3\30\5\30"+
		"\u00a3\n\30\3\31\6\31\u00a6\n\31\r\31\16\31\u00a7\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\33\3\33\5\33\u00b5\n\33\3\34\3\34\3\35\3\35"+
		"\3\36\3\36\7\36\u00bd\n\36\f\36\16\36\u00c0\13\36\3\36\3\36\3\36\3\36"+
		"\7\36\u00c6\n\36\f\36\16\36\u00c9\13\36\3\36\3\36\5\36\u00cd\n\36\3\37"+
		"\3\37\3\37\3 \3 \3 \3 \3 \7 \u00d7\n \f \16 \u00da\13 \3 \3 \3!\3!\3!"+
		"\3!\7!\u00e2\n!\f!\16!\u00e5\13!\3!\3!\3!\3\"\3\"\5\"\u00ec\n\"\3\"\3"+
		"\"\6\u00be\u00c7\u00d8\u00e3\2#\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\2\27\2\31\f\33\2\35\r\37\2!\2#\2%\2\'\2)\2+\2-\2/\2\61\2\63\2\65"+
		"\16\67\29\2;\17=\20?\2A\2C\21\3\2\n\4\2C\\c|\4\2&&aa\4\2--//\4\2GGgg\4"+
		"\2HHhh\4\2CHch\4\2RRrr\5\2\13\f\17\17\"\"\2\u00f1\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\31\3\2\2\2\2\35\3\2\2\2\2\65\3\2\2\2\2;\3\2\2"+
		"\2\2=\3\2\2\2\2C\3\2\2\2\3E\3\2\2\2\5G\3\2\2\2\7I\3\2\2\2\tK\3\2\2\2\13"+
		"M\3\2\2\2\rO\3\2\2\2\17U\3\2\2\2\21W\3\2\2\2\23Y\3\2\2\2\25a\3\2\2\2\27"+
		"c\3\2\2\2\31g\3\2\2\2\33q\3\2\2\2\35{\3\2\2\2\37~\3\2\2\2!\u0083\3\2\2"+
		"\2#\u0085\3\2\2\2%\u008a\3\2\2\2\'\u008c\3\2\2\2)\u0094\3\2\2\2+\u0098"+
		"\3\2\2\2-\u009c\3\2\2\2/\u00a2\3\2\2\2\61\u00a5\3\2\2\2\63\u00a9\3\2\2"+
		"\2\65\u00b4\3\2\2\2\67\u00b6\3\2\2\29\u00b8\3\2\2\2;\u00cc\3\2\2\2=\u00ce"+
		"\3\2\2\2?\u00d1\3\2\2\2A\u00dd\3\2\2\2C\u00eb\3\2\2\2EF\7}\2\2F\4\3\2"+
		"\2\2GH\7\177\2\2H\6\3\2\2\2IJ\7=\2\2J\b\3\2\2\2KL\7.\2\2L\n\3\2\2\2MN"+
		"\7?\2\2N\f\3\2\2\2OP\7r\2\2PQ\7t\2\2QR\7k\2\2RS\7p\2\2ST\7v\2\2T\16\3"+
		"\2\2\2UV\7*\2\2V\20\3\2\2\2WX\7+\2\2X\22\3\2\2\2YZ\7r\2\2Z[\7t\2\2[\\"+
		"\7k\2\2\\]\7p\2\2]^\7v\2\2^_\7n\2\2_`\7p\2\2`\24\3\2\2\2ab\4\62;\2b\26"+
		"\3\2\2\2cd\t\2\2\2d\30\3\2\2\2eh\5\27\f\2fh\t\3\2\2ge\3\2\2\2gf\3\2\2"+
		"\2hn\3\2\2\2im\5\27\f\2jm\t\3\2\2km\5\25\13\2li\3\2\2\2lj\3\2\2\2lk\3"+
		"\2\2\2mp\3\2\2\2nl\3\2\2\2no\3\2\2\2o\32\3\2\2\2pn\3\2\2\2qr\4\63;\2r"+
		"\34\3\2\2\2s|\7\62\2\2tx\5\33\16\2uw\5\25\13\2vu\3\2\2\2wz\3\2\2\2xv\3"+
		"\2\2\2xy\3\2\2\2y|\3\2\2\2zx\3\2\2\2{s\3\2\2\2{t\3\2\2\2|\36\3\2\2\2}"+
		"\177\5\25\13\2~}\3\2\2\2\177\u0080\3\2\2\2\u0080~\3\2\2\2\u0080\u0081"+
		"\3\2\2\2\u0081 \3\2\2\2\u0082\u0084\t\4\2\2\u0083\u0082\3\2\2\2\u0083"+
		"\u0084\3\2\2\2\u0084\"\3\2\2\2\u0085\u0086\t\5\2\2\u0086\u0087\5!\21\2"+
		"\u0087\u0088\5\37\20\2\u0088$\3\2\2\2\u0089\u008b\t\6\2\2\u008a\u0089"+
		"\3\2\2\2\u008a\u008b\3\2\2\2\u008b&\3\2\2\2\u008c\u008d\5\37\20\2\u008d"+
		"\u008e\7\60\2\2\u008e\u008f\5\37\20\2\u008f(\3\2\2\2\u0090\u0095\5\'\24"+
		"\2\u0091\u0092\5\'\24\2\u0092\u0093\5#\22\2\u0093\u0095\3\2\2\2\u0094"+
		"\u0090\3\2\2\2\u0094\u0091\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0097\5%"+
		"\23\2\u0097*\3\2\2\2\u0098\u0099\t\7\2\2\u0099,\3\2\2\2\u009a\u009d\5"+
		"\25\13\2\u009b\u009d\5+\26\2\u009c\u009a\3\2\2\2\u009c\u009b\3\2\2\2\u009d"+
		".\3\2\2\2\u009e\u009f\7\62\2\2\u009f\u00a3\7z\2\2\u00a0\u00a1\7\62\2\2"+
		"\u00a1\u00a3\7Z\2\2\u00a2\u009e\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a3\60\3"+
		"\2\2\2\u00a4\u00a6\5-\27\2\u00a5\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7"+
		"\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8\62\3\2\2\2\u00a9\u00aa\5/\30"+
		"\2\u00aa\u00ab\5\61\31\2\u00ab\u00ac\7\60\2\2\u00ac\u00ad\5\61\31\2\u00ad"+
		"\u00ae\t\b\2\2\u00ae\u00af\5!\21\2\u00af\u00b0\5\37\20\2\u00b0\u00b1\5"+
		"%\23\2\u00b1\64\3\2\2\2\u00b2\u00b5\5\63\32\2\u00b3\u00b5\5)\25\2\u00b4"+
		"\u00b2\3\2\2\2\u00b4\u00b3\3\2\2\2\u00b5\66\3\2\2\2\u00b6\u00b7\7$\2\2"+
		"\u00b78\3\2\2\2\u00b8\u00b9\7)\2\2\u00b9:\3\2\2\2\u00ba\u00be\5\67\34"+
		"\2\u00bb\u00bd\13\2\2\2\u00bc\u00bb\3\2\2\2\u00bd\u00c0\3\2\2\2\u00be"+
		"\u00bf\3\2\2\2\u00be\u00bc\3\2\2\2\u00bf\u00c1\3\2\2\2\u00c0\u00be\3\2"+
		"\2\2\u00c1\u00c2\5\67\34\2\u00c2\u00cd\3\2\2\2\u00c3\u00c7\59\35\2\u00c4"+
		"\u00c6\13\2\2\2\u00c5\u00c4\3\2\2\2\u00c6\u00c9\3\2\2\2\u00c7\u00c8\3"+
		"\2\2\2\u00c7\u00c5\3\2\2\2\u00c8\u00ca\3\2\2\2\u00c9\u00c7\3\2\2\2\u00ca"+
		"\u00cb\59\35\2\u00cb\u00cd\3\2\2\2\u00cc\u00ba\3\2\2\2\u00cc\u00c3\3\2"+
		"\2\2\u00cd<\3\2\2\2\u00ce\u00cf\t\t\2\2\u00cf\u00d0\b\37\2\2\u00d0>\3"+
		"\2\2\2\u00d1\u00d2\7\61\2\2\u00d2\u00d3\7\61\2\2\u00d3\u00d8\3\2\2\2\u00d4"+
		"\u00d7\13\2\2\2\u00d5\u00d7\7\f\2\2\u00d6\u00d4\3\2\2\2\u00d6\u00d5\3"+
		"\2\2\2\u00d7\u00da\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d9"+
		"\u00db\3\2\2\2\u00da\u00d8\3\2\2\2\u00db\u00dc\7\f\2\2\u00dc@\3\2\2\2"+
		"\u00dd\u00de\7\61\2\2\u00de\u00df\7,\2\2\u00df\u00e3\3\2\2\2\u00e0\u00e2"+
		"\13\2\2\2\u00e1\u00e0\3\2\2\2\u00e2\u00e5\3\2\2\2\u00e3\u00e4\3\2\2\2"+
		"\u00e3\u00e1\3\2\2\2\u00e4\u00e6\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e6\u00e7"+
		"\7,\2\2\u00e7\u00e8\7\61\2\2\u00e8B\3\2\2\2\u00e9\u00ec\5? \2\u00ea\u00ec"+
		"\5A!\2\u00eb\u00e9\3\2\2\2\u00eb\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed"+
		"\u00ee\b\"\3\2\u00eeD\3\2\2\2\27\2glnx{\u0080\u0083\u008a\u0094\u009c"+
		"\u00a2\u00a7\u00b4\u00be\u00c7\u00cc\u00d6\u00d8\u00e3\u00eb\4\3\37\2"+
		"\3\"\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}