grammar JvmDslParser;

//import JvmDslLexer;

//@header {package com.dongjiaqiang.jvm.dsl.core;}

options { tokenVocab=JvmDslLexer; }

program   :    PACKAGE LBRACE
                member*
            RBRACE
            ;

member :    funcDef
        |   classDef
        |   fieldDef    SEMI
        ;

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
            |   returnStatement
            |   synchronizedStatement
            |   throwStatement
            |   breakStatement
            |   continueStatement
            |   importClazzStatement
            |   usingJarStatement
            |   tryStatement
            |   assertStatement
            |   block
            |   SEMI
            ;

synchronizedStatement   :   SYNCHRONIZED    LPAREN  expression  RPAREN  block
                        ;

throwStatement  :   THROW expression  SEMI
                ;

returnStatement :   RETURN  expression  SEMI
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
catcheClause :   CATCH   LPAREN  parameter   RPAREN ;

//while statement
whileStatement   : WHILE LPAREN conditionalOrExpression RPAREN
              block ;

//do while statement
doWhileStatement :   DO
                block
                WHILE LPAREN conditionalOrExpression RPAREN;
//for statement
forStatement :   FOR LPAREN  varDef   SEMI    conditionalOrExpression   SEMI   assignment RPAREN
            block
        |   FOR LPAREN  varDef   COLON   literalAndFuncCall RPAREN
            block
        |   FOR LPAREN  varDef   COMMA   varDef   COLON   literalAndFuncCall RPAREN
            block;

ifStatement  :    IF LPAREN conditionalOrExpression RPAREN
             block
             (  ELSE   IF   LPAREN    conditionalOrExpression  RPAREN   block )*
             (  ELSE    block   )   ?  ;

expression  :   lambdaExpression
            |   conditionalOrExpression
            ;


assignment  :  (    variable    |   (variable LBRACK expression  RBRACK)  )   assignOperator  expression  ;

assignOperator :   ASSIGN  |   ADD_ASSIGN  |   SUB_ASSIGN  |   MUL_ASSIGN  |   DIV_ASSIGN  |   AND_ASSIGN  |   OR_ASSIGN   |   XOR_ASSIGN  |   MOD_ASSIGN  |   LSHIFT_ASSIGN   |   RSHIFT_ASSIGN   |   URSHIFT_ASSIGN;

conditionalOrExpression
	:	conditionalAndExpression
	|	conditionalOrExpression OR conditionalAndExpression
	;

conditionalAndExpression
	:	inclusiveOrExpression
	|	conditionalAndExpression AND inclusiveOrExpression
	;

inclusiveOrExpression   :	exclusiveOrExpression
	                    |	inclusiveOrExpression BITOR exclusiveOrExpression
                        ;

exclusiveOrExpression   :	andExpression
	                    |	exclusiveOrExpression  CARET andExpression
	                    ;

andExpression   :   equalityExpression
                |   andExpression   BITAND  equalityExpression
                ;

equalityExpression  :   relationExpression # Relation
                    |   equalityExpression  EQUAL relationExpression # Equal
                    |   equalityExpression  NOTEQUAL    relationExpression # NotEqual
                    ;

relationExpression  :   shiftExpression # Shift
                    |   relationExpression  LT  shiftExpression # Lt
                    |   relationExpression  GT  shiftExpression # Gt
                    |   relationExpression  LE  shiftExpression # Le
                    |   relationExpression  GE  shiftExpression # Ge
                    |   relationExpression  INSTANCEOF    type # Instanceof
                    ;

shiftExpression     :   additiveExpression # Additive
                    |   shiftExpression LT LT additiveExpression # LeftShift
                    |   shiftExpression GT GT additiveExpression # RightShift
                    |   shiftExpression GT GT GT additiveExpression # UnsignedRightShift
                    ;

additiveExpression   :   multiplicativeExpression # Multiplicative
                    |   additiveExpression   ADD   multiplicativeExpression  # Add
                    |   additiveExpression   SUB   multiplicativeExpression # Sub
                    ;

multiplicativeExpression    :   unaryExpression # Unary
                            |   multiplicativeExpression    DIV unaryExpression # Div
                            |   multiplicativeExpression    MUL unaryExpression # Mul
                            |   multiplicativeExpression    MOD unaryExpression # Mod
                            ;

//unary operator    +1,-2,
unaryExpression   :   literalAndFuncCall
                |   LPAREN  SUB  unaryExpression  RPAREN
                |   BANG    unaryExpression
                |   LPAREN  type    RPAREN  unaryExpression
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
type    :   INT
        |   FLOAT
        |   LONG
        |   DOUBLE
        |   BOOL
        |   BYTE
        |   STRING
        |   CHAR
        |   LIST  LBRACK type    RBRACK
        |   ARRAY   LBRACK type RBRACK
        |   SET   LBRACK type    RBRACE
        |   MAP   LBRACK type    COMMA  type  RBRACK
        |   LPAREN  type    (COMMA   type)+ RPAREN
        |   OPTION    LBRACK type    RBRACK
        |   FUTURE    LBRACK type    RBRACK
        |   type    ARROW   type
        |   type    ARROW   LPAREN  type    (   COMMA   type)+  RPAREN
        |   LPAREN  RPAREN  ARROW   type
        |   LPAREN  RPAREN  ARROW   LPAREN  type    (   COMMA   type)+  RPAREN
        |   LPAREN  type    (COMMA  type)+ RPAREN   ARROW   type
        |   LPAREN  type    (COMMA  type)+ RPAREN  ARROW   LPAREN  type    (   COMMA   type)+  RPAREN
        |   clazzType LBRACK type    (COMMA  type)*  RBRACK
        |   clazzType
        ;

clazzType   :   IDENTIFIER  (DOT IDENTIFIER)* ;

varDef   :   type localVariable    (   ASSIGN (    conditionalOrExpression | lambdaExpression  )  )?;

//Class Declare ex. class Foo(Int a,String b),  class Foo[T,K](T t,K k)
classDef    :   CLASS IDENTIFIER (LBRACK IDENTIFIER (COMMA IDENTIFIER)* RBRACK)* parameters
                LBRACE   funcDef* RBRACE
            ;

//Func Call ex. foo(),  bar.foo(1,2)
funcCall    :   singleFuncCall  (DOT singleFuncCall)*   ;

singleFuncCall   :   (variable DOT)? funcName LPAREN RPAREN
                 |    (variable DOT)? funcName LPAREN expression (COMMA  expression  )*    RPAREN;

funcDef :   DEF   funcName  parameters  ':' type    throwDef?   block;

fieldDef    :   VOLATILE?   varDef
        ;

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
mapLiteral :   LBRACE (   literalAndFuncCall    )    ':'    (   literalAndFuncCall   )
    (COMMA    (  literalAndFuncCall    )    ':' (  literalAndFuncCall  )   )*  RBRACE
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





