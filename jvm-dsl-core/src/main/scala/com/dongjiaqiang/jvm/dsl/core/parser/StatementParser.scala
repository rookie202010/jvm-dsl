package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.core.exception.SymbolParseException
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser, scope}
import com.dongjiaqiang.jvm.dsl.core.scope.{BlockScope, ClazzScope, Failure, MethodScope, ProgramScope, Success}

import scala.collection.convert.ImplicitConversionsToScala._
import java.util.{LinkedList ⇒ Stack}

class StatementParser(val programScope:ProgramScope) extends JvmDslParserBaseListener {

  var currentClazzScope: ClazzScope = _
  var currentMethodScope: MethodScope = _
  var currentBlockScope: BlockScope = _
  var currentBlockIndex: Int = _
  var indexStack: Stack[Int] = new Stack[Int]()

  override def enterClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
      val symbolName = ctx.IDENTIFIER( ).getText
      currentClazzScope = programScope.classes(symbolName)
  }

  override def exitClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
     currentClazzScope = null
  }

  override def enterFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
    val symbolName = ctx.funcName( ).getText
    if(currentClazzScope!=null){
        currentMethodScope = currentClazzScope.methods(symbolName)
    }else{
        currentMethodScope = programScope.methods(symbolName)
    }
  }

  override def exitFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
      currentMethodScope = null
  }

  override def enterBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
      if(currentBlockScope==null){
          currentBlockScope = currentMethodScope.blockScope
          currentBlockIndex = 0
          indexStack.push(currentBlockIndex)
      } else{
          currentBlockScope = currentBlockScope.childrenScopes(currentBlockIndex)
          currentBlockIndex = currentBlockIndex+1
          indexStack.push(currentBlockIndex)
      }
  }

  override def exitBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
      currentBlockScope.parentScope match {
        case blockScope: BlockScope⇒
            currentBlockScope = blockScope
            currentBlockIndex = indexStack.poll()
        case _: MethodScope⇒
            indexStack.poll()
      }
  }

//  override def enterAssignment(ctx: JvmDslParserParser.AssignmentContext): Unit = {
//      val variable = ctx.variable().IDENTIFIER().map(_.getText).toList
//      val resolved = currentBlockScope.resolveVarRefs(currentStatementIndex,variable)
//      if(resolved==Failure){
//        throw SymbolParseException( f"refs resolved fail ${variable.mkString(".")} in scope ${currentBlockScope.toString}" )
//      }
//      val array = ctx.LBRACK()!=null && ctx.RBRACK()!=null
//      val operator = ctx.assignOperator()
//
//      if(array){
//          val subscriptExpression = ctx.expression(0)
//          val assignExpression = ctx.expression(1)
//      }else{
//          val assignExpression = ctx.expression(0)
//         // assignExpression.conditionalOrExpression().
//      }
//      currentStatementIndex = currentStatementIndex+1
//  }



}
