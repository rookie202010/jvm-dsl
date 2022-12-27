package com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain

import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.{BoolLiteralCallChain, CharLiteralCallChain, ClazzLiteralCallChain, DoubleLiteralCallChain, Expression, FloatLiteralCallChain, FuncCallChain, IntLiteralCallChain, ListLiteralCallChain, LongLiteralCallChain, MapLiteralCallChain, MethodCall, OptionLiteralCallChain, Part, SetLiteralCallChain, StringLiteralCallChain, TupleLiteralCallChain}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait CallChainExpressionReviser extends CallChainExpressionVisitor[Expression]{
  def revise(tails:List[Part],visitor: ExpressionVisitor[Expression]):Boolean={

      tails

      val calls = tails.filter(_.isInstanceOf[MethodCall]).map(_.asInstanceOf[MethodCall])
      val revise = calls.map(_.params).map(expressions⇒{
        expressions.map(visitor.visit)
      })
      revise.zip(calls.map(_.params)).exists{
        case (ps1,ps2)⇒
            ps1.zip(ps2).exists{
              case (p1,p2)⇒ p1!=p2
            }
      }
  }

  override def visit(funcCallChain: FuncCallChain,
                     visitor: ExpressionVisitor[Expression]): Expression = {
      val params = funcCallChain.head.params.map(visitor.visit)
      val headRevise = params.zip(funcCallChain.head.params).exists{
        case (e1,e2)⇒e1!=e2
      }
      val tailRevise = revise(funcCallChain.tails,visitor)
      if(headRevise || tailRevise){

      }

  }

  override def visit(literalCallChain: IntLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {

  }

  override def visit(literalCallChain: LongLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???

  override def visit(literalCallChain: DoubleLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???

  override def visit(literalCallChain: FloatLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???

  override def visit(literalCallChain: BoolLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???

  override def visit(literalCallChain: StringLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???

  override def visit(literalCallChain: CharLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???

  override def visit(literalCallChain: ListLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???

  override def visit(literalCallChain: OptionLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???

  override def visit(literalCallChain: TupleLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???

  override def visit(literalCallChain: MapLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???

  override def visit(literalCallChain: SetLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???

  override def visit(literalCallChain: ClazzLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = ???
}
