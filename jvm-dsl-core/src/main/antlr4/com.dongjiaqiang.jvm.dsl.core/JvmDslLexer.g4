lexer grammar JvmDslLexer;

//Keywords
PROGRAM:            'program';
ASSERT:             'assert';
BREAK:              'break';
CASE:               'case';
CATCH:              'catch';
CLASS:              'class';
CONTINUE:           'continue';
DEFAULT:            'default';
DO:                 'do';
ELSE:               'else';
FINALLY:            'finally';
FOR:                'for';
IF:                 'if';
USING:              'using';
PACKAGE:            'package';
AS:                 'as';
FROM:               'from';
IMPORT:             'import';
INSTANCEOF:         'instanceof';
NEW:                'new';
RETURN:             'return';
MATCH:              'match';
SYNCHRONIZED:       'synchronized';
THROW:              'throw';
THROWS:             'throws';
TRY:                'try';
VOLATILE:           'volatile';
WHILE:              'while';
DEF:                'def';

FLOAT:              'Float';
VOID:               'Void';
INT:                'Int';
LONG:               'Long';
DOUBLE:             'Double';
BOOL:               'Bool';
BYTE:               'Byte';
CHAR:               'Char';
STRING:             'String';
LIST:               'List';
ARRAY:              'Array';
SET:                'Set';
MAP:                'Map';
OPTION:             'Option';
FUTURE:             'Future';
ASYNC:              'async';

//Literal
INT_LITERAL :   DIGIT+ ;
FLOAT_LITERAL   :   DIGIT+  DOT DIGIT+  ('f'|'F')
        |   DOT DIGIT+  ('f'|'F')
        |   DIGIT+ ('f'|'F')    ;
LONG_LITERAL    :   DIGIT+ ('l'|'L')   ;
DOUBLE_LITERAL  :   DIGIT+ DOT  DIGIT+  ('d'|'D')
        |   DOT DIGIT+  ('d'|'D')
        |   DIGIT+  ('d'|'D')  ;
BOOL_LITERAL    :   'false' | 'true'    ;
STRING_LITERAL  :   '"' (ESC|~["\\])*?   '"' ;
CHAR_LITERAL    :   '\'' (ESC| ~['\\] )? '\''  ;

NULL_LITERAL:       'null';

IDENTIFIER  :   ID_LETTER   (   ID_LETTER   |   DIGIT   )*;

// Separators
LPAREN:             '(';
RPAREN:             ')';
LBRACE:             '{';
RBRACE:             '}';
LBRACK:             '[';
RBRACK:             ']';
SEMI:               ';';
COMMA:              ',';
DOT:                '.';

// Operators
ASSIGN:             '=';
GT:                 '>';
LT:                 '<';
BANG:               '!';
TILDE:              '~';
QUESTION:           '?';
COLON:              ':';
EQUAL:              '==';
LE:                 '<=';
GE:                 '>=';
NOTEQUAL:           '!=';
AND:                '&&';
OR:                 '||';
ADD:                '+';
SUB:                '-';
MUL:                '*';
DIV:                '/';
BITAND:             '&';
BITOR:              '|';
CARET:              '^';
MOD:                '%';




ADD_ASSIGN:         '+=';
SUB_ASSIGN:         '-=';
MUL_ASSIGN:         '*=';
DIV_ASSIGN:         '/=';
AND_ASSIGN:         '&=';
OR_ASSIGN:          '|=';
XOR_ASSIGN:         '^=';
MOD_ASSIGN:         '%=';
LSHIFT_ASSIGN:      '<<=';
RSHIFT_ASSIGN:      '>>=';
URSHIFT_ASSIGN:     '>>>=';

//Lambda expression
ARROW:  '=>';

// Whitespace and comments
WS:                 [ \t\r\n\u000C]+ -> channel(HIDDEN);
COMMENT:            '/*' .*? '*/'    -> channel(HIDDEN);
LINE_COMMENT:       '//' ~[\r\n]*    -> channel(HIDDEN);

fragment
DIGIT   :   [0-9]   ;
ESC :   '\\'    [btnr"'\\]   ;
ID_LETTER   :   [a-zA-Z]
            |   '_' ;

