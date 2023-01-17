package com.dongjiaqiang.jvm.dsl.api.`type`

trait DslType {
  val name: String
}

trait NumberDslType extends DslType

trait BasicDslType extends DslType


//base type
object IntType extends NumberDslType with BasicDslType {
  override val name: String = "Int"

}

object LongType extends NumberDslType with BasicDslType {
  override val name: String = "Long"

}

object FloatType extends NumberDslType with BasicDslType {
  override val name: String = "Float"

}

object DoubleType extends NumberDslType with BasicDslType {
  override val name: String = "Double"

}

object StringType extends DslType with BasicDslType {
  override val name: String = "String"

}

object CharType extends DslType with BasicDslType {
  override val name: String = "Char"

}

object ByteType extends DslType with BasicDslType {
  override val name: String = "Byte"

}

object BoolType extends DslType with BasicDslType {
  override val name: String = "Bool"
}

object UnResolvedType extends DslType {
  override val name: String = "UnResolved"
}

object AnyType extends DslType {
  override val name: String = "Any"
}

object UnitType extends DslType {
  override val name: String = "Unit"
}

//collection type
class ListType(val valueType: DslType) extends DslType {
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

class OptionType(val valueType: DslType) extends DslType {
  override val name: String = "Option"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case option: OptionType ⇒ valueType == option.valueType
      case _ ⇒ false
    }
}

class SomeType(val valueType: DslType) extends DslType {
  override val name: String = "Some"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case someType: SomeType ⇒ valueType == someType.valueType
      case _ ⇒ false
    }
}

object NoneType extends DslType {
  override val name: String = "None"
}

class ArrayType(val valueType: DslType) extends DslType {
  override val name: String = "Array"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case array: ArrayType ⇒ valueType == array.valueType
      case _ ⇒ false
    }
}

class EitherType(val leftType: DslType, val rightType: DslType) extends DslType {
  override val name: String = "Either"

  override def equals(obj: Any): Boolean = {
    obj match {
      case eitherType: EitherType ⇒
        eitherType.leftType == leftType && eitherType.rightType == rightType
      case _ ⇒ false
    }
  }
}

class LeftType(val leftType: DslType) extends DslType {
  override val name: String = "Left"

  override def equals(obj: Any): Boolean = {
    obj match {
      case leftType: LeftType ⇒
        leftType.leftType == leftType
      case _ ⇒ false
    }
  }
}

class RightType(val rightType: DslType) extends DslType {
  override val name: String = "Left"

  override def equals(obj: Any): Boolean = {
    obj match {
      case leftType: RightType ⇒
        leftType.rightType == leftType
      case _ ⇒ false
    }
  }
}


class TupleType(val valueTypes: Array[DslType]) extends DslType {
  override val name: String = "Tuple"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case tupleType: TupleType ⇒
        valueTypes.sameElements( tupleType.valueTypes )
      case _ ⇒ false
    }
}

class FutureType(val valueType: DslType) extends DslType {
  override val name: String = "Future"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case futureType: FutureType ⇒
        valueType == futureType.valueType
      case _ ⇒ false
    }
}

class TryType(val valueType: DslType) extends DslType {
  override val name: String = "Try"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case tryType: TryType ⇒
        valueType == tryType.valueType
      case _ ⇒ false
    }
}

class SuccessType(val valueType: DslType) extends DslType {
  override val name: String = "Success"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case successType: SuccessType ⇒
        valueType == successType.valueType
      case _ ⇒ false
    }
}

object FailureType extends DslType {
  override val name: String = "Failure"
}

class LambdaType(val inputType: Option[DslType], val outputType: DslType) extends DslType {
  override val name: String = "Lambda"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case lambdaType: LambdaType ⇒
        lambdaType.inputType == inputType && outputType == lambdaType.outputType
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





