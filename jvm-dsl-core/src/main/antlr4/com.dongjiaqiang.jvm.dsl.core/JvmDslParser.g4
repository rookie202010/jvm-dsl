grammar JvmDslParser;

import JvmDslLexer;

//@header {package com.dongjiaqiang.jvm.dsl.core;}

options { tokenVocab=JvmDslLexer; }

program   :    PROGRAM LBRACE
                importDeppendency*
                member*
            RBRACE
            ;

member :    funcDef
        |   classDef
        |   fieldDef    SEMI
        ;

importDeppendency   :   importClazzStatement
                        usingJarStatement   ;

block   :   LBRACE  blockStatements?    RBRACE
        ;

blockStatements :   blockStatement+
                ;

blockStatement  :   varDef  SEMI
                |   statement
                ;

statement   :   doWhileStatement
            |   whileStatement
            |   forStatement
            |   ifStatement
            |   assignment SEMI
            |   synchronizedStatement
            |   throwReturnOrSideEffectStatement
            |   breakStatement
            |   continueStatement
            |   tryStatement
            |   assertStatement
            |   block
            |   SEMI
            ;

synchronizedStatement   :   SYNCHRONIZED    LPAREN  expression  RPAREN  block
                        ;

throwReturnOrSideEffectStatement  :   (THROW | RETURN)? expression  SEMI

                ;

breakStatement  :   BREAK   SEMI;

continueStatement  :   CONTINUE    SEMI;

importClazzStatement :   IMPORT importClazz  SEMI
                     |   IMPORT importClazz FROM jarName SEMI;

usingJarStatement: USING  JAR   jarPath  AS jarName  SEMI;

assertStatement :   ASSERT conditionalOrExpression SEMI;

tryStatement    :  TRY  block   catches
                |  TRY  block   catches?    FINALLY block
                ;

catches :   catcheClause    catcheClause*   ;
catcheClause :   CATCH   LPAREN  parameter   RPAREN block ;

//while statement
whileStatement   : WHILE LPAREN conditionalOrExpression RPAREN
              block ;

//do while statement
doWhileStatement :   DO
                block
                WHILE LPAREN conditionalOrExpression RPAREN;
//for statement
forStatement :   FOR LPAREN  varDef   SEMI    conditionalOrExpression   SEMI   assignment RPAREN
            block # ForStatementOne
        |   FOR LPAREN  varDef   COLON   literalAndFuncCall RPAREN
            block # ForStatementTwo
        |   FOR LPAREN  varDef   COMMA   varDef   COLON   literalAndFuncCall RPAREN
            block # ForStatementThree
        ;

ifStatement  :    IF LPAREN conditionalOrExpression RPAREN
             block
             (  ELSE   IF   LPAREN    conditionalOrExpression  RPAREN   block )*
             (  ELSE    block   )   ?  ;

expression  :   lambdaExpression
            |   conditionalOrExpression
            ;


assignment  :  (    variable    |   (variable LBRACK expression  RBRACK)  )   assignOperator  expression  ;

assignOperator :   ASSIGN  |   ADD_ASSIGN  |   SUB_ASSIGN  |   MUL_ASSIGN  |   DIV_ASSIGN  |   AND_ASSIGN  |   OR_ASSIGN   |   XOR_ASSIGN  |   MOD_ASSIGN  |   LSHIFT_ASSIGN   |   RSHIFT_ASSIGN   |   URSHIFT_ASSIGN;

parenExpression :   LPAREN conditionalOrExpression RPAREN;

//or expr a||b
conditionalOrExpression
	:	conditionalAndExpression # ConditionalAndExpr
	|	conditionalOrExpression OR conditionalAndExpression # OrOpExpr
	|   conditionalAndExpression OR parenExpression # OrOpParenExpr
	|   parenExpression OR conditionalAndExpression # ParenOrOpExpr
	|   parenExpression OR parenExpression # ParenOrOpParenExpr
	;

//and expr a && b
conditionalAndExpression
	:	inclusiveOrExpression # InclusiveOrExpr
	|	conditionalAndExpression AND inclusiveOrExpression # AndOpExpr
	|   conditionalAndExpression AND parenExpression # AndOpParenExpr
	|   parenExpression AND parenExpression # ParenAndOpParenExpr
	;

//inclusive or expr a|b
inclusiveOrExpression   :	exclusiveOrExpression # ExclusiveOrExpr
	                    |	inclusiveOrExpression BITOR exclusiveOrExpression # BitOrOpExpr
	                    |   inclusiveOrExpression BITOR parenExpression # BitOrParenOpExpr
	                    |   parenExpression BITOR   inclusiveOrExpression # ParenBitOrOpExpr
	                    |   parenExpression BITOR   parenExpression # ParenBitOrOpParenExpr
                        ;

//exclusive or expr a^b
exclusiveOrExpression   :	bitAndExpression # BitAndExpr
	                    |	exclusiveOrExpression  CARET bitAndExpression # CaretOpExpr
	                    |   exclusiveOrExpression   CARET   parenExpression # CaretOpParenExpr
	                    |   parenExpression CARET exclusiveOrExpression # ParenCaretOpExpr
	                    |   parenExpression CARET parenExpression # ParenCaretOpParenExpr
	                    ;
//bit and expr a&b
bitAndExpression   :   equalityExpression # EualityExpr
                |   bitAndExpression   BITAND  equalityExpression # BitAndOpExpr
                |   bitAndExpression    BITAND parenExpression  # BitAndOpParenExpr
                |   parenExpression BITAND  bitAndExpression # ParenBitAnOpExpr
                |   parenExpression BITAND parenExpression # ParenBitAndOpParenExpr
                ;

equalityOperation   :   EQUAL   |   NOTEQUAL;

//equality expr a==b,a!=b
equalityExpression  :   relationExpression # RelationExpr
                    |   equalityExpression  equalityOperation relationExpression # EqualExpr
                    |   equalityOperation   equalityOperation   parenExpression # EqualParenExpr
                    |   parenExpression equalityOperation   equalityOperation   # ParenEqualExpr
                    |   parenExpression equalityOperation   parenExpression # ParenEqualParenExpr
                    ;

relationOperation  :   LT  |   GT  |   LE  |   GE  ;

//relation expr a<b,a>b,a<=b,a>=b
relationExpression  :   shiftExpression # ShiftExpr
                    |   relationExpression  relationOperation  shiftExpression # RelationOpExpr
                    |   relationOperation   relationOperation   parenExpression # RelationOpParenExpr
                    |   parenExpression relationOperation   relationOperation   # ParenRelationOpExpr
                    |   parenExpression relationOperation   parenExpression #   ParenRelationOpParenExpr
                    ;

shiftOperation :   LT LT | GT GT | GT GT GT;

//shift expr a<<b,a>>b,a>>>>b
shiftExpression     :   additiveExpression # AddExpr
                    |   shiftExpression shiftOperation additiveExpression # ShiftOpExpr
                    |   shiftExpression shiftOperation parenExpression # ShiftOpParenExpr
                    |   parenExpression shiftOperation shiftExpression # ParenShiftOpExpr
                    |   parenExpression shiftOperation parenExpression # ParenShiftOpParenExpr
                    ;

additiveOperation  :   ADD |   SUB;

//additive expr a+b,a-b
additiveExpression   :   multiplicativeExpression # MultiExpr
                    |   additiveExpression   additiveOperation   multiplicativeExpression  # AddOpExpr
                    |   additiveExpression additiveOperation parenExpression # AddOpParenExpr
                    |   parenExpression additiveOperation additiveExpression # ParenAddOpExpr
                    |   parenExpression additiveOperation parenExpression # ParenAddOpParenExpr
                    ;


multiplicativeOperation    :   DIV |   MUL |   MOD;

//multiplicative expr   a*b,a/b,a%d
multiplicativeExpression    :   unaryExpression # UnaryExpr
                            |   multiplicativeExpression  multiplicativeOperation unaryExpression # MultiOpExpr
                            |   parenExpression multiplicativeOperation parenExpression #  ParenMultiOpParenExpr
                            |   multiplicativeExpression multiplicativeOperation parenExpression # MultiOpParenExpr
                            |   parenExpression multiplicativeOperation multiplicativeExpression # ParenMultiOpExpr
                            ;

//unary expr    func(a,b) a, (-a), (+a), (a), a instanceof Int, 1,[1,2],!a,(Int)a
unaryExpression   :   literalAndFuncCall # LiteralAndFuncCallExpr
                |   LPAREN  additiveOperation  unaryExpression  RPAREN # OppositeExpr
                |   BANG    unaryExpression # NegateExpr
                |   LPAREN  type    RPAREN  unaryExpression # CastExpr
                |   literalAndFuncCall INSTANCEOF    type # InstanceofExpr
                |   LPAREN unaryExpression RPAREN # ParenExpr
                ;

lambdaExpression    :   LPAREN localVariable  (COMMA localVariable)* RPAREN    ARROW  block
                    |   LPAREN RPAREN    ARROW   block
                    |   localVariable ARROW     block
                    |   localVariable   ARROW   LBRACE
                        (CASE (baseLiteral  |   unapplyExpression) ARROW     block)+
                        (DEFAULT ARROW  block)?
                        RBRACE
                    ;

unapplyExpression   :   clazzType   LPAREN  literal (COMMA literal)* RPAREN
                    |   clazzType
                    ;

//Type  ex. Int,    Float,  Char,   Set[Int]
type    :   INT #   IntType
        |   FLOAT   #   FloatType
        |   LONG    #   LongType
        |   DOUBLE  #   DoubleType
        |   BOOL    #   BoolType
        |   BYTE    #   ByteType
        |   STRING  #   StringType
        |   CHAR    #   CharType
        |   VOID    #   VoidType
        |   LIST  LBRACK type    RBRACK #   ListType
        |   ARRAY   LBRACK type RBRACK  #   ArrayType
        |   SET   LBRACK type    RBRACE #   SetType
        |   MAP   LBRACK type    COMMA  type  RBRACK    #   MapType
        |   LPAREN  type    (COMMA   type)+ RPAREN  #   TupleType
        |   OPTION    LBRACK type    RBRACK #   OptionType
        |   FUTURE    LBRACK type    RBRACK #   FutureType
        |   type    ARROW   type    #   LambdaOneInOneOutType
        |   type    ARROW   types  #   LambdaOneInMoreOutType
        |   LPAREN  RPAREN  ARROW   type    #   LambdaZeroInOneOutType
        |   LPAREN  RPAREN  ARROW   types  #   LambdaZeroInMoreOutType
        |   types   ARROW   type    #   LambdaMoreInOneOutType
        |   types  ARROW   types   #   LambdaMoreInMoreOutType
        |   clazzType LBRACK type    (COMMA  type)*  RBRACK # ParameterizedClassType
        |   clazzType   #   ClassType
        ;

types:  LPAREN  type    (COMMA  type)+ RPAREN;

clazzType   :   IDENTIFIER  (DOT IDENTIFIER)* ;

varDef   :   type localVariable    (   ASSIGN (    conditionalOrExpression | lambdaExpression  )  )?;

//Class Declare ex. class Foo(Int a,String b),  class Foo[T,K](T t,K k)
classDef    :   CLASS IDENTIFIER  parameters
                LBRACE   funcDef* RBRACE
            ;

//Func Call ex. foo(),  bar.foo(1,2)
funcCall    :   singleFuncCall  (DOT singleFuncCall)*   ;

singleFuncCall   :   (variable DOT)? funcName LPAREN RPAREN #   VarCallNoArgs
                 |   (variable DOT)? funcName LPAREN expression (COMMA  expression  )*    RPAREN    # VarCallArgs
                 |   (literal    DOT)?   funcName LPAREN RPAREN # LiteralCallNoArgs
                 |   (literal    DOT)?   funcName LPAREN expression (COMMA expression )*    RPAREN # LiteralCallArgs
                 |   (type DOT)?    funcName LPAREN  RPAREN # TypeCallNoArgs
                 |   (type DOT)?    funcName LPAREN expression (COMMA expression)*  RPAREN # TypeCalArgs
                 ;


funcDef :   DEF   funcName  parameters  ASSIGN type    throwDef?   block;

fieldDef    :   VOLATILE?   varDef ;

//Literal
literalAndFuncCall :   funcCall
        |   literal
        ;

literal :   baseLiteral
        |   classLiteral
        |   variable
        |   optionalLiteral
        |   listLiteral
        |   setLiteral
        |   mapLiteral
        |   tupleLiteral
        |   asyncLiteral
        ;

baseLiteral :   numberLiteral
            |   STRING_LITERAL
            |   BOOL_LITERAL
            |   CHAR_LITERAL
            ;

//number literal ex. 12,    12.1f,  131L,   1.1
numberLiteral   :   INT_LITERAL
                |   FLOAT_LITERAL
                |   LONG_LITERAL
                |   DOUBLE_LITERAL  ;

//list literal  ex. [], [1,2,3],    [[1,1],[2,1,1],[3,3,1,1]]
listLiteral:    |   LBRACK literalAndFuncCall    (COMMA    literalAndFuncCall    )* RBRACK
                |   LBRACK RBRACK ;

asyncLiteral:   |   ASYNC   (LPAREN  variable  RPAREN)? block;

//set literal   ex. (), (1,3,2)
setLiteral :   LBRACE  literalAndFuncCall    (COMMA    literalAndFuncCall    )* RBRACE
           |   LBRACE   RBRACE;

//optional literal  ex. ?12,    ?a
optionalLiteral  : QUESTION literalAndFuncCall ;

//map   literal ex. {1:2,3:1}
mapLiteral :   LBRACE (   literalAndFuncCall    )    COLON    (   literalAndFuncCall   )
    (COMMA    (  literalAndFuncCall    )    COLON (  literalAndFuncCall  )   )*  RBRACE
    |   LBRACE RBRACE ;
//tuple literal ex. (1,"11",1f)
tupleLiteral   :   LPAREN literalAndFuncCall  (COMMA    literalAndFuncCall)+    RPAREN ;

//class literal ex. new Foo(a,b)
classLiteral    :  NEW clazzType (LBRACK type (COMMA type)* RBRACK)* LPAREN literalAndFuncCall   (COMMA    literalAndFuncCall)*   RPAREN
                |  NEW clazzType (LBRACK type (COMMA type)* RBRACK)* LPAREN RPAREN
                ;

//local variable ex. a, b,  c
localVariable   :   IDENTIFIER;

//global variable   or  class instance member
variable :  IDENTIFIER  (DOT IDENTIFIER)* ;

importClazz :    IDENTIFIER  (DOT IDENTIFIER)* MUL?;

jarPath:    STRING_LITERAL;

jarName:    IDENTIFIER;

parameters    :   LPAREN parameter  (COMMA parameter)* RPAREN
              |   LPAREN    RPAREN;

throwDef    :   THROWS  clazzType  (COMMA  clazzType)*;

parameter    :   type  localVariable  ;

funcName  : IDENTIFIER;





