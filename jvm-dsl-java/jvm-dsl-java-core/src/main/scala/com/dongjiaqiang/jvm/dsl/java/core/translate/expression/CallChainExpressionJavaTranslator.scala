package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.VarRef
import com.dongjiaqiang.jvm.dsl.api.expression.call._
import com.dongjiaqiang.jvm.dsl.api.expression.literal.SetLiteral
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.callchain.CallChainExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaCode
import com.dongjiaqiang.jvm.dsl.java.core.translate.method.MultiMethodJavaTranslator

import scala.annotation.tailrec


trait CallChainExpressionJavaTranslator extends CallChainExpressionVisitor[String]{

  val multiTranslator:MultiMethodJavaTranslator

  @tailrec
  private def translate(callee:String, calleeType:DslType, parts:List[(Part,DslType)], visitor: ExpressionVisitor[String]):String={
      parts match {
        case Nil⇒
            callee
        case (head,headDslType)::tails⇒
            head match {
              case varRef: VarRef ⇒
                val newCallee = s"$callee.${visitor.visit( varRef )}"
                translate( newCallee, headDslType, tails, visitor )
              case methodCall: MethodCall ⇒
                val newCallee = multiTranslator.visit( calleeType, JavaCode( callee, calleeType ), methodCall.name, methodCall.params ).get
                translate( newCallee, headDslType, tails, visitor )
            }
      }
  }

  private def translate(expression:ValueExpression, tails:List[Part], tailDslTypes:List[DslType], visitor: ExpressionVisitor[String]):String={
    val code = visitor.visit( expression )
    translate( code, expression.getValueType( visitor.programScope ), tails.zip( tailDslTypes ), visitor )
  }

  override def visit(funcCallChain: FuncCallChain, visitor: ExpressionVisitor[String]): String = {
      translate(funcCallChain.head,funcCallChain.tails,funcCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: IntLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
      translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: LongLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: DoubleLiteralCallChain, visitor: ExpressionVisitor[String]): String ={
    translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: FloatLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: BoolLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: StringLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: CharLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: ListLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: OptionLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: TupleLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: MapLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }

  override def visit(literalCallChain: SetLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    val (newTails,newTailDslTypes,calleeType) = literalCallChain.tails.head match {
      case MethodCall(_, MethodVisitor.TO_SEQ_SET, _)⇒
          literalCallChain.head.dslType.copy(seq = true)
        (literalCallChain.tails.tail,literalCallChain.tailDslTypes.tail,literalCallChain.head.dslType.copy(seq = true))
      case MethodCall(_,MethodVisitor.TO_SORTED_SET,_)⇒
        (literalCallChain.tails.tail,literalCallChain.tailDslTypes.tail,literalCallChain.head.dslType.copy(sorted = true))
      case _⇒
        (literalCallChain.tails,literalCallChain.tailDslTypes,literalCallChain.head.dslType)
    }
    translate(new SetLiteral(literalCallChain.head.literal,calleeType),newTails,newTailDslTypes,visitor)
  }

  override def visit(literalCallChain: ClazzLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
    translate(literalCallChain.head,literalCallChain.tails,literalCallChain.tailDslTypes,visitor)
  }
}
