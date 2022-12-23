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
                        usingPackageStatement   ;

block   :   LBRACE  blockStatements?    RBRACE
        ;

lambdaBlock   :   LBRACE  blockStatements?    RBRACE
        ;

matchCaseBlock  :   LBRACE  blockStatements?    RBRACE;

blockStatements :   blockStatement+
                ;

blockStatement  :   varDef  SEMI  # VarDefExpr
                |   statement # StatementExpr
                ;

statement   :   doWhileStatement # DoWhileExpr
            |   whileStatement # WhileExpr
            |   forStatement # ForExpr
            |   ifStatement # IfExpr
            |   assignment SEMI # AssignExpr
            |   synchronizedStatement # SyncExpr
            |   throwReturnOrSideEffectStatement # ThrowOrSideEffectExpr
            |   breakStatement # BreakExpr
            |   continueStatement # ContinueExpr
            |   tryStatement # TryExpr
            |   assertStatement # AssertExpr
            |   block # BlockExpr
            |   SEMI # SemiExpr
            ;

synchronizedStatement   :   SYNCHRONIZED    LPAREN  conditionalOrExpression  RPAREN  block
                        ;


throwReturnOrSideEffectStatement  :   (THROW | RETURN)? expression  SEMI

                ;

breakStatement  :   BREAK   SEMI;

continueStatement  :   CONTINUE    SEMI;

importClazzStatement :   IMPORT importClazz  SEMI
                     |   IMPORT importClazz FROM packageName SEMI;

usingPackageStatement: USING  PACKAGE   packagePath  AS packageName  SEMI;

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
forStatement :   FOR LPAREN  varDef   SEMI    conditionalOrExpression   SEMI   assignment RPAREN block # ForStatementOne
             |   FOR LPAREN  varDef   COLON   literalAndCallChain RPAREN block # ForStatementTwo
             |   FOR LPAREN  varDef   COMMA   varDef   COLON   literalAndCallChain RPAREN block # ForStatementThree
             ;

ifStatement  :    IF LPAREN conditionalOrExpression RPAREN
             block
             (  ELSE   IF   LPAREN    conditionalOrExpression  RPAREN   block )*
             (  ELSE    block   )   ?  ;

expression  :   lambdaExpression
            |   matchCaseExpression
            |   blockExpression
            |   conditionalOrExpression
            ;


assignment  :  (    variable    |   arrayVariable | mapVariable  )   assignOperator  expression;

arrayVariable: variable LBRACK conditionalOrExpression  RBRACK;

mapVariable: variable LPAREN conditionalOrExpression RPAREN;

assignOperator :   ASSIGN  |   ADD_ASSIGN  |   SUB_ASSIGN  |   MUL_ASSIGN  |   DIV_ASSIGN  |   AND_ASSIGN  |   OR_ASSIGN   |   XOR_ASSIGN  |   MOD_ASSIGN  |   LSHIFT_ASSIGN   |   RSHIFT_ASSIGN   |   URSHIFT_ASSIGN;

parenExpression :   LPAREN conditionalOrExpression RPAREN;

//or expr a||b
conditionalOrExpression
	:	conditionalAndExpression # ConditionalAndExpr
	|	conditionalOrExpression OR conditionalAndExpression # OrOpExpr
	|   conditionalOrExpression OR parenExpression # OrOpParenExpr
	|   parenExpression OR conditionalOrExpression # ParenOrOpExpr
	|   parenExpression OR parenExpression # ParenOrOpParenExpr
	;

//and expr a && b
conditionalAndExpression
	:	inclusiveOrExpression # InclusiveOrExpr
	|	conditionalAndExpression AND inclusiveOrExpression # AndOpExpr
	|   conditionalAndExpression AND parenExpression # AndOpParenExpr
	|   parenExpression AND conditionalAndExpression # ParenAndOpExpr
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
                    |   equalityExpression   equalityOperation   parenExpression # EqualParenExpr
                    |   parenExpression equalityOperation   equalityExpression   # ParenEqualExpr
                    |   parenExpression equalityOperation   parenExpression # ParenEqualParenExpr
                    ;

relationOperation  :   LT  |   GT  |   LE  |   GE  ;

//relation expr a<b,a>b,a<=b,a>=b
relationExpression  :   shiftExpression # ShiftExpr
                    |   relationExpression  relationOperation  shiftExpression # RelationOpExpr
                    |   relationExpression   relationOperation   parenExpression # RelationOpParenExpr
                    |   parenExpression relationOperation   relationExpression   # ParenRelationOpExpr
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
unaryExpression   :   literalAndCallChain # LiteralAndFuncCallExpr
                |   LPAREN  additiveOperation  unaryExpression  RPAREN # OppositeExpr
                |   BANG    unaryExpression # NegateExpr
                |   LPAREN  type    RPAREN  unaryExpression # CastExpr
                |   literalAndCallChain INSTANCEOF    type # InstanceofExpr
                |   LPAREN unaryExpression RPAREN # ParenExpr
                ;

lambdaExpression    :   LPAREN localVariable  (COMMA localVariable)* RPAREN    ARROW  lambdaBlock # ParamsLambdaExpr
                    |   LPAREN RPAREN    ARROW   lambdaBlock # NoParamLambdaExpr
                    |   localVariable ARROW     lambdaBlock  # OneParamLambdaExpr
                    ;

matchCaseExpression :  localVariable   ARROW   LBRACE
                       (CASE caseExpression ARROW     matchCaseBlock)+
                       (DEFAULT ARROW  matchCaseBlock)?
                       RBRACE  # MatchCaseExpr;


caseExpression  :   unapplyExpression
                | typeMatchExpression;

unapplyExpression :   baseLiteral
        |   unapplyClazzExpression
        |   localVariable
        |   unapplyHeadTailExpression
        |   unapplyListExpression
        |   unapplySetExpression
        |   unapplyMapExpression
        |   unapplyTupleExpression
        ;

typeMatchExpression :   localVariable ':' type;

unapplyListExpression:    |   LBRACK unapplyExpression   (COMMA    unapplyExpression    )* RBRACK
                |   LBRACK RBRACK ;

unapplyHeadTailExpression:  |   localVariable ':'   ':' localVariable;

unapplyClazzExpression   :   clazzType   LPAREN  unapplyExpression (COMMA unapplyExpression)* RPAREN
                    |   clazzType
                    ;
unapplySetExpression :   LBRACE  unapplyExpression   (COMMA    unapplyExpression    )* RBRACE
           |   LBRACE   RBRACE;

unapplyMapExpression :   LBRACE (   unapplyExpression    )    COLON    (   unapplyExpression   )
    (COMMA    (  unapplyExpression    )    COLON (  unapplyExpression  )   )*  RBRACE
    |   LBRACE RBRACE ;

unapplyTupleExpression   :   LPAREN unapplyExpression  (COMMA    unapplyExpression)+    RPAREN ;


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

clazzType   :   IDENTIFIER;

varDef   :   parameter    (   ASSIGN expression  )?;

//Class Declare ex. class Foo(Int a,String b),  class Foo[T,K](T t,K k)
classDef    :   CLASS IDENTIFIER  parameters
                LBRACE   funcDef* RBRACE
            ;

//Func Call ex. foo(),  bar.foo(1,2)
callChain    :   (funcCall|literal)  (DOT    part    )*   ;

part:   variable   |   funcCall;

funcCall   :        (variable DOT)? funcName LPAREN RPAREN #   VarCallNoArgs
                 |   (variable DOT)? funcName LPAREN expression (COMMA  expression  )*    RPAREN    # VarCallArgs
                 |  (literal    DOT)?   funcName LPAREN RPAREN # LiteralCallNoArgs
                 |   (literal    DOT)?   funcName LPAREN expression (COMMA expression )*    RPAREN # LiteralCallArgs
                 |   (type DOT)?    funcName LPAREN  RPAREN # TypeCallNoArgs
                 |   (type DOT)?    funcName LPAREN expression (COMMA expression)*  RPAREN # TypeCallArgs
                 ;


funcDef :   DEF   funcName  parameters  ASSIGN type    throwDef?   block;

fieldDef    :   VOLATILE?   varDef ;

//Literal
literalAndCallChain :   callChain
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
listLiteral:    |   LBRACK literalAndCallChain    (COMMA    literalAndCallChain    )* RBRACK
                |   LBRACK RBRACK ;

blockExpression:    |   IDENTIFIER (LPAREN  variable  RPAREN)? lambdaBlock;


//set literal   ex. (), (1,3,2)
setLiteral :   LBRACE  literalAndCallChain    (COMMA    literalAndCallChain    )* RBRACE
           |   LBRACE   RBRACE;

//optional literal  ex. ?12,    ?a
optionalLiteral  : QUESTION literalAndCallChain ;

//map   literal ex. {1:2,3:1}
mapLiteral :   LBRACE (   literalAndCallChain    )    COLON    (   literalAndCallChain   )
    (COMMA    (  literalAndCallChain    )    COLON (  literalAndCallChain  )   )*  RBRACE
    |   LBRACE RBRACE ;
//tuple literal ex. (1,"11",1f)
tupleLiteral   :   LPAREN literalAndCallChain  (COMMA    literalAndCallChain)+    RPAREN ;

//class literal ex. new Foo(a,b)
classLiteral    :  NEW clazzType (LBRACK type (COMMA type)* RBRACK)* LPAREN literalAndCallChain   (COMMA    literalAndCallChain)*   RPAREN
                |  NEW clazzType (LBRACK type (COMMA type)* RBRACK)* LPAREN RPAREN
                ;

//local variable ex. a, b,  c
localVariable   :   IDENTIFIER;

//global variable   or  class instance member
variable :  IDENTIFIER  (DOT IDENTIFIER)* ;

importClazz :    IDENTIFIER  (DOT IDENTIFIER)* ;

packagePath:    STRING_LITERAL;

packageName:    IDENTIFIER;

parameters    :   LPAREN parameter  (COMMA parameter)* RPAREN
              |   LPAREN    RPAREN;

throwDef    :   THROWS  clazzType  (COMMA  clazzType)*;

parameter    :   type  localVariable  ;

funcName  : IDENTIFIER;





