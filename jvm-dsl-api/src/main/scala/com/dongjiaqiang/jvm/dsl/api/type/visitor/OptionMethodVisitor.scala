package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, LongType, OptionType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait OptionMethodVisitor[T] extends MonadMethodVisitor[T] {

  def get(calleeType: OptionType, callee: ValueExpression): T

  def getOrElse(calleeType: OptionType, callee: ValueExpression, default: ValueExpression): T

  def orElse(calleeType: OptionType, callee: ValueExpression, default: ValueExpression): T

  def isDefine(calleeType: OptionType, callee: ValueExpression): T

  override def visit(calleeType: DslType,
                     callee: ValueExpression,
                     name: String, params:
                     Array[ValueExpression]): Option[T] = {
    val optionType = calleeType.asInstanceOf[OptionType]
    (name match {
      case MethodVisitor.GET⇒
        generate(params,()⇒ get(optionType,callee))
      case MethodVisitor.OR_ELSE⇒
        generate(Array(LongType),params, ()⇒orElse(optionType,callee,params.head))
      case MethodVisitor.GET_OR_ELSE⇒
        if(paramsChecker) {
          Some(getOrElse( optionType, callee, params.head ))
    //      generate( Array( optionType.carryDslType ), params, () ⇒ getOrElse( optionType, callee, params.head ) )
        }else{
          Some(getOrElse( optionType, callee, params.head ))
        }
      case MethodVisitor.IS_DEFINE⇒
        generate(params,()⇒isDefine(optionType,callee))
      case _⇒
        None
    }).orElse(super.visit(calleeType, callee, name, params))
  }
}
