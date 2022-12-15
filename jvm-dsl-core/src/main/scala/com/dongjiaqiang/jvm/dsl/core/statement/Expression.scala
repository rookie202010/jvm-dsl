package com.dongjiaqiang.jvm.dsl.core.statement

import com.dongjiaqiang.jvm.dsl.core.`type`.DslType

sealed trait Expression extends Statement {

}

//value expression

case class LocalVarDef(name:String, dslType: DslType) extends Expression

case class VarRef(name:String) extends Expression

case class Literal(literal:Any,dslType: DslType) extends Expression

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
class BlockExpression(val expressions:List[Expression]) extends Expression

//for statement expression
class ForLoopExpression(val loopVarDef:LocalVarDef,
                        val loopVarAssign:Expression,
                        val loopVarCondition:Expression,
                        val loopVarUpdate:Expression,
                        override val expressions: List[Expression]) extends BlockExpression(expressions)

class ForLoopCollectionExpression(val localVarDef: LocalVarDef,
                                  val looped:Expression,
                                  override val expressions: List[Expression]) extends BlockExpression(expressions)

class ForLoopMapExpression(loopKeyDef:LocalVarDef,
                                loopValueDef:LocalVarDef,
                                looped:Expression,
                                override val expressions: List[Expression]) extends BlockExpression( expressions )

//while statement expression
class WhileExpression(val condition:Expression,
                      override val expressions: List[Expression]) extends BlockExpression(expressions)

//do while statement expression
class DoWhileExpression(val condition:Expression,
                        override val expressions: List[Expression]) extends BlockExpression(expressions)

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
                            override val expressions: List[Expression]) extends BlockExpression(expressions)

//try statement
class TryStatement(val tryBlock:BlockExpression,
                   val finalBlock:BlockExpression,
                   override val expressions: List[Expression]) extends BlockExpression( expressions )

