package com.dongjiaqiang.jvm.dsl.api.expression.unary
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

case class SignedNumber(signed:String, override val child: ValueExpression) extends UnaryExpression {

  override def toString: String = s"$signed$child"

  override def equals(obj: Any): Boolean = obj match {
    case signedNumber: SignedNumber ⇒ signedNumber.signed == signed && signedNumber.child == child
    case _ ⇒ false
  }


}
