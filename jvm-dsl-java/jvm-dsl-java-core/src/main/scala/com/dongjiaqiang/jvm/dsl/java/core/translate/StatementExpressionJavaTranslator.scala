package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, LambdaType}
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.statement.StatementExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.java.api.expression.{JavaLambda, JavaMatchCase, JavaTranslatorContext}

trait StatementExpressionJavaTranslator extends StatementExpressionVisitor[String] {

  val javaTranslatorContext: JavaTranslatorContext


  override def visit(assign: Assign, visitor: ExpressionVisitor[String]): String = {
    assign.assigned match {
      case matchCase: MatchCase ⇒
        val javaMatchCase = JavaMatchCase( assign.varRef.dslType, visitor.visit( matchCase ) )
        s"${assign.varRef.name.mkString( "." )} = ${visitor.visit( javaMatchCase )}"
      case lambda: Lambda ⇒
        s"${assign.varRef.name.mkString( "." )} = ${
          visitor.visit( JavaLambda( assign.varRef.dslType.asInstanceOf[LambdaType], lambda ) )
        }"
      case _ ⇒
        s"${assign.varRef.name.mkString( "." )} = ${visitor.visit( assign.assigned )}"
    }
  }


  override def visit(assert: Assert, visitor: ExpressionVisitor[String]): String = {
    assert.expression match {
      case matchCase:MatchCase⇒
        val javaMatchCase = JavaMatchCase( BoolType, visitor.visit( matchCase ) )
        s"assert ${
          visitor.visit( javaMatchCase )
        }"
      case _⇒
        s"assert ${visitor.visit(assert.expression)}"
    }
  }

  override def visit(returnExpr: Return, visitor: ExpressionVisitor[String]): String = {
    returnExpr.expression match {
      case UnitLiteral ⇒ "return"
      case _ ⇒
        returnExpr.expression match {
          case matchCase: MatchCase ⇒
            if (visitor.currentLambdaScope != null) {
              val javaMatchCase = JavaMatchCase( visitor.currentLambdaScope.outputType, visitor.visit( matchCase ) )
              s"return ${
                visitor.visit( javaMatchCase )
              }"
            } else {
              s"return ${
                visitor.visit( JavaMatchCase( visitor.currentMethodScope.returnType, visitor.visit( matchCase ) ) )
              }"
            }
          case lambda: Lambda ⇒
            if (visitor.currentLambdaScope != null) {
              s"return ${
                visitor.visit( JavaLambda( visitor.currentLambdaScope.outputType.asInstanceOf[LambdaType], lambda ) )
              }"
            } else {
              s"return ${
                visitor.visit( JavaLambda( visitor.currentMethodScope.returnType.asInstanceOf[LambdaType], lambda ) )
              }"
            }
          case _ ⇒
            s"return ${visitor.visit( returnExpr.expression )}"
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
