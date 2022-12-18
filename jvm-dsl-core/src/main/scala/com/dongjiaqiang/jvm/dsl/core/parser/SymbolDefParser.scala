package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.ForStatementContext
import com.dongjiaqiang.jvm.dsl.core.`type`._
import com.dongjiaqiang.jvm.dsl.core.scope._
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser}

import java.util.{LinkedList ⇒ Stack}
import scala.collection.convert.ImplicitConversionsToScala._
import scala.collection.mutable.{ListMap ⇒ MutableMap}


class BlockStack{

  private val currentBlockScopes: Stack[BlockScope] = new Stack[BlockScope]()

  def peek():BlockScope={
    val parent = currentBlockScopes.peekFirst()
    parent
  }

  def poll():Unit= {
    val current = currentBlockScopes.pollFirst()
    if(nonEmpty()){
      val parent = currentBlockScopes.peekFirst()
      parent.addScope(current)
    }
  }

  def empty():Boolean={
      currentBlockScopes.isEmpty
  }

  def nonEmpty():Boolean={
      !empty()
  }

  def push(blockScope: BlockScope):Unit= {
    currentBlockScopes.addFirst(blockScope)
  }

  def incStatement():Unit={
    currentBlockScopes.peekFirst().incStatement()
  }

  def incStatement(i:Int): Unit = {
    currentBlockScopes.peekFirst( ).incStatement(i)
  }

}

class SymbolDefParser(var programScope:ProgramScope = new ProgramScope()) extends JvmDslParserBaseListener {

  var currentMethodScope: MethodScope = _
  var stack: BlockStack = new BlockStack()
  var currentClazzScope: ClazzScope = _



  override def enterProgram(ctx: JvmDslParserParser.ProgramContext): Unit = {
    programScope = new ProgramScope( )
  }

  override def enterFieldDef(ctx: JvmDslParserParser.FieldDefContext): Unit = {
    val symbolName = ctx.varDef( ).localVariable( ).IDENTIFIER( ).getText
    programScope.addScope( symbolName, new FieldScope( programScope.statements, symbolName,
      DslType.unapply( ctx.varDef( ).`type`( ) ),programScope,programScope, Option( ctx.VOLATILE( ) ).isDefined ) )
    programScope.incStatement()
  }

  override def enterClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
    require( currentClazzScope == null )

    val symbolName = ctx.IDENTIFIER( ).getText
    programScope.duplicateSymbol( symbolName )

    currentClazzScope = new ClazzScope( programScope.statements,symbolName )

    currentClazzScope.addScope(ctx.parameters().parameter(),currentClazzScope,programScope)

    programScope.addScope( symbolName, currentClazzScope )

    programScope.incStatement()

  }

  override def exitClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
    currentClazzScope = null
  }

  override def enterFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
    val currentScope = if (currentClazzScope != null) {
      currentClazzScope
    } else {
      programScope
    }

    val symbolName = ctx.funcName( ).getText
    currentScope.duplicateSymbol( symbolName )
    val methodScope = new MethodScope( symbolName,currentScope.statements,currentScope,DslType.unapply(ctx.`type`()) )
    methodScope.addScope(new BlockScope( 0,methodScope,currentScope ))

    methodScope.addScope(ctx.parameters().parameter(),methodScope,programScope)

    currentScope.addScope( symbolName, methodScope )
    currentMethodScope = methodScope

    currentScope.incStatement()
  }

  override def exitFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
    currentMethodScope = null
  }

  override def enterBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    if (stack.empty()) {
      stack.push(currentMethodScope.blockScope )
    } else {
      val parent = stack.peek()
      if (!parent.isInstanceOf[ForStatementBlockScope]) {
        val topScope = Option.apply(currentClazzScope).getOrElse(programScope)
        stack.push(new BlockScope( parent.statements,parent ,topScope))
        parent.incStatement()
      }
    }
  }

  override def exitBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    stack.poll()
  }

  override def enterVarDef(ctx: JvmDslParserParser.VarDefContext): Unit = {
    if (stack.nonEmpty()) {
      val currentScope = stack.peek()
      val dslType = DslType.unapply( ctx.`type`( ) )
      val symbolName = ctx.localVariable( ).IDENTIFIER( ).getText

      if(!ctx.getParent.isInstanceOf[ForStatementContext]) {
        currentScope.addScope( symbolName, new FieldScope( currentScope.statements,symbolName, dslType, currentScope,programScope,false ) )
        currentScope.incStatement()
      }
    }

  }

  private def enterForStatement( ctxList:List[JvmDslParserParser.VarDefContext]): Unit = {
    val parent = stack.peek( )

    val topScope = Option.apply(currentClazzScope).getOrElse(programScope)
    val blockScope = new ForStatementBlockScope( parent.statements, MutableMap( ),
      parent,topScope)

    ctxList.foreach( ctx ⇒ {
      val symbolName = ctx.localVariable( ).IDENTIFIER( ).getText
      val dslType = DslType.unapply( ctx.`type`( ) )
      blockScope.addInitScope( symbolName, new FieldScope( 0, symbolName, dslType, blockScope,programScope,false ) )
      blockScope.incStatement( )
    } )
    parent.incStatement( )
    stack.push( blockScope )

  }


  override def enterForStatementOne(ctx: JvmDslParserParser.ForStatementOneContext): Unit = {
      enterForStatement(List(ctx.varDef()))
      stack.incStatement()
  }

  override def enterForStatementTwo(ctx: JvmDslParserParser.ForStatementTwoContext): Unit = {
    enterForStatement(List(ctx.varDef()))
    stack.incStatement()
  }

  override def enterForStatementThree(ctx: JvmDslParserParser.ForStatementThreeContext): Unit = {
    enterForStatement(ctx.varDef().toList)
    stack.incStatement()
  }

  override def exitDoWhileStatement(ctx: JvmDslParserParser.DoWhileStatementContext): Unit = {
    stack.incStatement()
  }

  override def enterWhileStatement(ctx: JvmDslParserParser.WhileStatementContext): Unit = {
    stack.incStatement()
  }


  override def enterAssignment(ctx: JvmDslParserParser.AssignmentContext): Unit = {
      stack.incStatement()
  }


  override def enterSynchronizedStatement(ctx: JvmDslParserParser.SynchronizedStatementContext): Unit = {
    stack.incStatement()
  }


  override def enterThrowReturnOrSideEffectStatement(ctx: JvmDslParserParser.ThrowReturnOrSideEffectStatementContext): Unit = {
    stack.incStatement()
  }

  override def enterAssertStatement(ctx: JvmDslParserParser.AssertStatementContext): Unit = {
    stack.incStatement()
  }

  override def enterIfStatement(ctx: JvmDslParserParser.IfStatementContext): Unit = {
    stack.incStatement(ctx.block().size()-1)
  }
}
