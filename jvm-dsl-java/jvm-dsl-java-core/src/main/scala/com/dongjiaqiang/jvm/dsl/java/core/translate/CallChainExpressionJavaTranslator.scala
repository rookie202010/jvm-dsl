package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.api.expression.{CharLiteralCallChain, IntLiteralCallChain, MethodCall, VarName}
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.callchain.CallChainExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api.exception.JavaTranslatorException

object CallChainExpressionJavaTranslator{
    val INT_LITERAL_CALL_METHODS = Set("until","to")
}
trait CallChainExpressionJavaTranslator extends CallChainExpressionVisitor[String]{

  override def visit(literalCallChain: IntLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    val head = literalCallChain.tails.head
    val tail = literalCallChain.tails.tail
    head match {
      case _:VarName⇒
        throw JavaTranslatorException("")
      case MethodCall(_, name, params)⇒
        ""
    }

  }

  override def visit(literalCallChain: CharLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    "xxss".toLong
  }
}
