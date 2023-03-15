package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, UnResolvedType}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.scope.toDslType

import java.util
import scala.collection.convert.ImplicitConversionsToScala._

class MatchCaseParser(val programScope: ProgramScope) {
  private val caseQueue: util.Queue[Array[(String, DslType)]] = new util.LinkedList[Array[(String, DslType)]]( )

  private def resolveUnapplyExpression(uec: UnapplyExpressionContext): Array[String] = {
    uec match {
      case c: UnapplyVarExprContext ⇒ Array( c.localVariable( ).IDENTIFIER( ).getText )
      case c: UnapplyListExprContext ⇒ c.unapplyExpression( ).
        flatMap( resolveUnapplyExpression ).toArray
      case c: UnapplyTupleExprContext ⇒ c.unapplyExpression( ).
        flatMap( resolveUnapplyExpression ).toArray
      case c: UnapplyClazzExprContext ⇒ c.unapplyExpression( )
        .flatMap( resolveUnapplyExpression ).toArray
      case c: UnapplyHeadExprContext ⇒ c.unapplyExpression( )
        .flatMap( resolveUnapplyExpression ).toArray
      case _ ⇒ Array( )
    }
  }

  def parseMatchCase(ctx: JvmDslParserParser.MatchCaseExpressionContext): Unit = {
    ctx.caseExpression( ).foreach {
      caseExpr ⇒
        if (caseExpr.typeMatchExpression( ) != null) {
          val fieldName = caseExpr.typeMatchExpression( ).localVariable( ).IDENTIFIER( ).getText
          val dslType = toDslType( caseExpr.typeMatchExpression( ).`type`( ),programScope )
          caseQueue.add( Array( (fieldName, dslType) ) )
        } else {
          caseQueue.add( resolveUnapplyExpression( caseExpr.unapplyExpression( ) ).map( v ⇒ (v, UnResolvedType) ) )
        }
    }
  }

  def poll(): Array[(String, DslType)] = Option.apply( caseQueue.poll( ) ).getOrElse( Array( ) )
}
