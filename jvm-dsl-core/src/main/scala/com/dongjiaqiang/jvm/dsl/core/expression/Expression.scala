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
case class LocalVarDef(fieldScope: FieldScope, assigned: Option[Expression]) extends Expression

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
case class VarRef(name: List[String], fieldScope: FieldScope) extends Expression


/**
 * <pre><code>
 *program{
 *      Array[Foo] fooArray = [new Foo(2,3),new Foo(1,2)];
 *
 *      def method()=Unit{
 *              Int i =0;
 *              Foo b = fooArray[i];  // fooArray[i] => ArrayVarRef
 *      }
 *
 * }<pre><code>
 */
class ArrayVarRef(val indexExpression: Expression, override val name: List[String], override val fieldScope: FieldScope) extends VarRef( name, fieldScope )

/**
 *<pre><code>
 *program{
 *      Map[Int,Foo] fooMap = {0:new Foo(2,3),1:new Foo(1,2)};
 *
 *      def method()=Unit{
 *          Int i =0;
 *          Foo b = fooMap(i);  // fooMap(i) => MapVarRef
 *      }
 *}<pre><code>
 */
class MapVarRef(val KeyExpression: Expression, override val name: List[String], override val fieldScope: FieldScope) extends VarRef( name, fieldScope )


//literal expression

abstract class Literal[T, D <: DslType](val literal: T) extends Expression {
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
 *      def method(Int a,Int b)=Unit{
 *          Foo foo = new Foo(a,b); // new Foo(a,b) => ClazzLiteral
 *          Foo foo = new Foo(1,2); // new Foo(1,2) => ClazzLiteral
 *      }
 *}<pre><code>
 */
class ClazzLiteral(literal: Array[Expression],
                   override val dslType: ClazzType) extends Literal[Array[Expression],ClazzType](literal)

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
 *      def method(Foo foo)=Unit{
 *          Option[Foo] option = ?foo; // ?foo => OptionLiteral
 *      }
 *}<pre><code>
 */
class OptionLiteral(literal:Expression,
                    override val dslType:OptionType) extends Literal[Expression,OptionType](literal)

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
                  override val dslType:ListType) extends Literal[Array[Expression],ListType](literal)

/**
 *
 *<pre><code>
 *program{
 *
 *      def method()=Unit{
 *          Set[Int] set = (1,2,3); //(1,2,3) => SetLiteral
 *      }
 *}<pre><code>
 */
class SetLiteral(literal: Array[Expression],
                 override val dslType: SetType) extends Literal[Array[Expression], SetType]( literal )

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
                   override val dslType: TupleType) extends Literal[Array[Expression], TupleType]( literal )

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
                 override val dslType: MapType) extends Literal[Array[(Expression, Expression)], MapType]( literal )

//async expression

/**
 *<pre><code>
 *program{
 *      def method()=Unit{
 *          Future[Int] future = Async{ // Async{ ... } => Async
 *              return foo();
 *          }
 *      }
 *}<pre><code>
 */
case class Async(body: Block, executor: FieldScope, dslType: FutureType) extends Expression


//try expression

/**
 *<pre><code>
 *program{
 *      def method()=Unit{
 *          Try[Int] maySuccess = Try{ // Try{ ... } => Try
 *              return foo();
 *          }
 *      }
 *}<pre><code>
 */
case class Try(body: Block, dslType: TryType) extends Expression


//lambda expression


/**
 *<pre><code>
 *program{
 *      def method()=Unit{
 *          Int=>Int lambda = p=>{  // p=> { ... } => Lambda
 *              return p*2;
 *          }
 *          lambda.apply(2);
 *      }
 *}<pre><code>
 */
case class Lambda(inputs: Array[String], body: Block) extends Expression


//match case expression

/**<pre><code>
 * a=>{
 *      case d:Int=> {}
 * }
 * <pre><code>
 *@see MatchCase
 */
case class MatchType(name: String, dslType: DslType) extends Expression

/**
 * <pre><code>
 * a=>{
 * case one:two:tail=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchHead(head: Array[Either[Expression, String]], tail: Either[Expression, String]) extends Expression

/**
 * <pre><code>
 * a=>{
 * case [1,2,b]=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchList(expressions: Array[Either[Expression, String]]) extends Expression

/**
 * <pre><code>
 * a=>{
 * case (a1,a2,a3)=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchTuple(expression: Array[Either[Expression, String]]) extends Expression

/**
 * <pre><code>
 * a=>{
 * case Foo(d,c,b)=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchClass(dslType: DslType, expression: Array[Either[Expression, String]]) extends Expression


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
case class MatchCase(matched: VarRef, cases: Array[(Expression, Block)], default: Option[Block]) extends Expression

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
case class Assign(varRef: VarRef, assigned: Expression) extends Expression


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
case class Negate(child:Expression) extends UnaryExpression

/**
 * <pre><code>
 *program{
 *      def method(Long j)=Unit{
 *          Long i = (-j)*2; // (-j) => Opposite
 *      }
 * }
 * <pre><code>
 */
case class Opposite(child: Expression) extends UnaryExpression

/**
 * <pre><code>
 *program{
 *      def method(Any j)=Unit{
 *          Foo i = (Foo)j; // (Foo)j => Cast
 *      }
 * }
 * <pre><code>
 */
case class Cast(child: Expression, castType: DslType) extends UnaryExpression

/**
 * <pre><code>
 *program{
 *      def method(Any j)=Unit{
 *          bool i = j instanceof Foo; // j instanceof Foo => Instanceof
 *      }
 * }
 * <pre><code>
 */
case class Instanceof(child: Expression,judgeType: DslType) extends UnaryExpression //a instance Int

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
case class Paren(child:Expression) extends UnaryExpression

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
case class Div(left:Expression, right:Expression) extends BinaryExpression
case class Mul(left:Expression, right:Expression) extends BinaryExpression
case class Mod(left:Expression, right:Expression) extends BinaryExpression

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
case class Add(left:Expression, right:Expression) extends BinaryExpression
case class Sub(left:Expression, right:Expression) extends BinaryExpression

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
case class LeftShift(left:Expression, right:Expression) extends BinaryExpression
case class RightShift(left:Expression, right:Expression) extends BinaryExpression
case class UnsignedRightShift(left:Expression,right:Expression) extends BinaryExpression

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

case class Lt(left:Expression, right:Expression) extends BinaryExpression
case class Gt(left:Expression, right:Expression) extends BinaryExpression
case class Le(left:Expression, right:Expression) extends BinaryExpression
case class Ge(left:Expression, right:Expression) extends BinaryExpression

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

case class Eq(left:Expression, right:Expression) extends BinaryExpression
case class NotEq(left:Expression, right:Expression) extends BinaryExpression

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

case class BitAnd(left:Expression, right:Expression) extends BinaryExpression

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
case class Caret(left:Expression, right:Expression) extends BinaryExpression

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
case class BitOr(left:Expression, right:Expression) extends BinaryExpression

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
case class And(left:Expression, right:Expression) extends BinaryExpression

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
case class Or(left:Expression, right:Expression) extends BinaryExpression


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
case class FuncCallChain(head:FuncCall, tails:List[Part]) extends Expression

class LiteralCallChain[T,D<:DslType](val head:Literal[T,D],val tails:List[Part]) extends Expression

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

class VarName(override val name:String) extends Part

//func call expression
trait FuncCall extends Expression {
    val name:String
    val params:Array[Expression]
}

//method call expression
class MethodCall(val methodScope:MethodScope,
                 override val name:String,
                 override val params:Array[Expression]) extends FuncCall with Part


//static call expression
class StaticCall(val `type`:DslType,
                 override val name:String,
                 override val params:Array[Expression]) extends FuncCall

//literal call expression
class LiteralCall(val literal: Expression,
                  override val name:String,
                  override val params:Array[Expression]) extends FuncCall

//var call expression
class VarCall(val varRef:VarRef,
              override val name:String,
              override val params:Array[Expression]) extends FuncCall


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
class Block(val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Expression

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
              override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)

case class For(loopVarDef:LocalVarDef,
               loopVarCondition:Expression,
               loopVarUpdate:Expression,
               body:Block) extends Expression

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
                        override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)

case class ForCollection(localVarDef: LocalVarDef,
                    looped:Expression,body:Block) extends Expression
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
                 override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)

case class ForMap(loopKeyDef:LocalVarDef,
             loopValueDef:LocalVarDef,
             looped:Expression,body:Block) extends Expression

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

class WhileCondition(val expression: Expression) extends Expression
class WhileBlock(override val expressions:ArrayBuffer[Expression] = new ArrayBuffer[Expression]()) extends Block(expressions)

case class While(condition: Expression, body:Block) extends Expression

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

class DoWhileCondition(val expression: Expression) extends Expression
class DoWhileBlock(override val expressions:ArrayBuffer[Expression] = new ArrayBuffer[Expression]()) extends Block(expressions)

case class DoWhile(condition:Expression,body:Block) extends Expression

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

object Break extends Expression

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

object Continue extends Expression

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

case class Throw(expression: Expression) extends Expression

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

case class Return(expression: Expression) extends Expression

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
case class Assert(expression: Expression) extends Expression

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

class SyncCondition(val expression: Expression) extends Expression
class SyncBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)

case class Sync(condition: Expression,body:Block) extends Expression

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

class IfCondition(val expression: Expression,val first:Boolean) extends Expression
class IfBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)

case class If(cases:Array[(Expression,Block)], default:Option[Block]) extends Expression

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

class TryBlock(override val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Block( expressions )

class CatchParameter(val name: String, val dslType: DslType) extends Expression

class CatchBlock(override val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Block( expressions )
class FinallyBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions)

case class TryCatch(tryBlock:Block,catches:Array[((String,DslType),Block)],finallyBlock:Option[Block]) extends Expression