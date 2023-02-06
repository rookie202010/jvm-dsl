package com.dongjiaqiang.jvm.dsl.api.`type`

trait DslType {
  val name: String

  override def toString: String = name
}

trait MonadDslType extends DslType {
    def carryDslType:DslType

    def transform(carryDslType:DslType):MonadDslType
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

object StringType extends DslType with BasicDslType with MonadDslType {
  override val name: String = "String"

  override def carryDslType: DslType = CharType

  override def transform(carryDslType: DslType): MonadDslType = StringType
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

case class ListType(parameterType: DslType) extends MonadDslType {
  override val name: String = s"List[${parameterType.name}]"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case listType: ListType ⇒ parameterType == listType.parameterType
      case _ ⇒ false

    }

  override def carryDslType: DslType = parameterType

  override def transform(carryDslType: DslType): MonadDslType = ListType(carryDslType)
}

case class SetType(parameterType:DslType) extends MonadDslType {
  override val name: String = s"Set[${parameterType.name}]"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case setType: SetType ⇒ parameterType == setType.parameterType
      case _ ⇒ false
    }

  override def carryDslType: DslType = parameterType

  override def transform(carryDslType: DslType): MonadDslType = SetType(carryDslType)
}

case class MapType(keyParameterType:DslType, valueParameterType:DslType) extends MonadDslType {
  override val name: String = s"Map[${keyParameterType.name},${valueParameterType.name}]"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case mapType: MapType ⇒ valueParameterType == mapType.valueParameterType && keyParameterType == mapType.keyParameterType
      case _ ⇒ false
    }

  override def carryDslType: DslType = TupleType(Array(keyParameterType,valueParameterType))

  override def transform(carryDslType: DslType): MonadDslType = MapType(carryDslType.asInstanceOf[TupleType].parameterTypes.head,
    carryDslType.asInstanceOf[TupleType].parameterTypes.last)
}

case class OptionType(parameterType: DslType) extends MonadDslType {
  override val name: String = s"Option[${parameterType.name}]"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case option: OptionType ⇒ parameterType == option.parameterType
      case _ ⇒ false
    }

  override def carryDslType: DslType = parameterType

  override def transform(carryDslType: DslType): MonadDslType = OptionType(carryDslType)
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

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case lambdaType: LambdaType ⇒
        lambdaType.mayInputType == mayInputType && outputType == lambdaType.outputType
      case _ ⇒ false
    }
}

//clazzType
case class ClazzType(clazzName:String,parameterTypes:Array[DslType] = Array()) extends DslType {
  override val name: String = if(parameterTypes.isEmpty) {
    s"$clazzName"
  }else{
    s"$clazzName[${parameterTypes.map(_.name).mkString(",")}]"
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case clazzType: ClazzType ⇒ clazzType.clazzName == clazzName &&
        parameterTypes.sameElements(clazzType.parameterTypes)
      case _ ⇒ false
    }
}

//resolve in expression reviser

case class SomeType(parameterType: DslType) extends DslType {
  override val name: String = s"Some[${parameterType.name}]"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case someType: SomeType ⇒ parameterType == someType.parameterType
      case _ ⇒ false
    }
}

object NoneType extends DslType {
  override val name: String = "None"
}

case class EitherType(leftParameterType: DslType, rightParameterType: DslType) extends MonadDslType {
  override val name: String = s"Either[${leftParameterType.name},${rightParameterType.name}]"

  override def equals(obj: Any): Boolean = {
    obj match {
      case eitherType: EitherType ⇒
        eitherType.leftParameterType == leftParameterType && eitherType.rightParameterType == rightParameterType
      case _ ⇒ false
    }
  }

  override def carryDslType: DslType = rightParameterType

  override def transform(carryDslType: DslType): MonadDslType = EitherType(leftParameterType,carryDslType)
}

case class LeftType(parameterType: DslType) extends DslType {
  override val name: String = s"Left[${parameterType.name}"

  override def equals(obj: Any): Boolean = {
    obj match {
      case leftType: LeftType ⇒
        leftType.parameterType == leftType
      case _ ⇒ false
    }
  }
}

case class RightType(parameterType: DslType) extends DslType {
  override val name: String = s"Right[${parameterType.name}"

  override def equals(obj: Any): Boolean = {
    obj match {
      case leftType: RightType ⇒
        leftType.parameterType == leftType
      case _ ⇒ false
    }
  }
}

case class TryType(parameterType: DslType) extends MonadDslType {
  override val name: String = s"Try[${parameterType.name}]"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case tryType: TryType ⇒
        parameterType == tryType.parameterType
      case _ ⇒ false
    }

  override def carryDslType: DslType = parameterType

  override def transform(carryDslType: DslType): MonadDslType = TryType(carryDslType)
}

case class SuccessType(parameterType: DslType) extends DslType {
  override val name: String = s"Success[${parameterType.name}"

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case successType: SuccessType ⇒
        parameterType == successType.parameterType
      case _ ⇒ false
    }
}

object FailureType extends DslType {
  override val name: String = "Failure"
}





