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

  def incStatement(): Unit = {
    currentBlockScopes.peekFirst( ).incStatement( )
  }

  def incStatement(i: Int): Unit = {
    currentBlockScopes.peekFirst( ).incStatement( i )
  }

}

/**
 * program{                              =>  program{
 * Int i = 10;                              Int i = 0 //program scope outerScopeIndex = 0;
 * Lambda[Int,Int] j = .... // program scope outerScopeIndex = 1;
 * class Foo ... // program scope outerScopeIndex = 2;
 * Int i // classScope foo outerScopeIndex = 0;
 * Int j // classScope foo outerScopeIndex = 0;
 * def foo... // classScope foo outerScopeIndex = 3;
 * String i // methodScope foo outerScopeIndex = 0;
 * Long k // methodScope foo outerScopeIndex = 0;
 * block0 // methodScope foo  outerScopeIndex = 0;
 * block00 // blockScope block0 outerScopeIndex = 0;
 * Int i==0 // blockScope block00 outerScopeIndex = 0;
 * i<100 // blockScope block00 outerScopeIndex = 1;
 * i++ //blockScope block00 outerScopeIndex = 2;
 * k==0 //blockScope block00 outerScopeIndex = 3;
 * return 10 //blockScope block000 outerScopeIndex = 0;
 * k==100 //blockScope block00 outerScopeIndex = 4;
 * return 20 //blockScope block001 outerScopeIndex = 0;
 * return 30 //blockScope block002 outerScopeIndex = 0;
 * j=k+30 //blockScope block0 outerScopeIndex = 1;
 * return j//blockScope block0 outerScopeIndex = 2;
 * def bar ... // program scope outerScopeIndex = 3;
 * String i // methodScope bar outerScopeIndex = 0;
 * this.i<10 //methodScope bar outerScopeIndex = 1;
 * block11 // methodScope bar  outerScopeIndex = 2;
 * i+=300 // blockScope block11 outerScopeIndex = 0;
 * i*200 // blockScope block11 outerScopeIndex = 1;
 * Int j = 40 //  methodScope bar  outerScopeIndex = 3;
 * block12 // methodScope bar  outerScopeIndex = 4;
 * j=j+30 // blockScope block12 outerScopeIndex = 0;
 * return i.toLong(); //methodScope bar  outerScopeIndex = 5;
 *
 *
 *
 * Lambda[Int,Int] j = a=>{
 * return i+a;
 * }
 *
 * class Foo(Int i,Int j){
 *
 * def foo(String i,Long k)=Int{
 *
 * for(Int i = 0;i<100;i++){
 * if(k==0){
 * return 10;         //block000
 * }else if(k==100){
 * return 20;   //block001
 * }else{
 * return 30;   //block002
 * }
 * } //block00
 *
 * j = k+30;
 *
 * return j;
 * } //block0
 * }
 *
 * def bar(String i)=Long{
 *
 * while(this.i<10){
 * i+=300;
 * i*200;
 * } //block11;
 *
 * Int j = 40;
 *
 * {
 * j=j+30;
 * } //block12;
 *
 * return i.toLong();
 * } //block1
 *
 * }
 *
 */

class SymbolDefParser(var programScope: ProgramScope = new ProgramScope( )) extends JvmDslParserBaseListener {

  var currentMethodScope: MethodScope = _
  var stack: BlockStack = new BlockStack( )
  var currentClazzScope: ClazzScope = _


  override def enterProgram(ctx: JvmDslParserParser.ProgramContext): Unit = {
    programScope = new ProgramScope( )
  }


  override def enterImportClazzStatement(ctx: JvmDslParserParser.ImportClazzStatementContext): Unit = {
    if (ctx.packageName( ) != null) {
      programScope.importClassesFromPackage.put( ctx.importClazz( ).IDENTIFIER( ).map( _.getText ).mkString( "." ),
        ctx.packageName( ).IDENTIFIER( ).getText )
    } else {
      programScope.importClasses.append( ctx.importClazz( ).IDENTIFIER( ).map( _.getText ).mkString( "." ) )
    }
  }

  override def enterUsingPackageStatement(ctx: JvmDslParserParser.UsingPackageStatementContext): Unit = {
    val packagePath = ctx.packagePath( ).STRING_LITERAL( ).getText
    val packageName = ctx.packageName( ).IDENTIFIER( ).getText
    programScope.importPackages.put( packageName, packagePath )
  }

  override def enterFieldDef(ctx: JvmDslParserParser.FieldDefContext): Unit = {
    val symbolName = ctx.varDef( ).localVariable( ).IDENTIFIER( ).getText
    programScope.addScope( symbolName, new FieldScope( programScope.statements, symbolName,
      DslType.unapply( ctx.varDef( ).`type`( ) ), programScope, programScope, Option( ctx.VOLATILE( ) ).isDefined ) )
    programScope.incStatement( )
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

    if (currentMethodScope == null) {
      return
    }

    if (stack.empty( )) {
      stack.push( currentMethodScope.blockScope )
    } else {
      val parent = stack.peek( )
      if (!parent.isInstanceOf[ForStatementBlockScope]) {
        val topScope = Option.apply( currentClazzScope ).getOrElse( programScope )
        stack.push( new BlockScope( parent.statements, parent, topScope ) )
        parent.incStatement( )
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
      val symbolName = ctx.localVariable( ).IDENTIFIER( ).getText
      val dslType = DslType.unapply( ctx.`type`( ) )
      blockScope.addInitScope( symbolName, new FieldScope( 0, symbolName, dslType, blockScope,programScope,false ) )
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
     * for(Int i:list){
     * //two statement
     * }
     */
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

  override def enterIfStatement(ctx: JvmDslParserParser.IfStatementContext): Unit = {
    stack.incStatement(ctx.block().size()-1)
  }
}
