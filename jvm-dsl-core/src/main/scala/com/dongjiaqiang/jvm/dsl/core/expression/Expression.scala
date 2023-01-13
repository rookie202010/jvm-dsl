package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.core.`type`._
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.scope.{FieldScope, MethodScope}

import scala.collection.mutable.ArrayBuffer

sealed trait Expression {
    def visit[T](expressionVisitor: ExpressionVisitor[T]):T = {
        expressionVisitor.visit(this)
    }
}


/**
 * <p>local var def should appear within a block expression including within a method body within a lambda expression</p>
 *
 *
 * <pre><code>
 * program{
 * def method()=Unit{
 * Int i = foo()*10; //LocalVarDef
 * Long j; //LocalVarDef
 * }
 * }<pre><code>
 */
case class LocalVarDef(fieldScope: FieldScope, dslType: DslType, assigned: Option[Expression]) extends Expression {
    override def toString: String = assigned match {
        case Some( v ) ⇒ s"${fieldScope.dslType} ${fieldScope.symbolName} = $v"
        case None ⇒ s"${fieldScope.dslType} ${fieldScope.symbolName}"
    }
}

/**
 * <pre><code>
 * program{
 * Foo foo = new Foo(a,b);
 *
 * def method()=Unit{
 * Int i = foo()*10; //LocalVarDef
 * foo.a * 10; // foo.a => VarRef
 * i*10; // i => VarRef
 * }
 * }<pre><code>
 */
case class VarRef(name: List[String], dslType: DslType, fieldScope: FieldScope) extends Expression {
    override def toString: String = s"${name.mkString( "." )}"
}


/**
 * <pre><code>
 *program{
 *      Array[Foo] fooArray = [new Foo(2,3),new Foo(1,2)];
 *
 * def method()=Unit{
 * Int i =0;
 * Foo b = fooArray[i];  // fooArray[i] => ArrayVarRef
 * }
 *
 * }<pre><code>
 */
class ArrayVarRef(val indexExpression: Expression,
                  override val name: List[String],
                  override val dslType: DslType,
                  override val fieldScope: FieldScope) extends VarRef( name, dslType, fieldScope ) {
    override def toString: String = s"${name.mkString( "." )}[$indexExpression]"
}

/**
 * <pre><code>
 * program{
 * Map[Int,Foo] fooMap = {0:new Foo(2,3),1:new Foo(1,2)};
 *
 * def method()=Unit{
 * Int i =0;
 * Foo b = fooMap(i);  // fooMap(i) => MapVarRef
 * }
 * }<pre><code>
 */
class MapVarRef(val KeyExpression: Expression,
                override val name: List[String],
                override val dslType: DslType,
                override val fieldScope: FieldScope) extends VarRef( name, dslType, fieldScope ) {
    override def toString: String = s"${name.mkString( "." )}($KeyExpression)"
}


//literal expression

abstract class Literal[T, D <: DslType](val literal: T) extends Expression {
    val dslType: D

    def resolvedType(dslType: DslType): Literal[T, D] = {
        this
    }
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

object UnitLiteral extends Literal[Unit, UnitType.type] {
    override val dslType: UnitType.type = UnitType
}

class IntLiteral(literal: Int) extends Literal[Int, IntType.type]( literal ) {
    override val dslType: IntType.type = IntType

    override def toString: String = literal.toString
}

class LongLiteral(literal: Long) extends Literal[Long, LongType.type]( literal ) {
    override val dslType: LongType.type = LongType

    override def toString: String = literal.toString
}

class FloatLiteral(literal: Float) extends Literal[Float,FloatType.type](literal){
    override val dslType: FloatType.type = FloatType

    override def toString: String = literal.toString
}

class DoubleLiteral(literal: Double) extends Literal[Double,DoubleType.type ](literal){
    override val dslType: DoubleType.type = DoubleType

    override def toString: String = literal.toString
}

class StringLiteral(literal:String) extends Literal[String,StringType.type ](literal){
    override val dslType: StringType.type = StringType

    override def toString: String = literal
}

class CharLiteral(literal: Char) extends Literal[Char,CharType.type ](literal){
    override val dslType: CharType.type = CharType

    override def toString: String = literal.toString
}

class BoolLiteral(literal: Boolean) extends Literal[Boolean,BoolType.type](literal){
    override val dslType: BoolType.type = BoolType

    override def toString: String = literal.toString
}

/**
 *
 *<pre><code>
 *program{
 *      class Foo(Int a,Int b){
 *          def foo()=Int{
 *              return a+b;
 *          }
 *      }
 *
 * def method(Int a,Int b)=Unit{
 * Foo foo = new Foo(a,b); // new Foo(a,b) => ClazzLiteral
 * Foo foo = new Foo(1,2); // new Foo(1,2) => ClazzLiteral
 * }
 * }<pre><code>
 */
class ClazzLiteral(literal: Array[Expression],
                   override val dslType: ClazzType) extends Literal[Array[Expression], ClazzType]( literal ) {
    override def toString: String = s"new ${dslType.clazzName}(${literal.mkString( "," )})"

    override def resolvedType(dslType: DslType): Literal[Array[Expression], ClazzType] = {
        if (this.dslType.valueTypes.contains( UnResolvedType ) || this.dslType.valueTypes.isEmpty) {
            new ClazzLiteral( literal, dslType.asInstanceOf[ClazzType] )
        } else {
            this
        }
    }
}

/**
 * <pre><code>
 * program{
 * class Foo(Int a,Int b){
 * def foo()=Int{
 * return a+b;
 * }
 *
 * def method(Int a,Int b)=Unit{
 * Array[Foo] array = new Array(new Foo(1,2),new Foo(2,3)); // new Array(new Foo(1,2),new Foo(2,3)) => ArrayLiteral
 * }
 * <pre><code>
 */
class ArrayLiteral(literal: Array[Expression], override val dslType: ArrayType) extends Literal[Array[Expression], ArrayType]( literal ) {
    override def toString: String = s"new Array(${literal.mkString( "," )})"

    override def resolvedType(dslType: DslType): Literal[Array[Expression], ArrayType] = {
        if (this.dslType.valueType == UnResolvedType) {
            new ArrayLiteral( literal, dslType.asInstanceOf[ArrayType] )
        } else {
            this
        }
    }
}

class EitherLiteral(literal: Either[Expression, Expression],
                    override val dslType: EitherType)
  extends Literal[Either[Expression, Expression], EitherType]( literal ) {

    override def toString: String = {
        literal match {
            case Left( left ) ⇒ s"Left($left)"
            case Right( right ) ⇒ s"Right($right)"
        }
    }

    override def resolvedType(dslType: DslType):
    Literal[Either[Expression, Expression], EitherType] = {
        if (this.dslType.leftType == UnResolvedType || this.dslType.rightType == UnResolvedType) {
            new EitherLiteral( literal, dslType.asInstanceOf[EitherType] )
        } else {
            this
        }
    }

}

/**
 *
 * <pre><code>
 * program{
 * class Foo(Int a,Int b){
 * def foo()=Int{
 * return a+b;
 *          }
 *      }
 *
 *      def method(Foo foo)=Unit{
 *          Option[Foo] option = ?foo; // ?foo => OptionLiteral
 *      }
 *}<pre><code>
 */
class OptionLiteral(literal:Expression,
                    override val dslType:OptionType) extends Literal[Expression,OptionType](literal) {
    override def toString: String = s"?$literal"

    override def resolvedType(dslType: DslType): Literal[Expression, OptionType] = {
        if (this.dslType.valueType == UnResolvedType) {
            new OptionLiteral( literal, dslType.asInstanceOf[OptionType] )
        } else {
            this
        }
    }
}

/**
 *
 *<pre><code>
 *program{
 *      class Foo(Int a,Int b){
 *          def foo()=Int{
 *              return a+b;
 *          }
 *      }
 *      def method()=Unit{
 *          List[Foo] option = [new Foo(1,2),new Foo(2,3)]; // [new Foo(1,2),new Foo(2,3)] => ListLiteral
 *      }
 *}<pre><code>
 */
class ListLiteral(literal:Array[Expression],
                  override val dslType:ListType) extends Literal[Array[Expression],ListType](literal) {
    override def toString: String = s"[${literal.mkString( "," )}]"

    override def resolvedType(dslType: DslType): Literal[Array[Expression], ListType] = {
        if (this.dslType.valueType == UnResolvedType) {
            new ListLiteral( literal, dslType.asInstanceOf[ListType] )
        } else {
            this
        }
    }
}

/**
 *
 * <pre><code>
 * program{
 *
 * def method()=Unit{
 * Set[Int] set = {1,2,3}; //{1,2,3} => SetLiteral
 * }
 * }<pre><code>
 */
class SetLiteral(literal: Array[Expression],
                 override val dslType: SetType) extends Literal[Array[Expression], SetType]( literal ) {
    override def toString: String = s"(${literal.mkString( "," )})"

    override def resolvedType(dslType: DslType): Literal[Array[Expression], SetType] = {
        if (this.dslType.valueType == UnResolvedType) {
            new SetLiteral( literal, dslType.asInstanceOf[SetType] )
        } else {
            this
        }
    }

}

/**
 *
 *<pre><code>
 *program{
 *
 *      def method()=Unit{
 *          (Int,Long,String) tuple = (1,12L,"xx"); //(1,12L,"xx") => TupleLiteral
 *      }
 *
 *}<pre><code>
 */
class TupleLiteral(literal: Array[Expression],
                   override val dslType: TupleType) extends Literal[Array[Expression], TupleType]( literal ) {
    override def toString: String = s"(${literal.mkString( "," )})"

    override def resolvedType(dslType: DslType): Literal[Array[Expression], TupleType] = {
        if (this.dslType.valueTypes.contains( UnResolvedType )) {
            new TupleLiteral( literal, dslType.asInstanceOf[TupleType] )
        } else {
            this
        }
    }
}

/**
 *<pre><code>
 *program{
 *      Map[Int,Foo] fooMap = {0:new Foo(2,3),1:new Foo(1,2)}; //{0:new Foo(2,3),1:new Foo(1,2)} => MapLiteral
 *
 *      def method()=Unit{
 *          Int i =0;
 *          Foo b = fooMap(i);  // fooMap(i) => MapVarRef
 *      }
 *}<pre><code>
 */
class MapLiteral(literal: Array[(Expression, Expression)],
                 override val dslType: MapType) extends Literal[Array[(Expression, Expression)], MapType]( literal ) {
    override def toString: String = s"{${literal.map( kv ⇒ kv._1.toString + ":" + kv._2.toString ).mkString( "," )}}"

    override def resolvedType(dslType: DslType): Literal[Array[(Expression, Expression)], MapType] = {
        if (this.dslType.keyType == UnResolvedType || this.dslType.valueType == UnResolvedType) {
            new MapLiteral( literal, dslType.asInstanceOf[MapType] )
        } else {
            this
        }
    }
}

//async expression

/**
 * <pre><code>
 * program{
 * def method()=Unit{
 * Future[Int] future = Async{ // Async{ ... } => Async
 * return foo();
 * }
 * }
 * }<pre><code>
 */
case class Async(body: Block, executor: Option[FieldScope], dslType: FutureType) extends Expression {
    override def toString: String = s"Async $body"

}


//try expression

/**
 *<pre><code>
 *program{
 *      def method()=Unit{
 *          Try[Int] maySuccess = Try{ // Try{ ... } => Try
 * return foo();
 * }
 * }
 * }<pre><code>
 */
case class Try(body: Block, dslType: TryType) extends Expression {
    override def toString: String = s"Try $body"

}

//custom block expression

/**
 * <pre><code>
 * program{
 * def method()=Unit{
 * String body = "body";
 * Int len = body.length;
 * Json maySuccess = Json{ // Json{ ... } => Json
 * {"body":body,"len":len}
 * }
 * }
 * }<pre><code>
 */
case class CustomBlockExpression(name: String, body: Block, param: Option[FieldScope]) extends Expression {
    override def toString: String = s"$name $body"
}


//lambda expression


/**
 * <pre><code>
 * program{
 * def method()=Unit{
 * Int=>Int lambda = p=>{  // p=> { ... } => Lambda
 *              return p*2;
 *          }
 *          lambda.apply(2);
 *      }
 *}<pre><code>
 */
case class Lambda(inputs: Array[String], body: Block) extends Expression{
    override def toString: String =
        s"""
           |(${inputs.mkString(",")}=>$body
           |""".stripMargin
}


//match case expression

/**<pre><code>
 * a=>{
 *      case d:Int=> {}
 * }
 * <pre><code>
 *@see MatchCase
 */
case class MatchType(name: String, dslType: DslType) extends Expression{
    override def toString: String = s"$name:$dslType"
}

/**
 * <pre><code>
 * a=>{
 * case one:two:tail=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchHead(head: Array[Either[Expression, String]], tail: Either[Expression, String]) extends Expression{
    override def toString: String = s"${getString("","",":",head)}:${tail match {
        case Left(i)⇒i.toString
        case Right(j)⇒j
    }}"
}

/**
 * <pre><code>
 * a=>{
 * case [1,2,b]=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchList(expressions: Array[Either[Expression, String]]) extends Expression{
    override def toString: String = getString("[","]",",",expressions)
}

/**
 * <pre><code>
 * a=>{
 * case (a1,a2,a3)=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchTuple(expressions: Array[Either[Expression, String]]) extends Expression{
    override def toString: String = getString("(",")",",",expressions)
}

/**
 * <pre><code>
 * a=>{
 * case Foo(d,c,b)=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchClass(dslType: DslType, expressions: Array[Either[Expression, String]]) extends Expression{
    override def toString: String = s"${dslType.asInstanceOf[ClazzType].clazzName}${getString("(",")",",",expressions)}"
}


/**
 *<pre><code>
 *program{
 *    def method(a:Any)=Int{
 *          a match {               // a match { ... } => MatchCases
 *              case i:Int=>{ ... }
 *              case (i,j,k)=>{ ... }
 *              case [e1,e2,e3]=>{ ... }
 *              default=>{ ... }
 *          }
 *    }
 *}
 *<pre><code>
 */
case class MatchCase(matched: VarRef, cases: Array[(Expression, Block)], default: Option[Block]) extends Expression{
    override def toString: String =
        s"""
          |$matched match {
          |     ${cases.map(`case`⇒{
            "case "+`case`._1.toString+" => "+`case`._2.toString
        }).mkString("\n")}
          |  ${if(default.isDefined) "default =>"+ default.get}
          |}
          |""".stripMargin
}

//assign expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          Int i = foo()*10; // Int i = foo()*10; => Assign
 *      }
 * }
 * <pre><code>
 */
case class Assign(varRef: VarRef, assigned: Expression) extends Expression{
    override def toString: String = s"$varRef = $assigned"
}


//unary expression

/**
 * <p>unary expression include negate, opposite, cast,instanceof and paren operator</p>
 */
sealed trait UnaryExpression extends Expression {
    val child:Expression
}

/**
 * <pre><code>
 *program{
 *      def method(Bool j)=Unit{
 *          Bool i = !j; // !j => Negate
 *      }
 * }
 * <pre><code>
 */
case class Negate(child:Expression) extends UnaryExpression{
    override def toString: String = s"!$child"
}

/**
 * <pre><code>
 *program{
 *      def method(Long j)=Unit{
 *          Long i = (-j)*2; // (-j) => Opposite
 *      }
 * }
 * <pre><code>
 */
case class Opposite(child: Expression) extends UnaryExpression{
    override def toString: String = s"(-$child)"
}

/**
 * <pre><code>
 *program{
 *      def method(Any j)=Unit{
 *          Foo i = (Foo)j; // (Foo)j => Cast
 *      }
 * }
 * <pre><code>
 */
case class Cast(child: Expression, castType: DslType) extends UnaryExpression{
    override def toString: String = s"($castType)$child"
}

/**
 * <pre><code>
 *program{
 *      def method(Any j)=Unit{
 *          bool i = j instanceof Foo; // j instanceof Foo => Instanceof
 *      }
 * }
 * <pre><code>
 */
case class Instanceof(child: Expression,judgeType: DslType) extends UnaryExpression{
    override def toString: String = s"$child instanceof $judgeType"
}

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          Long a=1;
 *          Long b=1;
 *          Long c=2;
 *          Long i = (a+b)*c; // (a+b) => Paren
 *      }
 * }
 * <pre><code>
 */
case class Paren(child:Expression) extends UnaryExpression{
    override def toString: String = s"($child)"
}

//binary expression
sealed trait BinaryExpression extends Expression{
    val left:Expression
    val right:Expression
}

//multiplicative expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          Long j = 1/2; // 1/2 => Div
 *          Long k = a%b; // a%b => Mod
 *          Long i = (a+b)*c; // (a+b)*c => Mul
 *      }
 * }
 * <pre><code>
 */
case class Div(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left/$right"
}
case class Mul(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left*$right"
}
case class Mod(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left%$right"
}

//additive expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          Long j = 1-2; // 1-2 => Sub
 *          Long i = (a+b)*c; // a+b => Add
 *      }
 * }
 * <pre><code>
 */
case class Add(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left+$right"
}
case class Sub(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left-$right"
}

//shit expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          Int j = 1&lt;&lt;2; // 1&lt;&lt;2 => LeftShit
 *          Int k = 1>>2; // 1>>2 => RightShift
 *          Int i = 1>>>2; // 1>>>2 => UnsignedRightShift
 *      }
 * }
 * <pre><code>
 */
case class LeftShift(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left<<$right"
}
case class RightShift(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left>>$right"
}
case class UnsignedRightShift(left:Expression,right:Expression) extends BinaryExpression{
    override def toString: String = s"$left>>>$right"
}

//relation expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          Bool j = 1&lt;2; // 1&lt;2 => Lt
 *          Bool k = 1>2; // 1>2 => Gt
 *          Bool i = 1&lt;=2; // 1&lt;=2 => Le
 *          Bool z = 1>=2; // 1>=2 => Ge
 *      }
 * }
 * <pre><code>
 */

case class Lt(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left<$right"
}
case class Gt(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left>$right"
}
case class Le(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left<=$right"
}
case class Ge(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left>=$right"
}

//equality expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          Bool j = 1==2; // 1==2 => Eq
 *          Bool k = 1!=2; // 1!=2 => NotEq
 *      }
 * }
 * <pre><code>
 */

case class Eq(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left ==  $right"
}
case class NotEq(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left !=  $right"
}

//bit and expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          Int value = 1 & 2; // 1 & 2 => BitAnd
 *      }
 * }
 * <pre><code>
 */

case class BitAnd(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left &   $right"
}

//exclusive or expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          Int value = 1 &#94 2; // 1 &#94 2 => BitOr
 *      }
 * }
 * <pre><code>
 */
case class Caret(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left ^   $right"
}

//inclusive or expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          Int value = 1|2; // 1|2 => BitOr
 *      }
 * }
 * <pre><code>
 */
case class BitOr(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left |   $right"
}

//and expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          bool condition = 1>2 && 2>1; // 1>2 && 2>1 => And
 *      }
 * }
 * <pre><code>
 */
case class And(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left &&  $right"
}

//or expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          bool condition = 1>2 || 2>1; // 1>2 || 2>1 => Or
 *      }
 * }
 * <pre><code>
 */
case class Or(left:Expression, right:Expression) extends BinaryExpression{
    override def toString: String = s"$left ||  $right"
}


//call chains expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          foo().a.bar(); // foo().a.bar() => MethodCall chain
 *          a.foo().b.a.bar(); // a.foo().b.a.bar() => VarCall chain
 *          A.foo().b.bar().a; // A.foo().b.bar().a => StaticCall chain
 *          "xxx".size(); // "xxx".size() => LiteralCall chain
 *      }
 * }
 * <pre><code>
 */
case class FuncCallChain(head:FuncCall, tails:List[Part]) extends Expression{
    override def toString: String = s"$head.${tails.mkString(".")}"
}

class LiteralCallChain[T,D<:DslType](val head:Literal[T,D],val tails:List[Part]) extends Expression{
    override def toString: String = s"$head.${tails.mkString(".")}"
}

class IntLiteralCallChain(override val head:IntLiteral,override val tails:List[Part])
  extends LiteralCallChain[Int,IntType.type ](head, tails)

class LongLiteralCallChain(override val head:LongLiteral,override val tails:List[Part])
  extends LiteralCallChain[Long,LongType.type ](head, tails)

class FloatLiteralCallChain(override val head:FloatLiteral,override val tails:List[Part])
  extends LiteralCallChain[Float,FloatType.type ](head, tails)

class DoubleLiteralCallChain(override val head:DoubleLiteral,override val tails:List[Part])
  extends LiteralCallChain[Double,DoubleType.type ](head, tails)

class StringLiteralCallChain(override val head:StringLiteral,override val tails:List[Part])
  extends LiteralCallChain[String,StringType.type ](head, tails)

class CharLiteralCallChain(override val head:CharLiteral,override val tails:List[Part])
  extends LiteralCallChain[Char,CharType.type ](head, tails)

class BoolLiteralCallChain(override val head:BoolLiteral,override val tails:List[Part])
  extends LiteralCallChain[Boolean,BoolType.type ](head, tails)

class ClazzLiteralCallChain(override val head:ClazzLiteral,override val tails:List[Part])
  extends LiteralCallChain[Array[Expression],ClazzType](head, tails)

class OptionLiteralCallChain(override val head:OptionLiteral,override val tails:List[Part])
  extends LiteralCallChain[Expression,OptionType](head, tails)

class ListLiteralCallChain(override val head:ListLiteral,override val tails:List[Part])
  extends LiteralCallChain[Array[Expression],ListType](head, tails)

class SetLiteralCallChain(override val head:SetLiteral,override val tails:List[Part])
  extends LiteralCallChain[Array[Expression],SetType](head, tails)

class TupleLiteralCallChain(override val head:TupleLiteral,override val tails:List[Part])
  extends LiteralCallChain[Array[Expression],TupleType](head, tails)

class MapLiteralCallChain(override val head:MapLiteral,override val tails:List[Part])
  extends LiteralCallChain[Array[(Expression,Expression)],MapType](head, tails)

trait Part{
    val name:String
}

class VarName(override val name:String) extends Part{
    override def toString: String = name
}

//func call expression
trait FuncCall extends Expression {
    val name:String
    val params:Array[Expression]
}

//method call expression
class MethodCall(val methodScope:MethodScope,
                 override val name:String,
                 override val params:Array[Expression]) extends FuncCall with Part{
    override def toString: String = getString("",name, params)
}


//static call expression
class StaticCall(val `type`:DslType,
                 override val name:String,
                 override val params:Array[Expression]) extends FuncCall{
    override def toString: String = getString(`type`,name, params)
}

//literal call expression
class LiteralCall(val literal: Expression,
                  override val name:String,
                  override val params:Array[Expression]) extends FuncCall{
    override def toString: String = getString(literal,name, params)
}

//var call expression
class VarCall(val varRef:VarRef,
              override val name:String,
              override val params:Array[Expression]) extends FuncCall{
    override def toString: String = getString(varRef,name, params)
}


//block expression

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *         {
 *              Int j = 10; // {..} => Block
 *         }
 *      }
 * }
 * <pre><code>
 */
class Block(val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Expression{
    override def toString: String =
        s"""
          |{
          |     ${expressions.map(e⇒e+";").mkString("\n")}
          |}
          |""".stripMargin
}

//for statement expression

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *         for(Int i=0;i<100;i++){  // for(...){...} => ForLoop
 *              foo();
 *         }
 *      }
 * }
 * <pre><code>
 */

class ForLoop(val loopVarDef:LocalVarDef,
              val loopVarCondition:Expression,
              val loopVarUpdate:Expression,
              override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions){
    override def toString: String =
        s"""
          |for($loopVarDef;$loopVarCondition;$loopVarUpdate){
          |     ${expressions.map(e⇒e+";").mkString("\n")}
          |}
          |""".stripMargin
}

case class For(loopVarDef:LocalVarDef,
               loopVarCondition:Expression,
               loopVarUpdate:Expression,
               body:Block) extends Expression{
    override def toString: String =
        s"""
          |for($loopVarDef;$loopVarCondition;$loopVarUpdate)
          | $body
          |""".stripMargin
}

/**
 * <pre><code>
 *program{
 *      def method(List[Int] list)=Unit{
 *         for(Int i:list){  // for(...){...} => ForLoopCollection
 *              foo();
 *         }
 *      }
 * }
 * <pre><code>
 */

class ForLoopCollection(val localVarDef: LocalVarDef,
                        val looped:Expression,
                        override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions){
    override def toString: String =
        s"""
          |for($localVarDef:$looped){
          |    ${expressions.map(e⇒e+";").mkString("\n")}
          |}
          |""".stripMargin
}

case class ForCollection(localVarDef: LocalVarDef,
                    looped:Expression,body:Block) extends Expression{
    override def toString: String =
        s"""
          |for($localVarDef:$looped)
          | $body
          |""".stripMargin
}
/**
 * <pre><code>
 *program{
 *      def method(Map[Int,String] map)=Unit{
 *         for(Int key,String value:map){  // for(...){...} => ForLoopMap
 *              foo();
 *         }
 *      }
 * }
 * <pre><code>
 */
class ForLoopMap(val loopKeyDef:LocalVarDef,
                 val loopValueDef:LocalVarDef,
                 val looped:Expression,
                 override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions){
    override def toString: String =
        s"""
          |for($loopKeyDef,$loopValueDef:$looped){
          |     ${expressions.map(e⇒e+";").mkString("\n")}
          |}
          |""".stripMargin
}

case class ForMap(loopKeyDef:LocalVarDef,
             loopValueDef:LocalVarDef,
             looped:Expression,body:Block) extends Expression{
    override def toString: String =
        s"""
          |for($loopKeyDef,$loopValueDef:$looped)
          |     $body
          |""".stripMargin
}

//while statement expression

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *          while(i>0){         //while(i>0) => WhileCondition
 *              if(i==10){
 *                  break;  // {...} => WhileBlock
 *              }
 *              foo();
 *              i++;
 *          }
 *      }
 * }        while(...){...} => While
 * <pre><code>
 */

class WhileCondition(val expression: Expression) extends Expression{
    override def toString: String = s"while($expression)"
}
class WhileBlock(override val expressions:ArrayBuffer[Expression] = new ArrayBuffer[Expression]()) extends Block(expressions){
    override def toString: String = s"${expressions.map(e⇒e+";").mkString("\n")}"
}

case class While(condition: Expression, body:Block) extends Expression{
    override def toString: String =
        s"""
          |while($condition)
          |$body
          |""".stripMargin
}

//do while statement expression

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *          do{                 //do{...} => DoWhileBlock
 *              if(i==10){
 *                  continue;
 *              }
 *              foo();
 *              i++;
 *          }while(i<20);   //while(i<20) => DoWhileCondition
 *      }
 * }    //do{...}while(..) => //DoWhile
 * <pre><code>
 */

class DoWhileCondition(val expression: Expression) extends Expression{
    override def toString: String = s"while($expression)"
}
class DoWhileBlock(override val expressions:ArrayBuffer[Expression] = new ArrayBuffer[Expression]()) extends Block(expressions){
    override def toString: String =
        s"""
          |do{
          |    ${expressions.map(e⇒e+";").mkString("\n")}
          |};
          |""".stripMargin
}

case class DoWhile(condition:Expression,body:Block) extends Expression{
    override def toString: String =
        s"""
          |do
          | $body
          |while($condition)
          |""".stripMargin
}

//break statement

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *          while(i>0){
 *              if(i==10){
 *                  break;  // break => Break
 *              }
 *              foo();
 *              i++;
 *          }
 *      }
 * }
 * <pre><code>
 */

object Break extends Expression{
    override def toString: String = "break"
}

//continue statement

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *          while(i>0){
 *              if(i==10){
 *                  continue;  // continue => Continue
 *              }
 *              foo();
 *              i++;
 *          }
 *      }
 * }
 * <pre><code>
 */

object Continue extends Expression{
    override def toString: String = "continue"
}

//throw statement

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *          throw new Exception(); // throw ... => Throw
 *      }
 * }
 * <pre><code>
 */

case class Throw(expression: Expression) extends Expression{
    override def toString: String = s"throw $expression"
}

//return statement

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *          return i;   // return ... => Return
 *      }
 * }
 * <pre><code>
 */

case class Return(expression: Expression) extends Expression{
    override def toString: String = s"return $expression"
}

//assert statement

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *          assert i==1; // assert ... => Assert
 *      }
 * }
 * <pre><code>
 */
case class Assert(expression: Expression) extends Expression{
    override def toString: String = s"assert $expression"
}

//synchronized statement

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *        sync(lock){   //sync(lock) => SyncCondition
 *             foo();   //{ foo();} => SyncBlock
 *        }
 *      }   //sync(...){ ...} => Sync
 * }
 * <pre><code>
 */

class SyncCondition(val expression: Expression) extends Expression{
    override def toString: String = s"sync($expression)"
}
class SyncBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions){
    override def toString: String =
        s"""
          |{
          |   ${expressions.map(e⇒e+";").mkString("\n")}
          |}
          |""".stripMargin
}

case class Sync(condition: Expression,body:Block) extends Expression{
    override def toString: String =
        s"""
          |sync($condition)
          | $body
          |""".stripMargin
}

//if statement

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *           if(i==0){          // i==0 => IfCondition
 *              foo();          // { foo(); } => IfBlock
 *           }else if(i<0){
 *              foo1();
 *           }else{
 *              foo2();
 *           }
 *      }
 * }         //if(...){...}else if(...){...}else{...} => If
 * <pre><code>
 */

class IfCondition(val expression: Expression,val first:Boolean) extends Expression{
    override def toString: String = if(first) s"if($expression)" else s"else if($expression)"
}
class IfBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions) {
    override def toString: String =
        s"""
           |{
           |    ${expressions.map(e ⇒ e + ";").mkString("\n")}
           |}
           |""".stripMargin
}

case class If(cases:Array[(Expression,Block)], default:Option[Block]) extends Expression{

}

//try statement

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          try{                       //   try { foo(); } => TryBlock
 *              foo();                 //   catch(Exception1 ex) => CatchParameter
 *          }catch(Exception1 ex){     //   {   handleEx1();   } => CatchBlock
 *              handleEx1();           //
 *          }catch(Exception2 ex){     //
 *              handleEx2();           //
 *          }finally{                  //   {   handleFinally();    } => FinallyBlock
 *              handleFinally();       //
 *          }                          //
 *      }
 * }         //try{...}catch(...){...}finally{...}=>TryCatch
 * <pre><code>
 */

class TryBlock(override val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Block( expressions ){
    override def toString: String =
        s"""
           |try{
           |    ${expressions.map(e⇒e+";").mkString("\n")}
           |}
           |""".stripMargin
}

class CatchParameter(val name: String, val dslType: DslType) extends Expression{
    override def toString: String =
        s"""
           |catch($dslType $name)
           |""".stripMargin
}

class CatchBlock(override val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Block( expressions ){
    override def toString: String =
        s"""
          |{
          |     ${expressions.map(e⇒e+";").mkString("\n")}
          |}
          |""".stripMargin
}

class FinallyBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions){
    override def toString: String =
        s"""
          |finally{
          |      ${expressions.map(e⇒e+";").mkString("\n")}
          |}
          |""".stripMargin
}

case class TryCatch(tryBlock:Block,catches:Array[((String,DslType),Block)],finallyBlock:Option[Block]) extends Expression {
    override def toString: String =
        s"""
           |try
           | $tryBlock

           |${catches.map{
            `catch`⇒ s"catch(${`catch`._1._2} ${`catch`._1._1}) ${`catch`._2}"
        }.mkString("\n")}

           |${
            finallyBlock match {
            case Some(
            block)⇒
                "finally"+block
            case None⇒""
        }}
          |""".stripMargin
}