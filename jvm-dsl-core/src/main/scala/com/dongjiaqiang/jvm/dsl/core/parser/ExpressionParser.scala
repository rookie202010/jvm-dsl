package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.LocalVarDef
import com.dongjiaqiang.jvm.dsl.api.expression.block.Block
import com.dongjiaqiang.jvm.dsl.api.expression.statement._
import com.dongjiaqiang.jvm.dsl.api.scope._
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.expression.generator._
import com.dongjiaqiang.jvm.dsl.core.program.{Clazz, Method, Program}
import com.dongjiaqiang.jvm.dsl.core.scope._
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser}

import java.util.{LinkedList ⇒ Stack}
import scala.collection.convert.ImplicitConversionsToScala._
import scala.collection.mutable.{LinkedHashMap ⇒ MutableMap}
import scala.reflect.ClassTag

/**
 *
 * <pre><code>
 *
 * statement parser response for parse statement to expression
 *
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

class  ParseContext {
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

  def mayNextRule[T: ClassTag]: Option[T] = {
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
    contextRules.size  <= contextRuleIndex
  }
}

trait ExprContext {

  def ignoreLambdaBlock(ignore:Boolean):Unit

  def getCurrentBlock:Block

  def getCurrentExpressionIndex: Int

  def getContextScope: BlockScope

  def getTopScope: Scope

  def getProgramScope:ProgramScope

  def resolveMethod(name:String):Option[MethodScope]={
      getTopScope.resolveMethod(name)
  }

  def pushBlock(lambdaBlock: Block): Unit
}

class ExpressionParser(val programScope: ProgramScope) extends JvmDslParserBaseListener with ExprContext {

  //context type
   private val parseContextStack = new Stack[ParseContext]()

  //scopes

  //current clazz scope
  var currentClazzScope: ClazzScope = _
  //current method scope
  var currentMethodScope: MethodScope = _
  //current block scope
   private var currentBlockScope: BlockScope = _


   private var currentBlockIndex: Int = _
  private val blockIndexStack: Stack[Int] = new Stack[Int]( )
  private var currentStatementIndex: Int = _
  private val statementIndexStack: Stack[Int] = new Stack[Int]( )

  //expressions
  var program: Program = _
  private var currentClazz: Clazz = _
   private var currentMethod: Method = _

   private var currentBlock: Block = _

   private var ignoreLambdaBlock: Boolean = false


  override def ignoreLambdaBlock(ignore: Boolean): Unit = ignoreLambdaBlock = ignore

  override def getCurrentBlock: Block = currentBlock

  private val blockStack: Stack[Block] = new Stack[Block]( )

  private val lambdaBlocks: java.util.List[(Scope, Block)] = new java.util.LinkedList[(Scope, Block)]( )

  override def enterProgram(ctx: JvmDslParserParser.ProgramContext): Unit = {

    /**
     * create program expression
     */
    program = Program( programScope, MutableMap( ), MutableMap( ), MutableMap( ) )

    parseContextStack.push(new ParseContext)
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
    currentMethod = Method( currentMethodScope,  Block( ) )

    (if (currentClazz != null) {
      currentClazz.methods
    } else {
      program.methods
    }).put( currentMethodScope.name, currentMethod )

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
          LocalVarDef( fieldScope, fieldScope.dslType, None )
        } else if (context.expression( ).lambdaExpression( ) != null) {
          expression.`var`.LocalVarDef( fieldScope, fieldScope.dslType, Some( LambdaGenerator.generate( this, context.expression( ).lambdaExpression( ) ) ) )
        } else {
          expression.`var`.LocalVarDef( fieldScope, fieldScope.dslType, Some( OrGenerator.generate( this, context.expression( ).conditionalOrExpression( ) ) ) )
        }
      }

      def getLooped(context: LiteralAndCallChainContext): ValueExpression = {
        context match {
          case c: LiteralExprContext ⇒
            LiteralGenerator.generate( this, c.literal( ) )
          case _ ⇒
            CallChainGenerator.generate( this, context )
        }
      }

      val parseContext = parseContextStack.peek()

      currentBlock = parseContext.get() match {
        case ContextType.BLOCK ⇒ Block()
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

          val (_, keyFieldScope) = currentBlockScope.asInstanceOf[ForStatementBlockScope].initFields.last
          val (_, valueFieldScope) = currentBlockScope.asInstanceOf[ForStatementBlockScope].initFields.head
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
          val nextRule = parseContext.mayNextRule[String]
          if (nextRule.contains( "TRY" )) {
            new TryBlock( )
          } else if (nextRule.contains( "FINALLY" )) {
            new FinallyBlock( )
          } else if(nextRule.contains("CATCH")){
            new CatchBlock( )
          }else{
            throw ExpressionParseException(s"error happen when parsing try expression: $nextRule")
          }
      }
    }
  }

  override def enterLambdaBlock(ctx: LambdaBlockContext): Unit = {
    if(ignoreLambdaBlock){
      ignoreLambdaBlock( false )
      return
    }

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
      programScope.lambdaScopes.remove( 0 )
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

    val parseContext = parseContextStack.peek()

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
            IfCondition( OrGenerator.generate( this, context ), first = false )
          } )
        }

        if (parseContext.get() == ContextType.TRY) {
          parseContext.mayNextRule[ParameterContext].foreach(p ⇒ {
              updateExpression(
                CatchParameter( p.localVariable( ).IDENTIFIER( ).getText,
                  toDslType( p.`type`( ),programScope ) )
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


  /**
   *<pre><code>
   *program{
   *    def method(Int i)=Int{
   *        Int i = 10;
   *        Long j = 10L;
   *    }
   * }
   *<pre><code>
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
      Assert( expression )
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
   * <pre><code>
   * {
   *    while(i<100){
   *      i++;
   *      foo();
   *    }
   * }
   *<pre><code>
   * */
  override def enterWhileStatement(ctx: JvmDslParserParser.WhileStatementContext): Unit = {
    updateExpression( {
      val expression = OrGenerator.generate( this, ctx.conditionalOrExpression( ) )
      WhileCondition( expression )
    } )
    val parseContext = new ParseContext
    parseContext.set( ContextType.WHILE )

    parseContextStack.push(parseContext)
  }

  override def exitWhileStatement(ctx: JvmDslParserParser.WhileStatementContext): Unit = {
    parseContextStack.pop()
  }


  /**
   * <pre><code>
   * {
   *    do{
   *      foo();
   *      i--;
   *    }while(i<100)
   * }
   *<pre><code>
   * */
  override def enterDoWhileStatement(ctx: JvmDslParserParser.DoWhileStatementContext): Unit = {
    val parseContext = new ParseContext
    parseContext.set(ContextType.DO_WHILE)
    parseContextStack.push(parseContext)
  }

  override def exitDoWhileStatement(ctx: JvmDslParserParser.DoWhileStatementContext): Unit = {
    updateExpression( {
      val expression = OrGenerator.generate( this, ctx.conditionalOrExpression( ) )
      DoWhileCondition( expression )
    } )
    parseContextStack.pop()
  }

  /**
   * <pre><code>
   * for(Int i=10;i<100;i++){
   * }
   * for(Int i:[1,2,3]){
   * }
   * for(Int k,Int v: {1:1,2:1} ){
   * }
   *<pre><code>
   * */
  override def enterForExpr(ctx: JvmDslParserParser.ForExprContext): Unit = {
    val parseContext = new ParseContext
    parseContext.set(List(ctx.forStatement()))
    parseContext.set(ctx.forStatement() match {
      case _:ForStatementOneContext ⇒ ContextType.FOR_LOOP
      case _:ForStatementTwoContext ⇒ ContextType.FOR_LOOP_COLLECTION
      case _:ForStatementThreeContext ⇒ ContextType.FOR_LOOP_MAP
    })

    parseContextStack.push(parseContext)
  }

  override def exitForExpr(ctx: JvmDslParserParser.ForExprContext): Unit = {
    parseContextStack.pop()
  }

  /**
   * <pre><code>
   * {
   *    synchronized(xx){
   *        foo()
   *    }
   * }
   * <pre><code>
   * */
  override def enterSyncExpr(ctx: JvmDslParserParser.SyncExprContext): Unit = {
    updateExpression( {
      val expression = OrGenerator.generate( this, ctx.synchronizedStatement( ).conditionalOrExpression( ) )
      SyncCondition( expression )
    } )
    val parseContext = new ParseContext
    parseContext.set( ContextType.SYNC )

    parseContextStack.push(parseContext)
  }

  override def exitSyncExpr(ctx: JvmDslParserParser.SyncExprContext): Unit = {
    parseContextStack.pop()
  }


  /**
   * <pre><code>
   * throw e;
   * e();
   * return e;
   *<pre><code>
   */
  override def enterThrowOrReturnSideEffectExpr(ctx: JvmDslParserParser.ThrowOrReturnSideEffectExprContext): Unit = {
    updateExpression( {
      ctx.throwOrReturnSideEffectStatement() match {
        case throwOrSideEffectExprContext: ThrowOrSideEffectExprContext ⇒
          if (throwOrSideEffectExprContext.throwOrSideEffectStatement( ).THROW( ) != null) {
            Throw( ExpressionGenerator.generate( this,
              throwOrSideEffectExprContext.throwOrSideEffectStatement( ).expression( ) ) )
          } else {
            ExpressionGenerator.generate( this, throwOrSideEffectExprContext.throwOrSideEffectStatement( ).expression( ) )
          }
        case _: ReturnStatementExprContext ⇒ Return( None )
        case returnExprStatementExprContext: ReturnExprStatementExprContext ⇒
          Return( Some( ExpressionGenerator.generate( this,
            returnExprStatementExprContext.returnExpressionStatement( ).expression( ) ) ) );
      }
    } )
  }


  /**
   * <pre><code>
   * if(i>10){
   *
   * }else if(i<10){
   *
   * }else{
   * }
   * <pre><code>
   * */
  override def enterIfExpr(ctx: JvmDslParserParser.IfExprContext): Unit = {
    val parseContext = new ParseContext
    val conditions = ctx.ifStatement().ifCondition().conditionalOrExpression()+:ctx.ifStatement().elseifCondition().map(_.conditionalOrExpression())
    parseContext.set( conditions.toList )
    parseContext.set( ContextType.IF )

    val context = parseContext.getNextRule[ConditionalOrExpressionContext]
    updateExpression(
      IfCondition( OrGenerator.generate( this, context ), first = true )
    )

    parseContextStack.push(parseContext)
  }

  override def exitIfExpr(ctx: JvmDslParserParser.IfExprContext): Unit = {
    parseContextStack.pop()
  }

  /**
   * <pre><code>
   * {
   *    try{
   *      foo();
   *    }catch(Exception e){
   *      return 1;
   *    }catch(Exception e1){
   *      return 2;
   *    }finally{
   *      return 3;
   *    }
   * }
   * <pre><code>
   * */
  override def enterTryExpr(ctx: JvmDslParserParser.TryExprContext): Unit = {
    val parseContext = new ParseContext
    parseContext.set( ContextType.TRY )
    val tryNode = List( "TRY" )
    val parameters = Option.apply( ctx.tryStatement( ).catches( ) ).map( c ⇒ c.catchClause( ).map( _.parameter( ) ) ).toList.flatten.flatMap( p ⇒ List( p, "CATCH" ) )
    val finallyNode = Option.apply( ctx.tryStatement( ).FINALLY( ) ).map( _ ⇒ "FINALLY" ).toList
    parseContext.set( tryNode ++ parameters ++ finallyNode )
    parseContextStack.push(parseContext)
  }

  override def exitTryExpr(ctx: JvmDslParserParser.TryExprContext): Unit = {
    parseContextStack.pop()
  }


  private def updateExpression(expression: Expression): Unit = {
    currentBlock.expressions.append( expression )
    currentStatementIndex = currentStatementIndex + 1
  }


  override def getCurrentExpressionIndex: Int = currentStatementIndex

  override def getContextScope: BlockScope = currentBlockScope

  override def getTopScope: Scope = Option.apply( currentClazzScope ).getOrElse( programScope )


  override def getProgramScope: ProgramScope = programScope

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
