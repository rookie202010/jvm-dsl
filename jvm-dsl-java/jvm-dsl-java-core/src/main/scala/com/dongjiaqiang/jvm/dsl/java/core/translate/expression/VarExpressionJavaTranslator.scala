package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.{FutureType, LambdaType, NumberDslType, TryType}
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.{LocalVarDef, VarRef}
import com.dongjiaqiang.jvm.dsl.api.expression.block._
import com.dongjiaqiang.jvm.dsl.api.expression.unary.Cast
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.`var`.VarExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api
import com.dongjiaqiang.jvm.dsl.java.api.expression._

import scala.annotation.tailrec

trait VarExpressionJavaTranslator extends VarExpressionVisitor[String] {

  val javaTranslatorContext: JavaTranslatorContext

  override def visit(localVarDef: LocalVarDef, visitor: ExpressionVisitor[String]): String = {
    val javaType = localVarDef.dslType match {
      case numberType: NumberDslType ⇒
        api.toBasicType( numberType,javaTranslatorContext )
      case _ ⇒ api.toJavaType(localVarDef.dslType,javaTranslatorContext)
    }
    localVarDef.assigned match {
      case None ⇒ s"$javaType ${localVarDef.fieldScope.symbolName}"
      case Some(expression) ⇒
        expression match {
          case matchCase: MatchCase ⇒
            val javaMatchCase = JavaMatchCase( localVarDef.dslType, visitor.visit( matchCase ) )
            s"$javaType ${localVarDef.fieldScope.symbolName} = ${visitor.visit( javaMatchCase )}"
          case lambda: Lambda ⇒
            localVarDef.dslType match {
              case lambdaType: LambdaType ⇒
                s"$javaType ${localVarDef.fieldScope.symbolName} = ${visitor.visit( JavaLambda( lambdaType, lambda ) )}"
            }
          case async: Async⇒
            localVarDef.dslType match {
              case futureType: FutureType⇒
                s"$javaType ${localVarDef.fieldScope.symbolName} = ${visitor.visit( JavaAsync( async.body,async.executor,futureType ) )}"
            }
          case `try`:Try⇒
            localVarDef.dslType match {
              case tryType:TryType⇒
                s"$javaType ${localVarDef.fieldScope.symbolName} = ${visitor.visit( JavaTry( `try`.body,tryType) )}"
            }
          case customBlockExpression: CustomBlockExpression⇒
            s"$javaType ${localVarDef.fieldScope.symbolName} = ${visitor.visit( JavaCustomBlockExpression( customBlockExpression,localVarDef.dslType) )}"
          case _ ⇒
            s"$javaType ${localVarDef.fieldScope.symbolName} = ${visitor.visit( expression )}"
        }
    }
  }


  override def visit(varRef: VarRef, visitor: ExpressionVisitor[String]): String = {
    //todo call here just query tuple cast type
    varRef.getValueType(visitor.programScope)
    var code:String = null
    varRef.refs.zipWithIndex.foreach{
      case (ref,index)⇒
          if(varRef.castIndex.contains(index)){
            val castType = varRef.castIndex( index )
            code = visitor.visit( Cast( JavaCode( s"$code.$ref", castType ), castType, 1 ) )
          }else{
            if(code==null){
              code = ref
            }else {
              code = s"$code.$ref"
            }
          }
          if(varRef.arrayRefIndexExpressions.contains(index)){
              code = varRef.arrayRefIndexExpressions( index ).map( expr ⇒ s"[${visitor.visit( expr )}]" ).mkString( code, "", "" )
          }
    }
    code
  }

  override def visit(lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
    visitor.visit(lambda.body)
  }
}
