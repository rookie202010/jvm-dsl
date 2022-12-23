package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.{BaseLiteralContext, ForStatementContext, UnapplyExpressionContext}
import com.dongjiaqiang.jvm.dsl.core.`type`._
import com.dongjiaqiang.jvm.dsl.core.exception.SymbolParseException
import com.dongjiaqiang.jvm.dsl.core.scope._
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser}
import org.antlr.v4.runtime.tree.TerminalNode

import java.util.{LinkedList ⇒ Stack}
import scala.collection.convert.ImplicitConversionsToScala._
import scala.collection.mutable.{ListMap ⇒ MutableMap}


class BlockStack(val programScope: ProgramScope){

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
      programScope.lambdaBlockScope.append(current)
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
  var stack: BlockStack = _
  var currentClazzScope: ClazzScope = _


  override def enterProgram(ctx: JvmDslParserParser.ProgramContext): Unit = {
    programScope = new ProgramScope( )
    stack = new BlockStack(programScope)
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
    val symbolName = ctx.varDef( ).parameter( ).localVariable( ).IDENTIFIER( ).getText
    programScope.addScope( symbolName, new FieldScope( programScope.statements, symbolName,
      DslType.unapply( ctx.varDef( ).parameter( ).`type`( ) ), programScope, programScope, Option( ctx.VOLATILE( ) ).isDefined ) )
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
    stack.poll( )
  }


  //start handle lambdaBlock
  private def enterLambdaExpr(variableList: List[JvmDslParserParser.LocalVariableContext]): Unit = {

    val parent = if(stack.empty()){
      programScope
    }else{
      stack.peek( )
    }

    val topScope = Option.apply( currentClazzScope ).getOrElse( programScope )
    val blockScope = new BlockScope( parent.statements-1, parent, topScope )
    variableList.zipWithIndex.foreach {
      case (lvc, index) ⇒
        blockScope.addScope( lvc.IDENTIFIER( ).getText,
          new FieldScope( index, lvc.IDENTIFIER( ).getText, UnResolvedType, blockScope, programScope, false ) )
    }
    blockScope.incStatement(variableList.size)
    stack.push( blockScope )
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

  def resolveVar(uec: UnapplyExpressionContext, mayType: Option[DslType]):Array[(String,DslType)]={
      def resolveLiteral(blc: BaseLiteralContext, mayType: Option[DslType]):Array[(String,DslType)]={
            def checkVar(terminalNode:TerminalNode,dslType: DslType,validDslType:DslType): Boolean = {
              Option.apply(terminalNode) match {
                case Some(_) ⇒ if (validDslType != dslType) {
                  //todo
                  throw SymbolParseException("")
                } else {
                  true
                }
                case None ⇒ false
              }
            }
            mayType match {
              case Some(dslType)⇒
                if(checkVar(blc.BOOL_LITERAL(),BoolType,dslType)){
                    Array()
                }else if(checkVar(blc.CHAR_LITERAL(),CharType,dslType)){
                    Array()
                }else if(checkVar(blc.STRING_LITERAL(),StringType,dslType)){
                    Array()
                }else if(blc.numberLiteral()!=null){
                  if(checkVar(blc.numberLiteral().INT_LITERAL(),IntType,dslType)){
                    Array()
                  }else if(checkVar(blc.numberLiteral().LONG_LITERAL(),LongType,dslType)) {
                    Array()
                  }else if(checkVar(blc.numberLiteral().FLOAT_LITERAL(),FloatType,dslType)){
                    Array()
                  }else if(checkVar(blc.numberLiteral().DOUBLE_LITERAL(),DoubleType,dslType)){
                    Array()
                  }
                }
              case None⇒ Array()
            }
      }
      if(uec.baseLiteral()!=null){
          resolveLiteral(uec.baseLiteral(),mayType)
      }else if(uec.localVariable()!=null){
          Array((uec.localVariable().IDENTIFIER().getText,mayType.getOrElse(AnyType)))
      }else if(uec.unapplyListExpression()!=null){
          mayType match {
              case Some(dslType)⇒
                dslType match {
                  case listType: ListType ⇒
                    uec.unapplyListExpression().unapplyExpression().
                      flatMap(c ⇒ resolveVar(c, Some(listType.valueType))).toArray
                    //todo
                  case _ ⇒ throw SymbolParseException("")
                }
              case None⇒
                  uec.unapplyListExpression().unapplyExpression().
                flatMap(c⇒resolveVar(c,None)).toArray
          }
      }else if(uec.unapplySetExpression()!=null){

          mayType match {
            case Some(dslType)⇒
                dslType match {
                  case setType: SetType ⇒
                      uec.unapplySetExpression().unapplyExpression().
                        flatMap(c ⇒ resolveVar(c,Some(setType.valueType))).toArray
                }
            case None⇒
                uec.unapplySetExpression().unapplyExpression().
                flatMap(c⇒resolveVar(c,None)).toArray
          }

      }else if(uec.unapplyTupleExpression().unapplyExpression()!=null){

          mayType match {
            case Some(dslType)⇒
                dslType match {
                  case tupleType: TupleType⇒
                      val expressions = uec.unapplyTupleExpression().unapplyExpression()
                      val types = tupleType.valueTypes
                      //todo
                      if(expressions.size()!=types.length){
                          throw SymbolParseException("")
                      }else{
                          expressions.zip(types).flatMap{
                            case (expression,dslType)⇒
                                resolveVar(expression,Some(dslType))
                          }.toArray
                      }
                }
            case None ⇒
              uec.unapplyTupleExpression().unapplyExpression().
                flatMap(c⇒resolveVar(c,None)).toArray
          }

      }else if(uec.unapplyMapExpression().unapplyExpression()!=null){

          mayType match {
            case Some(dslType)⇒
                dslType match {
                  case mapType: MapType⇒
                      uec.unapplyMapExpression().unapplyExpression().grouped(2).flatMap(kv⇒{
                          resolveVar(kv.head,Some(mapType.keyType))++resolveVar(kv.last,Some(mapType.valueType))
                      }).toArray
                }
            case None ⇒
              uec.unapplyMapExpression().unapplyExpression().
                flatMap(c⇒resolveVar(c,None)).toArray
          }

      }else{
        val clazzName = uec.unapplyClazzExpression().clazzType().IDENTIFIER().getText
        programScope.classes.get(clazzName) match {
          case None⇒
            uec.unapplyClazzExpression().unapplyExpression()
              .flatMap(c⇒resolveVar(c,None)).toArray
          case Some(scope)⇒
            val expressions = uec.unapplyClazzExpression().unapplyExpression()
            val types = scope.fields.map(_._2.dslType).toArray
            if(expressions.size!=types.length){
              //todo
                throw SymbolParseException("")
            }else{
                types.zip(expressions).flatMap{
                  case (dslType,expression)⇒
                  resolveVar(expression,Some(dslType))
                }
            }
        }
      }
  }

  override def enterMatchCaseExpr(ctx: JvmDslParserParser.MatchCaseExprContext): Unit = {
    val parent = stack.peek()
    val topScope = Option.apply(currentClazzScope).getOrElse(programScope)

    if (ctx.DEFAULT() != null) {

    } else {
      ctx.caseExpression().foreach {
        caseExpr ⇒
          val blockScope = new BlockScope(parent.statements-1, parent, topScope)
          if (caseExpr.typeMatchExpression() != null) {
            val fieldName = caseExpr.typeMatchExpression().localVariable().IDENTIFIER().getText
            val fieldScope = new FieldScope(0,
              fieldName,
              DslType.unapply(caseExpr.typeMatchExpression().`type`()), blockScope, programScope)
            blockScope.addScope(fieldName, fieldScope)
            blockScope.incStatement()
          } else {
            resolveVar(caseExpr.unapplyExpression(), None)
          }
      }
    }
  }

  //end handle lambda block


  override def enterVarDef(ctx: JvmDslParserParser.VarDefContext): Unit = {
    if (stack.nonEmpty( )) {
      val currentScope = stack.peek( )
      val dslType = DslType.unapply( ctx.parameter( ).`type`( ) )
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
      val dslType = DslType.unapply( ctx.parameter( ).`type`( ) )
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
