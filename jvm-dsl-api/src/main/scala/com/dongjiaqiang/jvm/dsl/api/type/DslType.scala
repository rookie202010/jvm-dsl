package com.dongjiaqiang.jvm.dsl.api.`type`

import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.api.scope.{ClazzScope, ImportResolver}

trait DslType {

  /**
   * name of dsl type
   */
  val name: String

  override def toString: String = name

  /**
   * common parent type of this dsl type and another dsl type
   * @param importResolver importResolver
   * @param anotherDslType another dsl type
   * @return
   */
  def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = {
    throw new UnsupportedOperationException( "not support" )
  }

  /**
   * whether another dsl type is a subtype of this dsl type
   * @param importResolver importResolver
   * @param anotherDslType another dsl type
   * @return
   */
  def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = {
    throw new UnsupportedOperationException( "not support" )
  }
}

trait MonadDslType extends DslType {
    def carryDslType:DslType

    def transform(carryDslType:DslType):MonadDslType

  /**
   * Set[Long] and Set[Int] common dsl type is Set[Long]
   * Set[Long] and List[Long] common dsl type is Any
   */
  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case monadDslType: MonadDslType if anotherDslType.getClass==this.getClass ⇒
      if(monadDslType.carryDslType == carryDslType) {
        transform(carryDslType)
      }else{
          transform( monadDslType.carryDslType.commonDslType( importResolver, carryDslType ) )
      }
    case _⇒AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType match {
    case monadDslType: MonadDslType if(anotherDslType.getClass==this.getClass) && carryDslType == monadDslType.carryDslType ⇒
        true
    case monadDslType: MonadDslType if(anotherDslType.getClass==this.getClass  && !monadDslType.carryDslType.isInstanceOf[BasicDslType])⇒
        carryDslType.isSuperDslType(importResolver,monadDslType.carryDslType)
    case _⇒false
  }
}

trait BasicDslType extends DslType

trait NumberDslType extends BasicDslType

//base type

object IntType extends NumberDslType {

  override val name: String = "Int"

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case CharType | ByteType ⇒IntType
    case numberDslType: NumberDslType⇒numberDslType
    case _⇒AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType match {
    case IntType | CharType | ByteType ⇒ true
    case _ ⇒ false
  }
}

object LongType extends NumberDslType{
  override val name: String = "Long"

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case IntType | CharType | ByteType ⇒ LongType
    case numberDslType: NumberDslType ⇒ numberDslType
    case _⇒AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType match {
    case LongType | CharType | ByteType | IntType⇒true
    case _⇒false
  }

}

object FloatType extends NumberDslType {
  override val name: String = "Float"

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case LongType | IntType | CharType | ByteType ⇒ FloatType
    case numberDslType: NumberDslType⇒numberDslType
    case _⇒AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType match {
    case FloatType | LongType | IntType | CharType | ByteType ⇒ true
    case _⇒false
  }

}

object DoubleType extends NumberDslType {
  override val name: String = "Double"

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case CharType | ByteType ⇒ DoubleType
    case _: NumberDslType⇒DoubleType
    case _⇒AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType match {
    case DoubleType | FloatType | LongType | IntType | CharType | ByteType ⇒ true
    case _⇒false
  }
}

object CharType extends BasicDslType {
  override val name: String = "Char"

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case ByteType⇒IntType
    case CharType⇒CharType
    case numberDslType: NumberDslType⇒numberDslType
    case _⇒AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType == CharType
}

object ByteType extends BasicDslType {
  override val name: String = "Byte"

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case ByteType⇒ByteType
    case CharType⇒IntType
    case numberDslType: NumberDslType⇒numberDslType
    case _⇒AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType == ByteType
}

object BoolType extends BasicDslType {
  override val name: String = "Bool"

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case BoolType ⇒ BoolType
    case _ ⇒ AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType == BoolType
}

object StringType extends MonadDslType {

  override def carryDslType: DslType = CharType

  override def transform(carryDslType: DslType): MonadDslType = StringType

  override val name: String = "String"
  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case StringType ⇒ StringType
    case _ ⇒ AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType == StringType
}

object UnResolvedType extends DslType {
  override val name: String = "UnResolved"
}

object AnyType extends DslType {
  override val name: String = "Any"
  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = AnyType

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = true
}

object NothingType extends DslType{
  override val name:String = "Nothing"
  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType
  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType == NothingType
}

object UnitType extends DslType {
  override val name: String = "Unit"

  override def commonDslType(importResolver: ImportResolver,
                             anotherDslType: DslType): DslType = UnitType
}

object ThrowableType extends ClazzType("Throwable")

//collection type

case class ListType(parameterType: DslType) extends MonadDslType {
  override val name: String = s"List[${parameterType.name}]"

  override def equals(obj: Any): scala.Boolean = {
    obj match {
      case listType: ListType ⇒ parameterType == listType.parameterType
      case _ ⇒ false

    }
  }

  override def carryDslType: DslType = parameterType

  override def transform(carryDslType: DslType): MonadDslType = ListType(carryDslType)
}

case class SetType(parameterType:DslType,
                   seq:Boolean=false,
                   sorted:Boolean=false,
                   sorter:Option[Lambda] = None) extends MonadDslType {
  override val name: String = s"Set[${parameterType.name}]"

  def asSeq():SetType={
      this.copy(seq=true,sorted=false,sorter=None)
  }

  def asSorted():SetType={
      this.copy(seq=false,sorted=true,sorter=None)
  }

  def asSorted(sorter: Lambda): SetType = {
    this.copy( seq = false, sorted = true, sorter = Some( sorter ) )
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case setType: SetType ⇒ parameterType == setType.parameterType &&
        seq == setType.seq &&
        sorted == setType.sorted &&
        sorter == setType.sorter
      case _ ⇒ false
    }

  override def carryDslType: DslType = parameterType

  override def transform(carryDslType: DslType): MonadDslType = SetType(carryDslType,seq, sorted,sorter)
}

case class MapType(keyParameterType:DslType,
                   valueParameterType:DslType,
                   seq:Boolean=false,
                   sorted:Boolean=false,
                   sorter:Option[Lambda] = None) extends MonadDslType {
  override val name: String = s"Map[${keyParameterType.name},${valueParameterType.name}]"

  def asSeq(): MapType = {
    this.copy( seq = true, sorted = false, sorter = None )
  }

  def asSorted(): MapType = {
    this.copy( seq = false, sorted = true, sorter = None )
  }

  def asSorted(sorter: Lambda): MapType = {
    this.copy( seq = false, sorted = true, sorter = Some( sorter ) )
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case mapType: MapType ⇒ valueParameterType == mapType.valueParameterType &&
        keyParameterType == mapType.keyParameterType &&
        seq == mapType.seq &&
        sorted == mapType.sorted &&
        sorter == mapType.sorter
      case _ ⇒ false
    }

  override def carryDslType: DslType = TupleType(Array(keyParameterType,valueParameterType))

  override def transform(carryDslType: DslType): MonadDslType = MapType(carryDslType.asInstanceOf[TupleType].parameterTypes.head,
    carryDslType.asInstanceOf[TupleType].parameterTypes.last,seq, sorted)
}

case class ArrayType(parameterType: DslType) extends MonadDslType {
  override val name: String = s"Array[${parameterType.name}]"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case array: ArrayType ⇒ parameterType == array.parameterType
      case _ ⇒ false
    }

  override def carryDslType: DslType = parameterType

  override def transform(carryDslType: DslType): MonadDslType = ArrayType(carryDslType)

}

case class TupleType(parameterTypes: Array[DslType]) extends DslType {
  override val name: String = s"(${parameterTypes.map(_.name).mkString(",")})"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case tupleType: TupleType ⇒
        parameterTypes.sameElements( tupleType.parameterTypes )
      case _ ⇒ false
    }

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case TupleType(dslTypes) if(parameterTypes.length == dslTypes.length) ⇒ TupleType(dslTypes.zip(parameterTypes).map{
      case (t1,t2)⇒t1.commonDslType(importResolver,t2)
    })
    case _⇒AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType match {
    case TupleType(dslTypes) if(parameterTypes.length==dslTypes.length) ⇒ parameterTypes.zip(dslTypes).forall{
      case (t1,t2)⇒t1.isSuperDslType(importResolver,t2)
    }
    case _⇒false
  }
}

case class FutureType(parameterType: DslType) extends MonadDslType {
  override val name: String = s"Future[${parameterType.name}]"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case futureType: FutureType ⇒
        parameterType == futureType.parameterType
      case _ ⇒ false
    }

  override def carryDslType: DslType = parameterType

  override def transform(carryDslType: DslType): MonadDslType = FutureType(carryDslType)
}


case class LambdaType(mayInputType: Option[DslType], outputType: DslType) extends DslType {
  override val name: String = mayInputType match {
    case None⇒s"()=>${outputType.name}"
    case Some(inputType)⇒
        inputType match {
          case tupleType: TupleType⇒s"(${tupleType.parameterTypes.map(_.name).mkString(",")})=>${outputType.name}"
          case _⇒s"${inputType.name}=>${outputType.name}"
        }
  }

  def getInputParamNum:Int={
      mayInputType match {
        case None⇒0
        case Some(TupleType(parameterTypes))⇒ parameterTypes.length
        case _⇒ 1
      }
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case lambdaType: LambdaType ⇒
        lambdaType.mayInputType == mayInputType && outputType == lambdaType.outputType
      case _ ⇒ false
    }

  private def commonDslType(importResolver: ImportResolver, inputType:DslType, anotherInputType:DslType, outputType:DslType):DslType={
    inputType match {
      case inputTupleType: TupleType if anotherInputType.isInstanceOf[TupleType] ⇒
        val anotherTupleType = anotherInputType.asInstanceOf[TupleType]
        val inputTypes = inputTupleType.parameterTypes.zip( anotherTupleType.parameterTypes )
          .map {
            case (t1, t2) ⇒
              if (t1.isSuperDslType( importResolver, t2 )) {
                Some( t2 )
              } else if (t2.isSuperDslType( importResolver, t1 )) {
                Some( t1 )
              } else {
                None
              }
          }
        if (inputTypes.forall( _.isDefined )) {
          LambdaType( Some( TupleType( inputTypes.map( _.get ) ) ), outputType )
        } else {
          AnyType
        }
      case _ ⇒ if (inputType.isInstanceOf[TupleType] ^ anotherInputType.isInstanceOf[TupleType]) {
        AnyType
      } else {
        if (inputType.isSuperDslType( importResolver, anotherInputType )) {
          LambdaType( Some( anotherInputType ), outputType )
        } else if (anotherInputType.isSuperDslType( importResolver, inputType )) {
          LambdaType( Some( inputType ), outputType )
        } else {
          AnyType
        }
      }
    }
  }

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case LambdaType( mayInputType, outputType ) if outputType == UnitType && this.outputType == UnitType ⇒
      //compare consumer
      val inputType = this.mayInputType.get
      val anotherInputType = mayInputType.get
      commonDslType(importResolver,inputType, anotherInputType, UnitType)
    case LambdaType( mayInputType, outputType ) if mayInputType.isEmpty && this.mayInputType.isEmpty ⇒
      //compare supplier
      LambdaType(None,outputType.commonDslType(importResolver,this.outputType))
    case LambdaType(mayInputType,outputType)
      if mayInputType.nonEmpty &&
      this.mayInputType.nonEmpty &&
        outputType!=UnitType &&
        this.outputType!=UnitType⇒
      //compare function
      val inputType = this.mayInputType.get
      val anotherInputType = mayInputType.get
      commonDslType(importResolver, inputType, anotherInputType, outputType.commonDslType(importResolver,this.outputType))
    case _ ⇒ AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType match {
    case LambdaType(mayInputType,outputType) if outputType == UnitType && this.outputType == UnitType⇒
      //compare consumer
      val inputType = this.mayInputType.get
      val anotherInputType = mayInputType.get
      anotherInputType.isSuperDslType(importResolver,inputType)
    case LambdaType( mayInputType, outputType ) if mayInputType.isEmpty && this.mayInputType.isEmpty ⇒
      //compare supplier
      this.outputType.isSuperDslType(importResolver,outputType)
    case LambdaType( mayInputType, outputType )
      if mayInputType.nonEmpty &&
        this.mayInputType.nonEmpty &&
        outputType != UnitType &&
        this.outputType != UnitType ⇒
      //compare function
      val inputType = this.mayInputType.get
      val anotherInputType = mayInputType.get
      anotherInputType.isSuperDslType(importResolver,inputType) && this.outputType.isSuperDslType(importResolver,outputType)
    case _⇒ false
  }
}

object ClazzType {
  def apply(clazzName: String, parameterTypes: Array[DslType] = Array( )): ClazzType = {
    new ClazzType( clazzName, parameterTypes )
  }
  def unapply(clazzType: ClazzType): Option[(String, Array[DslType])] = {
    Some( (clazzType.clazzName, clazzType.parameterTypes) )
  }
}
//clazzType
class ClazzType(val clazzName:String,val parameterTypes:Array[DslType] = Array()) extends DslType {
  override val name: String = if(parameterTypes.isEmpty) {
    s"$clazzName"
  }else{
    s"$clazzName[${parameterTypes.map(_.name).mkString(",")}]"
  }
  override def equals(obj: Any): scala.Boolean = {
    obj match {
      case clazzType: ClazzType ⇒ clazzType.clazzName == clazzName &&
        parameterTypes.sameElements(clazzType.parameterTypes)
      case _ ⇒ false
    }
  }

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case clazzType: ClazzType if clazzType.clazzName==this.clazzName ⇒ this
    case clazzType: ClazzType ⇒
        importResolver.resolve(this.clazzName).commonDslType(importResolver,importResolver.resolve(clazzType.clazzName))
    case _⇒ AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType match {
    case clazzType: ClazzType if clazzType.clazzName == this.clazzName ⇒ true
    case clazzType: ClazzType⇒
      importResolver.resolve(this.clazzName).isSuperDslType(importResolver,importResolver.resolve(clazzType.clazzName))
    case _⇒false
  }
}

case class DefinitionClazzType(clazzName:String,clazzScope: ClazzScope) extends DslType {
  override val name: String = clazzName

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case DefinitionClazzType(clazzName,_) if clazzName == this.clazzName ⇒ this
    case _⇒AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType match {
    case DefinitionClazzType(clazzName,_) if clazzName == this.clazzName ⇒ true
    case _⇒false
  }
}


case class CompositeClazzType(clazzNames:Set[String]) extends DslType{
  override val name: String = clazzNames.mkString("[",",","]")

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = ???
}

case class ImportClazzField(name:String,
                            dslType: DslType)
case class ImportClazzMethod(name:String,
                             params:Array[DslType],
                             returnType:DslType)

case class Inherit(clazzName:String,inherits:Option[Set[Inherit]]) {
  def commonDslType(inherit: Inherit): Option[String] = {
    None
  }

  def isSuperDslType(inherit: Inherit): Boolean = false
}

 case  class ImportClazzType(clazzName:String,
                           fields:Map[String,ImportClazzField] = Map(),
                           staticFields:Map[String,ImportClazzField] = Map(),
                           methods:Map[String,ImportClazzMethod] = Map(),
                           staticMethods:Map[String,ImportClazzMethod] = Map(),
                           inherit:Option[Inherit] = None) extends DslType {
  override val name: String = clazzName

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case importClazzType: ImportClazzType⇒
        if(importClazzType.clazzName == clazzName){
            this
        }else{
//            inherit.commonDslType(importClazzType.inherit) match {
//              case Some(clazzName)⇒ClazzType(clazzName)
//              case None⇒AnyType
//            }
          AnyType
        }
    case _⇒AnyType
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType match {
    case importClazzType: ImportClazzType⇒
        if(importClazzType.clazzName == clazzName){
            true
        }else{
            //inherit.isSuperDslType(importClazzType.inherit)
          false
        }
    case _⇒false
  }
}

object OptionType{
    def apply(parameterType: DslType):OptionType={
        new OptionType(parameterType)
    }
}
class OptionType(val parameterType: DslType) extends MonadDslType {
  override val name: String = s"Option[${parameterType.name}]"
  override def equals(obj: Any): scala.Boolean =
    obj match {
      case option: OptionType ⇒ this.parameterType == option.parameterType
      case _ ⇒ false
    }
  override def carryDslType: DslType = parameterType
  override def transform(carryDslType: DslType): MonadDslType = new OptionType(carryDslType)

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = {
    anotherDslType match {
      case optionType: OptionType ⇒
        new OptionType( parameterType.commonDslType(importResolver,optionType.parameterType) )
      case _ ⇒
        AnyType
    }
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = {
    anotherDslType match {
      case optionType: OptionType ⇒
         parameterType.isSuperDslType(importResolver,optionType.parameterType)
      case _ ⇒ false
    }
  }
}

//resolve in expression reviser

case class SomeType(override val parameterType: DslType) extends OptionType(parameterType) {
  override val name: String = s"Some[${parameterType.name}]"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case someType: SomeType ⇒ parameterType == someType.parameterType
      case _ ⇒ false
    }

  override def carryDslType: DslType = parameterType

  override def transform(carryDslType: DslType): MonadDslType = SomeType(carryDslType)

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case NothingType⇒ new OptionType(parameterType)
    case SomeType(parameterType)⇒ SomeType(parameterType.commonDslType(importResolver, this.parameterType))
    case _⇒ super.commonDslType(importResolver, anotherDslType)
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = this.asInstanceOf[MonadDslType].
    isSuperDslType(importResolver, anotherDslType)
}

object NoneType extends OptionType(NothingType) {
  override val name: String = "None"

  override def carryDslType: DslType = NoneType

  override def transform(carryDslType: DslType): MonadDslType = NoneType

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case SomeType(parameterType)⇒ new OptionType(parameterType)
    case NoneType ⇒ this
    case _⇒ super.commonDslType(importResolver, anotherDslType)
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = this.asInstanceOf[MonadDslType].
    isSuperDslType( importResolver, anotherDslType )
}

class EitherType(val leftParameterType: DslType, val rightParameterType: DslType) extends MonadDslType {
  override val name: String = s"Either[${leftParameterType.name},${rightParameterType.name}]"
  override def equals(obj: Any): Boolean = {
    obj match {
      case eitherType: EitherType ⇒
        eitherType.leftParameterType == leftParameterType && eitherType.rightParameterType == rightParameterType
      case _ ⇒ false
    }
  }
  override def carryDslType: DslType = TupleType(Array(leftParameterType,rightParameterType))
  override def transform(carryDslType: DslType): MonadDslType = new EitherType(leftParameterType,carryDslType)
  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = {
      anotherDslType match {
        case eitherType: EitherType ⇒
          new EitherType( eitherType.leftParameterType.commonDslType( importResolver, leftParameterType ),
            eitherType.rightParameterType.commonDslType( importResolver, rightParameterType ) )
        case _ ⇒
          AnyType
      }
  }
  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = {
     anotherDslType match {
       case eitherType: EitherType⇒
          leftParameterType.isSuperDslType(importResolver,eitherType.leftParameterType) &&
            rightParameterType.isSuperDslType(importResolver,eitherType.rightParameterType)
       case _⇒ false
     }
  }
}

case class LeftType(override val leftParameterType: DslType, override val rightParameterType: DslType = AnyType) extends
  EitherType(leftParameterType, rightParameterType) {
  override val name: String = s"Left[${leftParameterType.name},${rightParameterType.name}]"

  override def equals(obj: Any): Boolean = {
    obj match {
      case leftType: LeftType ⇒
        leftType.leftParameterType == leftType.leftParameterType &&
          leftType.rightParameterType == leftType.rightParameterType
      case _ ⇒ false
    }
  }
  override def transform(carryDslType: DslType): MonadDslType = LeftType(carryDslType,rightParameterType)
  override def commonDslType(importResolver: ImportResolver,
                             anotherDslType: DslType): DslType = {
    anotherDslType match {
      case LeftType(leftParameterType,rightParameterType)⇒
          LeftType(leftParameterType.commonDslType(importResolver,this.leftParameterType),
            rightParameterType.commonDslType(importResolver,this.rightParameterType))
      case RightType(leftParameterType,rightParameterType)⇒
          new EitherType(leftParameterType.commonDslType(importResolver,this.leftParameterType),
            rightParameterType.commonDslType(importResolver,this.rightParameterType))
      case _⇒ super.commonDslType(importResolver,anotherDslType)
    }
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = {
    this.asInstanceOf[MonadDslType].isSuperDslType(importResolver,anotherDslType)
  }
}

case class RightType(override val leftParameterType: DslType = AnyType, override val rightParameterType: DslType) extends EitherType(leftParameterType, rightParameterType) {
  override val name: String = s"Right[${leftParameterType.name},${rightParameterType.name}]"

  override def equals(obj: Any): Boolean = {
    obj match {
      case rightType: RightType ⇒
        rightType.rightParameterType == rightType.rightParameterType && rightType.leftParameterType == leftParameterType
      case _ ⇒ false
    }
  }

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = {
    anotherDslType match {
      case RightType( leftParameterType, rightParameterType ) ⇒
        RightType( leftParameterType.commonDslType( importResolver, this.leftParameterType ),
          rightParameterType.commonDslType( importResolver, this.rightParameterType ) )
      case LeftType( leftParameterType, rightParameterType ) ⇒
        new EitherType( leftParameterType.commonDslType( importResolver, this.leftParameterType ),
          rightParameterType.commonDslType( importResolver, this.rightParameterType ) )
      case _ ⇒ super.commonDslType( importResolver, anotherDslType )
    }
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = {
    this.asInstanceOf[MonadDslType].isSuperDslType(importResolver,anotherDslType)
  }

  override def transform(carryDslType: DslType): MonadDslType = RightType( leftParameterType, carryDslType )
}

object TryType{
  def apply(parameterType: DslType):TryType={
      new TryType(parameterType)
  }
}
class TryType(val parameterType: DslType) extends MonadDslType {
  override val name: String = s"Try[${parameterType.name}]"
  override def equals(obj: Any): scala.Boolean =
    obj match {
      case tryType: TryType ⇒
        parameterType == tryType.parameterType
      case _ ⇒ false
    }
  override def carryDslType: DslType = parameterType
  override def transform(carryDslType: DslType): MonadDslType = new TryType( carryDslType )
  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case tryType: TryType ⇒ new TryType( parameterType.commonDslType( importResolver, tryType.carryDslType ) )
    case _ ⇒ AnyType
  }
  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = anotherDslType match {
    case tryType:TryType⇒ parameterType.isSuperDslType(importResolver,tryType.carryDslType)
    case _⇒ false
  }
}

case class SuccessType(override val parameterType: DslType) extends TryType(parameterType) {
  override val name: String = s"Success[${parameterType.name}]"
  override def equals(obj: Any): scala.Boolean =
    obj match {
      case successType: SuccessType ⇒
        parameterType == successType.parameterType
      case _ ⇒ false
    }
  override def carryDslType: DslType = parameterType
  override def transform(carryDslType: DslType): MonadDslType = SuccessType(carryDslType)
  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case FailureType(_,parameterType) ⇒ new TryType(parameterType)
    case SuccessType(parameterType)⇒ SuccessType(parameterType.commonDslType(importResolver,this.parameterType))
    case _⇒ super.commonDslType(importResolver, anotherDslType)
  }

  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = this.asInstanceOf[MonadDslType].
    isSuperDslType( importResolver, anotherDslType )
}

case class  FailureType(error:ThrowableType.type ,override val parameterType: DslType) extends TryType(parameterType) {
  override val name: String = "Failure"

  override def carryDslType: DslType = parameterType

  override def transform(carryDslType: DslType): MonadDslType = FailureType(error,carryDslType)

  override def commonDslType(importResolver: ImportResolver, anotherDslType: DslType): DslType = anotherDslType match {
    case FailureType(error,parameterType) ⇒ FailureType(error,parameterType.commonDslType(importResolver,this.parameterType))
    case SuccessType( parameterType ) ⇒ new TryType( parameterType.commonDslType(importResolver,this.parameterType) )
    case _ ⇒ super.commonDslType( importResolver, anotherDslType )
  }
  override def isSuperDslType(importResolver: ImportResolver, anotherDslType: DslType): Boolean = this.asInstanceOf[MonadDslType].
    isSuperDslType( importResolver, anotherDslType )
}





