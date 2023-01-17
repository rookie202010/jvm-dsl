package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement.StatementExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression._

trait StatementExpressionJavaTranslator extends StatementExpressionVisitor[String] {

  val javaTranslatorContext: JavaTranslatorContext


  override def visit(assign: Assign, visitor: ExpressionVisitor[String]): String = {
    assign.assigned match {
      case matchCase: MatchCase ⇒
        s"${assign.varRef.name.mkString( "." )} = ${
          visitor.visit( JavaMatchCase( assign.varRef.dslType, ${
            visitor.visit( matchCase )
          } ) )
        }"
      case _ ⇒
        s"${assign.varRef.name.mkString( "." )} = ${visitor.visit( assign.assigned )}"
    }
  }


  override def visit(assert: Assert, visitor: ExpressionVisitor[String]): String = {
    s"assert ${visitor.visit( assert.expression )}"
  }

  override def visit(returnExpr: Return, visitor: ExpressionVisitor[String]): String = {
    returnExpr.expression match {
      case UnitLiteral ⇒ "return"
      case _ ⇒ {
        returnExpr.expression match {
          case matchCase: MatchCase ⇒
            s"return ${
              visitor.visit( JavaMatchCase( visitor.currentMethodScope.returnType, ${
                visitor.visit( matchCase )
              } ) )
            }"
          case _ ⇒
            s"return ${visitor.visit( returnExpr.expression )}"
        }
      }
    }
  }

  override def visit(break: Break.type, visitor: ExpressionVisitor[String]): String = {
    "break"
  }

  override def visit(continue: Continue.type, visitor: ExpressionVisitor[String]): String = {
    "continue"
  }

  override def visit(throwExpr: Throw, visitor: ExpressionVisitor[String]): String = {
    s"throw ${visitor.visit( throwExpr.expression )}"
  }
}
