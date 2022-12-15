package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser}
import com.dongjiaqiang.jvm.dsl.core.scope.{BlockScope, ClazzScope, MethodScope, ProgramScope}

import scala.collection.convert.ImplicitConversionsToScala._
class StatementParser(val programScope:ProgramScope) extends JvmDslParserBaseListener {

  var currentClazzScope: ClazzScope = _
  var currentMethodScope: MethodScope = _
  var blockScope: BlockScope = _
  var currentBlockIndex:Int = _
  var currentStatementIndex: Int = _

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
    blockScope = currentMethodScope.blockScope
  }

  override def enterBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
      blockScope = blockScope.childrenScopes(currentBlockIndex)
      currentBlockIndex = currentBlockIndex + 1
  }

  override def enterAssignment(ctx: JvmDslParserParser.AssignmentContext): Unit = {
      val variable = ctx.variable().IDENTIFIER().map(_.getText).toList
      currentStatementIndex = currentStatementIndex+1
  }

}
