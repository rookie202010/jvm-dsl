package com.dongjiaqiang.jvm.dsl.core.expression.visitor

import com.dongjiaqiang.jvm.dsl.core.expression.Expression
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`.VarExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.binary.expression.BinaryExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.block.BlockExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain.CallChainExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal.LiteralExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement.StatementExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.unary.expression.UnaryExpressionReviser

import scala.reflect.ClassTag


trait ExpressionReviser extends ExpressionVisitor[Expression]
  with BinaryExpressionReviser
  with BlockExpressionReviser
  with CallChainExpressionReviser
  with LiteralExpressionReviser
  with StatementExpressionReviser
  with UnaryExpressionReviser
  with VarExpressionReviser {
  override def defaultVisit(expression: Expression, visitor: ExpressionVisitor[Expression]): Expression = expression
}

object ExpressionReviser{
  def revise[T <: Expression, RT: ClassTag](origin: Option[T], visitor: ExpressionVisitor[Expression]): Option[Option[RT]] = {
    val revise = origin.map( visitor.visit )
    val success = origin match {
      case Some( expr ) ⇒ revise match {
        case Some( reviseExpr ) ⇒ expr != reviseExpr
        case None ⇒ false
      }
      case None ⇒ revise match {
        case None ⇒ false
        case Some( _ ) ⇒ true
      }
    }
    if (success) {
      Some(revise.map(_.asInstanceOf[RT]))
    } else {
      None
    }
  }

  def revise[T <: Expression, RT: ClassTag](origin: Array[T], visitor: ExpressionVisitor[Expression]): Option[Array[RT]] = {
    val revise = origin.map( visitor.visit )
    if (revise.length != origin.length) {
      None
    } else {
      val success = revise.zip( origin ).exists {
        case (r, o) ⇒ r != o
      }
      if (success) {
        Some( revise.map( _.asInstanceOf[RT] ) )
      } else {
          None
      }
    }
  }

  def revise[T <: Expression, K <: Expression, RT: ClassTag, RK: ClassTag](origin: Array[(T, K)],
                                                                           visitorT: ExpressionVisitor[Expression],
                                                                           visitorK: ExpressionVisitor[Expression]): Option[Array[(RT, RK)]] = {
    val revise = origin.map {
      case (t, k) ⇒ (visitorT.visit( t ), visitorK.visit( k ))
    }
    if (revise.length != origin.length) {
      None
    } else {
      val success = revise.zip( origin ).exists {
        case (rs, os) ⇒
            rs._1 != os._1 || rs._2 !=os._2
      }
      if(success){
        Some(revise.map(kv⇒(kv._1.asInstanceOf[RT],kv._2.asInstanceOf[RK])))
      }else{
        None
      }
    }
  }
}