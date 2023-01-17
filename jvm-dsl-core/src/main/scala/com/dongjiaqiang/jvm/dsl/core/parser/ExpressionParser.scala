package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.api.expression
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.scope._
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.exception.ExpressionParserException
import com.dongjiaqiang.jvm.dsl.core.expression.generator._
import com.dongjiaqiang.jvm.dsl.core.program.{Clazz, Method, Program}
import com.dongjiaqiang.jvm.dsl.core.scope._
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser}

import java.util.{LinkedList ⇒ Stack}
import scala.collection.convert.ImplicitConversionsToScala._
import scala.collection.mutable.{ListMap ⇒ MutableMap}

/**
 *
 * statement parser response for parse statement to expression
 * <pre><code>
 * program{
 *     Int i = 100;
 *     Int j = 200;
 *
 *     def f()=Unit // method scope
 *     {  // block scope
 *         Int k = 100;  // sIndex = 0
 *         k = 100+i;  // sIndex = 1
 *
 *         {  // sIndex = 2
 *             Int k = k;  // sIndex = 0,bIndex = 0
 *             j = 300+k; // sIndex = 1, bIndex = 0
 *             i = i*2; // sIndex = 2, bIndex = 0
 *
 *         }
 *
 *         k = 100+i; // sIndex = 3
 *
 *         {  // sIndex = 4
 *             j+100;  // sIndex = 0,bIndex = 1
 *             k+300; // sIndex = 1,bIndex = 1
 *
 *             { // sIndex = 2
 *                 Int k = k; // sIndex = 0, bIndex = 0
 *                 k+=1; // sIndex = 1,bIndex = 0
 *             }
 *         }
 *
 *         {  // sIndex = 5
 *
 *         }
 *     }
 * }
 *<pre><code>
 * program scope
 */

object ContextType extends Enumeration {
  val BLOCK, WHILE, DO_WHILE, FOR_LOOP, FOR_LOOP_COLLECTION, FOR_LOOP_MAP, SYNC, IF, TRY = Value
}

class ParseContext {
  private var contextType: ContextType.Value = ContextType.BLOCK
  private var contextRules: List[AnyRef] = List()
  private var contextRuleIndex: Int = 0

  def getNextRule[T]: T = {
    val rule = contextRules(contextRuleIndex).asInstanceOf[T]
    contextRuleIndex = contextRuleIndex + 1
    rule
  }

  def set(contextType: ContextType.Value): Unit = {
    this.contextType = contextType
  }

  def set(contextRules: List[AnyRef]): Unit = {
    this.contextRules = contextRules
  }

  def get(): ContextType.Value = contextType

  def reset(): Unit = {
    contextType = ContextType.BLOCK
    contextRules = List()
    contextRuleIndex = 0
  }

  def mayNextRule[T]: Option[T] = {
    if (empty()) {
      None
    } else {
      contextRules( contextRuleIndex ) match {
        case rule: T ⇒
          contextRuleIndex = contextRuleIndex + 1
          Some( rule )
        case _ ⇒
          None
      }
    }
  }

  def empty(): Boolean = {
    contextRules.size - 1 == contextRuleIndex
  }
}

trait ExprContext {
  def getCurrentExpressionIndex: Int

  def getContextScope: BlockScope

  def getTopScope: Scope

  def resolveMethod(name:String):MethodScope={
      getTopScope.resolveMethod(name) match {
        case Some(scope)⇒scope
        case None⇒
          throw ExpressionParserException(s"can not resolve $name in $getTopScope")
      }
  }

  def pushBlock(lambdaBlock: Block): Unit
}

class ExpressionParser(val programScope: ProgramScope) extends JvmDslParserBaseListener with ExprContext {

  //context type
  val parseContext = new ParseContext

  //scopes

  //current clazz scope
  var currentClazzScope: ClazzScope = _
  //current method scope
  var currentMethodScope: MethodScope = _
  //current block scope
  var currentBlockScope: BlockScope = _


  var currentBlockIndex: Int = _
  var blockIndexStack: Stack[Int] = new Stack[Int]( )
  var currentStatementIndex: Int = _
  var statementIndexStack: Stack[Int] = new Stack[Int]( )

  //expressions
  var program: Program = _
  var currentClazz: Clazz = _
  var currentMethod: Method = _
  var currentBlock: Block = _
  var blockStack: Stack[Block] = new Stack[Block]( )

  var lambdaBlocks: java.util.List[(Scope, Block)] = new java.util.LinkedList[(Scope, Block)]( )

  override def enterProgram(ctx: JvmDslParserParser.ProgramContext): Unit = {

    /**
     * create program expression
     */
    program = Program( programScope, MutableMap( ), MutableMap( ), MutableMap( ) )
  }

  override def enterClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
    /**
     * enter class scope
     */
    val clazzName = ctx.IDENTIFIER( ).getText
    currentClazzScope = programScope.classes( clazzName )

    /**
     * create class expression
     */
    currentClazz = Clazz( currentClazzScope, MutableMap( ) )
    program.classes.put( currentClazzScope.name, currentClazz )
  }

  override def exitClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {

    /**
     * exit class scope
     */
    currentClazzScope = null


    currentClazz = null
  }

  override def enterFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {

    /**
     * enter method scope
     */
    val methodName = ctx.funcName( ).getText
    if (currentClazzScope != null) {
      currentMethodScope = currentClazzScope.methods( methodName )
    } else {
      currentMethodScope = programScope.methods( methodName )
    }

    /*
     * create method body ( a block)
     */
    currentMethod = Method( currentMethodScope, new Block( ) )

    if (currentClazz != null) {
      currentClazz.methods
    } else {
      program.methods
    }.put( currentMethodScope.name, currentMethod )

  }

  override def exitFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
    /**
     * exit method scope
     */
    currentMethodScope = null

    currentMethod = null
  }

  override def enterFieldDef(ctx: FieldDefContext): Unit = {
    val assigned = if (ctx.varDef( ).expression( ) == null) {
      None
    } else {
      Some( if (ctx.varDef( ).expression( ).lambdaExpression( ) != null) {
        LambdaGenerator.generate( this, ctx.varDef( ).expression( ).lambdaExpression( ) )
      } else if (ctx.varDef( ).expression( ).conditionalOrExpression( ) != null) {
        OrGenerator.generate( this, ctx.varDef( ).expression( ).conditionalOrExpression( ) )
      } else if (ctx.varDef( ).expression( ).blockExpression( ) != null) {
        BlockExpressionGenerator.generate( this, ctx.varDef( ).expression( ).blockExpression( ) )
      } else {
        MatchCaseExpressionGenerator.generate( this, ctx.varDef( ).expression( ).matchCaseExpression( ) )
      } )
    }
    assigned.foreach( expression ⇒ program.assigned.put( ctx.varDef( ).parameter( ).localVariable( ).IDENTIFIER( ).getText
      , expression ) )
  }

  override def enterBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    if (currentBlockScope == null) {

      //enter first block scope ( method body)
      currentBlockScope = currentMethodScope.blockScope

      currentBlockIndex = 0
      blockIndexStack.push( currentBlockIndex )

      currentStatementIndex = 0
      statementIndexStack.push( currentStatementIndex )

      currentBlock = currentMethod.body

    } else {

      currentStatementIndex = currentStatementIndex + 1
      statementIndexStack.push(currentStatementIndex)
      currentStatementIndex = 0

      currentBlockScope = currentBlockScope.childrenScopes(currentBlockIndex)
      currentBlockIndex = currentBlockIndex + 1
      blockIndexStack.push(currentBlockIndex)
      currentBlockIndex = 0
      blockStack.push(currentBlock)

      def varDef(fieldScope: FieldScope, context: VarDefContext): LocalVarDef = {
        if (context.expression( ) == null) {
          expression.LocalVarDef( fieldScope, fieldScope.dslType, None )
        } else if (context.expression( ).lambdaExpression( ) != null) {
          expression.LocalVarDef( fieldScope, fieldScope.dslType, Some( LambdaGenerator.generate( this, context.expression( ).lambdaExpression( ) ) ) )
        } else {
          expression.LocalVarDef( fieldScope, fieldScope.dslType, Some( OrGenerator.generate( this, context.expression( ).conditionalOrExpression( ) ) ) )
        }
      }

      def getLooped(context: LiteralAndCallChainContext): Expression = {
        context match {
          case c: LiteralExprContext ⇒
            LiteralGenerator.generate( this, c.literal( ) )
          case _ ⇒
            CallChainGenerator.generate( this, context )
        }
      }

      currentBlock = parseContext.get() match {
        case ContextType.BLOCK ⇒ new Block()
        case ContextType.WHILE ⇒ new WhileBlock()
        case ContextType.DO_WHILE ⇒ new DoWhileBlock()
        case ContextType.FOR_LOOP ⇒
          val (_, fieldScope) = currentBlockScope.asInstanceOf[ForStatementBlockScope].initFields.head
          val context = parseContext.getNextRule[ForStatementOneContext]

          //var def
          val localVarDef = varDef( fieldScope, context.varDef( ) )
          //condition
          val condition = OrGenerator.generate( this, context.conditionalOrExpression( ) )
          //assignment
          val assignment = AssignGenerator.generate( this, context.assignment( ) )

          new ForLoop( localVarDef, condition, assignment )
        case ContextType.FOR_LOOP_COLLECTION ⇒
          val (_, fieldScope) = currentBlockScope.asInstanceOf[ForStatementBlockScope].initFields.head
          val context = parseContext.getNextRule[ForStatementTwoContext]

          //var def
          val localVarDef = varDef( fieldScope, context.varDef( ) )
          //looped
          val looped = getLooped( context.literalAndCallChain( ) )

          new ForLoopCollection(localVarDef, looped)

        case ContextType.FOR_LOOP_MAP ⇒

          val (_, keyFieldScope) = currentBlockScope.asInstanceOf[ForStatementBlockScope].initFields.head
          val (_, valueFieldScope) = currentBlockScope.asInstanceOf[ForStatementBlockScope].initFields.last
          val context = parseContext.getNextRule[ForStatementThreeContext]

          //key var def
          val keyVarDef = varDef( keyFieldScope, context.varDef( ).head )
          //value var def
          val valueVarDef = varDef( valueFieldScope, context.varDef( ).last )
          //looped
          val looped = getLooped( context.literalAndCallChain( ) )
          new ForLoopMap( keyVarDef, valueVarDef, looped )

        case ContextType.SYNC ⇒
          new SyncBlock()
        case ContextType.IF ⇒
          new IfBlock()
        case ContextType.TRY ⇒
          if (parseContext.mayNextRule[String].map(r ⇒ r == "TRY").isDefined) {
            new TryBlock( )
          } else if (parseContext.mayNextRule[String].map( r ⇒ r == "FINALLY" ).isDefined) {
            new FinallyBlock( )
          } else {
            new CatchBlock( )
          }
      }
    }
  }

  override def enterLambdaBlock(ctx: LambdaBlockContext): Unit = {
    if (currentBlock != null) {
      blockStack.push( currentBlock )
      statementIndexStack.push( currentStatementIndex )
      blockIndexStack.push( currentBlockIndex )
    }

    currentStatementIndex = 0
    currentBlockIndex = 0
    currentBlock = lambdaBlocks.remove( 0 )._2

    currentBlockScope = if (currentBlockScope != null) {
      currentBlockScope.lambdaScopes.remove( 0 )
    } else {
      programScope.lambdaBlockScope.remove( 0 )
    }
  }

  override def enterMatchCaseBlock(ctx: MatchCaseBlockContext): Unit = {
    enterLambdaBlock( null )
  }

  override def exitLambdaBlock(ctx: LambdaBlockContext): Unit = {
    currentBlockScope.parentScope match {

      case blockScope: BlockScope ⇒

        currentBlockScope = blockScope
        currentBlockIndex = blockIndexStack.poll( )
        currentStatementIndex = statementIndexStack.poll( )

        currentBlock = blockStack.poll( )
      case _: ProgramScope ⇒
        blockIndexStack.poll( )
        statementIndexStack.poll( )

        currentBlockIndex = 0
        currentStatementIndex = 0

        currentBlockScope = null
    }
  }

  override def exitMatchCaseBlock(ctx: MatchCaseBlockContext): Unit = {
    exitLambdaBlock( null )
  }

  override def exitBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    currentBlockScope.parentScope match {
      case blockScope: BlockScope ⇒
        currentBlockScope = blockScope
        currentBlockIndex = blockIndexStack.poll( )
        currentStatementIndex = statementIndexStack.poll( )

        val parentBlock = blockStack.poll( )
        parentBlock.expressions.append( currentBlock )
        currentBlock = parentBlock

        if (parseContext.get() == ContextType.IF && !parseContext.empty()) {
          val context = parseContext.getNextRule[ConditionalOrExpressionContext]
          updateExpression( {
            new IfCondition( OrGenerator.generate( this, context ), false )
          } )
        }

        if (parseContext.get() == ContextType.TRY) {
          parseContext.mayNextRule[ParameterContext]
            .foreach(p ⇒ {
              updateExpression(
                new CatchParameter( p.localVariable( ).IDENTIFIER( ).getText,
                  toDslType( p.`type`( ) ) )
              )
            } )
        }

      case _: MethodScope ⇒
        blockIndexStack.poll( )
        statementIndexStack.poll( )

        currentBlockIndex = 0
        currentStatementIndex = 0

        currentBlockScope = null


    }
  }


  /*
   *program{
   *    def method(Int i)=Int{
   *        Int i = 10;
   *        Long j = 10L;
   *
   *    }
   * }
   *
   */
  override def enterVarDefExpr(ctx: VarDefExprContext): Unit = {
    updateExpression( VarDefGenerator.generate( this, ctx.varDef( ) ) )
  }

  //i = k;
  override def enterAssignExpr(ctx: JvmDslParserParser.AssignExprContext): Unit = {
    updateExpression( AssignGenerator.generate( this, ctx.assignment( ) ) )
  }

  //assert  i == 1
  override def enterAssertExpr(ctx: JvmDslParserParser.AssertExprContext): Unit = {
    updateExpression( {
      val expression = OrGenerator.generate( this, ctx.assertStatement( ).conditionalOrExpression( ) )
      new Assert( expression )
    } )
  }

  //break
  override def enterBreakExpr(ctx: JvmDslParserParser.BreakExprContext): Unit = {
    currentBlock.expressions.append(Break)
    currentStatementIndex = currentStatementIndex + 1
  }

  //continue
  override def enterContinueExpr(ctx: JvmDslParserParser.ContinueExprContext): Unit = {
    currentBlock.expressions.append(Continue)
    currentStatementIndex = currentStatementIndex + 1
  }


  /**
   * {
   * while(i<100){
   * i++;
   * foo();
   * }
   * }
   *
   * */
  override def enterWhileStatement(ctx: JvmDslParserParser.WhileStatementContext): Unit = {
    updateExpression( {
      val expression = OrGenerator.generate( this, ctx.conditionalOrExpression( ) )
      new WhileCondition( expression )
    } )
    parseContext.set( ContextType.WHILE )
  }

  override def exitWhileStatement(ctx: JvmDslParserParser.WhileStatementContext): Unit = {
    parseContext.reset()
  }


  /**
   * {
   *
   * do{
   * foo();
   * i--;
   * }while(i<100)
   * }
   *
   * */
  override def enterDoWhileStatement(ctx: JvmDslParserParser.DoWhileStatementContext): Unit = {
    parseContext.set(ContextType.DO_WHILE)
  }

  override def exitDoWhileStatement(ctx: JvmDslParserParser.DoWhileStatementContext): Unit = {
    updateExpression( {
      val expression = OrGenerator.generate( this, ctx.conditionalOrExpression( ) )
      new DoWhileCondition( expression )
    } )
    parseContext.reset( )
  }

  /**
   * for(Int i=10;i<100;i++){}
   * for(Int i:[1,2,3]){}
   * for(Int k,Int v: {1:1,2:1} ){}
   *
   * */
  override def enterForExpr(ctx: JvmDslParserParser.ForExprContext): Unit = {
    parseContext.set(List(ctx.forStatement()))
    parseContext.set(ctx.forStatement() match {
      case _:ForStatementOneContext ⇒ ContextType.FOR_LOOP
      case _:ForStatementTwoContext ⇒ ContextType.FOR_LOOP_COLLECTION
      case _:ForStatementThreeContext ⇒ ContextType.FOR_LOOP_MAP
    })
  }

  override def exitForExpr(ctx: JvmDslParserParser.ForExprContext): Unit = {
    parseContext.reset()
  }

  /**
   * {
   * synchronized(xx){
   * foo()
   * }
   * }
   * */
  override def enterSyncExpr(ctx: JvmDslParserParser.SyncExprContext): Unit = {
    updateExpression( {
      val expression = OrGenerator.generate( this, ctx.synchronizedStatement( ).conditionalOrExpression( ) )
      new SyncCondition( expression )
    } )
    parseContext.set( ContextType.SYNC )
  }

  override def exitSyncExpr(ctx: JvmDslParserParser.SyncExprContext): Unit = {
    parseContext.reset( )
  }


  /**
   * throw e;
   * e();
   * return e;
   *
   */
  override def enterThrowOrSideEffectExpr(ctx: JvmDslParserParser.ThrowOrSideEffectExprContext): Unit = {
    updateExpression( {
      if (ctx.throwReturnOrSideEffectStatement( ).THROW( ) != null) {
        Throw( ExpressionGenerator.generate( this, ctx.throwReturnOrSideEffectStatement( ).expression( ) ) )
      } else if (ctx.throwReturnOrSideEffectStatement( ).RETURN( ) != null) {
        Return( ExpressionGenerator.generate( this, ctx.throwReturnOrSideEffectStatement( ).expression( ) ) )
      } else {
        ExpressionGenerator.generate( this, ctx.throwReturnOrSideEffectStatement( ).expression( ) )
      }
    } )
  }


  /**
   * if(i>10){
   *
   * }else if(i<10){
   *
   * }else{
   * }
   * */
  override def enterIfExpr(ctx: JvmDslParserParser.IfExprContext): Unit = {
    parseContext.set( ctx.ifStatement( ).conditionalOrExpression( ).toList )
    parseContext.set( ContextType.IF )

    val context = parseContext.getNextRule[ConditionalOrExpressionContext]
    updateExpression(
      new IfCondition( OrGenerator.generate( this, context ), true )
    )
  }

  override def exitIfExpr(ctx: JvmDslParserParser.IfExprContext): Unit = {
    parseContext.reset( )
  }

  /**
   * {
   * try{
   * foo();
   * }catch(Exception e){
   * return 1;
   * }catch(Exception e1){
   * return 2;
   * }finally{
   * return 3;
   * }
   * }
   * */
  override def enterTryExpr(ctx: JvmDslParserParser.TryExprContext): Unit = {
    parseContext.set( ContextType.TRY )
    val tryNode = List( "TRY" )
    val parameters = ctx.tryStatement( ).catches( ).catcheClause( ).map( _.parameter( ) ).toList
    val finallyNode = Option.apply( ctx.tryStatement( ).FINALLY( ) ).map( _ ⇒ "FINALLY" ).toList
    parseContext.set( tryNode ++ parameters ++ finallyNode )
  }

  override def exitTryExpr(ctx: JvmDslParserParser.TryExprContext): Unit = {
    parseContext.reset( )
  }


  private def updateExpression(expression: Expression): Unit = {
    currentBlock.expressions.append( expression )
    currentStatementIndex = currentStatementIndex + 1
  }


  override def getCurrentExpressionIndex: Int = currentStatementIndex

  override def getContextScope: BlockScope = currentBlockScope

  override def getTopScope: Scope = Option.apply( currentClazzScope ).getOrElse( programScope )

  override def pushBlock(lambdaBlock: Block): Unit = {
    val currentScope = if (currentBlockScope != null) {
      currentBlockScope
    } else {
      programScope
    }

    if (lambdaBlocks.isEmpty) {
      lambdaBlocks.add( (currentScope, lambdaBlock) )
    } else {
      if (currentScope == lambdaBlocks.get( 0 )._1) {
        lambdaBlocks.add( (currentScope, lambdaBlock) )
      } else {
        lambdaBlocks.add( 0, (currentScope, lambdaBlock) )
      }

    }
  }

}
