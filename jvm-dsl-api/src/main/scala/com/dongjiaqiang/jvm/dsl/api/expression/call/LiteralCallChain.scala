package com.dongjiaqiang.jvm.dsl.api.expression.call

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.literal._
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class LiteralCallChain[T, D <: DslType](val head: Literal[T, D], val tails: List[Part],val tailDslTypes:List[DslType] = List()) extends ValueExpression {
  override def toString: String = s"$head.${tails.mkString( "." )}"

  override def getValueType(programScope: ProgramScope): DslType = {
    val calleeType = head.getValueType( programScope )
    FuncCallChain.getValueType( programScope, calleeType, tails )
  }
}

class BoolLiteralCallChain(override val head: BoolLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[Boolean, BoolType.type]( head, tails,tailDslTypes ) {

  override def equals(obj: Any): Boolean = obj match {
    case boolLiteralCallChain: BoolLiteralCallChain ⇒
      boolLiteralCallChain.head == head && boolLiteralCallChain.tails == tails
    case _ ⇒ false
  }

}

class CharLiteralCallChain(override val head: CharLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[Char, CharType.type]( head, tails,tailDslTypes ) {

  override def equals(obj: Any): Boolean = obj match {
    case charLiteralCallChain: CharLiteralCallChain ⇒
      charLiteralCallChain.head == head && charLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}

class ClazzLiteralCallChain(override val head: ClazzLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[Array[ValueExpression], DslType]( head, tails ,tailDslTypes) {

  override def equals(obj: Any): Boolean = obj match {
    case clazzLiteralCallChain: ClazzLiteralCallChain ⇒
      clazzLiteralCallChain.head == head && clazzLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}

class DoubleLiteralCallChain(override val head: DoubleLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[Double, DoubleType.type]( head, tails ,tailDslTypes) {
  override def equals(obj: Any): Boolean = obj match {
    case doubleLiteralCallChain: DoubleLiteralCallChain ⇒
      doubleLiteralCallChain.head == head && doubleLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}

class FloatLiteralCallChain(override val head: FloatLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[Float, FloatType.type]( head, tails ,tailDslTypes) {
  override def equals(obj: Any): Boolean = obj match {
    case floatLiteralCallChain: FloatLiteralCallChain ⇒
      floatLiteralCallChain.head == head && floatLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}

class IntLiteralCallChain(override val head: IntLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType] = List())
  extends LiteralCallChain[Int, IntType.type]( head, tails ,tailDslTypes) {
  override def equals(obj: Any): Boolean = obj match {
    case intLiteralCallChain: IntLiteralCallChain ⇒
      intLiteralCallChain.head == head && intLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}

class ListLiteralCallChain(override val head: ListLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[Array[ValueExpression], ListType]( head, tails,tailDslTypes ) {

  override def equals(obj: Any): Boolean = obj match {
    case listLiteralCallChain: ListLiteralCallChain ⇒
      listLiteralCallChain.head == head && listLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}

class LongLiteralCallChain(override val head: LongLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[Long, LongType.type]( head, tails ,tailDslTypes) {

  override def equals(obj: Any): Boolean = obj match {
    case longLiteralCallChain: LongLiteralCallChain ⇒
      longLiteralCallChain.head == head && longLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}

class MapLiteralCallChain(override val head: MapLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[Array[(ValueExpression, ValueExpression)], MapType]( head, tails,tailDslTypes ) {
  override def equals(obj: Any): Boolean = obj match {
    case mapLiteralCallChain: MapLiteralCallChain ⇒
      mapLiteralCallChain.head == head && mapLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}

class OptionLiteralCallChain(override val head: OptionLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[ValueExpression, OptionType]( head, tails,tailDslTypes ) {

  override def equals(obj: Any): Boolean = obj match {
    case optionLiteralCallChain: OptionLiteralCallChain ⇒
      optionLiteralCallChain.head == head && optionLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}

class SetLiteralCallChain(override val head: SetLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[Array[ValueExpression], SetType]( head, tails,tailDslTypes ) {

  override def equals(obj: Any): Boolean = obj match {
    case setLiteralCallChain: SetLiteralCallChain ⇒
      setLiteralCallChain.head == head && setLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}

class StringLiteralCallChain(override val head: StringLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[String, StringType.type]( head, tails ,tailDslTypes) {

  override def equals(obj: Any): Boolean = obj match {
    case stringLiteralCallChain: StringLiteralCallChain ⇒
      stringLiteralCallChain.head == head && stringLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}


class TupleLiteralCallChain(override val head: TupleLiteral, override val tails: List[Part], override val tailDslTypes: List[DslType]= List())
  extends LiteralCallChain[Array[ValueExpression], TupleType]( head, tails ,tailDslTypes) {

  override def equals(obj: Any): Boolean = obj match {
    case tupleLiteralCallChain: TupleLiteralCallChain ⇒
      tupleLiteralCallChain.head == head && tupleLiteralCallChain.tails == tails
    case _ ⇒ false
  }
}



