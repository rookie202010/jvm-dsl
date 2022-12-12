package com.dongjiaqiang.jvm.dsl.core.symbol

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.{ForStatementContext, ForStatementOneContext}
import com.dongjiaqiang.jvm.dsl.core.scope.{BlockScope, ClazzScope, FieldScope, ForStatementBlockScope, MethodScope, ProgramScope, SymbolType}
import com.dongjiaqiang.jvm.dsl.core.`type`._
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser}

import java.util.{LinkedList ⇒ Stack}
import scala.collection.mutable.{ListMap ⇒ MutableMap}
import scala.collection.convert.ImplicitConversionsToScala._

class SymbolDefParser(var programScope:ProgramScope = new ProgramScope()) extends JvmDslParserBaseListener {

  var currentMethodScope: MethodScope = _
  var currentBlockScopes: Stack[BlockScope] = new Stack[BlockScope]( )
  var currentClazzScope: ClazzScope = _

  override def enterProgram(ctx: JvmDslParserParser.ProgramContext): Unit = {
    programScope = new ProgramScope( )
  }

  override def enterFieldDef(ctx: JvmDslParserParser.FieldDefContext): Unit = {
    val symbolName = ctx.varDef( ).localVariable( ).IDENTIFIER( ).getText
    programScope.addScope( symbolName, new FieldScope( programScope.size, symbolName,
      DslType.unapply( ctx.varDef( ).`type`( ) ), Option( ctx.VOLATILE( ) ).isDefined ) )
  }

  override def enterClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
    require( currentClazzScope == null )

    val symbolName = ctx.IDENTIFIER( ).getText
    programScope.duplicateSymbol( symbolName )

    currentClazzScope = new ClazzScope( programScope.size,symbolName )

    ctx.parameters( ).parameter( ).foreach( currentClazzScope.addScope )

    programScope.addScope( symbolName, currentClazzScope )

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
    val methodScope = new MethodScope( symbolName,currentScope.size,currentScope,DslType.unapply(ctx.`type`()) )
    methodScope.addScope(new BlockScope( 0,methodScope ))
    ctx.parameters( ).parameter( ).foreach( methodScope.addScope )

    currentScope.addScope( symbolName, methodScope )
    currentMethodScope = methodScope
  }

  override def exitFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
    currentMethodScope = null
  }

  override def enterBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    if (currentBlockScopes.isEmpty) {
      currentBlockScopes.addFirst( currentMethodScope.blockScope )
    } else {
      if (!currentBlockScopes.peekFirst( ).isInstanceOf[ForStatementBlockScope]) {
        currentBlockScopes.addFirst( new BlockScope( currentBlockScopes.peekFirst().size,currentBlockScopes.peekFirst( ) ) )
      }
    }
  }

  override def exitBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    val currentScope = currentBlockScopes.pollFirst( )
    if(currentBlockScopes.isEmpty){
    //    currentMethodScope.addScope(currentScope)
    }else{
        currentBlockScopes.peekFirst().addScope(currentScope)
    }
  }

  override def enterVarDef(ctx: JvmDslParserParser.VarDefContext): Unit = {
    if (currentBlockScopes.nonEmpty) {
      val currentScope = currentBlockScopes.peekFirst( )
      val dslType = DslType.unapply( ctx.`type`( ) )
      val symbolName = ctx.localVariable( ).IDENTIFIER( ).getText

      if(!ctx.getParent.isInstanceOf[ForStatementContext]) {
        currentScope.addScope( symbolName, new FieldScope( symbolName, dslType, false ) )
      }
    }

  }

  private def enterForStatement( ctxList:List[JvmDslParserParser.VarDefContext]): Unit = {
    val blockScope = new ForStatementBlockScope(currentBlockScopes.size(), MutableMap( ),
      currentBlockScopes.peekFirst( ) )

    ctxList.foreach(ctx⇒ {
      val symbolName = ctx.localVariable().IDENTIFIER().getText
      val dslType = DslType.unapply( ctx.`type`( ) )
      blockScope.addInitScope(symbolName,new FieldScope(0,symbolName,dslType,false))
    })

    currentBlockScopes.addFirst( blockScope )
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

}
