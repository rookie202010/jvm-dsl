package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.{FutureType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.block._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.block.BlockExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api
import com.dongjiaqiang.jvm.dsl.java.api.exception.JavaTranslatorException
import com.dongjiaqiang.jvm.dsl.java.api.expression.{JavaAsync, JavaCustomBlockExpression, JavaTranslatorContext, JavaVarCall}
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._Runnable

trait BlockExpressionJavaTranslator extends BlockExpressionVisitor[String] {


  val javaTranslatorContext: JavaTranslatorContext

  //block compute expression

  override def visit(forExpr: For, visitor: ExpressionVisitor[String]): String = {
    s"""
       |for(${visitor.visit( forExpr.loopVarDef )};${visitor.visit( forExpr.loopVarCondition )};${visitor.visit( forExpr.loopVarUpdate )})
       |   ${visitor.visit( forExpr.body )}
       |""".stripMargin
  }

  override def visit(forCollection: ForCollection, visitor: ExpressionVisitor[String]): String = {
    s"""
       |for(${visitor.visit( forCollection.localVarDef )}:${visitor.visit( forCollection.looped )})
       |  ${visitor.visit( forCollection.body )}
       |""".stripMargin
  }

  override def visit(forMap: ForMap, visitor: ExpressionVisitor[String]): String = {
    val sysEntryVar = s"_sys_entry_${javaTranslatorContext.systemVarIndex.incrementAndGet( )}"
    val keyDef = forMap.loopKeyDef.copy( assigned = Some( JavaVarCall( List( sysEntryVar ), "getKey", Array( ),
      forMap.loopKeyDef.dslType ) ) )
    val valueDef = forMap.loopValueDef.copy( assigned = Some( JavaVarCall( List( sysEntryVar ), "getValue", Array( )
    ,forMap.loopValueDef.dslType) ) )
    forMap.body.expressions.insert( 0, keyDef, valueDef )

    s"""
       |for(java.util.Map.Entry<${api.toJavaType( forMap.loopKeyDef.dslType,javaTranslatorContext )},
       |   ${api.toJavaType( forMap.loopValueDef.dslType,javaTranslatorContext )}> $sysEntryVar:${visitor.visit( forMap.looped )})
       | ${visitor.visit( forMap.body )}
       |""".stripMargin
  }

  override def visit(whileExpr: While, visitor: ExpressionVisitor[String]): String = {
    s"""
       |
       |while(${visitor.visit( whileExpr.condition )})
       |   ${visitor.visit( whileExpr.body )}
       |""".stripMargin
  }

  override def visit(doWhile: DoWhile, visitor: ExpressionVisitor[String]): String = {
    s"""
       |do
       |  ${visitor.visit( doWhile.body )}
       |while (${visitor.visit( doWhile.condition )})
       |""".stripMargin
  }

  override def visit(ifExpr: If, visitor: ExpressionVisitor[String]): String = {
    val head = ifExpr.cases.head
    val tail = ifExpr.cases.tail
    s"""
       |if(${visitor.visit( head._1 )})
       |   ${visitor.visit( head._2 )}
       |""".stripMargin + tail.map( `case` ⇒ {
      s"""
         |else if(${visitor.visit( `case`._1 )})
         |   ${visitor.visit( `case`._2 )}
         |""".stripMargin
    } ).mkString( "\n" ) + ifExpr.default.map( block ⇒
      s"""
         |else
         |   ${visitor.visit( block )}
         |""".stripMargin ).getOrElse( "" )
  }


  override def visit(sync: Sync, visitor: ExpressionVisitor[String]): String = {
    s"""
       |synchronized(${visitor.visit( sync.condition )}
       |   ${visitor.visit( sync.body )}
       |""".stripMargin
  }

  override def visit(tryCatch: TryCatch, visitor: ExpressionVisitor[String]): String = {
    s"""
       |try
       |   ${visitor.visit( tryCatch.tryBlock )}
       |""".stripMargin + tryCatch.catches
      .map {
        case ((name, dslType), body) ⇒
          s"""
             |catch(${api.toJavaType( dslType ,javaTranslatorContext)} $name)
             | ${visitor.visit( body )}

             |""".stripMargin
      }.mkString( "\n" ) + tryCatch.finallyBlock.map( block ⇒ {
      s"""
         |finally
         | ${visitor.visit( block )}
         |""".stripMargin
    } ).getOrElse( "" )
  }


  override def visit(block: Block, visitor: ExpressionVisitor[String]): String = {

    def help(expression: Expression): String = {
      expression match {
        case matchCase: MatchCase ⇒
          val code = visitor.visit( matchCase )
          s"""
             |new ${classOf[_Runnable].getCanonicalName}(){
             |     @Override
             |     public void run() throws Exception {
             |           $code
             |     }
             |}.run()
             |
             |""".stripMargin
        case async: Async⇒
          visitor.visit(JavaAsync(async.body,async.executor,new FutureType(UnitType)))
        case _: Try⇒
          throw JavaTranslatorException("illegal definition of a try block alone in java")
        case _: Lambda ⇒
          throw JavaTranslatorException( "illegal definition of a lambda expression alone in java" )
        case customBlockExpression: CustomBlockExpression⇒
          visitor.visit(JavaCustomBlockExpression(customBlockExpression,UnitType))
        case _ ⇒ visitor.visit( expression )
      }
    }
    s"""
       |{
       |   ${block.expressions.map( help ).mkString("", ";\n" ,";")}
       |}
       |""".stripMargin
  }
}
