// Generated from com.dongjiaqiang.jvm.dsl.core/JvmDslLexer.g4 by ANTLR 4.9.3
package com.dongjiaqiang.jvm.dsl.core;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JvmDslLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PACKAGE=1, ASSERT=2, BREAK=3, CASE=4, CATCH=5, CLASS=6, CONTINUE=7, DEFAULT=8, 
		DO=9, ELSE=10, FINALLY=11, FOR=12, IF=13, USING=14, JAR=15, AS=16, FROM=17, 
		IMPORT=18, INSTANCEOF=19, NEW=20, RETURN=21, MATCH=22, SYNCHRONIZED=23, 
		THROW=24, THROWS=25, TRY=26, VOLATILE=27, WHILE=28, DEF=29, FLOAT=30, 
		INT=31, LONG=32, DOUBLE=33, BOOL=34, BYTE=35, CHAR=36, STRING=37, LIST=38, 
		ARRAY=39, SET=40, MAP=41, OPTION=42, FUTURE=43, ASYNC=44, INT_LITERAL=45, 
		FLOAT_LITERAL=46, LONG_LITERAL=47, DOUBLE_LITERAL=48, BOOL_LITERAL=49, 
		STRING_LITERAL=50, CHAR_LITERAL=51, NULL_LITERAL=52, IDENTIFIER=53, LPAREN=54, 
		RPAREN=55, LBRACE=56, RBRACE=57, LBRACK=58, RBRACK=59, SEMI=60, COMMA=61, 
		DOT=62, ASSIGN=63, GT=64, LT=65, BANG=66, TILDE=67, QUESTION=68, COLON=69, 
		EQUAL=70, LE=71, GE=72, NOTEQUAL=73, AND=74, OR=75, ADD=76, SUB=77, MUL=78, 
		DIV=79, BITAND=80, BITOR=81, CARET=82, MOD=83, ADD_ASSIGN=84, SUB_ASSIGN=85, 
		MUL_ASSIGN=86, DIV_ASSIGN=87, AND_ASSIGN=88, OR_ASSIGN=89, XOR_ASSIGN=90, 
		MOD_ASSIGN=91, LSHIFT_ASSIGN=92, RSHIFT_ASSIGN=93, URSHIFT_ASSIGN=94, 
		ARROW=95, WS=96, COMMENT=97, LINE_COMMENT=98, ESC=99, ID_LETTER=100;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"PACKAGE", "ASSERT", "BREAK", "CASE", "CATCH", "CLASS", "CONTINUE", "DEFAULT", 
			"DO", "ELSE", "FINALLY", "FOR", "IF", "USING", "JAR", "AS", "FROM", "IMPORT", 
			"INSTANCEOF", "NEW", "RETURN", "MATCH", "SYNCHRONIZED", "THROW", "THROWS", 
			"TRY", "VOLATILE", "WHILE", "DEF", "FLOAT", "INT", "LONG", "DOUBLE", 
			"BOOL", "BYTE", "CHAR", "STRING", "LIST", "ARRAY", "SET", "MAP", "OPTION", 
			"FUTURE", "ASYNC", "INT_LITERAL", "FLOAT_LITERAL", "LONG_LITERAL", "DOUBLE_LITERAL", 
			"BOOL_LITERAL", "STRING_LITERAL", "CHAR_LITERAL", "NULL_LITERAL", "IDENTIFIER", 
			"LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", "SEMI", "COMMA", 
			"DOT", "ASSIGN", "GT", "LT", "BANG", "TILDE", "QUESTION", "COLON", "EQUAL", 
			"LE", "GE", "NOTEQUAL", "AND", "OR", "ADD", "SUB", "MUL", "DIV", "BITAND", 
			"BITOR", "CARET", "MOD", "ADD_ASSIGN", "SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", 
			"AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", "MOD_ASSIGN", "LSHIFT_ASSIGN", 
			"RSHIFT_ASSIGN", "URSHIFT_ASSIGN", "ARROW", "WS", "COMMENT", "LINE_COMMENT", 
			"DIGIT", "ESC", "ID_LETTER"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'package'", "'assert'", "'break'", "'case'", "'catch'", "'class'", 
			"'continue'", "'default'", "'do'", "'else'", "'finally'", "'for'", "'if'", 
			"'using'", "'jar'", "'as'", "'from'", "'import'", "'instanceof'", "'new'", 
			"'return'", "'match'", "'synchronized'", "'throw'", "'throws'", "'try'", 
			"'volatile'", "'while'", "'def'", "'Float'", "'Int'", "'Long'", "'Double'", 
			"'Bool'", "'Byte'", "'Char'", "'String'", "'List'", "'Array'", "'Set'", 
			"'Map'", "'Option'", "'Future'", "'async'", null, null, null, null, null, 
			null, null, "'null'", null, "'('", "')'", "'{'", "'}'", "'['", "']'", 
			"';'", "','", "'.'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", "':'", 
			"'=='", "'<='", "'>='", "'!='", "'&&'", "'||'", "'+'", "'-'", "'*'", 
			"'/'", "'&'", "'|'", "'^'", "'%'", "'+='", "'-='", "'*='", "'/='", "'&='", 
			"'|='", "'^='", "'%='", "'<<='", "'>>='", "'>>>='", "'=>'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PACKAGE", "ASSERT", "BREAK", "CASE", "CATCH", "CLASS", "CONTINUE", 
			"DEFAULT", "DO", "ELSE", "FINALLY", "FOR", "IF", "USING", "JAR", "AS", 
			"FROM", "IMPORT", "INSTANCEOF", "NEW", "RETURN", "MATCH", "SYNCHRONIZED", 
			"THROW", "THROWS", "TRY", "VOLATILE", "WHILE", "DEF", "FLOAT", "INT", 
			"LONG", "DOUBLE", "BOOL", "BYTE", "CHAR", "STRING", "LIST", "ARRAY", 
			"SET", "MAP", "OPTION", "FUTURE", "ASYNC", "INT_LITERAL", "FLOAT_LITERAL", 
			"LONG_LITERAL", "DOUBLE_LITERAL", "BOOL_LITERAL", "STRING_LITERAL", "CHAR_LITERAL", 
			"NULL_LITERAL", "IDENTIFIER", "LPAREN", "RPAREN", "LBRACE", "RBRACE", 
			"LBRACK", "RBRACK", "SEMI", "COMMA", "DOT", "ASSIGN", "GT", "LT", "BANG", 
			"TILDE", "QUESTION", "COLON", "EQUAL", "LE", "GE", "NOTEQUAL", "AND", 
			"OR", "ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", "CARET", "MOD", 
			"ADD_ASSIGN", "SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", "AND_ASSIGN", 
			"OR_ASSIGN", "XOR_ASSIGN", "MOD_ASSIGN", "LSHIFT_ASSIGN", "RSHIFT_ASSIGN", 
			"URSHIFT_ASSIGN", "ARROW", "WS", "COMMENT", "LINE_COMMENT", "ESC", "ID_LETTER"
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


	public JvmDslLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "JvmDslLexer.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2f\u02dc\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5"+
		"\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3"+
		"\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16"+
		"\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33"+
		"\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 "+
		"\3 \3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3$\3$\3"+
		"$\3$\3$\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\3("+
		"\3(\3(\3(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,"+
		"\3,\3,\3,\3-\3-\3-\3-\3-\3-\3.\6.\u01d6\n.\r.\16.\u01d7\3/\6/\u01db\n"+
		"/\r/\16/\u01dc\3/\3/\7/\u01e1\n/\f/\16/\u01e4\13/\3/\3/\3/\3/\6/\u01ea"+
		"\n/\r/\16/\u01eb\3/\3/\3/\6/\u01f1\n/\r/\16/\u01f2\3/\3/\5/\u01f7\n/\3"+
		"\60\6\60\u01fa\n\60\r\60\16\60\u01fb\3\60\3\60\3\61\6\61\u0201\n\61\r"+
		"\61\16\61\u0202\3\61\3\61\7\61\u0207\n\61\f\61\16\61\u020a\13\61\3\61"+
		"\5\61\u020d\n\61\3\61\3\61\6\61\u0211\n\61\r\61\16\61\u0212\3\61\5\61"+
		"\u0216\n\61\3\61\6\61\u0219\n\61\r\61\16\61\u021a\3\61\5\61\u021e\n\61"+
		"\5\61\u0220\n\61\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\5\62\u022b"+
		"\n\62\3\63\3\63\3\63\7\63\u0230\n\63\f\63\16\63\u0233\13\63\3\63\3\63"+
		"\3\64\3\64\3\64\5\64\u023a\n\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65\3\66"+
		"\3\66\3\66\7\66\u0246\n\66\f\66\16\66\u0249\13\66\3\67\3\67\38\38\39\3"+
		"9\3:\3:\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?\3@\3@\3A\3A\3B\3B\3C\3C\3D\3D\3"+
		"E\3E\3F\3F\3G\3G\3G\3H\3H\3H\3I\3I\3I\3J\3J\3J\3K\3K\3K\3L\3L\3L\3M\3"+
		"M\3N\3N\3O\3O\3P\3P\3Q\3Q\3R\3R\3S\3S\3T\3T\3U\3U\3U\3V\3V\3V\3W\3W\3"+
		"W\3X\3X\3X\3Y\3Y\3Y\3Z\3Z\3Z\3[\3[\3[\3\\\3\\\3\\\3]\3]\3]\3]\3^\3^\3"+
		"^\3^\3_\3_\3_\3_\3_\3`\3`\3`\3a\6a\u02b6\na\ra\16a\u02b7\3a\3a\3b\3b\3"+
		"b\3b\7b\u02c0\nb\fb\16b\u02c3\13b\3b\3b\3b\3b\3b\3c\3c\3c\3c\7c\u02ce"+
		"\nc\fc\16c\u02d1\13c\3c\3c\3d\3d\3e\3e\3e\3f\5f\u02db\nf\4\u0231\u02c1"+
		"\2g\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36"+
		";\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67"+
		"m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083C\u0085D\u0087E\u0089F\u008bG\u008d"+
		"H\u008fI\u0091J\u0093K\u0095L\u0097M\u0099N\u009bO\u009dP\u009fQ\u00a1"+
		"R\u00a3S\u00a5T\u00a7U\u00a9V\u00abW\u00adX\u00afY\u00b1Z\u00b3[\u00b5"+
		"\\\u00b7]\u00b9^\u00bb_\u00bd`\u00bfa\u00c1b\u00c3c\u00c5d\u00c7\2\u00c9"+
		"e\u00cbf\3\2\f\4\2HHhh\4\2NNnn\4\2FFff\4\2$$^^\4\2))^^\5\2\13\f\16\17"+
		"\"\"\4\2\f\f\17\17\3\2\62;\t\2$$))^^ddppttvv\5\2C\\aac|\2\u02f5\2\3\3"+
		"\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2"+
		"\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3"+
		"\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2"+
		"%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61"+
		"\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2"+
		"\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I"+
		"\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2"+
		"\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2"+
		"\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o"+
		"\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2"+
		"\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085"+
		"\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2"+
		"\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097"+
		"\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2"+
		"\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9"+
		"\3\2\2\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af\3\2\2\2\2\u00b1\3\2\2"+
		"\2\2\u00b3\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2\2\2\u00b9\3\2\2\2\2\u00bb"+
		"\3\2\2\2\2\u00bd\3\2\2\2\2\u00bf\3\2\2\2\2\u00c1\3\2\2\2\2\u00c3\3\2\2"+
		"\2\2\u00c5\3\2\2\2\2\u00c9\3\2\2\2\2\u00cb\3\2\2\2\3\u00cd\3\2\2\2\5\u00d5"+
		"\3\2\2\2\7\u00dc\3\2\2\2\t\u00e2\3\2\2\2\13\u00e7\3\2\2\2\r\u00ed\3\2"+
		"\2\2\17\u00f3\3\2\2\2\21\u00fc\3\2\2\2\23\u0104\3\2\2\2\25\u0107\3\2\2"+
		"\2\27\u010c\3\2\2\2\31\u0114\3\2\2\2\33\u0118\3\2\2\2\35\u011b\3\2\2\2"+
		"\37\u0121\3\2\2\2!\u0125\3\2\2\2#\u0128\3\2\2\2%\u012d\3\2\2\2\'\u0134"+
		"\3\2\2\2)\u013f\3\2\2\2+\u0143\3\2\2\2-\u014a\3\2\2\2/\u0150\3\2\2\2\61"+
		"\u015d\3\2\2\2\63\u0163\3\2\2\2\65\u016a\3\2\2\2\67\u016e\3\2\2\29\u0177"+
		"\3\2\2\2;\u017d\3\2\2\2=\u0181\3\2\2\2?\u0187\3\2\2\2A\u018b\3\2\2\2C"+
		"\u0190\3\2\2\2E\u0197\3\2\2\2G\u019c\3\2\2\2I\u01a1\3\2\2\2K\u01a6\3\2"+
		"\2\2M\u01ad\3\2\2\2O\u01b2\3\2\2\2Q\u01b8\3\2\2\2S\u01bc\3\2\2\2U\u01c0"+
		"\3\2\2\2W\u01c7\3\2\2\2Y\u01ce\3\2\2\2[\u01d5\3\2\2\2]\u01f6\3\2\2\2_"+
		"\u01f9\3\2\2\2a\u021f\3\2\2\2c\u022a\3\2\2\2e\u022c\3\2\2\2g\u0236\3\2"+
		"\2\2i\u023d\3\2\2\2k\u0242\3\2\2\2m\u024a\3\2\2\2o\u024c\3\2\2\2q\u024e"+
		"\3\2\2\2s\u0250\3\2\2\2u\u0252\3\2\2\2w\u0254\3\2\2\2y\u0256\3\2\2\2{"+
		"\u0258\3\2\2\2}\u025a\3\2\2\2\177\u025c\3\2\2\2\u0081\u025e\3\2\2\2\u0083"+
		"\u0260\3\2\2\2\u0085\u0262\3\2\2\2\u0087\u0264\3\2\2\2\u0089\u0266\3\2"+
		"\2\2\u008b\u0268\3\2\2\2\u008d\u026a\3\2\2\2\u008f\u026d\3\2\2\2\u0091"+
		"\u0270\3\2\2\2\u0093\u0273\3\2\2\2\u0095\u0276\3\2\2\2\u0097\u0279\3\2"+
		"\2\2\u0099\u027c\3\2\2\2\u009b\u027e\3\2\2\2\u009d\u0280\3\2\2\2\u009f"+
		"\u0282\3\2\2\2\u00a1\u0284\3\2\2\2\u00a3\u0286\3\2\2\2\u00a5\u0288\3\2"+
		"\2\2\u00a7\u028a\3\2\2\2\u00a9\u028c\3\2\2\2\u00ab\u028f\3\2\2\2\u00ad"+
		"\u0292\3\2\2\2\u00af\u0295\3\2\2\2\u00b1\u0298\3\2\2\2\u00b3\u029b\3\2"+
		"\2\2\u00b5\u029e\3\2\2\2\u00b7\u02a1\3\2\2\2\u00b9\u02a4\3\2\2\2\u00bb"+
		"\u02a8\3\2\2\2\u00bd\u02ac\3\2\2\2\u00bf\u02b1\3\2\2\2\u00c1\u02b5\3\2"+
		"\2\2\u00c3\u02bb\3\2\2\2\u00c5\u02c9\3\2\2\2\u00c7\u02d4\3\2\2\2\u00c9"+
		"\u02d6\3\2\2\2\u00cb\u02da\3\2\2\2\u00cd\u00ce\7r\2\2\u00ce\u00cf\7c\2"+
		"\2\u00cf\u00d0\7e\2\2\u00d0\u00d1\7m\2\2\u00d1\u00d2\7c\2\2\u00d2\u00d3"+
		"\7i\2\2\u00d3\u00d4\7g\2\2\u00d4\4\3\2\2\2\u00d5\u00d6\7c\2\2\u00d6\u00d7"+
		"\7u\2\2\u00d7\u00d8\7u\2\2\u00d8\u00d9\7g\2\2\u00d9\u00da\7t\2\2\u00da"+
		"\u00db\7v\2\2\u00db\6\3\2\2\2\u00dc\u00dd\7d\2\2\u00dd\u00de\7t\2\2\u00de"+
		"\u00df\7g\2\2\u00df\u00e0\7c\2\2\u00e0\u00e1\7m\2\2\u00e1\b\3\2\2\2\u00e2"+
		"\u00e3\7e\2\2\u00e3\u00e4\7c\2\2\u00e4\u00e5\7u\2\2\u00e5\u00e6\7g\2\2"+
		"\u00e6\n\3\2\2\2\u00e7\u00e8\7e\2\2\u00e8\u00e9\7c\2\2\u00e9\u00ea\7v"+
		"\2\2\u00ea\u00eb\7e\2\2\u00eb\u00ec\7j\2\2\u00ec\f\3\2\2\2\u00ed\u00ee"+
		"\7e\2\2\u00ee\u00ef\7n\2\2\u00ef\u00f0\7c\2\2\u00f0\u00f1\7u\2\2\u00f1"+
		"\u00f2\7u\2\2\u00f2\16\3\2\2\2\u00f3\u00f4\7e\2\2\u00f4\u00f5\7q\2\2\u00f5"+
		"\u00f6\7p\2\2\u00f6\u00f7\7v\2\2\u00f7\u00f8\7k\2\2\u00f8\u00f9\7p\2\2"+
		"\u00f9\u00fa\7w\2\2\u00fa\u00fb\7g\2\2\u00fb\20\3\2\2\2\u00fc\u00fd\7"+
		"f\2\2\u00fd\u00fe\7g\2\2\u00fe\u00ff\7h\2\2\u00ff\u0100\7c\2\2\u0100\u0101"+
		"\7w\2\2\u0101\u0102\7n\2\2\u0102\u0103\7v\2\2\u0103\22\3\2\2\2\u0104\u0105"+
		"\7f\2\2\u0105\u0106\7q\2\2\u0106\24\3\2\2\2\u0107\u0108\7g\2\2\u0108\u0109"+
		"\7n\2\2\u0109\u010a\7u\2\2\u010a\u010b\7g\2\2\u010b\26\3\2\2\2\u010c\u010d"+
		"\7h\2\2\u010d\u010e\7k\2\2\u010e\u010f\7p\2\2\u010f\u0110\7c\2\2\u0110"+
		"\u0111\7n\2\2\u0111\u0112\7n\2\2\u0112\u0113\7{\2\2\u0113\30\3\2\2\2\u0114"+
		"\u0115\7h\2\2\u0115\u0116\7q\2\2\u0116\u0117\7t\2\2\u0117\32\3\2\2\2\u0118"+
		"\u0119\7k\2\2\u0119\u011a\7h\2\2\u011a\34\3\2\2\2\u011b\u011c\7w\2\2\u011c"+
		"\u011d\7u\2\2\u011d\u011e\7k\2\2\u011e\u011f\7p\2\2\u011f\u0120\7i\2\2"+
		"\u0120\36\3\2\2\2\u0121\u0122\7l\2\2\u0122\u0123\7c\2\2\u0123\u0124\7"+
		"t\2\2\u0124 \3\2\2\2\u0125\u0126\7c\2\2\u0126\u0127\7u\2\2\u0127\"\3\2"+
		"\2\2\u0128\u0129\7h\2\2\u0129\u012a\7t\2\2\u012a\u012b\7q\2\2\u012b\u012c"+
		"\7o\2\2\u012c$\3\2\2\2\u012d\u012e\7k\2\2\u012e\u012f\7o\2\2\u012f\u0130"+
		"\7r\2\2\u0130\u0131\7q\2\2\u0131\u0132\7t\2\2\u0132\u0133\7v\2\2\u0133"+
		"&\3\2\2\2\u0134\u0135\7k\2\2\u0135\u0136\7p\2\2\u0136\u0137\7u\2\2\u0137"+
		"\u0138\7v\2\2\u0138\u0139\7c\2\2\u0139\u013a\7p\2\2\u013a\u013b\7e\2\2"+
		"\u013b\u013c\7g\2\2\u013c\u013d\7q\2\2\u013d\u013e\7h\2\2\u013e(\3\2\2"+
		"\2\u013f\u0140\7p\2\2\u0140\u0141\7g\2\2\u0141\u0142\7y\2\2\u0142*\3\2"+
		"\2\2\u0143\u0144\7t\2\2\u0144\u0145\7g\2\2\u0145\u0146\7v\2\2\u0146\u0147"+
		"\7w\2\2\u0147\u0148\7t\2\2\u0148\u0149\7p\2\2\u0149,\3\2\2\2\u014a\u014b"+
		"\7o\2\2\u014b\u014c\7c\2\2\u014c\u014d\7v\2\2\u014d\u014e\7e\2\2\u014e"+
		"\u014f\7j\2\2\u014f.\3\2\2\2\u0150\u0151\7u\2\2\u0151\u0152\7{\2\2\u0152"+
		"\u0153\7p\2\2\u0153\u0154\7e\2\2\u0154\u0155\7j\2\2\u0155\u0156\7t\2\2"+
		"\u0156\u0157\7q\2\2\u0157\u0158\7p\2\2\u0158\u0159\7k\2\2\u0159\u015a"+
		"\7|\2\2\u015a\u015b\7g\2\2\u015b\u015c\7f\2\2\u015c\60\3\2\2\2\u015d\u015e"+
		"\7v\2\2\u015e\u015f\7j\2\2\u015f\u0160\7t\2\2\u0160\u0161\7q\2\2\u0161"+
		"\u0162\7y\2\2\u0162\62\3\2\2\2\u0163\u0164\7v\2\2\u0164\u0165\7j\2\2\u0165"+
		"\u0166\7t\2\2\u0166\u0167\7q\2\2\u0167\u0168\7y\2\2\u0168\u0169\7u\2\2"+
		"\u0169\64\3\2\2\2\u016a\u016b\7v\2\2\u016b\u016c\7t\2\2\u016c\u016d\7"+
		"{\2\2\u016d\66\3\2\2\2\u016e\u016f\7x\2\2\u016f\u0170\7q\2\2\u0170\u0171"+
		"\7n\2\2\u0171\u0172\7c\2\2\u0172\u0173\7v\2\2\u0173\u0174\7k\2\2\u0174"+
		"\u0175\7n\2\2\u0175\u0176\7g\2\2\u01768\3\2\2\2\u0177\u0178\7y\2\2\u0178"+
		"\u0179\7j\2\2\u0179\u017a\7k\2\2\u017a\u017b\7n\2\2\u017b\u017c\7g\2\2"+
		"\u017c:\3\2\2\2\u017d\u017e\7f\2\2\u017e\u017f\7g\2\2\u017f\u0180\7h\2"+
		"\2\u0180<\3\2\2\2\u0181\u0182\7H\2\2\u0182\u0183\7n\2\2\u0183\u0184\7"+
		"q\2\2\u0184\u0185\7c\2\2\u0185\u0186\7v\2\2\u0186>\3\2\2\2\u0187\u0188"+
		"\7K\2\2\u0188\u0189\7p\2\2\u0189\u018a\7v\2\2\u018a@\3\2\2\2\u018b\u018c"+
		"\7N\2\2\u018c\u018d\7q\2\2\u018d\u018e\7p\2\2\u018e\u018f\7i\2\2\u018f"+
		"B\3\2\2\2\u0190\u0191\7F\2\2\u0191\u0192\7q\2\2\u0192\u0193\7w\2\2\u0193"+
		"\u0194\7d\2\2\u0194\u0195\7n\2\2\u0195\u0196\7g\2\2\u0196D\3\2\2\2\u0197"+
		"\u0198\7D\2\2\u0198\u0199\7q\2\2\u0199\u019a\7q\2\2\u019a\u019b\7n\2\2"+
		"\u019bF\3\2\2\2\u019c\u019d\7D\2\2\u019d\u019e\7{\2\2\u019e\u019f\7v\2"+
		"\2\u019f\u01a0\7g\2\2\u01a0H\3\2\2\2\u01a1\u01a2\7E\2\2\u01a2\u01a3\7"+
		"j\2\2\u01a3\u01a4\7c\2\2\u01a4\u01a5\7t\2\2\u01a5J\3\2\2\2\u01a6\u01a7"+
		"\7U\2\2\u01a7\u01a8\7v\2\2\u01a8\u01a9\7t\2\2\u01a9\u01aa\7k\2\2\u01aa"+
		"\u01ab\7p\2\2\u01ab\u01ac\7i\2\2\u01acL\3\2\2\2\u01ad\u01ae\7N\2\2\u01ae"+
		"\u01af\7k\2\2\u01af\u01b0\7u\2\2\u01b0\u01b1\7v\2\2\u01b1N\3\2\2\2\u01b2"+
		"\u01b3\7C\2\2\u01b3\u01b4\7t\2\2\u01b4\u01b5\7t\2\2\u01b5\u01b6\7c\2\2"+
		"\u01b6\u01b7\7{\2\2\u01b7P\3\2\2\2\u01b8\u01b9\7U\2\2\u01b9\u01ba\7g\2"+
		"\2\u01ba\u01bb\7v\2\2\u01bbR\3\2\2\2\u01bc\u01bd\7O\2\2\u01bd\u01be\7"+
		"c\2\2\u01be\u01bf\7r\2\2\u01bfT\3\2\2\2\u01c0\u01c1\7Q\2\2\u01c1\u01c2"+
		"\7r\2\2\u01c2\u01c3\7v\2\2\u01c3\u01c4\7k\2\2\u01c4\u01c5\7q\2\2\u01c5"+
		"\u01c6\7p\2\2\u01c6V\3\2\2\2\u01c7\u01c8\7H\2\2\u01c8\u01c9\7w\2\2\u01c9"+
		"\u01ca\7v\2\2\u01ca\u01cb\7w\2\2\u01cb\u01cc\7t\2\2\u01cc\u01cd\7g\2\2"+
		"\u01cdX\3\2\2\2\u01ce\u01cf\7c\2\2\u01cf\u01d0\7u\2\2\u01d0\u01d1\7{\2"+
		"\2\u01d1\u01d2\7p\2\2\u01d2\u01d3\7e\2\2\u01d3Z\3\2\2\2\u01d4\u01d6\5"+
		"\u00c7d\2\u01d5\u01d4\3\2\2\2\u01d6\u01d7\3\2\2\2\u01d7\u01d5\3\2\2\2"+
		"\u01d7\u01d8\3\2\2\2\u01d8\\\3\2\2\2\u01d9\u01db\5\u00c7d\2\u01da\u01d9"+
		"\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\u01da\3\2\2\2\u01dc\u01dd\3\2\2\2\u01dd"+
		"\u01de\3\2\2\2\u01de\u01e2\5}?\2\u01df\u01e1\5\u00c7d\2\u01e0\u01df\3"+
		"\2\2\2\u01e1\u01e4\3\2\2\2\u01e2\u01e0\3\2\2\2\u01e2\u01e3\3\2\2\2\u01e3"+
		"\u01e5\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e5\u01e6\t\2\2\2\u01e6\u01f7\3\2"+
		"\2\2\u01e7\u01e9\5}?\2\u01e8\u01ea\5\u00c7d\2\u01e9\u01e8\3\2\2\2\u01ea"+
		"\u01eb\3\2\2\2\u01eb\u01e9\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec\u01ed\3\2"+
		"\2\2\u01ed\u01ee\t\2\2\2\u01ee\u01f7\3\2\2\2\u01ef\u01f1\5\u00c7d\2\u01f0"+
		"\u01ef\3\2\2\2\u01f1\u01f2\3\2\2\2\u01f2\u01f0\3\2\2\2\u01f2\u01f3\3\2"+
		"\2\2\u01f3\u01f4\3\2\2\2\u01f4\u01f5\t\2\2\2\u01f5\u01f7\3\2\2\2\u01f6"+
		"\u01da\3\2\2\2\u01f6\u01e7\3\2\2\2\u01f6\u01f0\3\2\2\2\u01f7^\3\2\2\2"+
		"\u01f8\u01fa\5\u00c7d\2\u01f9\u01f8\3\2\2\2\u01fa\u01fb\3\2\2\2\u01fb"+
		"\u01f9\3\2\2\2\u01fb\u01fc\3\2\2\2\u01fc\u01fd\3\2\2\2\u01fd\u01fe\t\3"+
		"\2\2\u01fe`\3\2\2\2\u01ff\u0201\5\u00c7d\2\u0200\u01ff\3\2\2\2\u0201\u0202"+
		"\3\2\2\2\u0202\u0200\3\2\2\2\u0202\u0203\3\2\2\2\u0203\u0204\3\2\2\2\u0204"+
		"\u0208\5}?\2\u0205\u0207\5\u00c7d\2\u0206\u0205\3\2\2\2\u0207\u020a\3"+
		"\2\2\2\u0208\u0206\3\2\2\2\u0208\u0209\3\2\2\2\u0209\u020c\3\2\2\2\u020a"+
		"\u0208\3\2\2\2\u020b\u020d\t\4\2\2\u020c\u020b\3\2\2\2\u020c\u020d\3\2"+
		"\2\2\u020d\u0220\3\2\2\2\u020e\u0210\5}?\2\u020f\u0211\5\u00c7d\2\u0210"+
		"\u020f\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0210\3\2\2\2\u0212\u0213\3\2"+
		"\2\2\u0213\u0215\3\2\2\2\u0214\u0216\t\4\2\2\u0215\u0214\3\2\2\2\u0215"+
		"\u0216\3\2\2\2\u0216\u0220\3\2\2\2\u0217\u0219\5\u00c7d\2\u0218\u0217"+
		"\3\2\2\2\u0219\u021a\3\2\2\2\u021a\u0218\3\2\2\2\u021a\u021b\3\2\2\2\u021b"+
		"\u021d\3\2\2\2\u021c\u021e\t\4\2\2\u021d\u021c\3\2\2\2\u021d\u021e\3\2"+
		"\2\2\u021e\u0220\3\2\2\2\u021f\u0200\3\2\2\2\u021f\u020e\3\2\2\2\u021f"+
		"\u0218\3\2\2\2\u0220b\3\2\2\2\u0221\u0222\7h\2\2\u0222\u0223\7c\2\2\u0223"+
		"\u0224\7n\2\2\u0224\u0225\7u\2\2\u0225\u022b\7g\2\2\u0226\u0227\7v\2\2"+
		"\u0227\u0228\7t\2\2\u0228\u0229\7w\2\2\u0229\u022b\7g\2\2\u022a\u0221"+
		"\3\2\2\2\u022a\u0226\3\2\2\2\u022bd\3\2\2\2\u022c\u0231\7$\2\2\u022d\u0230"+
		"\5\u00c9e\2\u022e\u0230\n\5\2\2\u022f\u022d\3\2\2\2\u022f\u022e\3\2\2"+
		"\2\u0230\u0233\3\2\2\2\u0231\u0232\3\2\2\2\u0231\u022f\3\2\2\2\u0232\u0234"+
		"\3\2\2\2\u0233\u0231\3\2\2\2\u0234\u0235\7$\2\2\u0235f\3\2\2\2\u0236\u0239"+
		"\7)\2\2\u0237\u023a\5\u00c9e\2\u0238\u023a\n\6\2\2\u0239\u0237\3\2\2\2"+
		"\u0239\u0238\3\2\2\2\u0239\u023a\3\2\2\2\u023a\u023b\3\2\2\2\u023b\u023c"+
		"\7)\2\2\u023ch\3\2\2\2\u023d\u023e\7p\2\2\u023e\u023f\7w\2\2\u023f\u0240"+
		"\7n\2\2\u0240\u0241\7n\2\2\u0241j\3\2\2\2\u0242\u0247\5\u00cbf\2\u0243"+
		"\u0246\5\u00cbf\2\u0244\u0246\5\u00c7d\2\u0245\u0243\3\2\2\2\u0245\u0244"+
		"\3\2\2\2\u0246\u0249\3\2\2\2\u0247\u0245\3\2\2\2\u0247\u0248\3\2\2\2\u0248"+
		"l\3\2\2\2\u0249\u0247\3\2\2\2\u024a\u024b\7*\2\2\u024bn\3\2\2\2\u024c"+
		"\u024d\7+\2\2\u024dp\3\2\2\2\u024e\u024f\7}\2\2\u024fr\3\2\2\2\u0250\u0251"+
		"\7\177\2\2\u0251t\3\2\2\2\u0252\u0253\7]\2\2\u0253v\3\2\2\2\u0254\u0255"+
		"\7_\2\2\u0255x\3\2\2\2\u0256\u0257\7=\2\2\u0257z\3\2\2\2\u0258\u0259\7"+
		".\2\2\u0259|\3\2\2\2\u025a\u025b\7\60\2\2\u025b~\3\2\2\2\u025c\u025d\7"+
		"?\2\2\u025d\u0080\3\2\2\2\u025e\u025f\7@\2\2\u025f\u0082\3\2\2\2\u0260"+
		"\u0261\7>\2\2\u0261\u0084\3\2\2\2\u0262\u0263\7#\2\2\u0263\u0086\3\2\2"+
		"\2\u0264\u0265\7\u0080\2\2\u0265\u0088\3\2\2\2\u0266\u0267\7A\2\2\u0267"+
		"\u008a\3\2\2\2\u0268\u0269\7<\2\2\u0269\u008c\3\2\2\2\u026a\u026b\7?\2"+
		"\2\u026b\u026c\7?\2\2\u026c\u008e\3\2\2\2\u026d\u026e\7>\2\2\u026e\u026f"+
		"\7?\2\2\u026f\u0090\3\2\2\2\u0270\u0271\7@\2\2\u0271\u0272\7?\2\2\u0272"+
		"\u0092\3\2\2\2\u0273\u0274\7#\2\2\u0274\u0275\7?\2\2\u0275\u0094\3\2\2"+
		"\2\u0276\u0277\7(\2\2\u0277\u0278\7(\2\2\u0278\u0096\3\2\2\2\u0279\u027a"+
		"\7~\2\2\u027a\u027b\7~\2\2\u027b\u0098\3\2\2\2\u027c\u027d\7-\2\2\u027d"+
		"\u009a\3\2\2\2\u027e\u027f\7/\2\2\u027f\u009c\3\2\2\2\u0280\u0281\7,\2"+
		"\2\u0281\u009e\3\2\2\2\u0282\u0283\7\61\2\2\u0283\u00a0\3\2\2\2\u0284"+
		"\u0285\7(\2\2\u0285\u00a2\3\2\2\2\u0286\u0287\7~\2\2\u0287\u00a4\3\2\2"+
		"\2\u0288\u0289\7`\2\2\u0289\u00a6\3\2\2\2\u028a\u028b\7\'\2\2\u028b\u00a8"+
		"\3\2\2\2\u028c\u028d\7-\2\2\u028d\u028e\7?\2\2\u028e\u00aa\3\2\2\2\u028f"+
		"\u0290\7/\2\2\u0290\u0291\7?\2\2\u0291\u00ac\3\2\2\2\u0292\u0293\7,\2"+
		"\2\u0293\u0294\7?\2\2\u0294\u00ae\3\2\2\2\u0295\u0296\7\61\2\2\u0296\u0297"+
		"\7?\2\2\u0297\u00b0\3\2\2\2\u0298\u0299\7(\2\2\u0299\u029a\7?\2\2\u029a"+
		"\u00b2\3\2\2\2\u029b\u029c\7~\2\2\u029c\u029d\7?\2\2\u029d\u00b4\3\2\2"+
		"\2\u029e\u029f\7`\2\2\u029f\u02a0\7?\2\2\u02a0\u00b6\3\2\2\2\u02a1\u02a2"+
		"\7\'\2\2\u02a2\u02a3\7?\2\2\u02a3\u00b8\3\2\2\2\u02a4\u02a5\7>\2\2\u02a5"+
		"\u02a6\7>\2\2\u02a6\u02a7\7?\2\2\u02a7\u00ba\3\2\2\2\u02a8\u02a9\7@\2"+
		"\2\u02a9\u02aa\7@\2\2\u02aa\u02ab\7?\2\2\u02ab\u00bc\3\2\2\2\u02ac\u02ad"+
		"\7@\2\2\u02ad\u02ae\7@\2\2\u02ae\u02af\7@\2\2\u02af\u02b0\7?\2\2\u02b0"+
		"\u00be\3\2\2\2\u02b1\u02b2\7?\2\2\u02b2\u02b3\7@\2\2\u02b3\u00c0\3\2\2"+
		"\2\u02b4\u02b6\t\7\2\2\u02b5\u02b4\3\2\2\2\u02b6\u02b7\3\2\2\2\u02b7\u02b5"+
		"\3\2\2\2\u02b7\u02b8\3\2\2\2\u02b8\u02b9\3\2\2\2\u02b9\u02ba\ba\2\2\u02ba"+
		"\u00c2\3\2\2\2\u02bb\u02bc\7\61\2\2\u02bc\u02bd\7,\2\2\u02bd\u02c1\3\2"+
		"\2\2\u02be\u02c0\13\2\2\2\u02bf\u02be\3\2\2\2\u02c0\u02c3\3\2\2\2\u02c1"+
		"\u02c2\3\2\2\2\u02c1\u02bf\3\2\2\2\u02c2\u02c4\3\2\2\2\u02c3\u02c1\3\2"+
		"\2\2\u02c4\u02c5\7,\2\2\u02c5\u02c6\7\61\2\2\u02c6\u02c7\3\2\2\2\u02c7"+
		"\u02c8\bb\2\2\u02c8\u00c4\3\2\2\2\u02c9\u02ca\7\61\2\2\u02ca\u02cb\7\61"+
		"\2\2\u02cb\u02cf\3\2\2\2\u02cc\u02ce\n\b\2\2\u02cd\u02cc\3\2\2\2\u02ce"+
		"\u02d1\3\2\2\2\u02cf\u02cd\3\2\2\2\u02cf\u02d0\3\2\2\2\u02d0\u02d2\3\2"+
		"\2\2\u02d1\u02cf\3\2\2\2\u02d2\u02d3\bc\2\2\u02d3\u00c6\3\2\2\2\u02d4"+
		"\u02d5\t\t\2\2\u02d5\u00c8\3\2\2\2\u02d6\u02d7\7^\2\2\u02d7\u02d8\t\n"+
		"\2\2\u02d8\u00ca\3\2\2\2\u02d9\u02db\t\13\2\2\u02da\u02d9\3\2\2\2\u02db"+
		"\u00cc\3\2\2\2\34\2\u01d7\u01dc\u01e2\u01eb\u01f2\u01f6\u01fb\u0202\u0208"+
		"\u020c\u0212\u0215\u021a\u021d\u021f\u022a\u022f\u0231\u0239\u0245\u0247"+
		"\u02b7\u02c1\u02cf\u02da\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}