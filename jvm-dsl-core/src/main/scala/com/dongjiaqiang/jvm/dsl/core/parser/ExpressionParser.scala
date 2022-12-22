package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.`type`.DslType
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.expression.generator.{ExpressionContext, _}
import com.dongjiaqiang.jvm.dsl.core.program.{Clazz, Method, Program}
import com.dongjiaqiang.jvm.dsl.core.scope._
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser}

import java.util.{LinkedList ⇒ Stack}
import scala.collection.convert.ImplicitConversionsToScala._
import scala.collection.mutable.{ListMap ⇒ MutableMap}

/**
 *
 * statement parser response for parse statement to expression
 *
 * //program{
 * //    Int i = 100;
 * //    Int j = 200;
 * //
 * //    def f()=Unit // method scope
 * //    {  // block scope
 * //        Int k = 100;  // sIndex = 0
 * //        k = 100+i;  // sIndex = 1
 * //
 * //        {  // sIndex = 2
 * //            Int k = k;  // sIndex = 0,bIndex = 0
 * //            j = 300+k; // sIndex = 1, bIndex = 0
 * //            i = i*2; // sIndex = 2, bIndex = 0
 * //
 * //        }
 * //
 * //        k = 100+i; // sIndex = 3
 * //
 * //        {  // sIndex = 4
 * //            j+100;  // sIndex = 0,bIndex = 1
 * //            k+300; // sIndex = 1,bIndex = 1
 * //
 * //            { // sIndex = 2
 * //                Int k = k; // sIndex = 0, bIndex = 0
 * //                k+=1; // sIndex = 1,bIndex = 0
 * //            }
 * //        }
 * //
 * //        {  // sIndex = 5
 * //
 * //        }
 * //    }
 * //}
 *
 *
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

class ExpressionParser(val programScope: ProgramScope) extends JvmDslParserBaseListener {

  //context type
  val parseContext = new ParseContext

  //scopes
  var currentClazzScope: ClazzScope = _
  var currentMethodScope: MethodScope = _
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

  //lambda expression
  var lambdaContext: Boolean = false

  override def enterProgram(ctx: JvmDslParserParser.ProgramContext): Unit = {
    program = Program( programScope, MutableMap( ), MutableMap( ) )
  }

  override def enterClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
    /**
     * 进入当前类作用域
     */
    val symbolName = ctx.IDENTIFIER( ).getText
    currentClazzScope = programScope.classes(symbolName)

    currentClazz = Clazz(currentClazzScope, MutableMap())
    program.classes.put(currentClazzScope.name, currentClazz)
  }

  override def exitClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
    /**
     * 退出当前类作用域
     */
    currentClazzScope = null

    currentClazz = null
  }

  override def enterFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
    /**
     * 进入当前方法域
     */
    val symbolName = ctx.funcName().getText
    if (currentClazzScope != null) {
      currentMethodScope = currentClazzScope.methods(symbolName)
    } else {
      currentMethodScope = programScope.methods(symbolName)
    }

    if (currentClazz != null) {
      currentClazz.methods
    } else {
      program.methods
    }.put(currentMethodScope.name, Method(currentMethodScope, new Block()))

  }

  override def exitFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
    /**
     * 退出当前方法域
     */
    currentMethodScope = null

    currentMethod = null
  }

  override def enterBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    if (currentBlockScope == null) {
      currentBlockScope = currentMethodScope.blockScope

      currentBlockIndex = 0
      blockIndexStack.push(currentBlockIndex)

      currentStatementIndex = 0
      statementIndexStack.push(currentStatementIndex)

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


      def varDef(name: String, fieldScope: FieldScope, context: VarDefContext): LocalVarDef = {
        if (context.expression( ).lambdaExpression( ) != null) {
          LocalVarDef( name, fieldScope.dslType, Some( getExpression( c ⇒ LambdaGenerator.generate( c, context.expression( ).lambdaExpression( ) ) ) ) )
        } else {
          LocalVarDef( name, fieldScope.dslType, Some( getExpression( c ⇒ OrGenerator.generate( c, context.expression( ).conditionalOrExpression( ) ) ) ) )
        }
      }

      def getLooped(context: LiteralAndCallChainContext): Expression = {
        if (context.literal() != null) {
          getExpression(c ⇒ LiteralGenerator.generate(c, context.literal()))
        } else {
          getExpression(c ⇒ CallChainGenerator.generate(c, context.callChain()))
        }
      }

      currentBlock = parseContext.get() match {
        case ContextType.BLOCK ⇒ new Block()
        case ContextType.WHILE ⇒ new WhileBlock()
        case ContextType.DO_WHILE ⇒ new DoWhileBlock()
        case ContextType.FOR_LOOP ⇒
          val (name, fieldScope) = currentBlockScope.asInstanceOf[ForStatementBlockScope].initFields.head
          val context = parseContext.getNextRule[ForStatementOneContext]

          //var def
          val localVarDef = varDef(name, fieldScope, context.varDef())

          //condition
          val condition = getExpression(c ⇒ OrGenerator.generate(c, context.conditionalOrExpression()))
          //assignment
          val assignment = getExpression(c ⇒ AssignGenerator.generate(c, context.assignment()))
          new ForLoop(localVarDef, condition, assignment)
        case ContextType.FOR_LOOP_COLLECTION ⇒
          val (name, fieldScope) = currentBlockScope.asInstanceOf[ForStatementBlockScope].initFields.head
          val context = parseContext.getNextRule[ForStatementTwoContext]

          //var def
          val localVarDef = varDef(name, fieldScope, context.varDef())

          //looped
          val looped = getLooped(context.literalAndCallChain())
          new ForLoopCollection(localVarDef, looped)

        case ContextType.FOR_LOOP_MAP ⇒
          val (key, keyFieldScope) = currentBlockScope.asInstanceOf[ForStatementBlockScope].initFields.head
          val (value, valueFieldScope) = currentBlock.asInstanceOf[ForStatementBlockScope].initFields.last
          val context = parseContext.getNextRule[ForStatementThreeContext]

          //key var def
          val keyVarDef = varDef(key, keyFieldScope, context.varDef().head)
          //value var def
          val valueVarDef = varDef(value, valueFieldScope, context.varDef().last)
          //looped
          val looped = getLooped(context.literalAndCallChain())
          new ForLoopMap(keyVarDef, valueVarDef, looped)

        case ContextType.SYNC ⇒
          new SyncBlock()
        case ContextType.IF ⇒
          new IfBlock()
        case ContextType.TRY ⇒
          if (parseContext.mayNextRule[String].map(r ⇒ r == "TRY").isDefined) {
            new TryBlock()
          } else if (parseContext.mayNextRule[String].map(r ⇒ r == "FINALLY").isDefined) {
            new FinallyBlock()
          } else {
            new CatchBlock()
          }
      }
    }
  }


  override def exitBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    currentBlockScope.parentScope match {
      case blockScope: BlockScope ⇒
        currentBlockScope = blockScope
        currentBlockIndex = blockIndexStack.poll()
        currentStatementIndex = statementIndexStack.poll()

        val parentBlock = blockStack.poll()
        parentBlock.expressions.append(currentBlock)
        currentBlock = parentBlock

        if (parseContext.get() == ContextType.IF && !parseContext.empty()) {
          val context = parseContext.getNextRule[ConditionalOrExpressionContext]
          updateExpression(c ⇒ {
            new IfCondition(OrGenerator.generate(c, context), false)
          })
        }

        if (parseContext.get() == ContextType.TRY) {
          parseContext.mayNextRule[ParameterContext]
            .foreach(p ⇒ {
               updateExpression(_ ⇒ {
                new CatchParameter(LocalVarDef(p.localVariable().IDENTIFIER().getText,
                  DslType.unapply(p.`type`()), None))
              })
            })
        }

      case _: MethodScope ⇒
        blockIndexStack.poll()
        statementIndexStack.poll()

        currentBlockIndex = 0
        currentStatementIndex = 0
    }
  }

  //i = k;
  override def enterAssignExpr(ctx: JvmDslParserParser.AssignExprContext): Unit = {
    updateExpression(expressionContext ⇒ {
      AssignGenerator.generate(expressionContext, ctx.assignment())
    })
  }

  //assert  i == 1
  override def enterAssertExpr(ctx: JvmDslParserParser.AssertExprContext): Unit = {
    updateExpression(expressionContext ⇒ {
      val expression = OrGenerator.generate(expressionContext, ctx.assertStatement().conditionalOrExpression())
      new Assert(expression)
    })
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
    updateExpression(expressionContext ⇒ {
      val expression = OrGenerator.generate(expressionContext, ctx.conditionalOrExpression())
      new WhileCondition(expression)
    })

    parseContext.set(ContextType.WHILE)
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
    updateExpression(expressionContext ⇒ {
      val expression = OrGenerator.generate(expressionContext, ctx.conditionalOrExpression())
      new DoWhileCondition(expression)
    })
    parseContext.reset()
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
      case ForStatementOneContext ⇒ ContextType.FOR_LOOP
      case ForStatementTwoContext ⇒ ContextType.FOR_LOOP_COLLECTION
      case ForStatementThreeContext ⇒ ContextType.FOR_LOOP_MAP
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
    updateExpression(expressionContext ⇒ {
      val expression = OrGenerator.generate( expressionContext, ctx.synchronizedStatement( ).conditionalOrExpression( ) )
      new SyncCondition(expression)
    })
    parseContext.set(ContextType.SYNC)
  }

  def updateExpression(expressionGenerator: ExpressionContext ⇒ Expression): Unit = {
    currentBlock.expressions.append(getExpression(expressionGenerator))
    currentStatementIndex = currentStatementIndex + 1
  }

  def getExpression(expressionGenerator: ExpressionContext ⇒ Expression): Expression = {
    val expressionContext = generator.ExpressionContext(currentStatementIndex, currentBlockScope,
      Option.apply(currentClazzScope).getOrElse(programScope))
    expressionGenerator.apply(expressionContext)
  }

  override def exitSyncExpr(ctx: JvmDslParserParser.SyncExprContext): Unit = {
    parseContext.reset()
  }

  /**
   * throw e;
   * e();
   * return e;
   *
   */
  override def enterThrowOrSideEffectExpr(ctx: JvmDslParserParser.ThrowOrSideEffectExprContext): Unit = {
    updateExpression(c ⇒ {
      if (ctx.throwReturnOrSideEffectStatement().THROW() != null) {
        new Throw(ExpressionGenerator.generate(c, ctx.throwReturnOrSideEffectStatement().expression()))
      } else if (ctx.throwReturnOrSideEffectStatement().RETURN() != null) {
        new Return(ExpressionGenerator.generate(c, ctx.throwReturnOrSideEffectStatement().expression()))
      } else {
        ExpressionGenerator.generate(c, ctx.throwReturnOrSideEffectStatement().expression())
      }
    })
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
    parseContext.set(ctx.ifStatement().conditionalOrExpression().toList)
    parseContext.set(ContextType.IF)

    val context = parseContext.getNextRule[ConditionalOrExpressionContext]
    updateExpression(c ⇒ {
      new IfCondition(OrGenerator.generate(c, context), true)
    })
  }

  override def exitIfExpr(ctx: JvmDslParserParser.IfExprContext): Unit = {
    parseContext.reset()
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
    parseContext.set(ContextType.TRY)
    val tryNode = List("TRY")
    val parameters = ctx.tryStatement( ).catches( ).catcheClause( ).map( _.parameter( ) ).toList
    val finallyNode = Option.apply( ctx.tryStatement( ).FINALLY( ) ).map( _ ⇒ "FINALLY" ).toList
    parseContext.set( tryNode ++ parameters ++ finallyNode )
  }

  override def exitTryExpr(ctx: JvmDslParserParser.TryExprContext): Unit = {
    parseContext.reset( )
  }


  override def enterVarDefExpr(ctx: VarDefExprContext): Unit = {
    updateExpression( c ⇒ {
      VariableGenerator.generate( c, ctx.varDef( ).expression( ) )
    } )
  }

  //lambda expression
  override def enterParamsLambdaExpr(ctx: ParamsLambdaExprContext): Unit = {
    lambdaContext = true
  }


}
