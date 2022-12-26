package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.UnapplyExpressionContext
import com.dongjiaqiang.jvm.dsl.core.`type`.{DslType, UnResolvedType}
import com.dongjiaqiang.jvm.dsl.core.scope.ProgramScope

import java.util
import scala.collection.convert.ImplicitConversionsToScala._

class MatchCaseParser(val programScope: ProgramScope) {
  private val caseQueue: util.Queue[Array[(String, DslType)]] = new util.LinkedList[Array[(String, DslType)]]( )

  private def resolveUnapplyExpression(uec: UnapplyExpressionContext): Array[String] = {
    if (uec.matchVariable( ) != null) {
      Array( uec.matchVariable( ).IDENTIFIER( ).getText )
    } else if (uec.unapplyListExpression( ) != null) {
      uec.unapplyListExpression( ).unapplyExpression( ).
        flatMap( resolveUnapplyExpression ).toArray
    } else if (uec.unapplyTupleExpression( ) != null) {
      uec.unapplyTupleExpression( ).unapplyExpression( ).
        flatMap( resolveUnapplyExpression ).toArray
    } else if (uec.unapplyClazzExpression( ) != null) {
      uec.unapplyClazzExpression( ).unapplyExpression( )
        .flatMap( resolveUnapplyExpression ).toArray
    } else if (uec.unapplyHeadExpression( ) != null) {
      uec.unapplyHeadExpression( ).matchVariable( ).map( _.IDENTIFIER( ).getText ).toArray
    } else {
      Array( )
    }
  }

  def parseMatchCase(ctx: JvmDslParserParser.MatchCaseExpressionContext): Unit = {
    ctx.caseExpression( ).foreach {
      caseExpr ⇒
        if (caseExpr.typeMatchExpression( ) != null) {
          val fieldName = caseExpr.typeMatchExpression( ).matchVariable( ).IDENTIFIER( ).getText
          val dslType = DslType.unapply( caseExpr.typeMatchExpression( ).`type`( ) )
          caseQueue.add( Array( (fieldName, dslType) ) )
        } else {
          caseQueue.add( resolveUnapplyExpression( caseExpr.unapplyExpression( ) ).map( v ⇒ (v, UnResolvedType) ) )
        }
    }
  }

  def poll(): Array[(String, DslType)] = Option.apply( caseQueue.poll( ) ).getOrElse( Array( ) )
}
