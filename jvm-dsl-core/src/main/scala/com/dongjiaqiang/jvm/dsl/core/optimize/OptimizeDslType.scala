package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.{Assign, LocalVarDef, VarRef}
import com.dongjiaqiang.jvm.dsl.api.expression.block._
import com.dongjiaqiang.jvm.dsl.api.expression.call._
import com.dongjiaqiang.jvm.dsl.api.expression.literal.{BoolLiteral, CharLiteral, ClazzLiteral, DoubleLiteral, FloatLiteral, IntLiteral, ListLiteral, LongLiteral, MapLiteral, OptionLiteral, SetLiteral, StringLiteral, TupleLiteral}
import com.dongjiaqiang.jvm.dsl.api.expression.unary.Cast
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}
import com.dongjiaqiang.jvm.dsl.api.scope.{MethodScope, ProgramScope}
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionReviser

import java.util.{LinkedList ⇒ Stack}
import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

case class LambdaInputTypes(types: Map[String,DslType],lambdaInputTypes: LambdaInputTypes){


  def resolvedType(name:String):DslType={
      types.get(name) match {
        case Some(dslType)⇒ dslType
        case _⇒ if(lambdaInputTypes==null){
            UnResolvedType
        }else{
            lambdaInputTypes.resolvedType(name)
        }
      }
  }
}

/**
 * determines occurrences of UnResolvedType in expressions
 *
 * for example:
 *
 * <pre><code>
 * program{
 *
 *    def method1(Int=>Int lambda,Int p)=Int{
 *        return lambda.apply(p);
 *    }
 *
 *    def method(Array[Int] array)=Unit{
 *       //LocalVarDef Expression
 *       (Int,Long)=>Long lambda = (i,j)=>{ return i+j;}; //i and j is UnResolvedType and should determine i to IntType and j to LongType
 *
 *       //Assign Expression
 *       Long=>Long lambda1 = i=>{ return i;}; //i is UnResolvedType and should determine i to LongType
 *
 *       //method call
 *       method1(i=> { return i+10;},12); //i is UnResolvedType and should determine i to IntType
 *
 *       //var call
 *       array.map(i=> { return i+10;}); //i is UnResolvedType and should determine i to IntType
 *
 *    }
 * }
 * <pre><code>
 */
class OptimizeDslType(val programScope: ProgramScope) extends ExpressionReviser {


  private val stack: Stack[LambdaInputTypes] = new Stack( )

  private var methodReturnTypes: Map[Int, DslType] = _

  private val multiOptimize: MultiOptimize = new MultiOptimize( programScope, this )

  def push(types: Map[String, DslType]): Unit = {
    if (stack.isEmpty) {
      stack.push( LambdaInputTypes( types, null ) )
    } else {
      stack.push( LambdaInputTypes( types, stack.poll( ) ) )
    }
  }

  def pop(): Unit = {
    stack.poll( )
  }

  private def resolveSymbolType(symbol: String, index: Int): DslType = {
    val dslType = if (stack.isEmpty) {
      UnResolvedType
    } else {
      stack.peek( ).resolvedType( symbol )
    }
    dslType match {
      case UnResolvedType ⇒
        if (methodReturnTypes != null) {
          methodReturnTypes.get( index ) match {
            case Some( inputType ) ⇒ inputType
            case _ ⇒ UnResolvedType
          }
        } else {
          UnResolvedType
        }
      case _ ⇒ dslType
    }
  }

  override def setCurrentMethodScope(currentMethodScope: MethodScope): Unit = {
    methodReturnTypes = null
    currentMethodScope.returnType match {
      case LambdaType( Some( inputType ), _ ) ⇒
        inputType match {
          case TupleType( parameterTypes ) ⇒
            methodReturnTypes = parameterTypes.zipWithIndex.map( kv ⇒ (kv._2, kv._1) ).toMap
          case _ ⇒
            methodReturnTypes = Map( 0 → inputType )
        }
      case _ ⇒
    }
    super.setCurrentMethodScope( currentMethodScope )
  }

  override def visit(varRef: VarRef, visitor: ExpressionVisitor[Expression]): Expression = {
    varRef.fieldScope match {
      case Some( fieldScope ) ⇒
        fieldScope.dslType match {
          case UnResolvedType ⇒
            val dslType = resolveSymbolType( fieldScope.symbolName, Math.abs( fieldScope.outerScopeIndex ) )
            if (dslType != UnResolvedType) {
              super.visit( Cast(VarRef( varRef.refs, varRef.arrayRefIndexExpressions, Some( fieldScope.resolveDslType( dslType ) ) ) ,dslType,flag = 1))
            } else {
              super.visit( varRef, visitor )
            }
          case _ ⇒
            super.visit( varRef, visitor )
        }
      case _ ⇒
        super.visit( varRef, visitor )
    }
  }


  private def visit(calleeType: DslType, callee: ValueExpression): Boolean = {
    callee match {
      case Lambda( inputs, _ ) ⇒
        calleeType match {
          case LambdaType( mayInputType, _ ) ⇒
            mayInputType match {
              case Some( dslType ) ⇒
                dslType match {
                  case TupleType( parameterTypes ) ⇒
                    if (stack.isEmpty) {
                      stack.push( LambdaInputTypes( inputs.zip( parameterTypes ).toMap, null ) )
                    } else {
                      stack.push( LambdaInputTypes( inputs.zip( parameterTypes ).toMap, stack.poll( ) ) )
                    }
                  case _ ⇒
                    if (stack.isEmpty) {
                      stack.push( LambdaInputTypes( Array( (inputs.head, dslType) ).toMap, null ) )
                    } else {
                      stack.push( LambdaInputTypes( Array( (inputs.head, dslType) ).toMap, stack.poll( ) ) )
                    }
                }
                true
            }
          case _ ⇒ false
        }
      case _ ⇒ false
    }
  }

  override def visit(localVarDef: LocalVarDef, visitor: ExpressionVisitor[Expression]): Expression = {
    var input = false
    localVarDef.assigned match {
      case Some( valueExpression ) ⇒
        input = visit( localVarDef.dslType, valueExpression )
      case None ⇒
    }
    val expression = super.visit( localVarDef, visitor )
    if (input) {
      stack.poll( )
    }
    expression
  }

  override def visit(assign: Assign, visitor: ExpressionVisitor[Expression]): Expression = {
    val calleeType = assign.varRef.getValueType( programScope )
    val input = visit( calleeType, assign.assigned )
    val expression = super.visit( assign, visitor )
    if (input) {
      stack.poll( )
    }
    expression
  }

  override def visit(methodCall: MethodCall, visitor: ExpressionVisitor[Expression]): Expression = {
    val newParams = ArrayBuffer[ValueExpression]( )
    methodCall.methodScope match {
      case Some( methodScope ) ⇒
        val zips = methodScope.params.toList.zip( methodCall.params )
        zips.foreach {
          case ((_, fieldScope), param) ⇒
            val input = visit( fieldScope.dslType, param )
            newParams.append( visitor.visit( param ).asInstanceOf[ValueExpression] )
            if (input) {
              stack.poll( )
            }
        }
        MethodCall( methodCall.methodScope, methodCall.name, newParams.toArray )
      case _ ⇒
        super.visit( methodCall, visitor )
    }
  }

  override def visit(varCall: VarCall, visitor: ExpressionVisitor[Expression]): Expression = {
    val newVarRef = visitor.visit( varCall.varRef ).asInstanceOf[ValueExpression]
    val newParams = multiOptimize.visit( newVarRef.getValueType( programScope ), newVarRef, varCall.name, varCall.params )
    VarCall( newVarRef, varCall.name, newParams.get )
  }

  override def visit(literalCall: LiteralCall, visitor: ExpressionVisitor[Expression]): Expression = {
    val newLiteral = visitor.visit( literalCall.literal ).asInstanceOf[ValueExpression]
    val newParams = multiOptimize.visit( newLiteral.getValueType( programScope ), literalCall.literal, literalCall.name, literalCall.params )
    newLiteral match {
      case setLiteral: SetLiteral⇒
        literalCall.name match {
          case MethodVisitor.TO_SEQ_SET⇒
              setLiteral.asSeq()
          case MethodVisitor.TO_SORTED_SET⇒
              newParams match {
                case Some(ps)⇒
                    if(ps.isEmpty){
                      setLiteral.asSort()
                    }else {
                      setLiteral.asSort( ps.head.asInstanceOf[Lambda] )
                    }
                case None⇒
                    setLiteral.asSort()
              }
          case _⇒
            new LiteralCall( newLiteral, literalCall.name, newParams.get )
        }
      case _⇒
        new LiteralCall( newLiteral, literalCall.name, newParams.get )
    }
  }

  override def visit(staticCall: StaticCall, visitor: ExpressionVisitor[Expression]): Expression = {
    staticCall.`type` match {
      case clazzType: ClazzType ⇒
        programScope.importManager.resolve( clazzType ) match {
          case Some( importClazzType ) ⇒ importClazzType.staticMethods.get( staticCall.name ) match {
            case Some( importClazzMethod ) ⇒
              importClazzMethod.params.zip( staticCall.params ).foreach {
                case (d, p) ⇒
                  val input = visit( d, p )
                  visitor.visit( p )
                  if (input) {
                    stack.poll( )
                  }
              }
              super.visit( staticCall, visitor )
            case _ ⇒ super.visit( staticCall, visitor )
          }
          case _ ⇒ super.visit( staticCall, visitor )
        }
      case _ ⇒
        super.visit( staticCall, visitor )
    }
  }

  private def visit(calleeType: DslType, part: Part, visitor: ExpressionVisitor[Expression], programScope: ProgramScope): (DslType, Part) = {
    part match {
      case varRef: VarRef ⇒
        val newArrayRefIndexExpressions = ExpressionReviser.revise[ValueExpression, ValueExpression]( varRef.arrayRefIndexExpressions, visitor ).getOrElse( varRef.arrayRefIndexExpressions )
        val refsWithIndex = varRef.refs.zipWithIndex
        calleeType match {
          case definitionClazzType: DefinitionClazzType ⇒
            val newCalleeType = VarRef.getDslType( refsWithIndex, newArrayRefIndexExpressions, definitionClazzType, programScope )
            (newCalleeType, VarRef( varRef.refs, varRef.arrayRefIndexExpressions, varRef.fieldScope ))
          case clazzType: ClazzType ⇒
            val newCalleeType = VarRef.getDslType( refsWithIndex, newArrayRefIndexExpressions, clazzType, programScope )
            (newCalleeType, VarRef( varRef.refs, varRef.arrayRefIndexExpressions, varRef.fieldScope ))
        }
      case methodCall: MethodCall ⇒
        val newMethodCall = multiOptimize.visit( calleeType, WrapValueTypeExpression( calleeType ), methodCall.name, methodCall.params ) match {
                case Some( newParams ) ⇒
                  MethodCall( methodCall.methodScope, methodCall.name, newParams )
                case None ⇒
                  throw ExpressionParseException( "" )
              }
        val newCalleeType = programScope.callType( WrapValueTypeExpression( calleeType ), newMethodCall.name, newMethodCall.params )
        (newCalleeType,newMethodCall)
    }
  }

  @tailrec
  private def visit(calleeType: DslType, tails: List[Part],newPartDslTypes:ArrayBuffer[DslType], newParts: ArrayBuffer[Part], visitor: ExpressionVisitor[Expression], programScope: ProgramScope): Unit = {
    tails match {
      case Nil ⇒
      case head :: tail ⇒
        val (newCalleeType, newPart) = visit( calleeType, head, visitor, programScope )
        newPartDslTypes.append(newCalleeType)
        newParts.append( newPart )
        visit( newCalleeType, tail, newPartDslTypes,newParts, visitor, programScope )
      case head :: Nil ⇒
        val (newCalleeType, newPart) = visit( calleeType, head, visitor, programScope )
        newPartDslTypes.append(newCalleeType)
        newParts.append( newPart )
    }
  }

  override def visit(funcCallChain: FuncCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val funcCall = funcCallChain.head match {
      case literalCall: LiteralCall⇒
        val newLiteralCall = visit(literalCall,visitor)
        newLiteralCall.asInstanceOf[ValueExpression]
      case varCall: VarCall ⇒
        visit( varCall, visitor ).asInstanceOf[VarCall]
      case methodCall: MethodCall ⇒
        visit( methodCall, visitor ).asInstanceOf[MethodCall]
      case staticCall: StaticCall ⇒
        visit( staticCall, visitor ).asInstanceOf[StaticCall]
    }
    val newParts = ArrayBuffer[Part]( )
    val newPartDslTypes = ArrayBuffer[DslType]()
    visit( funcCall.getValueType( programScope ), funcCallChain.tails,newPartDslTypes, newParts, visitor, programScope )

    funcCall match {
      case setLiteral: SetLiteral⇒
          new SetLiteralCallChain(setLiteral,newParts.toList,newPartDslTypes.toList)
      case _⇒
        FuncCallChain( funcCall.asInstanceOf[FuncCall], newParts.toList,newPartDslTypes.toList)
    }
  }


  private def visit[T<:ValueExpression](expression:T,tails:List[Part],visitor:ExpressionVisitor[Expression],programScope: ProgramScope):(T,List[Part],List[DslType])={
    val newParts = ArrayBuffer[Part]( )
    val newPartDslTypes = ArrayBuffer[DslType]( )
    visit( expression.getValueType( programScope ),tails, newPartDslTypes, newParts, visitor, programScope )
    (visitor.visit(expression).asInstanceOf[T],newParts.toList,newPartDslTypes.toList)
  }

  override def visit(literalCallChain: BoolLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression,newParts,newDslTypes) = visit[BoolLiteral](literalCallChain.head,literalCallChain.tails,visitor, programScope)
    new BoolLiteralCallChain( expression, newParts,newDslTypes )
  }

  override def visit(literalCallChain: IntLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[IntLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    new IntLiteralCallChain( expression, newParts, newDslTypes )
  }

  override def visit(literalCallChain: LongLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[LongLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    new LongLiteralCallChain( expression, newParts, newDslTypes )
  }

  override def visit(literalCallChain: DoubleLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[DoubleLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    new DoubleLiteralCallChain( expression, newParts, newDslTypes )
  }

  override def visit(literalCallChain: FloatLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[FloatLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    new FloatLiteralCallChain( expression, newParts, newDslTypes )
  }

  override def visit(literalCallChain: StringLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[StringLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    new StringLiteralCallChain( expression, newParts, newDslTypes )
  }

  override def visit(literalCallChain: CharLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[CharLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    new CharLiteralCallChain( expression, newParts, newDslTypes )
  }

  override def visit(literalCallChain: ListLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[ListLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    new ListLiteralCallChain( expression, newParts, newDslTypes )
  }

  override def visit(literalCallChain: OptionLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[OptionLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    new OptionLiteralCallChain( expression, newParts, newDslTypes )
  }

  override def visit(literalCallChain: TupleLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[TupleLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    new TupleLiteralCallChain( expression, newParts, newDslTypes )
  }

  override def visit(literalCallChain: MapLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[MapLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    new MapLiteralCallChain( expression, newParts, newDslTypes )
  }

  override def visit(literalCallChain: SetLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[SetLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    newParts.head match {
      case MethodCall(_,MethodVisitor.TO_SEQ_SET,_)⇒
          val newExpression = expression.asSeq()
          if(newParts.tail.isEmpty){
              newExpression
          }else{
              new SetLiteralCallChain(newExpression,newParts.tail,newDslTypes.tail)
          }
      case MethodCall(_,MethodVisitor.TO_SORTED_SET,params)⇒
          val newExpression = if(params.isEmpty){
              expression.asSort()
          }else{
              expression.asSort(params.head.asInstanceOf[Lambda])
          }
          if(newParts.tail.isEmpty){
              newExpression
          }else{
              new SetLiteralCallChain(newExpression,newParts.tail,newDslTypes.tail)
          }
      case _⇒
        new SetLiteralCallChain( expression, newParts, newDslTypes )
    }
  }

  override def visit(literalCallChain: ClazzLiteralCallChain, visitor: ExpressionVisitor[Expression]): Expression = {
    val (expression, newParts, newDslTypes) = visit[ClazzLiteral]( literalCallChain.head, literalCallChain.tails, visitor, programScope )
    new ClazzLiteralCallChain( expression, newParts, newDslTypes )
  }

  override def visit(matchCase: MatchCase, visitor: ExpressionVisitor[Expression]): Expression = {

    def matchedListType(calleeType: DslType): DslType = {
      calleeType match {
        case listType: ListType ⇒ listType.carryDslType
        case AnyType ⇒ AnyType
        case _ ⇒ throw ExpressionParseException( "" )
      }
    }

    def matchedHead(calleeType: DslType, matchHead: MatchHead): (Expression, Array[(String, DslType)]) = {
      val carriedDslType = matchedListType( calleeType )

      val head = ArrayBuffer[Either[Expression, String]]( )
      val types = ArrayBuffer[(String, DslType)]( )
      matchHead.head.foreach {
        case Left( expression ) ⇒
          val (newCase, newTypes) = expression match {
            case matchList: MatchList ⇒
              matchedList( carriedDslType, matchList )
            case matchTuple: MatchTuple ⇒
              matchedTuple( carriedDslType, matchTuple )
            case _ ⇒
              (expression, Array[(String,DslType)]( ))
          }
          types.appendAll( newTypes )
          head.append( Left( newCase ) )
        case Right( varName ) ⇒
          types.append( (varName, carriedDslType) )
          head.append( Right( varName ) )
      }

      val tail = matchHead.tail match {
        case Left( expression ) ⇒
          val (newCase, newTypes) = expression match {
            case matchList: MatchList ⇒
              matchedList( carriedDslType, matchList )
            case _ ⇒
              (expression, Array[(String,DslType)]( ))
          }
          types.appendAll( newTypes )
          Left( newCase )
        case Right( varName ) ⇒
          types.append( (varName, ListType( calleeType )) )
          Right( varName )

      }
      (MatchHead( head.toArray, tail ), types.toArray)
    }

    def matchedMonad(carriedDslType: DslType, matchClass: MatchClass): (Expression, Array[(String, DslType)]) = {
      val expression = matchClass.expressions.head
      val types = ArrayBuffer[(String, DslType)]( )
      val newExpression = expression match {
        case Left( exr ) ⇒
          val (newExpr, newTypes) = exr match {
            case matchTuple: MatchTuple ⇒
              matchedTuple( carriedDslType, matchTuple )
            case matchHead: MatchHead ⇒
              matchedHead( carriedDslType, matchHead )
            case matchList: MatchList ⇒
              matchedList( carriedDslType, matchList )
            case matchClass: MatchClass ⇒
              matchedClass( carriedDslType, matchClass )
            case _ ⇒
              (exr, Array[(String,DslType)]( ))
          }
          types.appendAll( newTypes )
          Left( newExpr )
        case Right( varName ) ⇒
          types.append( (varName, carriedDslType) )
          Right( varName )
      }
      (MatchClass( matchClass.dslType, Array( newExpression ) ), types.toArray)
    }

    def matchedDefinitionClazz(definitionClazzType: DefinitionClazzType, matchClass: MatchClass): (Expression, Array[(String, DslType)]) = {
      val fields = definitionClazzType.clazzScope.fields.toList.map {
        case (name, scope) ⇒ (name, scope.dslType)
      }

      val expressions = ArrayBuffer[Either[Expression, String]]( )
      val types = ArrayBuffer[(String, DslType)]( )

      matchClass.expressions.zip( fields ).foreach {
        case (expr, (_, dslType)) ⇒
          expr match {
            case Left( expression ) ⇒
              val (newExpression, newTypes) = expression match {
                case matchList: MatchList ⇒
                  matchedList( dslType, matchList )
                case matchHead: MatchHead ⇒
                  matchedHead( dslType, matchHead )
                case matchClass: MatchClass ⇒
                  matchedClass( dslType, matchClass )
                case matchTuple: MatchTuple ⇒
                  matchedTuple( dslType, matchTuple )
                case _ ⇒
                  (expression, Array[(String,DslType)]( ))
              }
              expressions.append( Left( newExpression ) )
              types.appendAll( newTypes )
            case Right( varName ) ⇒
              expressions.append( Right( varName ) )
              types.append( (varName, dslType) )
          }
      }
      (MatchClass( matchClass.dslType, expressions.toArray ), types.toArray)
    }

    def matchedSome(someType: SomeType, matchClass: MatchClass): (Expression, Array[(String, DslType)]) = {
      matchedMonad( someType.carryDslType, matchClass )
    }

    def matchedLeft(left: LeftType, matchClass: MatchClass): (Expression, Array[(String, DslType)]) = {
      matchedMonad( left.carryDslType, matchClass )
    }

    def matchedRight(right: RightType, matchClass: MatchClass): (Expression, Array[(String, DslType)]) = {
      matchedMonad( right.carryDslType, matchClass )
    }

    def matchedSuccess(success: SuccessType, matchClass: MatchClass): (Expression, Array[(String, DslType)]) = {
      matchedMonad( success.carryDslType, matchClass )
    }


    def matchedClass(calleeType: DslType, matchClass: MatchClass): (Expression, Array[(String, DslType)]) = {
      matchClass.dslType match {
        case _: SomeType ⇒
          calleeType match {
            case optionType:OptionType ⇒
              matchedSome( SomeType(optionType.carryDslType), matchClass )
            case AnyType ⇒
              matchedSome( SomeType( AnyType ), matchClass )
          }
        case _: LeftType ⇒
          calleeType match {
            case eitherType: EitherType ⇒
              matchedLeft( LeftType(eitherType.leftParameterType), matchClass )
            case AnyType ⇒
              matchedLeft( LeftType( AnyType ), matchClass )
          }
        case _: RightType ⇒
          calleeType match {
            case eitherType: EitherType ⇒
              matchedRight( RightType(eitherType.leftParameterType,eitherType.rightParameterType), matchClass )
            case AnyType ⇒
              matchedRight( RightType( AnyType,AnyType ), matchClass )
          }
        case _: SuccessType ⇒
          calleeType match {
            case tryType: TryType ⇒
              matchedSuccess( SuccessType(tryType), matchClass )
            case AnyType ⇒
              matchedSuccess( SuccessType( AnyType ), matchClass )
          }
        case _: NoneType.type ⇒
          (matchClass, Array( ))
        case definitionClazz: DefinitionClazzType ⇒
          matchedDefinitionClazz( definitionClazz, matchClass )
      }
    }

    def matchedTuple(calleeType: DslType, matchTuple: MatchTuple): (Expression, Array[(String, DslType)]) = {
      val tupleType = calleeType match {
        case tupleType: TupleType ⇒
          tupleType
        case AnyType ⇒ TupleType( matchTuple.expressions.indices.map( _ ⇒ AnyType ).toArray )
      }
      val expressions = ArrayBuffer[Either[Expression, String]]( )
      val types = ArrayBuffer[(String, DslType)]( )

      tupleType.parameterTypes.zip( matchTuple.expressions ).foreach {
        case (d, e) ⇒
          e match {
            case Left( expression ) ⇒
              val (newCase, newTypes) = expression match {
                case matchList: MatchList ⇒
                  matchedList( d, matchList )
                case matchHead: MatchHead ⇒
                  matchedHead( d, matchHead )
                case matchTuple: MatchTuple ⇒
                  matchedTuple( d, matchTuple )
                case matchClass: MatchClass ⇒
                  matchedClass( d, matchClass )
                case _ ⇒
                  (expression, Array[(String,DslType)]( ))
              }
              types.appendAll( newTypes )
              expressions.append( Left( newCase ) )
            case Right( varName ) ⇒
              types.append( (varName, d) )
              expressions.append( Right( varName ) )
          }
      }
      (MatchTuple( expressions.toArray ), types.toArray)
    }

    def matchedList(calleeType: DslType, matchList: MatchList): (Expression, Array[(String, DslType)]) = {
      val carriedDslType = matchedListType( calleeType )

      val types = ArrayBuffer[(String, DslType)]( )
      val expressions = ArrayBuffer[Either[Expression, String]]( )
      matchList.expressions.foreach {
        case Left( expression ) ⇒
          val (newExpression, newTypes) = expression match {
            case matchTuple: MatchTuple ⇒
              matchedTuple( carriedDslType, matchTuple )
            case matchList: MatchList ⇒
              matchedList( carriedDslType, matchList )
            case matchHead: MatchHead ⇒
              matchedHead( carriedDslType, matchHead )
            case matchClass: MatchClass ⇒
              matchedClass( carriedDslType, matchClass )
            case _ ⇒
              (expression, Array[(String,DslType)]( ))
          }
          types.appendAll( newTypes )
          expressions.append( Left( newExpression ) )
        case Right( varName ) ⇒
          types.append( (varName, carriedDslType) )
          expressions.append( Right( varName ) )
      }
      (MatchList( expressions.toArray ), types.toArray)
    }

    def visitBlock(newCases: ArrayBuffer[(Expression, Block)], newCase: Expression, block: Block, types: Array[(String, DslType)]): Unit = {
      var input = false
      if (types.nonEmpty) {
        push( types.toMap )
        input = true
      }
      newCases.append( (newCase, visit( block, visitor )) )
      if (input) {
        stack.poll( )
      }
    }

    val newVarRef = visit( matchCase.matched, visitor ).asInstanceOf[VarRef]
    val newCases = ArrayBuffer[(Expression, Block)]( )
    matchCase.cases.foreach {
      case (condition, block) ⇒
        condition match {
          case matchType: MatchType ⇒
            newCases.append( (matchType, visit( block, visitor )) )
          case matchHead: MatchHead ⇒
            val (newMatchHead, types) = matchedHead( newVarRef.getValueType( programScope ), matchHead )
            visitBlock( newCases, newMatchHead, block, types )
          case matchList: MatchList ⇒
            val (newMatchList, types) = matchedList( newVarRef.getValueType( programScope ), matchList )
            visitBlock( newCases, newMatchList, block, types )
          case matchTuple: MatchTuple ⇒
            val (newMatchTuple, types) = matchedTuple( newVarRef.getValueType( programScope ), matchTuple )
            visitBlock( newCases, newMatchTuple, block, types )
          case matchClass: MatchClass ⇒
            val (newMatchClass, types) = matchedClass( newVarRef.getValueType( programScope ), matchClass )
            visitBlock( newCases, newMatchClass, block, types )
        }
    }
    MatchCase( newVarRef, newCases.toArray, matchCase.default.map( d ⇒ visit( d, visitor ) ) )
  }
}
