package com.dongjiaqiang.jvm.dsl.core.statement

import com.dongjiaqiang.jvm.dsl.core.`type`.DslType

sealed trait Expression extends Statement {

}

//value expression

case class LocalVarDef(name:String, dslType: DslType) extends Expression

case class VarRef(name:List[String]) extends Expression

case class Literal(literal:Any,dslType: DslType) extends Expression

case class Lambda(inputs:Array[String], block: Block)

//assign expression
class Assign(val varRef: VarRef,val right: Expression) extends Expression


//unary expression
sealed trait UnaryExpression extends Expression{
    val child:Expression
}

class Negate(val child:Expression) extends UnaryExpression // !a
class Opposite(val child: Expression) extends UnaryExpression // -a
class Cast(val child: Expression, castType: DslType) extends UnaryExpression // (Int)a
class Instanceof(val child: Expression,judgeType: DslType) extends UnaryExpression //a instance Int

//binary expression
sealed trait BinaryExpression extends Expression{
    val left:Expression
    val right:Expression
}

//multiplicative expression
class DivExpression(val left:Expression,val right:Expression) extends BinaryExpression // a/b
class MulExpression(val left:Expression,val right:Expression) extends BinaryExpression // a*b
class ModExpression(val left:Expression,val right:Expression) extends BinaryExpression // a%b

//additive expression
class AddExpression(val left:Expression,val right:Expression) extends BinaryExpression // a+b
class SubExpression(val left:Expression,val right:Expression) extends BinaryExpression // a-b

//shit expression
class LeftShiftExpression(val left:Expression,val right:Expression) extends BinaryExpression // a<<b
class RightShiftExpression(val left:Expression,val right:Expression) extends BinaryExpression // a>>b
class UnsignedRightShift(val left:Expression,val right:Expression) extends BinaryExpression // a>>>b

//relation expression
class LtExpression(val left:Expression,val right:Expression) extends BinaryExpression // a<b
class GtExpression(val left:Expression,val right:Expression) extends BinaryExpression // a>b
class LeExpression(val left:Expression,val right:Expression) extends BinaryExpression // a<=b
class GeExpression(val left:Expression,val right:Expression) extends BinaryExpression // a>=b

//equality expression
class EqExpression(val left:Expression,val right:Expression) extends BinaryExpression // a==b
class NotEqExpression(val left:Expression,val right:Expression) extends BinaryExpression // a!=b

//bit and expression
class BitAndExpression(val left:Expression,val right:Expression) extends BinaryExpression // a & b

//exclusive or expression
class CaretExpression(val left:Expression,val right:Expression) extends BinaryExpression // a ^ b

//inclusive or expression
class BitOrExpression(val left:Expression,val right:Expression) extends BinaryExpression // a | b

//and expression
class AndExpression(val left:Expression,val right:Expression) extends BinaryExpression // a && b

//or expression
class OrExpression(val left:Expression,val right:Expression) extends BinaryExpression // a || b

//ternary expression
trait TernaryExpression extends Expression{
    val left:Expression
    val mid:Expression
    val right:Expression
}

//if expression
class IfExpression(val left:Expression,
                   val mid:Expression,
                   val right:Expression) extends TernaryExpression

//func call expression
sealed trait FuncCall extends Expression {
    val funcName:String
    val params:List[Expression]
}

class ClazzFuncCall(types:List[DslType],
                    override val funcName:String,
                    override val params:List[Expression]) extends FuncCall
class LiteralFuncCall(literal: Literal,
                      override val funcName:String,
                      override val params:List[Expression]) extends FuncCall
class VarFuncCall(vars:List[VarRef],
                     override val funcName:String,
                     override val params:List[Expression]) extends FuncCall


//block expression
class Block(val expressions:List[Expression]) extends Expression

//for statement expression
class ForLoop(val loopVarDef:LocalVarDef,
              val loopVarAssign:Expression,
              val loopVarCondition:Expression,
              val loopVarUpdate:Expression,
              override val expressions: List[Expression]) extends Block(expressions)

class ForLoopCollection(val localVarDef: LocalVarDef,
                        val looped:Expression,
                        override val expressions: List[Expression]) extends Block(expressions)

class ForLoopMap(loopKeyDef:LocalVarDef,
                 loopValueDef:LocalVarDef,
                 looped:Expression,
                 override val expressions: List[Expression]) extends Block( expressions )

//while statement expression
class While(val condition:Expression,
            override val expressions: List[Expression]) extends Block(expressions)

//do while statement expression
class DoWhile(val condition:Expression,
              override val expressions: List[Expression]) extends Block(expressions)

//break statement
object BreakStatement extends Expression

//continue statement
object ContinueStatement extends Expression

//throw statement
class ThrowStatement(val expression: Expression) extends Expression

//return statement
class ReturnStatement(val expression: Expression) extends Expression

//assert statement
class AssertStatement(val expression: Expression) extends Expression

//synchronized statement
class SynchronizedStatement(val condition:Expression,
                            override val expressions: List[Expression]) extends Block(expressions)

//try statement
class TryStatement(val tryBlock:Block,
                   val finalBlock:Block,
                   override val expressions: List[Expression]) extends Block( expressions )

