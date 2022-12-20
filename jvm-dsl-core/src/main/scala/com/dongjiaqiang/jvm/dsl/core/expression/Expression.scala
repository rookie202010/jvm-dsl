package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.core.`type`._
import com.dongjiaqiang.jvm.dsl.core.scope.FieldScope

import scala.collection.mutable.ArrayBuffer

sealed trait Expression {

}

//value expression

case class LocalVarDef(name: String, dslType: DslType, assigned: Option[Expression]) extends Expression

case class VarRef(name:List[String],fieldScope: FieldScope) extends Expression
case class ArrayVarRef(indexExpression:Expression,override val name:List[String],override  val fieldScope: FieldScope) extends VarRef(name, fieldScope)
case class MapVarRef(indexExpression:Expression,override val name:List[String],override  val fieldScope: FieldScope) extends VarRef(name, fieldScope)

//literal expression
abstract class Literal[T,D<:DslType](val literal:T) extends Expression{
    val dslType: D
}

object Literal {
    def literal(literal: Int): IntLiteral = new IntLiteral( literal )

    def literal(literal: Long): LongLiteral = new LongLiteral( literal )

    def literal(literal: Float): FloatLiteral = new FloatLiteral( literal )

    def literal(literal: Double): DoubleLiteral = new DoubleLiteral( literal )

    def literal(literal: Boolean): BoolLiteral = new BoolLiteral( literal )

    def literal(literal: String): StringLiteral = new StringLiteral( literal )

    def literal(literal: Char): CharLiteral = new CharLiteral( literal )
}

class IntLiteral(literal: Int) extends Literal[Int,IntType.type](literal){
    override val dslType: IntType.type = IntType
}

class LongLiteral(literal:Long) extends Literal[Long,LongType.type](literal){
    override val dslType: LongType.type = LongType
}

class FloatLiteral(literal: Float) extends Literal[Float,FloatType.type](literal){
    override val dslType: FloatType.type = FloatType
}

class DoubleLiteral(literal: Double) extends Literal[Double,DoubleType.type ](literal){
    override val dslType: DoubleType.type = DoubleType
}

class StringLiteral(literal:String) extends Literal[String,StringType.type ](literal){
    override val dslType: StringType.type = StringType
}

class CharLiteral(literal: Char) extends Literal[Char,CharType.type ](literal){
    override val dslType: CharType.type = CharType
}

class BoolLiteral(literal: Boolean) extends Literal[Boolean,BoolType.type](literal){
    override val dslType: BoolType.type = BoolType
}

class ClazzLiteral(literal: Array[Expression],
                   override val dslType: ClazzType) extends Literal[Array[Expression],ClazzType](literal)

class OptionLiteral(literal:Expression,
                    override val dslType:OptionType) extends Literal[Expression,OptionType](literal)

class ListLiteral(literal:Array[Expression],
                  override val dslType:ListType) extends Literal[Array[Expression],ListType](literal)

class SetLiteral(literal: Array[Expression],
                 override val dslType: SetType) extends Literal[Array[Expression],SetType](literal)

class TupleLiteral(literal: Array[Expression],
                   override val dslType:TupleType) extends Literal[Array[Expression],TupleType](literal)

class MapLiteral(literal:Array[(Expression,Expression)],
                 override val dslType: MapType) extends Literal[Array[(Expression,Expression)],MapType](literal)

class AsyncLiteral(literal:Block,executor:VarRef,
                   override val dslType: FutureType) extends Literal[Block,FutureType](literal)

//lambda expression

case class Lambda(inputs:Array[String], block: Block) extends Expression

//match case expression
case class MatchCase(matched:String,cases:List[(Expression,Block)],default:Option[Block]) extends Expression

//unapply clazz literal expression
case class UnapplyClazzLiteral(clazzType: ClazzType,literals:Array[Expression]) extends Expression

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
class Paren(val child:Expression) extends UnaryExpression // (a+c)

//binary expression
sealed trait BinaryExpression extends Expression{
    val left:Expression
    val right:Expression
}

//multiplicative expression
class Div(val left:Expression, val right:Expression) extends BinaryExpression // a/b
class Mul(val left:Expression, val right:Expression) extends BinaryExpression // a*b
class Mod(val left:Expression, val right:Expression) extends BinaryExpression // a%b

//additive expression
class Add(val left:Expression, val right:Expression) extends BinaryExpression // a+b
class Sub(val left:Expression, val right:Expression) extends BinaryExpression // a-b

//shit expression
class LeftShift(val left:Expression, val right:Expression) extends BinaryExpression // a<<b
class RightShift(val left:Expression, val right:Expression) extends BinaryExpression // a>>b
class UnsignedRightShift(val left:Expression,val right:Expression) extends BinaryExpression // a>>>b

//relation expression
class Lt(val left:Expression, val right:Expression) extends BinaryExpression // a<b
class Gt(val left:Expression, val right:Expression) extends BinaryExpression // a>b
class Le(val left:Expression, val right:Expression) extends BinaryExpression // a<=b
class Ge(val left:Expression, val right:Expression) extends BinaryExpression // a>=b

//equality expression
class Eq(val left:Expression, val right:Expression) extends BinaryExpression // a==b
class NotEq(val left:Expression, val right:Expression) extends BinaryExpression // a!=b

//bit and expression
class BitAnd(val left:Expression, val right:Expression) extends BinaryExpression // a & b

//exclusive or expression
class Caret(val left:Expression, val right:Expression) extends BinaryExpression // a ^ b

//inclusive or expression
class BitOr(val left:Expression, val right:Expression) extends BinaryExpression // a | b

//and expression
class And(val left:Expression, val right:Expression) extends BinaryExpression // a && b

//or expression
class Or(val left:Expression, val right:Expression) extends BinaryExpression // a || b

//call chains expression
class FuncCallChain(val head:FuncCall, val tails:List[Part]) extends Expression

class LiteralCallChain[T,D<:DslType](val head:Literal[T,D],val tails:List[Part]) extends Expression

trait Part{
    val name:String
}

class VarName(override val name:String) extends Part

//func call expression
trait FuncCall extends Expression {
    val name:String
    val params:List[Expression]
}

//method call expression
class MethodCall(override val name:String,
                 override val params:List[Expression]) extends FuncCall with Part


//static call expression
class StaticCall(val `type`:DslType,
                 override val name:String,
                 override val params:List[Expression]) extends FuncCall

//literal call expression
class LiteralCall(val literal: Expression,
                  override val name:String,
                  override val params:List[Expression]) extends FuncCall

//var call expression
class VarCall(val varRef:VarRef,
              override val name:String,
              override val params:List[Expression]) extends FuncCall


//block expression
class Block(val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Expression

//for statement expression
class ForLoop(val loopVarDef:LocalVarDef,
              val loopVarCondition:Expression,
              val loopVarUpdate:Expression,
              override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)

class ForLoopCollection(val localVarDef: LocalVarDef,
                        val looped:Expression,
                        override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)

class ForLoopMap(loopKeyDef:LocalVarDef,
                 loopValueDef:LocalVarDef,
                 looped:Expression,
                 override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block( expressions )

//while statement expression
class WhileCondition(val expression: Expression) extends Expression
class WhileBlock(override val expressions:ArrayBuffer[Expression] = new ArrayBuffer[Expression]()) extends Block(expressions)

//do while statement expression
class DoWhileCondition(val expression: Expression) extends Expression
class DoWhileBlock(override val expressions:ArrayBuffer[Expression] = new ArrayBuffer[Expression]()) extends Block(expressions)

//break statement
object Break extends Expression

//continue statement
object Continue extends Expression

//throw statement
class Throw(val expression: Expression) extends Expression

//return statement
class Return(val expression: Expression) extends Expression

//assert statement
class Assert(val expression: Expression) extends Expression

//synchronized statement
class SyncCondition(val expression: Expression) extends Expression
class SyncBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)

//if statement
class IfCondition(val expression: Expression,val first:Boolean) extends Expression
class IfBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)

//try statement
class TryBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)
class CatchCondition(val expression: Expression) extends Expression
class CatchBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)
class FinallyBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)

