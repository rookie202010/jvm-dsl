package com.dongjiaqiang.jvm.dsl.core.symbol

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.{ForStatementContext, ForStatementOneContext}
import com.dongjiaqiang.jvm.dsl.core.scope.{BlockScope, ClazzScope, FieldScope, ForStatementBlockScope, MethodScope, ProgramScope, SymbolType}
import com.dongjiaqiang.jvm.dsl.core.`type`._
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser}

import java.util.{LinkedList ⇒ Stack}
import scala.collection.mutable.{ListMap ⇒ MutableMap}
import scala.collection.convert.ImplicitConversionsToScala._


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

  def push():Unit={
    currentBlockScopes.peekFirst().incSymbols()
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
    programScope.addScope( symbolName, new FieldScope( programScope.symbols, symbolName,
      DslType.unapply( ctx.varDef( ).`type`( ) ), Option( ctx.VOLATILE( ) ).isDefined ) )
    programScope.incSymbols()
  }

  override def enterClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
    require( currentClazzScope == null )

    val symbolName = ctx.IDENTIFIER( ).getText
    programScope.duplicateSymbol( symbolName )

    currentClazzScope = new ClazzScope( programScope.symbols,symbolName )

    ctx.parameters( ).parameter( ).foreach( p⇒{
      currentClazzScope.addScope(p)
      currentClazzScope.incSymbols()
    } )

    programScope.addScope( symbolName, currentClazzScope )

    programScope.incSymbols()

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
    val methodScope = new MethodScope( symbolName,currentScope.symbols,currentScope,DslType.unapply(ctx.`type`()) )
    methodScope.addScope(new BlockScope( 0,methodScope ))

    ctx.parameters( ).parameter( ).foreach( p⇒{
      methodScope.addScope(p)
      methodScope.incSymbols()
    } )


    currentScope.addScope( symbolName, methodScope )
    currentMethodScope = methodScope

    currentScope.incSymbols()
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
        stack.push(new BlockScope( parent.symbols,parent ))
        parent.incSymbols()
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
        currentScope.addScope( symbolName, new FieldScope( currentScope.symbols,symbolName, dslType, false ) )
        currentScope.incSymbols()
      }
    }

  }

  private def enterForStatement( ctxList:List[JvmDslParserParser.VarDefContext]): Unit = {
    val parent = stack.peek()

      val blockScope = new ForStatementBlockScope(parent.symbols, MutableMap(),
        parent)

      ctxList.foreach(ctx ⇒ {
        val symbolName = ctx.localVariable().IDENTIFIER().getText
        val dslType = DslType.unapply(ctx.`type`())
        blockScope.addInitScope(symbolName, new FieldScope(0, symbolName, dslType, false))
        blockScope.incSymbols()
      })
    parent.incSymbols()
      stack.push(blockScope)

  }


  override def enterForStatementOne(ctx: JvmDslParserParser.ForStatementOneContext): Unit = {
      enterForStatement(List(ctx.varDef()))
  }

  override def enterForStatementTwo(ctx: JvmDslParserParser.ForStatementTwoContext): Unit = {
    enterForStatement(List(ctx.varDef()))
  }

  override def enterForStatementThree(ctx: JvmDslParserParser.ForStatementThreeContext): Unit = {
    enterForStatement(ctx.varDef().toList)
  }

  override def exitDoWhileStatement(ctx: JvmDslParserParser.DoWhileStatementContext): Unit = {
    stack.push()
  }

  override def exitWhileStatement(ctx: JvmDslParserParser.WhileStatementContext): Unit = {
    stack.push()
  }


}
