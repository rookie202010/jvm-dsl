package com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain

import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.VarRef
import com.dongjiaqiang.jvm.dsl.api.expression.call._
import com.dongjiaqiang.jvm.dsl.api.expression.literal._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.callchain.CallChainExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionReviser


trait CallChainExpressionReviser extends CallChainExpressionVisitor[Expression] {
  def revise(tails: List[Part], visitor: ExpressionVisitor[Expression]): Option[List[Part]] = {

    val reviseTails = tails.map {
      case varRef: VarRef ⇒
        VarRef(varRef.refs,varRef.arrayRefIndexExpressions.map{
          case (index,expression)⇒ (index,
            expression.map(visitor.visit).map(_.asInstanceOf[ValueExpression]))
        },varRef.fieldScope)
      case methodCall: MethodCall ⇒
        MethodCall( methodCall.methodScope,
          methodCall.name,
          methodCall.params.map( visitor.visit ).map(_.asInstanceOf[ValueExpression]) )
    }

    val success = reviseTails.zip(tails)
      .exists {
        case (p1, p2) ⇒
          p1 match {
            case call: MethodCall if p2.isInstanceOf[MethodCall] ⇒
              if (call.params.length != p2.asInstanceOf[MethodCall].params.length) {
                true
              } else {
                call.params.zip(p2.asInstanceOf[MethodCall].params).exists {
                  case (e1, e2) ⇒ e1 != e2
                }
              }
            case _ ⇒
              false
          }
      }
    if (success) {
      Some(reviseTails)
    } else {
      None
    }
  }

  override def visit(funcCallChain: FuncCallChain,
                     visitor: ExpressionVisitor[Expression]): Expression = {
      val reviseTails = revise(funcCallChain.tails,visitor)
      val params = ExpressionReviser.revise[ValueExpression,Expression](funcCallChain.head.params,visitor)
      funcCallChain.head match {
        case methodCall: MethodCall⇒
          if(reviseTails.isDefined || params.isDefined) {
            FuncCallChain( MethodCall( methodCall.methodScope,
              methodCall.name, params.getOrElse( methodCall.params ).map(_.asInstanceOf[ValueExpression]) ),
              reviseTails.getOrElse( funcCallChain.tails ),funcCallChain.tailDslTypes )
          }else{
              funcCallChain
          }
        case staticCall: StaticCall⇒
          if(reviseTails.isDefined || params.isDefined) {
            FuncCallChain( StaticCall( staticCall.`type`, staticCall.name,
              params.getOrElse( staticCall.params ) .map(_.asInstanceOf[ValueExpression])),
              reviseTails.getOrElse( funcCallChain.tails ),funcCallChain.tailDslTypes )
          }else{
            funcCallChain
          }
        case literalCall: LiteralCall⇒
          val reviseLiteral = visitor.visit(literalCall.literal)
          if(reviseTails.isDefined || reviseLiteral!=literalCall.literal || params.isDefined){
            FuncCallChain( new LiteralCall( reviseLiteral.asInstanceOf[ValueExpression],
              literalCall.name,
              params.getOrElse( literalCall.params ) .map(_.asInstanceOf[ValueExpression])),
              reviseTails.getOrElse( funcCallChain.tails ),funcCallChain.tailDslTypes )
          }else{
              funcCallChain
          }
        case varCall:VarCall⇒
          val reviseVar = visitor.visit(varCall.varRef) .asInstanceOf[ValueExpression]
          if(reviseTails.isDefined || reviseVar!=varCall.varRef || params.isDefined){
            FuncCallChain( VarCall( reviseVar, varCall.name,
              params.getOrElse( varCall.params ).map(_.asInstanceOf[ValueExpression]) ),
              reviseTails.getOrElse( funcCallChain.tails ),funcCallChain.tailDslTypes )
          }else{
            funcCallChain
          }
      }
  }

  override def visit(literalCallChain: IntLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
      val reviseTails = revise(literalCallChain.tails,visitor)
      if(reviseTails.isDefined){
          new IntLiteralCallChain(literalCallChain.head,reviseTails.get)
      }else{
          literalCallChain
      }
  }

  override def visit(literalCallChain: LongLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val reviseTails = revise(literalCallChain.tails,visitor)
    if(reviseTails.isDefined){
      new LongLiteralCallChain(literalCallChain.head,reviseTails.get)
    }else{
      literalCallChain
    }
  }

  override def visit(literalCallChain: DoubleLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val reviseTails = revise(literalCallChain.tails,visitor)
    if(reviseTails.isDefined){
      new DoubleLiteralCallChain(literalCallChain.head,reviseTails.get)
    }else{
      literalCallChain
    }
  }

  override def visit(literalCallChain: FloatLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val reviseTails = revise(literalCallChain.tails,visitor)
    if(reviseTails.isDefined){
      new FloatLiteralCallChain(literalCallChain.head,reviseTails.get)
    }else{
      literalCallChain
    }
  }

  override def visit(literalCallChain: BoolLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val reviseTails = revise(literalCallChain.tails,visitor)
    if(reviseTails.isDefined){
      new BoolLiteralCallChain(literalCallChain.head,reviseTails.get)
    }else{
      literalCallChain
    }
  }

  override def visit(literalCallChain: StringLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val reviseTails = revise(literalCallChain.tails,visitor)
    if(reviseTails.isDefined){
      new StringLiteralCallChain(literalCallChain.head,reviseTails.get)
    }else{
      literalCallChain
    }
  }

  override def visit(literalCallChain: CharLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val reviseTails = revise(literalCallChain.tails,visitor)
    if(reviseTails.isDefined){
      new CharLiteralCallChain(literalCallChain.head,reviseTails.get)
    }else{
      literalCallChain
    }
  }

  override def visit(literalCallChain: ListLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
      val head = visitor.visit(literalCallChain.head).asInstanceOf[ListLiteral]
      val tails = revise(literalCallChain.tails,visitor)
    if (head != literalCallChain.head || tails.isDefined) {
      new ListLiteralCallChain( head, tails.getOrElse( literalCallChain.tails ) )
    } else {
      literalCallChain
    }
  }

  override def visit(literalCallChain: OptionLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val head = visitor.visit(literalCallChain.head).asInstanceOf[OptionLiteral]
    val tails = revise(literalCallChain.tails,visitor)
    if (head != literalCallChain.head || tails.isDefined) {
      new OptionLiteralCallChain( head, tails.getOrElse( literalCallChain.tails ) )
    } else {
      literalCallChain
    }
  }

  override def visit(literalCallChain: TupleLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val head = visitor.visit(literalCallChain.head).asInstanceOf[TupleLiteral]
    val tails = revise(literalCallChain.tails,visitor)
    if (head != literalCallChain.head || tails.isDefined) {
      new TupleLiteralCallChain( head, tails.getOrElse( literalCallChain.tails ) )
    } else {
      literalCallChain
    }
  }

  override def visit(literalCallChain: MapLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val head = visitor.visit(literalCallChain.head).asInstanceOf[MapLiteral]
    val tails = revise(literalCallChain.tails,visitor)
    if (head != literalCallChain.head || tails.isDefined) {
      new MapLiteralCallChain( head, tails.getOrElse( literalCallChain.tails ) )
    } else {
      literalCallChain
    }
  }

  override def visit(literalCallChain: SetLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val head = visitor.visit(literalCallChain.head).asInstanceOf[SetLiteral]
    val tails = revise(literalCallChain.tails,visitor)
    if (head != literalCallChain.head || tails.isDefined) {
      new SetLiteralCallChain( head, tails.getOrElse( literalCallChain.tails ) )
    } else {
      literalCallChain
    }
  }

  override def visit(literalCallChain: ClazzLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val head = visitor.visit(literalCallChain.head).asInstanceOf[ClazzLiteral]
    val tails = revise(literalCallChain.tails,visitor)
    if (head != literalCallChain.head || tails.isDefined) {
      new ClazzLiteralCallChain( head, tails.getOrElse( literalCallChain.tails ) )
    } else {
      literalCallChain
    }
  }
}
