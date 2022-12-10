package com.dongjiaqiang.jvm.dsl.core.`type`
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.{ArrayTypeContext, CharTypeContext, ClassTypeContext, DoubleTypeContext, FloatTypeContext, FutureTypeContext,
  IntTypeContext,
  LambdaMoreInMoreOutTypeContext, LambdaMoreInOneOutTypeContext, LambdaOneInMoreOutTypeContext,
  LambdaOneInOneOutTypeContext, LambdaZeroInMoreOutTypeContext, LambdaZeroInOneOutTypeContext, ListTypeContext, LongTypeContext, MapTypeContext, OptionTypeContext,
  ParameterizedClassTypeContext, SetTypeContext, StringTypeContext, TupleTypeContext}
import org.antlr.v4.runtime.tree.TerminalNode

import java.lang.Integer
import java.lang.Long
import java.lang.Float
import java.lang.Double
import java.lang.String
import java.lang.Character
import java.lang.Byte
import java.lang.Boolean
import java.util

trait DslType {
    val name:String
}

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.TypeContext
object DslType{
    import scala.collection.convert.ImplicitConversionsToScala._
    def unapply(typeContext:TypeContext):DslType={
        typeContext match {
          case _:IntTypeContext⇒IntType
          case _:LongTypeContext⇒LongType
          case _:FloatTypeContext⇒FloatType
          case _:DoubleTypeContext⇒DoubleType
          case _:StringTypeContext⇒StringType
          case _:CharTypeContext⇒CharType
          case context: ListTypeContext⇒
            new ListType(unapply(context.`type`()))
          case context: ArrayTypeContext⇒
            new ArrayType(unapply(context))
          case context: SetTypeContext⇒
            new SetType(unapply(context.`type`()))
          case  context: MapTypeContext⇒
            new MapType(unapply(context.`type`(0)),unapply(context.`type`(1)))
          case context: TupleTypeContext⇒
            new TupleType(context.`type`().map(`type`⇒unapply(`type`)).toArray)
          case context:OptionTypeContext⇒
            new OptionType(unapply(context.`type`()))
          case context:FutureTypeContext⇒
            new FutureType(unapply(context.`type`()))
          case context:LambdaOneInOneOutTypeContext⇒
            new LambdaType(Array(unapply(context.`type`().head)),Array(unapply(context.`type`.last)))
          case context:LambdaOneInMoreOutTypeContext⇒
            new LambdaType(Array(unapply(context.`type`())),context.types().`type`().map(unapply).toArray)
          case context:LambdaZeroInOneOutTypeContext⇒
            new LambdaType(Array(),Array(unapply(context.`type`())))
          case context:LambdaZeroInMoreOutTypeContext⇒
            new LambdaType(Array(),context.types().`type`().map(unapply).toArray)
          case context:LambdaMoreInOneOutTypeContext⇒
            new LambdaType(context.types().`type`().map(unapply).toArray,Array(unapply(context.`type`())))
          case context:LambdaMoreInMoreOutTypeContext⇒
            new LambdaType(context.types(0).`type`().map(unapply).toArray,
              context.types(1).`type`().map(unapply).toArray)
          case context:ParameterizedClassTypeContext⇒
              new ClazzType(context.children.map( _.asInstanceOf[TerminalNode] )
                .map( _.getText ).mkString(""),context.`type`().map(unapply).toArray)
          case context:ClassTypeContext⇒
            new ClazzType( context.children.map( _.asInstanceOf[TerminalNode] )
              .map( _.getText ).mkString(""),Array())
        }
    }
}

//base type
object IntType extends DslType{
  override val name: String = "Int"

}

object LongType extends DslType{
  override val name: String = "Long"

}

object FloatType extends DslType{
  override val name: String = "Float"

}

object DoubleType extends DslType{
  override val name: String = "Double"

}

object StringType extends DslType{
  override val name: String = "String"

}

object CharType extends DslType{
  override val name: String = "Char"

}

object ByteType extends DslType{
  override val name: String = "Byte"

}

object BoolType extends DslType{
  override val name: String = "Bool"

}

import java.util.{List⇒JavaList}
import java.util.{Set⇒JavaSet}
import java.util.{Map⇒JavaMap}
import java.util.{Optional⇒Option}

//collection type
class ListType(val valueType:DslType) extends DslType{
  override val name: String = "List"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case listType: ListType ⇒ valueType == listType.valueType
      case _ ⇒ false

    }
}

class SetType(val valueType:DslType) extends DslType {
  override val name: String = "Set"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case setType: SetType ⇒ valueType == setType.valueType
      case _ ⇒ false
    }

}

class MapType(val keyType:DslType, val valueType:DslType) extends DslType {
  override val name: String = "Map"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case mapType: MapType ⇒ valueType == mapType.valueType && keyType == mapType.keyType
      case _ ⇒ false
    }
}

class OptionType(val valueType:DslType) extends DslType{
  override val name: String = "Option"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case option: OptionType ⇒ valueType equals  option.valueType
      case _ ⇒ false
    }


}

class ArrayType(val valueType:DslType) extends DslType{
  override val name: String = "Array"

  override def equals(obj: Any): scala.Boolean =
      obj match {
        case array: ArrayType ⇒ valueType == array.valueType
        case _⇒false
      }
}

class TupleType(val valueTypes:Array[DslType]) extends DslType{
  override val name: String = "Tuple"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case tupleType: TupleType ⇒
        valueTypes.sameElements(tupleType.valueTypes)
      case _ ⇒ false
    }
}

class FutureType(val valueType:DslType) extends DslType{
  override val name: String = "Future"

  override def equals(obj: Any): scala.Boolean =
      obj match {
        case futureType: FutureType⇒
            valueType==futureType.valueType
        case _ ⇒ false
      }
}

class LambdaType(val inputTypes:Array[DslType], val outputTypes:Array[DslType]) extends DslType {
  override val name: String = "Lambda"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case lambdaType: LambdaType ⇒
        inputTypes.sameElements(lambdaType.inputTypes) &&
          outputTypes.sameElements(lambdaType.outputTypes)
      case _ ⇒ false
    }
}

//clazzType
class ClazzType(val clazzName:String,val valueTypes:Array[DslType]) extends DslType {
  override val name: String = "Clazz"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case clazzType: ClazzType ⇒ clazzType.clazzName == clazzName &&
        valueTypes.sameElements(clazzType.valueTypes)
      case _ ⇒ false
    }
}





