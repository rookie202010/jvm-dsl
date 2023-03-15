grammar JvmDslParser;

import JvmDslLexer;

//@header {package com.dongjiaqiang.jvm.dsl.core;}

options { tokenVocab=JvmDslLexer; }

program   :    PROGRAM LBRACE
                importDependency*
                member*
            RBRACE
            ;

member :    funcDef
        |   classDef
        |   fieldDef    SEMI
        ;

importDependency   :   importClazzStatement
                   |   usingPackageStatement   ;

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

statement   :   SEMI # SemiExpr
            |   doWhileStatement # DoWhileExpr
            |   whileStatement # WhileExpr
            |   forStatement # ForExpr
            |   ifStatement # IfExpr
            |   assignment SEMI # AssignExpr
            |   synchronizedStatement # SyncExpr
            |   throwOrReturnSideEffectStatement # ThrowOrReturnSideEffectExpr
            |   breakStatement # BreakExpr
            |   continueStatement # ContinueExpr
            |   tryStatement # TryExpr
            |   assertStatement # AssertExpr
            |   block # BlockExpr
            ;

synchronizedStatement   :   SYNCHRONIZED    LPAREN  conditionalOrExpression  RPAREN  block
                        ;

throwOrReturnSideEffectStatement  :    throwOrSideEffectStatement # ThrowOrSideEffectExpr
                                  | returnStatement # ReturnStatementExpr
                                  | returnExpressionStatement # ReturnExprStatementExpr
                                  ;

returnExpressionStatement : RETURN  expression SEMI;

returnStatement: RETURN SEMI;

throwOrSideEffectStatement : THROW? expression SEMI;

breakStatement  :   BREAK   SEMI;

continueStatement  :   CONTINUE    SEMI;

importClazzStatement :   IMPORT importClazz  SEMI
                     |   IMPORT importClazz FROM packageName SEMI;

usingPackageStatement: USING  PACKAGE   packagePath  AS packageName  SEMI;

assertStatement :   ASSERT conditionalOrExpression SEMI;

tryStatement    :  TRY  block   catches
                |  TRY  block   catches?    FINALLY block
                ;

catches :   catchClause    catchClause*   ;
catchClause :   CATCH   LPAREN  parameter   RPAREN block ;

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

ifStatement  :    ifCondition
             block
             (    elseifCondition block )*
             (    elseCondition    block   )   ?  ;

ifCondition :     IF LPAREN conditionalOrExpression RPAREN   ;
elseifCondition:    ELSE   IF   LPAREN    conditionalOrExpression  RPAREN   ;
elseCondition: ELSE;

expression  :   lambdaExpression
            |   matchCaseExpression
            |   blockExpression
            |   conditionalOrExpression
            ;


assignment  :  variable   assignOperator  expression;


assignOperator :   ASSIGN  |   ADD_ASSIGN  |   SUB_ASSIGN  |   MUL_ASSIGN  |   DIV_ASSIGN  |   AND_ASSIGN  |   OR_ASSIGN   |   XOR_ASSIGN  |   MOD_ASSIGN  |   LSHIFT_ASSIGN   |   RSHIFT_ASSIGN   |   URSHIFT_ASSIGN;

// (a+b)
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

matchCaseExpression :  localVariable   MATCH   LBRACE
                       (CASE caseExpression ARROW     matchCaseBlock)+
                       (DEFAULT ARROW  matchCaseBlock)?
                       RBRACE;


caseExpression  :   unapplyExpression
                | typeMatchExpression;

unapplyExpression :   baseLiteral   # unapplyLiteralExpr
        |   clazzType   LPAREN  unapplyExpression (COMMA unapplyExpression)* RPAREN # unapplyClazzExpr
        |   localVariable # unapplyVarExpr
        |   unapplyExpression COLON COLON unapplyExpression   (COLON COLON unapplyExpression)*  # unapplyHeadExpr
        |   LBRACK unapplyExpression   (COMMA    unapplyExpression    )* RBRACK # unapplyListExpr
        |   LPAREN unapplyExpression  (COMMA    unapplyExpression)+    RPAREN # unapplyTupleExpr
        ;

typeMatchExpression :   localVariable COLON type;

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
        |   SET   LBRACK type    RBRACK #   SetType
        |   MAP   LBRACK type    COMMA  type  RBRACK    #   MapType
        |   LPAREN  type    (COMMA   type)+ RPAREN  #   TupleType
        |   OPTION    LBRACK type    RBRACK #   OptionType
        |   FUTURE    LBRACK type    RBRACK #   FutureType
        |   type    ARROW   type    #   LambdaType
        |   LPAREN  RPAREN  ARROW   type    #   SupplierType
        |   clazzType LBRACK type    (COMMA  type)*  RBRACK # ParameterizedClassType
        |   clazzType   #   ClassType
        ;

clazzType   :   IDENTIFIER (DOT IDENTIFIER)*;

varDef   :   parameter    (   ASSIGN expression  )?;

//Class Declare ex. class Foo(Int a,String b),  class Foo[T,K](T t,K k)
classDef    :   CLASS IDENTIFIER  parameters
                LBRACE   funcDef* RBRACE
            ;

funcCallChain   : funcCall  (DOT    part    )*;
literalCallChain :  literal (DOT    part)+;

part:   variable  |   funcCall;

funcCall   :        (variable DOT)? funcName LPAREN RPAREN #   VarCallNoArgs
                 |   (variable DOT)? funcName LPAREN expression (COMMA  expression  )*    RPAREN    # VarCallArgs
                 |  (literal    DOT)?   funcName LPAREN RPAREN # LiteralCallNoArgs
                 |   (literal    DOT)?   funcName LPAREN expression (COMMA expression )*    RPAREN # LiteralCallArgs
                 |   (type DOT)?    funcName LPAREN  RPAREN # TypeCallNoArgs
                 |   (type DOT)?    funcName LPAREN expression (COMMA expression)*  RPAREN # TypeCallArgs
                 ;


funcDef :   SYNCHRONIZED? DEF   funcName  parameters  ASSIGN type    throwDef?   block;

fieldDef    :   VOLATILE?   varDef ;

//Literal
literalAndCallChain :   funcCallChain   # FuncCallChainExpr
        |   literalCallChain    #   LiteralCallChainExpr
        |   literal #   LiteralExpr
        ;

literalAndCallChainAndExpression: literalAndCallChain # LiteralAndCallChainExpr
                                | expression # ExpressionExpr
                                ;


literal :   baseLiteral
        |   classLiteral
        |   variable
        |   optionalLiteral
        |   listLiteral
        |   setLiteral
        |   mapLiteral
        |   tupleLiteral
        |   nulLiteral
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

nulLiteral  : NULL_LITERAL;

//list literal  ex. [], [1,2,3],    [[1,1],[2,1,1],[3,3,1,1]]
listLiteral:    |   LBRACK literalAndCallChainAndExpression    (COMMA    literalAndCallChainAndExpression    )* RBRACK
                |   LBRACK RBRACK ;

blockExpression:    |   IDENTIFIER (LPAREN  variable  RPAREN)? lambdaBlock;


//set literal   ex. {}, {1,3,2}
setLiteral :   LBRACE  literalAndCallChainAndExpression    (COMMA    literalAndCallChainAndExpression    )* RBRACE
           |   LBRACE   RBRACE;

//optional literal  ex. ?12,    ?a
optionalLiteral  : QUESTION literalAndCallChainAndExpression ;

//map   literal ex. {1:2,3:1}
mapLiteral :   LBRACE (   literalAndCallChainAndExpression    )    COLON    (   literalAndCallChainAndExpression   )
    (COMMA    (  literalAndCallChainAndExpression    )    COLON (  literalAndCallChainAndExpression  )   )*  RBRACE
    |   LBRACE RBRACE ;
//tuple literal ex. (1,"11",1f)
tupleLiteral   :   LPAREN literalAndCallChainAndExpression  (COMMA    literalAndCallChainAndExpression)+    RPAREN ;

//class literal ex. new Foo(a,b)
classLiteral    : NEW clazzType (LBRACK type (COMMA type)* RBRACK)* LPAREN RPAREN
                | NEW clazzType (LBRACK type (COMMA type)* RBRACK)* LPAREN literalAndCallChainAndExpression   (COMMA    literalAndCallChainAndExpression)*   RPAREN
                ;

//local variable ex. a, b,  c
localVariable   :   IDENTIFIER;

localArrayVariable : localVariable (LBRACK conditionalOrExpression  RBRACK)+;

localVarOrArrayVar: localVariable | localArrayVariable;

//global variable   or  class instance member
variable :  localVarOrArrayVar  (DOT localVarOrArrayVar)* ;

importClazz :    IDENTIFIER  (DOT IDENTIFIER)* ;

packagePath:    STRING_LITERAL;

packageName:    IDENTIFIER;

parameters    :   LPAREN parameter  (COMMA parameter)* RPAREN
              |   LPAREN    RPAREN;

throwDef    :   THROWS  clazzType  (COMMA  clazzType)*;

parameter    :   type  localVariable  ;

funcName  : IDENTIFIER;





