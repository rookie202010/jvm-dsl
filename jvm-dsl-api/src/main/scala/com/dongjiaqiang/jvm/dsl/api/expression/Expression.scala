package com.dongjiaqiang.jvm.dsl.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParserException
import com.dongjiaqiang.jvm.dsl.api.expression.expression.getString
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.scope.{FieldScope, MethodScope, ProgramScope, ResolveField}

import scala.collection.mutable.ArrayBuffer
import scala.language.{existentials, postfixOps}


trait Expression {
    def visit[T](expressionVisitor: ExpressionVisitor[T]): T = {
        expressionVisitor.visit( this )
    }
}

/**
 *
 * <pre><code>
 * program{
 *      def method()=Unit{
 *          A a = null; // null is Null
 *      }
 * }
 * <pre><code>
 */
object Null extends Expression {
}

/**
 * <p>local var def should appear within a block expression including within a method body within a lambda expression</p>
 *
 *
 * <pre><code>
 * program{
 *      def method()=Unit{
 *          Int i = foo()*10; //LocalVarDef
 *          Long j; //LocalVarDef
 *      }
 * }<pre><code>
 */
case class LocalVarDef(fieldScope: FieldScope, dslType: DslType, assigned: Option[Expression]) extends Expression {
    override def toString: String = assigned match {
        case Some( v ) ⇒ s"${fieldScope.dslType} ${fieldScope.symbolName} = $v"
        case None ⇒ s"${fieldScope.dslType} ${fieldScope.symbolName}"
    }

    override def equals(obj: Any): Boolean = {
        obj match {
            case localVarDef: LocalVarDef ⇒
                localVarDef.fieldScope == fieldScope &&
                  localVarDef.dslType == dslType &&
                  localVarDef.assigned == assigned
            case _ ⇒ false
        }
    }
}

/**
 *<p>var ref can refer to a local var def or a global var def<p>
 *
 * <pre><code>
 * program{
 *      Foo foo = new Foo(a,b);
 *
 *      def method()=Unit{
 *          Int i = foo()*10; //LocalVarDef
 *          foo.a * 10; // foo.a => VarRef
 *          i*10; // i => VarRef
 *      }
 * }<pre><code>
 */
case class VarRef(name: List[String], fieldScope: Option[FieldScope]) extends Expression {
    override def toString: String = s"${name.mkString( "." )}"

    override def equals(obj: Any): Boolean = {
        obj match {
            case varRef: VarRef ⇒
                varRef.name == name && varRef.fieldScope == fieldScope
            case _ ⇒ false
        }
    }

    def getDslType(refs:List[String],clazzType: ClazzType,programScope:ProgramScope):DslType={
        programScope.classes.get(clazzType.clazzName) match {
            case Some(clazzScope)⇒
                refs match {
                    case ref::Nil⇒
                        clazzScope.fields.get(ref) match {
                            case Some(fieldScope)⇒
                                fieldScope.dslType
                            case None⇒
                                throw ExpressionParserException(s"${clazzScope.name} can't find field $ref")
                        }
                    case ref::tail⇒
                        clazzScope.fields.get(ref) match {
                            case Some(fieldScope)⇒
                                fieldScope.dslType match {
                                    case clazzType: ClazzType ⇒
                                        getDslType(tail, clazzType, programScope)
                                    case _ ⇒
                                        throw ExpressionParserException(s"dot separator work for clazzType ${fieldScope.dslType.name} refs: ${name.mkString(".")}")
                                }
                            case None⇒
                                throw ExpressionParserException(s"${clazzScope.name} can't find field $ref")
                        }
                }
            case None⇒
                if(programScope.isImportClazz(clazzType.clazzName)){
                    val resolvedClazz = programScope.importManager.resolve(clazzType.clazzName)
                    refs match {
                        case ref::Nil⇒
                            val field = resolvedClazz.fields.get(ref)
                            field match {
                                case Some(ResolveField(_,dslType))⇒
                                    dslType
                                case None⇒
                                    throw ExpressionParserException(s"${clazzType.clazzName} can't find field $ref")
                            }
                        case ref::tail⇒
                            val field = resolvedClazz.fields.get(ref)
                            field match {
                                case Some(ResolveField(_,dslType))⇒
                                    dslType match {
                                        case clazzType: ClazzType⇒
                                            getDslType(tail, clazzType, programScope)
                                        case _⇒
                                            throw ExpressionParserException( s"dot separator work for clazzType ${fieldScope.map(_.dslType.name).getOrElse(UnResolvedType.name)} refs: ${name.mkString( "." )}" )

                                    }
                                case None⇒
                                    throw ExpressionParserException(s"${clazzType.clazzName} can't find field $ref")
                            }
                    }

                }else{
                    throw ExpressionParserException(s"can not find ${clazzType.clazzName} represent ${refs.mkString(",")}")
                }
        }
    }

    def getDslType:DslType={
        fieldScope match {
            case None⇒UnResolvedType
            case Some(scope)⇒
                if(name.length==1){
                    scope.dslType
                }else{
                    scope.dslType match {
                        case clazzType: ClazzType⇒
                            getDslType(name,clazzType,scope.programScope)
                        case _⇒
                            throw ExpressionParserException(s"dot separator work for clazzType ${scope.dslType.name} refs: ${name.mkString(".")}")
                    }
                }
        }
    }
}


/**
 * <pre><code>
 *program{
 *      Array[Foo] fooArray = Array(new Foo(2,3),new Foo(1,2));
 *
 *      def method()=Unit{
 *          Int i =0;
 *          Foo b = fooArray[i];  // fooArray[i] => ArrayVarRef
 *      }
 * }<pre><code>
 */
class ArrayVarRef(val indexExpression: Expression,
                  override val name: List[String],
                  override val fieldScope: Option[FieldScope]) extends VarRef( name,  fieldScope ) {
    override def toString: String = s"${name.mkString( "." )}[$indexExpression]"

    override def equals(obj: Any): Boolean = {
        obj match {
            case arrayVarRef: ArrayVarRef ⇒
                arrayVarRef.indexExpression == indexExpression &&
                  arrayVarRef.name == name &&
                  arrayVarRef.fieldScope == fieldScope
            case _ ⇒ false
        }
    }

    override def getDslType: DslType = {
        val dslType = super.getDslType
        dslType match {
            case arrayType: ArrayType⇒ arrayType.parameterType
            case UnResolvedType⇒UnResolvedType
            case _⇒
                throw ExpressionParserException(s"var ref must be array type but is ${dslType.name}")
        }
    }

}

/**
 * <pre><code>
 * program{
 *      Map[Int,Foo] fooMap = {0:new Foo(2,3),1:new Foo(1,2)};
 *
 *      def method()=Unit{
 *          Int i =0;
 *          Foo b = fooMap(i);  // fooMap(i) => MapVarRef
 *      }
 * }<pre><code>
 */
class MapVarRef(val keyExpression: Expression,
                override val name: List[String],
                override val fieldScope: Option[FieldScope]) extends VarRef( name,  fieldScope ) {
    override def toString: String = s"${name.mkString( "." )}($keyExpression)"

    override def equals(obj: Any): Boolean = {
        obj match {
            case mapVarRef: MapVarRef ⇒
                mapVarRef.keyExpression == keyExpression &&
                  mapVarRef.name == name &&
                  mapVarRef.fieldScope == fieldScope
            case _ ⇒ false
        }
    }

    override def getDslType: DslType = {
        val dslType = super.getDslType
        dslType match {
            case mapType: MapType ⇒ mapType.valueParameterType
            case UnResolvedType ⇒ UnResolvedType
            case _ ⇒
                throw ExpressionParserException( s"var ref must be map type but is ${dslType.name}" )
        }
    }
}


trait BaseLiteral
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

//todo
object UnitLiteral extends Literal[Unit, UnitType.type] with BaseLiteral {
    override val dslType: UnitType.type = UnitType

    override def equals(obj: Any): Boolean = {
        obj.isInstanceOf[UnitLiteral.type]
    }
}

class IntLiteral(literal: Int) extends Literal[Int, IntType.type]( literal ) with BaseLiteral {
    override val dslType: IntType.type = IntType

    override def toString: String = literal.toString

    override def equals(obj: Any): Boolean = {
        obj match {
            case intLiteral: IntLiteral ⇒ intLiteral.literal == literal
            case _ ⇒ false
        }
    }
}

class LongLiteral(literal: Long) extends Literal[Long, LongType.type]( literal ) with BaseLiteral {
    override val dslType: LongType.type = LongType

    override def toString: String = literal.toString

    override def equals(obj: Any): Boolean = {
        obj match {
            case longLiteral: LongLiteral ⇒ longLiteral.literal == literal
            case _ ⇒ false
        }
    }
}

class FloatLiteral(literal: Float) extends Literal[Float, FloatType.type]( literal ) with BaseLiteral {
    override val dslType: FloatType.type = FloatType

    override def toString: String = literal.toString

    override def equals(obj: Any): Boolean = {
        obj match {
            case floatLiteral: FloatLiteral ⇒ floatLiteral.literal == literal
            case _ ⇒ false
        }
    }
}

class DoubleLiteral(literal: Double) extends Literal[Double, DoubleType.type]( literal ) with BaseLiteral {
    override val dslType: DoubleType.type = DoubleType

    override def toString: String = literal.toString

    override def equals(obj: Any): Boolean = {
        obj match {
            case doubleLiteral: DoubleLiteral ⇒ doubleLiteral.literal == literal
            case _ ⇒ false
        }
    }
}

class StringLiteral(literal: String) extends Literal[String, StringType.type]( literal ) with BaseLiteral {
    override val dslType: StringType.type = StringType

    override def toString: String =
        s"""
          |"$literal"
          |""".stripMargin

    override def equals(obj: Any): Boolean = {
        obj match {
            case stringLiteral: StringLiteral ⇒ stringLiteral.literal == literal
            case _ ⇒ false
        }
    }
}

class CharLiteral(literal: Char) extends Literal[Char, CharType.type]( literal ) with BaseLiteral {
    override val dslType: CharType.type = CharType

    override def toString: String = s"\'$literal\'"

    override def equals(obj: Any): Boolean = {
        obj match {
            case charLiteral: CharLiteral ⇒ charLiteral.literal == literal
            case _ ⇒ false
        }
    }
}

class BoolLiteral(literal: Boolean) extends Literal[Boolean, BoolType.type]( literal ) with BaseLiteral {
    override val dslType: BoolType.type = BoolType

    override def toString: String = literal.toString

    override def equals(obj: Any): Boolean = {
        obj match {
            case boolLiteral: BoolLiteral ⇒ boolLiteral.literal == literal
            case _ ⇒ false
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
 *
 *      def method(Int a,Int b)=Unit{
 *          Foo foo = new Foo(a,b); // new Foo(a,b) => ClazzLiteral
 *          Foo foo = new Foo(1,2); // new Foo(1,2) => ClazzLiteral
 *      }
 * }<pre><code>
 */
class ClazzLiteral(literal: Array[Expression],
                   override val dslType: ClazzType) extends Literal[Array[Expression], ClazzType]( literal ) {
    override def toString: String = s"new ${dslType.clazzName}(${literal.mkString( "," )})"

    override def resolvedType(dslType: DslType): Literal[Array[Expression], ClazzType] = {
        if (this.dslType.parameterTypes.contains( UnResolvedType ) || this.dslType.parameterTypes.isEmpty) {
            new ClazzLiteral( literal, dslType.asInstanceOf[ClazzType] )
        } else {
            this
        }
    }


    override def equals(obj: Any): Boolean = {
        obj match {
            case clazzLiteral: ClazzLiteral =>
                clazzLiteral.literal.sameElements( literal ) && clazzLiteral.dslType == dslType
            case _ ⇒ false
        }
    }
}

/**
 * in optimizer phase clazzLiteral represent array will be converted to arrayLiteral
 * <pre><code>
 * program{
 *      class Foo(Int a,Int b){
 *          def foo()=Int{
 *          return a+b;
 *      }
 *
 *      def method(Int a,Int b)=Unit{
 *          Array[Foo] array = new Array(new Foo(1,2),new Foo(2,3)); // new Array(new Foo(1,2),new Foo(2,3)) => ArrayLiteral
 *      }
 * <pre><code>
 */
class ArrayLiteral(literal: Array[Expression], override val dslType: ArrayType)
  extends Literal[Array[Expression], ArrayType]( literal ) {
    override def toString: String = s"new Array(${literal.mkString( "," )})"

    override def resolvedType(dslType: DslType): Literal[Array[Expression], ArrayType] = {
        if (this.dslType.parameterType == UnResolvedType) {
            new ArrayLiteral( literal, dslType.asInstanceOf[ArrayType] )
        } else {
            this
        }
    }

    override def equals(obj: Any): Boolean = {
        obj match {
            case arrayLiteral: ArrayLiteral ⇒
                arrayLiteral.literal.sameElements( literal ) && arrayLiteral.dslType == dslType
            case _ ⇒ false
        }
    }

}

/**
 * in optimizer phase clazzLiteral represent either(left or right) will be converted to eitherLiteral
 *<pre><code>
 *
 *program{
 *   def foo(Either[Int,Long] a)=String{
 *          a match {
 *              case Left(1)=> { return "left";}  // Left(1) => EitherLiteral
 *              case right(1L)=> { return "right";} // Right(1L) => EitherLiteral
 *          }
 *   }
 *
 *   Either[Int,Long] b = Left(1) // Left(1) => EitherLiteral
 *}
 *
 *<pre><code>
 */
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
        if (this.dslType.leftParameterType == UnResolvedType || this.dslType.rightParameterType == UnResolvedType) {
            new EitherLiteral( literal, dslType.asInstanceOf[EitherType] )
        } else {
            this
        }
    }

    override def equals(obj: Any): Boolean = {
        obj match {
            case eitherLiteral: EitherLiteral ⇒
                eitherLiteral.literal == literal && eitherLiteral.dslType == dslType
            case _ ⇒ false
        }
    }

}

/**
 *
 * <pre><code>
 * program{
 *      class Foo(Int a,Int b){
 *          def foo()=Int{
 *              return a+b;
 *          }
 *      }
 *
 *      def method(Foo foo)=Unit{
 *          Option[Foo] option = ?foo; // ?foo => OptionLiteral
 *
 *         Foo foo =  option match{
 *              case Some(f)=>f // Some(f) => OptionalLiteral  happen in optimizer phase
 *              case None()=>null // None => OptionalLiteral happen in optimizer phase
 *          }
 *
 *      }
 *}<pre><code>
 */
class OptionLiteral(literal:Expression,
                    override val dslType:OptionType)
  extends Literal[Expression,OptionType](literal) {
    override def toString: String = s"?$literal"

    override def resolvedType(dslType: DslType): Literal[Expression, OptionType] = {
        if (this.dslType.parameterType == UnResolvedType) {
            new OptionLiteral( literal, dslType.asInstanceOf[OptionType] )
        } else {
            this
        }
    }

    override def equals(obj: Any): Boolean = {
        obj match {
            case optionLiteral: OptionLiteral ⇒
                optionLiteral.literal == literal && optionLiteral.dslType == dslType
            case _ ⇒ false
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
                  override val dslType:ListType)
  extends Literal[Array[Expression],ListType](literal) {
    override def toString: String = s"[${literal.mkString( "," )}]"

    override def resolvedType(dslType: DslType): Literal[Array[Expression], ListType] = {
        if (this.dslType.parameterType == UnResolvedType) {
            new ListLiteral( literal, dslType.asInstanceOf[ListType] )
        } else {
            this
        }
    }

    override def equals(obj: Any): Boolean = {
        obj match {
            case listLiteral: ListLiteral ⇒
                listLiteral.literal.sameElements( literal ) && listLiteral.dslType == dslType
            case _ ⇒ false
        }
    }
}

/**
 *
 * <pre><code>
 * program{
 *
 *      def method()=Unit{
 *          Set[Int] set = {1,2,3}; //{1,2,3} => SetLiteral
 *      }
 * }<pre><code>
 */
class SetLiteral(literal: Array[Expression],
                 override val dslType: SetType)
  extends Literal[Array[Expression], SetType]( literal ) {
    override def toString: String = s"(${literal.mkString( "," )})"

    override def resolvedType(dslType: DslType): Literal[Array[Expression], SetType] = {
        if (this.dslType.parameterType == UnResolvedType) {
            new SetLiteral( literal, dslType.asInstanceOf[SetType] )
        } else {
            this
        }
    }

    override def equals(obj: Any): Boolean = {
        obj match {
            case setLiteral: SetLiteral ⇒ setLiteral.literal.sameElements( literal ) && setLiteral.dslType == dslType
            case _ ⇒ false
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
                   override val dslType: TupleType)
  extends Literal[Array[Expression], TupleType]( literal ) {
    override def toString: String = s"(${literal.mkString( "," )})"

    override def resolvedType(dslType: DslType): Literal[Array[Expression], TupleType] = {
        if (this.dslType.parameterTypes.contains( UnResolvedType )) {
            new TupleLiteral( literal, dslType.asInstanceOf[TupleType] )
        } else {
            this
        }
    }

    override def equals(obj: Any): Boolean = {
        obj match {
            case tupleLiteral: TupleLiteral ⇒
                tupleLiteral.literal.sameElements( literal ) && tupleLiteral.dslType == dslType
            case _ ⇒ false
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
                 override val dslType: MapType)
  extends Literal[Array[(Expression, Expression)], MapType]( literal ) {
    override def toString: String = s"{${literal.map( kv ⇒ kv._1.toString + ":" + kv._2.toString ).mkString( "," )}}"

    override def resolvedType(dslType: DslType): Literal[Array[(Expression, Expression)], MapType] = {
        if (this.dslType.keyParameterType == UnResolvedType || this.dslType.valueParameterType == UnResolvedType) {
            new MapLiteral( literal, dslType.asInstanceOf[MapType] )
        } else {
            this
        }
    }

    override def equals(obj: Any): Boolean = {
        obj match {
            case mapLiteral: MapLiteral ⇒
                literal.sameElements( mapLiteral.literal ) && dslType == mapLiteral.dslType
            case _ ⇒ false
        }
    }
}

//async expression

/**
 * <pre><code>
 * program{
 *      def method()=Unit{
 *          Future[Int] future = Async{ // Async{ ... } => Async
 *          return foo();
 *      }
 * }<pre><code>
 */
case class Async(body: Block, executor: Option[FieldScope], dslType: FutureType)
  extends Expression {
    override def toString: String = {
        executor match {
            case Some(fieldScope)⇒
                s"Async(${fieldScope.symbolName})$body"
            case None⇒
                s"Async $body"
        }
    }

    override def equals(obj: Any): Boolean = {
        obj match {
            case async: Async =>
                async.body == body && async.executor == executor && async.dslType == dslType
            case _ => false
        }
    }
}


//try expression

/**
 *<pre><code>
 *program{
 *      def method()=Unit{
 *          Try[Int] maySuccess = Try{ // Try{ ... } => Try
 *              return foo();
 *          }
 *      }
 * }<pre><code>
 */
case class Try(body: Block, dslType: TryType) extends Expression {
    override def toString: String = s"Try $body"

    override def equals(obj: Any): Boolean = {
        obj match {
            case `try`: Try ⇒
                `try`.body == body && `try`.dslType == dslType
            case _ ⇒ false
        }
    }

}

//custom block expression

/**
 * <pre><code>
 * program{
 *      def method()=Unit{
 *          String body = "body";
 *          Int len = body.length;
 *          Json maySuccess = Json{ // Json{ ... } => Json
 *              {"body":body,"len":len}
 *          }
 *
 *          Json data = Json{
 *              [1,2,3]
 *          }
 *      }
 * }<pre><code>
 */
case class CustomBlockExpression(name: String, body: Block, param: Option[FieldScope]) extends Expression {
    override def toString: String = s"$name $body"

    override def equals(obj: Any): Boolean = {
        obj match {
            case customBlockExpression: CustomBlockExpression ⇒
                customBlockExpression.name == name &&
                  customBlockExpression.body == body &&
                  customBlockExpression.param == param
            case _ ⇒ false
        }
    }
}


//lambda expression


/**
 * <pre><code>
 * program{
 *      def method()=Unit{
 *              Int=>Int lambda = p=>{  // p=> { ... } => Lambda
 *                  return p*2;
 *              }
 *              lambda.apply(2);
 *      }
 * }<pre><code>
 */
case class Lambda(inputs: Array[String], body: Block) extends Expression {
    override def toString: String =
        s"""
           |(${inputs.mkString( "," )}=>$body
           |""".stripMargin

    override def equals(obj: Any): Boolean = {
        obj match {
            case lambda: Lambda ⇒
                lambda.inputs.sameElements( inputs ) && lambda.body == body
            case _ ⇒ false
        }
    }

}


//match case expression

/** <pre><code>
 * a match {
 *      case d:Int=> {}
 * }
 * <pre><code>
 *
 *@see MatchCase
 */
case class MatchType(name: String, dslType: DslType) extends Expression {
    override def toString: String = s"$name:$dslType"

    override def equals(obj: Any): Boolean = {
        obj match {
            case matchType: MatchType ⇒
                matchType.name == name && matchType.dslType == dslType
            case _ ⇒ false
        }
    }
}

/**
 * <pre><code>
 * a match {
 *      case one:two:tail=>{}
 *      case one:Nil=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchHead(head: Array[Either[Expression, String]], tail: Either[Expression, String]) extends Expression {
    override def toString: String = s"${getString( "", "", ":", head )}:${
        tail match {
            case Left( i ) ⇒ i.toString
            case Right( j ) ⇒ j
        }
    }"

    override def equals(obj: Any): Boolean = obj match {
        case matchHead: MatchHead ⇒ matchHead.head.sameElements( head ) && matchHead.tail == tail
        case _ => false
    }
}

/**
 * <pre><code>
 * a match {
 *      case [1,2,b]=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchList(expressions: Array[Either[Expression, String]]) extends Expression {
    override def toString: String = getString( "[", "]", ",", expressions )

    override def equals(obj: Any): Boolean = obj match {
        case matchList: MatchList =>
            matchList.expressions.sameElements( expressions )
        case _ ⇒ false
    }
}

/**
 * <pre><code>
 * a match {
 *      case (a1,a2,a3)=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchTuple(expressions: Array[Either[Expression, String]]) extends Expression {
    override def toString: String = getString( "(", ")", ",", expressions )

    override def equals(obj: Any): Boolean = obj match {
        case matchTuple: MatchTuple ⇒ matchTuple.expressions.sameElements( expressions )
        case _ ⇒ false
    }
}

/**
 * <pre><code>
 * a match {
 *      case Foo(d,c,b)=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchClass(dslType: DslType, expressions: Array[Either[Expression, String]]) extends Expression {
    override def toString: String = s"${dslType.asInstanceOf[ClazzType].clazzName}${getString( "(", ")", ",", expressions )}"

    override def equals(obj: Any): Boolean = {
        obj match {
            case matchClass: MatchClass ⇒
                matchClass.dslType == dslType && matchClass.expressions.sameElements( expressions )
            case _ ⇒ false
        }
    }
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
case class MatchCase(matched: VarRef, cases: Array[(Expression, Block)], default: Option[Block]) extends Expression {
    override def toString: String =
        s"""
           |$matched match {
           |     ${
            cases.map( `case` ⇒ {
                "case " + `case`._1.toString + " => " + `case`._2.toString
            } ).mkString( "\n" )
        }
           |  ${if (default.isDefined) "default =>" + default.get}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case matchCase: MatchCase ⇒
            matchCase.matched == matched && matchCase.cases.sameElements( cases ) && matchCase.default == default
        case _ ⇒ false
    }
}

//assign expression

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          i = foo()*10; // i = foo()*10; => Assign
 *      }
 * }
 * <pre><code>
 */
case class Assign(varRef: VarRef, assigned: Expression) extends Expression {
    override def toString: String = s"$varRef = $assigned"

    override def equals(obj: Any): Boolean = obj match {
        case assign: Assign ⇒
            assign.varRef == varRef && assign.assigned == assigned
        case _ => false
    }
}


//unary expression

/**
 * <p>unary expression include negate, opposite, cast,instanceof and paren operator</p>
 */
trait UnaryExpression extends Expression {
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
case class Negate(child:Expression) extends UnaryExpression {
    override def toString: String = s"!$child"

    override def equals(obj: Any): Boolean = obj match {
        case negate: Negate ⇒ negate.child == child
        case _ ⇒ false
    }
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
case class Opposite(child: Expression) extends UnaryExpression {
    override def toString: String = s"(-$child)"

    override def equals(obj: Any): Boolean = obj match {
        case opposite: Opposite ⇒ opposite.child == child
        case _ ⇒ false
    }
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
case class Cast(child: Expression, castType: DslType) extends UnaryExpression {
    override def toString: String = s"($castType)$child"

    override def equals(obj: Any): Boolean = obj match {
        case cast: Cast ⇒ cast.child == child && cast.castType == castType
        case _ ⇒ false
    }
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
case class Instanceof(child: Expression,judgeType: DslType) extends UnaryExpression {
    override def toString: String = s"$child instanceof $judgeType"

    override def equals(obj: Any): Boolean = obj match {
        case instanceof: Instanceof ⇒ instanceof.child == child && instanceof.judgeType == judgeType
        case _ ⇒ false
    }
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
case class Paren(child:Expression) extends UnaryExpression {
    override def toString: String = s"($child)"

    override def equals(obj: Any): Boolean = obj match {
        case paren: Paren ⇒ paren.child == child
        case _ ⇒ false
    }
}

//binary expression
trait BinaryExpression extends Expression {
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
case class Div(left:Expression, right:Expression) extends BinaryExpression {
    override def toString: String = s"$left/$right"

    override def equals(obj: Any): Boolean = obj match {
        case div: Div ⇒
            div.left == left && div.right == right
        case _ ⇒ false
    }
}
case class Mul(left:Expression, right:Expression) extends BinaryExpression {
    override def toString: String = s"$left*$right"

    override def equals(obj: Any): Boolean = obj match {
        case mul: Mul ⇒
            mul.left == left && mul.right == right
        case _ ⇒ false
    }
}
case class Mod(left:Expression, right:Expression) extends BinaryExpression {
    override def toString: String = s"$left%$right"

    override def equals(obj: Any): Boolean = obj match {
        case mod: Mod ⇒
            mod.left == left && mod.right == right
        case _ ⇒ false
    }
}

//additive expression

/**
 * <pre><code>
 * program{
 *      def method()=Unit{
 *          Long j = 1-2; // 1-2 => Sub
 *          Long i = (a+b)*c; // a+b => Add
 *      }
 * }
 * <pre><code>
 */
case class Add(left: Expression, right: Expression) extends BinaryExpression {
    override def toString: String = s"$left+$right"

    override def equals(obj: Any): Boolean = obj match {
        case add: Add ⇒
            add.left == left && add.right == right
        case _ ⇒ false
    }
}

case class Sub(left: Expression, right: Expression) extends BinaryExpression {
    override def toString: String = s"$left-$right"

    override def equals(obj: Any): Boolean = obj match {
        case sub: Sub ⇒
            sub.left == left && sub.right == right
        case _ ⇒ false
    }
}

//shit expression

/**
 * <pre><code>
 *program{
 *  def method()=Unit{
 *      Int j = 1&lt;&lt;2; // 1&lt;&lt;2 => LeftShit
 *      Int k = 1>>2; // 1>>2 => RightShift
 *      Int i = 1>>>2; // 1>>>2 => UnsignedRightShift
 *  }
 * }
 * <pre><code>
 */
case class LeftShift(left: Expression, right: Expression) extends BinaryExpression {
    override def toString: String = s"$left<<$right"

    override def equals(obj: Any): Boolean = obj match {
        case leftShift: LeftShift ⇒
            leftShift.left == left && leftShift.right == right
        case _ ⇒ false
    }
}

case class RightShift(left: Expression, right: Expression) extends BinaryExpression {
    override def toString: String = s"$left>>$right"

    override def equals(obj: Any): Boolean = obj match {
        case rightShift: RightShift ⇒
            rightShift.left == left && rightShift.right == right
        case _ ⇒ false
    }

}

case class UnsignedRightShift(left: Expression, right: Expression) extends BinaryExpression {
    override def toString: String = s"$left>>>$right"

    override def equals(obj: Any): Boolean = obj match {
        case unsignedRightShift: UnsignedRightShift ⇒
            unsignedRightShift.left == left && unsignedRightShift.right == right
        case _ ⇒ false
    }
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

case class Lt(left:Expression, right:Expression) extends BinaryExpression {
    override def toString: String = s"$left<$right"

    override def equals(obj: Any): Boolean = obj match {
        case lt: Lt ⇒
            lt.left == left && lt.right == right
        case _ ⇒ false
    }
}

case class Gt(left: Expression, right: Expression) extends BinaryExpression {
    override def toString: String = s"$left>$right"

    override def equals(obj: Any): Boolean = obj match {
        case gt: Gt ⇒
            gt.left == left && gt.right == right
        case _ ⇒ false
    }

}

case class Le(left: Expression, right: Expression) extends BinaryExpression {
    override def toString: String = s"$left<=$right"

    override def equals(obj: Any): Boolean = obj match {
        case le: Le ⇒
            le.left == left && le.right == right
        case _ ⇒ false
    }

}

case class Ge(left: Expression, right: Expression) extends BinaryExpression {
    override def toString: String = s"$left>=$right"

    override def equals(obj: Any): Boolean = obj match {
        case ge: Ge ⇒
            ge.left == left && ge.right == right
        case _ ⇒ false
    }
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

case class Eq(left:Expression, right:Expression) extends BinaryExpression {
    override def toString: String = s"$left ==  $right"

    override def equals(obj: Any): Boolean = obj match {
        case eq: Eq ⇒
            eq.left == left && eq.right == right
        case _ ⇒ false
    }

}

case class NotEq(left:Expression, right:Expression) extends BinaryExpression {
    override def toString: String = s"$left !=  $right"

    override def equals(obj: Any): Boolean = obj match {
        case notEq: NotEq ⇒
            notEq.left == left && notEq.right == right
        case _ ⇒ false
    }

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

case class BitAnd(left:Expression, right:Expression) extends BinaryExpression {
    override def toString: String = s"$left &  $right"

    override def equals(obj: Any): Boolean = obj match {
        case bitAnd: BitAnd ⇒
            bitAnd.left == left && bitAnd.right == right
        case _ ⇒ false
    }
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
case class Caret(left:Expression, right:Expression) extends BinaryExpression {
    override def toString: String = s"$left ^  $right"

    override def equals(obj: Any): Boolean = obj match {
        case caret: Caret ⇒
            caret.left == left && caret.right == right
        case _ ⇒ false
    }
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
case class BitOr(left:Expression, right:Expression) extends BinaryExpression {
    override def toString: String = s"$left |   $right"

    override def equals(obj: Any): Boolean = obj match {
        case bitOr: BitOr ⇒
            bitOr.left == left && bitOr.right == right
        case _ ⇒ false
    }

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
case class And(left:Expression, right:Expression) extends BinaryExpression {
    override def toString: String = s"$left &&  $right"

    override def equals(obj: Any): Boolean = obj match {
        case and: And ⇒
            and.left == left && and.right == right
        case _ ⇒ false
    }

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
case class Or(left:Expression, right:Expression) extends BinaryExpression {
    override def toString: String = s"$left ||  $right"

    override def equals(obj: Any): Boolean = obj match {
        case or: Or ⇒
            or.left == left && or.right == right
        case _ ⇒ false
    }

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
case class FuncCallChain(head:FuncCall, tails:List[Part]) extends Expression {
    override def toString: String = s"$head.${tails.mkString( "." )}"

    override def equals(obj: Any): Boolean = obj match {
        case funcCallChain: FuncCallChain ⇒
            funcCallChain.head == head && funcCallChain.tails == tails
        case _ ⇒ false
    }
}

class LiteralCallChain[T,D<:DslType](val head:Literal[T,D],val tails:List[Part]) extends Expression{
    override def toString: String = s"$head.${tails.mkString(".")}"
}

class IntLiteralCallChain(override val head: IntLiteral, override val tails: List[Part])
  extends LiteralCallChain[Int, IntType.type]( head, tails ) {
    override def equals(obj: Any): Boolean = obj match {
        case intLiteralCallChain: IntLiteralCallChain ⇒
            intLiteralCallChain.head == head && intLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class LongLiteralCallChain(override val head: LongLiteral, override val tails: List[Part])
  extends LiteralCallChain[Long, LongType.type]( head, tails ) {

    override def equals(obj: Any): Boolean = obj match {
        case longLiteralCallChain: LongLiteralCallChain ⇒
            longLiteralCallChain.head == head && longLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class FloatLiteralCallChain(override val head: FloatLiteral, override val tails: List[Part])
  extends LiteralCallChain[Float, FloatType.type]( head, tails ) {
    override def equals(obj: Any): Boolean = obj match {
        case floatLiteralCallChain: FloatLiteralCallChain ⇒
            floatLiteralCallChain.head == head && floatLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class DoubleLiteralCallChain(override val head: DoubleLiteral, override val tails: List[Part])
  extends LiteralCallChain[Double, DoubleType.type]( head, tails ) {
    override def equals(obj: Any): Boolean = obj match {
        case doubleLiteralCallChain: DoubleLiteralCallChain ⇒
            doubleLiteralCallChain.head == head && doubleLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class StringLiteralCallChain(override val head: StringLiteral, override val tails: List[Part])
  extends LiteralCallChain[String, StringType.type]( head, tails ) {

    override def equals(obj: Any): Boolean = obj match {
        case stringLiteralCallChain: StringLiteralCallChain ⇒
            stringLiteralCallChain.head == head && stringLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class CharLiteralCallChain(override val head: CharLiteral, override val tails: List[Part])
  extends LiteralCallChain[Char, CharType.type]( head, tails ) {

    override def equals(obj: Any): Boolean = obj match {
        case charLiteralCallChain: CharLiteralCallChain ⇒
            charLiteralCallChain.head == head && charLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class BoolLiteralCallChain(override val head: BoolLiteral, override val tails: List[Part])
  extends LiteralCallChain[Boolean, BoolType.type]( head, tails ) {

    override def equals(obj: Any): Boolean = obj match {
        case boolLiteralCallChain: BoolLiteralCallChain ⇒
            boolLiteralCallChain.head == head && boolLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class ClazzLiteralCallChain(override val head: ClazzLiteral, override val tails: List[Part])
  extends LiteralCallChain[Array[Expression], ClazzType]( head, tails ) {

    override def equals(obj: Any): Boolean = obj match {
        case clazzLiteralCallChain: ClazzLiteralCallChain ⇒
            clazzLiteralCallChain.head == head && clazzLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class OptionLiteralCallChain(override val head: OptionLiteral, override val tails: List[Part])
  extends LiteralCallChain[Expression, OptionType]( head, tails ) {

    override def equals(obj: Any): Boolean = obj match {
        case optionLiteralCallChain: OptionLiteralCallChain ⇒
            optionLiteralCallChain.head == head && optionLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class ListLiteralCallChain(override val head: ListLiteral, override val tails: List[Part])
  extends LiteralCallChain[Array[Expression], ListType]( head, tails ) {

    override def equals(obj: Any): Boolean = obj match {
        case listLiteralCallChain: ListLiteralCallChain ⇒
            listLiteralCallChain.head == head && listLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class SetLiteralCallChain(override val head: SetLiteral, override val tails: List[Part])
  extends LiteralCallChain[Array[Expression], SetType]( head, tails ) {

    override def equals(obj: Any): Boolean = obj match {
        case setLiteralCallChain: SetLiteralCallChain ⇒
            setLiteralCallChain.head == head && setLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class TupleLiteralCallChain(override val head: TupleLiteral, override val tails: List[Part])
  extends LiteralCallChain[Array[Expression], TupleType]( head, tails ) {

    override def equals(obj: Any): Boolean = obj match {
        case tupleLiteralCallChain: TupleLiteralCallChain ⇒
            tupleLiteralCallChain.head == head && tupleLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

class MapLiteralCallChain(override val head: MapLiteral, override val tails: List[Part])
  extends LiteralCallChain[Array[(Expression, Expression)], MapType]( head, tails ) {
    override def equals(obj: Any): Boolean = obj match {
        case mapLiteralCallChain: MapLiteralCallChain ⇒
            mapLiteralCallChain.head == head && mapLiteralCallChain.tails == tails
        case _ ⇒ false
    }
}

trait Part{
    val name:String
}

case class VarName(override val name:String) extends Part {
    override def toString: String = name

    override def equals(obj: Any): Boolean = obj match {
        case varName: VarName ⇒ varName.name == name
        case _ ⇒ false
    }
}


//method call expression
case class MethodCall(methodScope:Option[MethodScope],
                      override val name:String,
                      override val params:Array[Expression]) extends FuncCall with Part {
    override def toString: String = getString( "", name, params )


    override def equals(obj: Any): Boolean = obj match {
        case methodCall: MethodCall ⇒
            methodCall.params.sameElements( params ) &&
              methodCall.methodScope == methodScope &&
              methodCall.name == name
        case _ ⇒ false
    }
}

//func call expression
trait FuncCall extends Expression {
    val name:String
    val params:Array[Expression]
}


//static call expression
case class StaticCall(`type`:DslType,
                      override val name:String,
                      override val params:Array[Expression]) extends FuncCall {
    override def toString: String = getString( `type`, name, params )

    override def equals(obj: Any): Boolean = obj match {
        case staticCall: StaticCall ⇒
            staticCall.params.sameElements( params ) &&
              staticCall.`type` == `type` &&
              staticCall.name == name
        case _ ⇒ false
    }
}

//literal call expression
class LiteralCall(val literal: Expression,
                  override val name:String,
                  override val params:Array[Expression]) extends FuncCall {
    override def toString: String = getString( literal, name, params )

    override def equals(obj: Any): Boolean = obj match {
        case literalCall: LiteralCall ⇒
            literalCall.params.sameElements( params ) &&
              literalCall.literal == literal &&
              literalCall.name == name
        case _ ⇒ false
    }
}

//var call expression
case class VarCall(varRef:VarRef,
                   override val name:String,
                   override val params:Array[Expression]) extends FuncCall {
    override def toString: String = getString( varRef, name, params )

    override def equals(obj: Any): Boolean = obj match {
        case varCall: VarCall ⇒
            varCall.params.sameElements( params ) &&
              varCall.varRef == varRef &&
              varCall.name == name
        case _ ⇒ false
    }
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
case class Block(expressions: ArrayBuffer[Expression] = ArrayBuffer( ),var ignoreVarRefResolved:Boolean = false) extends Expression {
    override def toString: String =
        s"""
           |{
           |     ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case block: Block ⇒
            block.expressions == expressions && block.ignoreVarRefResolved == ignoreVarRefResolved
        case _ ⇒ false
    }
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
              override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |for($loopVarDef;$loopVarCondition;$loopVarUpdate){
           |     ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case forLoop: ForLoop ⇒
            forLoop.loopVarDef == loopVarDef &&
              forLoop.loopVarCondition == loopVarCondition &&
              forLoop.loopVarUpdate == loopVarUpdate &&
              forLoop.expressions == expressions
        case _ ⇒ false
    }
}

case class For(loopVarDef:LocalVarDef,
               loopVarCondition:Expression,
               loopVarUpdate:Expression,
               body:Block) extends Expression {
    override def toString: String =
        s"""
           |for($loopVarDef;$loopVarCondition;$loopVarUpdate)
           | $body
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case `for`: For ⇒
            `for`.loopVarDef == loopVarDef &&
              `for`.loopVarCondition == loopVarCondition &&
              `for`.loopVarUpdate == loopVarUpdate &&
              `for`.body == body
        case _ ⇒ false
    }
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
                        override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |for($localVarDef:$looped){
           |    ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case forLoopCollection: ForLoopCollection ⇒
            forLoopCollection.localVarDef == localVarDef &&
              forLoopCollection.looped == looped &&
              forLoopCollection.expressions == expressions
        case _ ⇒ false
    }
}

case class ForCollection(localVarDef: LocalVarDef,
                    looped:Expression,body:Block) extends Expression {
    override def toString: String =
        s"""
           |for($localVarDef:$looped)
           | $body
           |""".stripMargin

    override def equals(obj: Any): Boolean = {
        obj match {
            case forCollection: ForCollection ⇒
                forCollection.localVarDef == localVarDef &&
                  forCollection.looped == looped &&
                  forCollection.body == body
            case _ ⇒ false
        }
    }
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
                 override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |for($loopKeyDef,$loopValueDef:$looped){
           |     ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case forLoopMap: ForLoopMap ⇒
            forLoopMap.loopKeyDef == loopKeyDef &&
              forLoopMap.loopValueDef == loopValueDef &&
              forLoopMap.looped == looped &&
              forLoopMap.expressions == expressions
        case _ ⇒ false
    }

}

case class ForMap(loopKeyDef:LocalVarDef,
             loopValueDef:LocalVarDef,
             looped:Expression,body:Block) extends Expression {
    override def toString: String =
        s"""
           |for($loopKeyDef,$loopValueDef:$looped)
           |     $body
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case forMap: ForMap ⇒
            forMap.loopKeyDef == loopKeyDef &&
              forMap.loopValueDef == loopValueDef &&
              forMap.looped == looped &&
              forMap.body == body
        case _ ⇒ false
    }
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

case class WhileCondition(expression: Expression) extends Expression {
    override def toString: String = s"while($expression)"

    override def equals(obj: Any): Boolean = obj match {
        case whileCondition: WhileCondition => whileCondition.expression == expression
        case _ ⇒ false
    }
}
class WhileBlock(override val expressions:ArrayBuffer[Expression] = new ArrayBuffer[Expression]()) extends Block(expressions ) {
    override def toString: String = s"${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}"

    override def equals(obj: Any): Boolean = obj match {
        case whileBlock: WhileBlock ⇒ whileBlock.expressions == expressions
        case _ ⇒ false
    }
}

case class While(condition: Expression, body:Block) extends Expression {
    override def toString: String =
        s"""
           |while($condition)
           |$body
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case `while`: While ⇒
            `while`.condition == condition &&
              `while`.body == body
        case _ ⇒ false
    }

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

case class DoWhileCondition(expression: Expression) extends Expression {
    override def toString: String = s"while($expression)"

    override def equals(obj: Any): Boolean = obj match {
        case doWhileCondition: DoWhileCondition ⇒
            doWhileCondition.expression == expression
        case _ ⇒ false
    }
}
class DoWhileBlock(override val expressions:ArrayBuffer[Expression] = new ArrayBuffer[Expression]()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |do{
           |    ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |};
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case doWhileBlock: DoWhileBlock ⇒ doWhileBlock.expressions == expressions
        case _ ⇒ false
    }
}

case class DoWhile(condition:Expression,body:Block) extends Expression {
    override def toString: String =
        s"""
           |do
           | $body
           |while($condition)
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case doWhile: DoWhile ⇒
            doWhile.condition == condition &&
              doWhile.body == body
        case _ ⇒ false
    }
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

object Break extends Expression {
    override def toString: String = "break"

    override def equals(obj: Any): Boolean = obj.isInstanceOf[Break.type]
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

object Continue extends Expression {
    override def toString: String = "continue"

    override def equals(obj: Any): Boolean = obj.isInstanceOf[Continue.type]
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

case class Throw(expression: Expression) extends Expression {
    override def toString: String = s"throw $expression"

    override def equals(obj: Any): Boolean = obj match {
        case `throw`: Throw ⇒
            `throw`.expression == expression
        case _ ⇒ false
    }
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

case class Return(expression: Expression) extends Expression {
    override def toString: String = s"return $expression"

    override def equals(obj: Any): Boolean = obj match {
        case `return`: Return ⇒
            `return`.expression == expression
        case _ ⇒ false
    }

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
case class Assert(expression: Expression) extends Expression {
    override def toString: String = s"assert $expression"

    override def equals(obj: Any): Boolean = obj match {
        case `assert`: Assert ⇒
            `assert`.expression == expression
        case _ ⇒ false
    }

}

//synchronized statement

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *          sync(lock){   //sync(lock) => SyncCondition
 *                  foo();   //{ foo();} => SyncBlock
 *          }
 *      }   //sync(...){ ...} => Sync
 * }
 * <pre><code>
 */

case class SyncCondition(expression: Expression) extends Expression {
    override def toString: String = s"sync($expression)"

    override def equals(obj: Any): Boolean = obj match {
        case syncCondition: SyncCondition ⇒
            syncCondition.expression == expression
        case _ ⇒ false
    }
}

class SyncBlock(override val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Block( expressions ) {
    override def toString: String =
        s"""
           |{
           |   ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case syncBlock: SyncBlock ⇒
            syncBlock.expressions == expressions
        case _ ⇒ false
    }
}

case class Sync(condition: Expression,body:Block) extends Expression {
    override def toString: String =
        s"""
           |sync($condition)
           | $body
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case sync: Sync ⇒
            sync.condition == condition &&
              sync.body == body
        case _ ⇒ false
    }

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

case class IfCondition(expression: Expression,first:Boolean) extends Expression {
    override def toString: String = if (first) s"if($expression)" else s"else if($expression)"

    override def equals(obj: Any): Boolean = obj match {
        case ifCondition: IfCondition ⇒
            ifCondition.expression == expression && ifCondition.first == first
        case _ ⇒ false
    }
}
class IfBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |{
           |    ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case ifBlock: IfBlock ⇒
            ifBlock.expressions == expressions
        case _ ⇒ false
    }

}

case class If(cases:Array[(Expression,Block)], default:Option[Block]) extends Expression {
    override def equals(obj: Any): Boolean = obj match {
        case `if`: If ⇒
            `if`.cases.sameElements( cases ) && default == `if`.default
        case _ ⇒ false
    }

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

class TryBlock(override val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Block( expressions ) {
    override def toString: String =
        s"""
           |try{
           |    ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case tryBlock: TryBlock ⇒
            tryBlock.expressions == expressions
        case _ ⇒ false
    }
}

case class CatchParameter(name: String, dslType: DslType) extends Expression {
    override def toString: String =
        s"""
           |catch($dslType $name)
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case catchParameter: CatchParameter ⇒
            catchParameter.name == name && catchParameter.dslType == dslType
        case _ ⇒ false
    }
}

class CatchBlock(override val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Block( expressions ) {
    override def toString: String =
        s"""
           |{
           |     ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case catchBlock: CatchBlock ⇒
            catchBlock.expressions == expressions
        case _ ⇒ false
    }
}

class FinallyBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |finally{
           |      ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case finallyBlock: FinallyBlock ⇒
            finallyBlock.expressions == expressions
        case _ ⇒ false
    }
}

case class TryCatch(tryBlock:Block,catches:Array[((String,DslType),Block)],finallyBlock:Option[Block]) extends Expression {
    override def toString: String =
        s"""
           |try
           | $tryBlock

           |${catches.map{
            `catch`⇒ s"catch(${`catch`._1._2} ${`catch`._1._1}) ${`catch`._2}"
        }.mkString( "\n" )
        }
           |${
            finallyBlock match {
                case Some(
                block ) ⇒
                    "finally" + block
                case None ⇒ ""
            }
        }
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case tryCatch: TryCatch ⇒
            tryCatch.tryBlock == tryCatch.tryBlock &&
              tryCatch.catches.sameElements( tryCatch.catches ) &&
              tryCatch.finallyBlock == finallyBlock
        case _ ⇒ false
    }

}

package object expression {

    def getString(start: String, end: String, sep: String, expressions: Array[Either[Expression, String]]): String =
        s"$start${
            expressions.map {
                case Left( i ) ⇒ i.toString
                case Right( j ) ⇒ j
            }.mkString( sep )
        }$end"

    def getString[T](t: T, name: String, params: Array[Expression]): String = {
        s"$t.$name(${params.mkString( "." )})"
    }

    def commonParenDslType(dslTypes: DslType*):DslType={
        dslTypes match {
            case leftDslType::tail⇒
                val rightDslType = commonParenDslType(tail:_*)
                leftDslType match {
                    case DoubleType⇒
                        rightDslType match {
                            case DoubleType⇒DoubleType
                            case LongType⇒LongType
                            case FloatType⇒FloatType
                            case IntType⇒IntType
                            case _⇒AnyType
                        }
                    case LongType⇒
                        rightDslType match {
                            case DoubleType⇒DoubleType
                            case LongType⇒LongType
                            case FloatType⇒FloatType
                            case IntType⇒LongType
                            case _⇒AnyType
                        }
                    case FloatType⇒
                        rightDslType match {
                            case FloatType⇒FloatType
                            case LongType⇒FloatType
                            case DoubleType⇒DoubleType
                            case IntType⇒FloatType
                            case _⇒AnyType
                        }
                    case IntType⇒
                        rightDslType match {
                            case IntType⇒IntType
                            case LongType⇒LongType
                            case DoubleType⇒DoubleType
                            case FloatType⇒FloatType
                            case _⇒AnyType
                        }
                    case _⇒
                        if(leftDslType==rightDslType){
                            leftDslType
                        }else{
                            AnyType
                        }
                }
        }
    }
}