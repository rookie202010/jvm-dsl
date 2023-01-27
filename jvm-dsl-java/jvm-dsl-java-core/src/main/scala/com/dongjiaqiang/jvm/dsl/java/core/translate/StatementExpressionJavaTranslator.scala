package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, FutureType, LambdaType, TryType}
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.statement.StatementExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.java.api.expression.{JavaAsync, JavaCustomBlockExpression, JavaLambda, JavaMatchCase, JavaTranslatorContext, JavaTry}

trait StatementExpressionJavaTranslator extends StatementExpressionVisitor[String] {

  val javaTranslatorContext: JavaTranslatorContext


  override def visit(assign: Assign, visitor: ExpressionVisitor[String]): String = {
    assign.assigned match {
      case matchCase: MatchCase ⇒
        val javaMatchCase = JavaMatchCase( assign.varRef.getDslType, visitor.visit( matchCase ) )
        s"${assign.varRef.name.mkString( "." )} = ${visitor.visit( javaMatchCase )}"
      case lambda: Lambda ⇒
        s"${assign.varRef.name.mkString( "." )} = ${
          visitor.visit( JavaLambda( assign.varRef.getDslType.asInstanceOf[LambdaType], lambda ) )
        }"
      case async: Async⇒
        s"${assign.varRef.name.mkString( "." )} = ${
          visitor.visit( JavaAsync( async.body,async.executor,assign.varRef.getDslType.asInstanceOf[FutureType] ) )
        }"
      case `try`:Try⇒
        s"${assign.varRef.name.mkString( "." )} = ${
          visitor.visit( JavaTry( `try`.body,assign.varRef.getDslType.asInstanceOf[TryType] ) )
        }"
      case customBlockExpression: CustomBlockExpression⇒
        s"${assign.varRef.name.mkString( "." )} = ${
          visitor.visit( JavaCustomBlockExpression( customBlockExpression,assign.varRef.getDslType ) )
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
      case customBlockExpression: CustomBlockExpression⇒
        s"assert ${visitor.visit(JavaCustomBlockExpression(customBlockExpression,BoolType))}"
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
          case async: Async⇒
             if(visitor.currentLambdaScope!=null){
               s"return ${
                 visitor.visit( JavaAsync( async.body,async.executor,visitor.currentLambdaScope.outputType.asInstanceOf[FutureType] ) )
               }"
             } else{
               s"return ${
                 visitor.visit( JavaAsync( async.body,async.executor,visitor.currentMethodScope.returnType.asInstanceOf[FutureType] ) )
               }"
             }
          case `try`:Try⇒
              if(visitor.currentLambdaScope!=null){
                s"return ${
                  visitor.visit( JavaTry( `try`.body,visitor.currentLambdaScope.outputType.asInstanceOf[TryType] ) )
                }"
              }else{
                s"return ${
                  visitor.visit( JavaTry( `try`.body,visitor.currentMethodScope.returnType.asInstanceOf[TryType] ) )
                }"
              }
          case customBlockExpression: CustomBlockExpression⇒
              if(visitor.currentLambdaScope!=null){
                s"return ${
                  visitor.visit( JavaCustomBlockExpression( customBlockExpression,visitor.currentLambdaScope.outputType) )
                }"
              }else{
                s"return ${
                  visitor.visit( JavaCustomBlockExpression( customBlockExpression,visitor.currentMethodScope.returnType) )
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
