package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.scope._
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.ForStatementContext
import com.dongjiaqiang.jvm.dsl.core.scope._
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser}

import java.util.{LinkedList ⇒ Stack}
import scala.collection.convert.ImplicitConversionsToScala._
import scala.collection.mutable.{ListMap ⇒ MutableMap}


class BlockStack(val programScope: ProgramScope) {

  private val currentBlockScopes: Stack[BlockScope] = new Stack[BlockScope]()

  def peek():BlockScope={
    val parent = currentBlockScopes.peekFirst()
    parent
  }

  def poll(): Unit = {
    val current = currentBlockScopes.pollFirst( )
    if (nonEmpty( )) {
      val parent = currentBlockScopes.peekFirst( )
      parent.addScope( current )
    }
  }

  def pollLambda(): Unit = {
    val current = currentBlockScopes.pollFirst( )
    if (nonEmpty( )) {
      val parent = currentBlockScopes.peekFirst( )
      parent.lambdaScopes.append( current )
    }else{
      programScope.lambdaScopes.append(current)
    }
  }

  def empty(): Boolean = {
    currentBlockScopes.isEmpty
  }

  def nonEmpty(): Boolean = {
    !empty( )
  }

  def push(blockScope: BlockScope): Unit = {
    currentBlockScopes.addFirst(blockScope)
  }

  def incStatement(): Unit = {
    currentBlockScopes.peekFirst( ).incStatement( )
  }

  def incStatement(i: Int): Unit = {
    currentBlockScopes.peekFirst( ).incStatement( i )
  }

}

class SymbolDefParser(var programScope: ProgramScope = new ProgramScope( )) extends JvmDslParserBaseListener {

  var currentMethodScope: MethodScope = _
  var stack: BlockStack = _
  var currentClazzScope: ClazzScope = _


  var matchCaseParser: MatchCaseParser = _

  var catchParam:(DslType,String) = _

  override def enterProgram(ctx: JvmDslParserParser.ProgramContext): Unit = {
    programScope = new ProgramScope( )
    stack = new BlockStack( programScope )
    matchCaseParser = new MatchCaseParser( programScope )
  }


  override def enterImportClazzStatement(ctx: JvmDslParserParser.ImportClazzStatementContext): Unit = {
    if (ctx.packageName( ) != null) {
      programScope.importManager.addClass( ctx.importClazz( ).IDENTIFIER( ).map( _.getText ).mkString( "." ),
        ctx.packageName( ).IDENTIFIER( ).getText )
    } else {
      programScope.importManager.addClass( ctx.importClazz( ).IDENTIFIER( ).map( _.getText ).mkString( "." ) )
    }
  }

  override def enterUsingPackageStatement(ctx: JvmDslParserParser.UsingPackageStatementContext): Unit = {
    val packagePath = ctx.packagePath( ).STRING_LITERAL( ).getText
    val packageName = ctx.packageName( ).IDENTIFIER( ).getText
    programScope.importManager.addPackage( packageName, packagePath )
  }

  override def enterFieldDef(ctx: JvmDslParserParser.FieldDefContext): Unit = {
    val symbolName = ctx.varDef( ).parameter( ).localVariable( ).IDENTIFIER( ).getText
    programScope.addScope( symbolName, new FieldScope( programScope.statements, symbolName,
      toDslType( ctx.varDef( ).parameter( ).`type`( ) ), programScope, programScope, Option( ctx.VOLATILE( ) ).isDefined ) )
    programScope.incStatement( )
  }

  override def enterClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
    require( currentClazzScope == null )

    val symbolName = ctx.IDENTIFIER( ).getText
    programScope.duplicateSymbol( symbolName )

    currentClazzScope = new ClazzScope( programScope.statements,symbolName )

    addScope( currentClazzScope, ctx.parameters( ).parameter( ), currentClazzScope, programScope )

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
    val methodScope = new MethodScope( symbolName, currentScope.statements, currentScope, toDslType( ctx.`type`( ) ) )
    methodScope.addScope(new BlockScope( 0,methodScope,currentScope ))

    if(ctx.throwDef()!=null){
        methodScope.throws.appendAll(ctx.throwDef().clazzType().map(_.getText).map(c⇒ClazzType(c,Array())))
    }

    addScope( methodScope, ctx.parameters( ).parameter( ), methodScope, programScope )

    currentScope.addScope( symbolName, methodScope )
    currentMethodScope = methodScope

    currentScope.incStatement()
  }

  override def exitFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
    currentMethodScope = null
  }

  override def enterBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    if (stack.empty( )) {
      stack.push( currentMethodScope.blockScope )
    } else {
      val parent = stack.peek( )
      if (!parent.isInstanceOf[ForStatementBlockScope]) {
        val topScope = Option.apply( currentClazzScope ).getOrElse( programScope )

        val blockScope = new BlockScope( parent.statements, parent, topScope )

        if(catchParam!=null){
          blockScope.addScope(catchParam._2,new FieldScope(0,catchParam._2,catchParam._1,blockScope,programScope))
          catchParam=null
        }

        stack.push( blockScope )
        parent.incStatement( )
      }
    }
  }

  override def exitBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    stack.poll( )
  }


  private def enterContexts[T](contexts: List[T], producer: (T, Int, BlockScope) ⇒ FieldScope): Unit = {
    val parent = if (stack.empty( )) {
      programScope
    } else {
      stack.peek( )
    }

    val topScope = Option.apply( currentClazzScope ).getOrElse( programScope )
    val blockScope = new BlockScope( parent.statements - 1, parent, topScope )
    contexts.zipWithIndex.foreach {
      case (product, index) ⇒
        val fieldScope = producer.apply( product, index, blockScope )
        blockScope.addScope( fieldScope.symbolName,
          fieldScope )
    }
    blockScope.incStatement( contexts.size )
    stack.push( blockScope )
  }

  //start handle lambdaBlock
  private def enterLambdaExpr(variableList: List[JvmDslParserParser.LocalVariableContext]): Unit = {
    enterContexts[JvmDslParserParser.LocalVariableContext]( variableList, (variable, _, blockScope) ⇒ {
      new FieldScope( 0, variable.IDENTIFIER( ).getText, UnResolvedType, blockScope, programScope, false )
    } )
  }

  override def enterParamsLambdaExpr(ctx: JvmDslParserParser.ParamsLambdaExprContext): Unit = {
    enterLambdaExpr( ctx.localVariable( ).toList )
  }

  override def exitParamsLambdaExpr(ctx: JvmDslParserParser.ParamsLambdaExprContext): Unit = {
    stack.pollLambda( )
  }

  override def enterNoParamLambdaExpr(ctx: JvmDslParserParser.NoParamLambdaExprContext): Unit = {
    enterLambdaExpr( List( ) )
  }

  override def exitNoParamLambdaExpr(ctx: JvmDslParserParser.NoParamLambdaExprContext): Unit = {
    stack.pollLambda( )
  }

  override def enterOneParamLambdaExpr(ctx: JvmDslParserParser.OneParamLambdaExprContext): Unit = {
    enterLambdaExpr( List( ctx.localVariable( ) ) )
  }

  override def exitOneParamLambdaExpr(ctx: JvmDslParserParser.OneParamLambdaExprContext): Unit = {
    stack.pollLambda( )
  }

  override def enterBlockExpression(ctx: JvmDslParserParser.BlockExpressionContext): Unit = {
    enterLambdaExpr( List( ) )
  }

  override def exitBlockExpression(ctx: JvmDslParserParser.BlockExpressionContext): Unit = {
    stack.pollLambda( )
  }

  override def enterMatchCaseExpression(ctx: JvmDslParserParser.MatchCaseExpressionContext): Unit = {
    matchCaseParser.parseMatchCase( ctx )
  }

  override def enterMatchCaseBlock(ctx: JvmDslParserParser.MatchCaseBlockContext): Unit = {
    val vars = matchCaseParser.poll( )
    enterContexts[(String, DslType)]( vars.toList, (v, _, blockScope) ⇒
      new FieldScope( 0, v._1, v._2, blockScope, programScope, false ) )
  }

  override def exitMatchCaseBlock(ctx: JvmDslParserParser.MatchCaseBlockContext): Unit = {
    stack.pollLambda( )
  }

  //end handle lambda block


  override def enterVarDef(ctx: JvmDslParserParser.VarDefContext): Unit = {
    if (stack.nonEmpty( )) {
      val currentScope = stack.peek( )
      val dslType = toDslType( ctx.parameter( ).`type`( ) )
      val symbolName = ctx.parameter( ).localVariable( ).IDENTIFIER( ).getText

      if (!ctx.getParent.isInstanceOf[ForStatementContext]) {
        currentScope.addScope( symbolName,
          new FieldScope( currentScope.statements, symbolName, dslType, currentScope, programScope, false ) )
        currentScope.incStatement( )
      }
    }
  }

  private def enterForStatement( ctxList:List[JvmDslParserParser.VarDefContext]): Unit = {
    val parent = stack.peek( )

    val topScope = Option.apply(currentClazzScope).getOrElse(programScope)
    val blockScope = new ForStatementBlockScope( parent.statements, MutableMap( ),
      parent,topScope)

    ctxList.foreach( ctx ⇒ {
      val symbolName = ctx.parameter( ).localVariable( ).IDENTIFIER( ).getText
      val dslType = toDslType( ctx.parameter( ).`type`( ) )
      blockScope.addInitScope( symbolName, new FieldScope( 0, symbolName, dslType, blockScope, programScope, false ) )
      blockScope.incStatement( )
    } )
    parent.incStatement( )
    stack.push( blockScope )

  }


  override def enterForStatementOne(ctx: JvmDslParserParser.ForStatementOneContext): Unit = {
    /**
     * for(Int i=0;i<10;i++){
     * //three statement;
     * }
     */
    enterForStatement( List( ctx.varDef( ) ) )
    //enterAssertStatement will be called so just call one
    stack.incStatement( )
  }

  override def enterForStatementTwo(ctx: JvmDslParserParser.ForStatementTwoContext): Unit = {

    /**
     * <pre><code>
     * for(Int i:list){
     * //two statement
     * }
     * <pre><code>
     **/
    enterForStatement( List( ctx.varDef( ) ) )
    stack.incStatement( )
  }

  override def enterForStatementThree(ctx: JvmDslParserParser.ForStatementThreeContext): Unit = {
    /*
     * for(Int k,Int v:map){
     *    //two statement
     * }
     */
    enterForStatement( ctx.varDef( ).toList )
    stack.incStatement( )
  }

  override def exitDoWhileStatement(ctx: JvmDslParserParser.DoWhileStatementContext): Unit = {
    /**
     * do{
     * //one statement
     * } while(i<100)
     *
     */
    stack.incStatement( )
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

//  override def enterIfStatement(ctx: JvmDslParserParser.IfStatementContext): Unit = {
//    stack.incStatement(ctx.block().size()-1)
//  }


  override def enterIfCondition(ctx: JvmDslParserParser.IfConditionContext): Unit = {
    stack.incStatement()
  }

  override def enterElseifCondition(ctx: JvmDslParserParser.ElseifConditionContext): Unit = {
    stack.incStatement()
  }

  override def enterCatchClause(ctx: JvmDslParserParser.CatchClauseContext): Unit = {
      stack.incStatement()
      val varName = ctx.parameter().localVariable().getText
      val dslType = toDslType(ctx.parameter().`type`())
      catchParam = (dslType,varName)
  }


}
