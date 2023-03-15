package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.VarRef
import com.dongjiaqiang.jvm.dsl.api.expression.block._
import com.dongjiaqiang.jvm.dsl.api.expression.literal.{BaseLiteral, IntLiteral, ListLiteral}
import com.dongjiaqiang.jvm.dsl.api.expression.unary.Cast
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.block.BlockExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api
import com.dongjiaqiang.jvm.dsl.java.api.exception.JavaTranslatorException
import com.dongjiaqiang.jvm.dsl.java.api.expression.{JavaLocalVarDef, JavaTranslatorContext, JavaVarCall, JavaVarRef}

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.ArrayBuffer

trait MatchCaseExpressionJavaTranslator extends BlockExpressionVisitor[String] {

  val javaTranslatorContext: JavaTranslatorContext

  /**
   *
   * translate match type condition into java code
   * <pre><code>
   *    x.b match{             ===>        if(x.b instanceof Long){
   *        case d:Long=>{                      long d = (long)(x.b);
   *          return d+1;                       return d+1;
   *        }                              }
   *    }
   * <pre><code>
   */
  private def translateMatchType(matched: VarRef,
                                 matchCondition: MatchType,
                                 body: Block,programScope:ProgramScope): String = {
    val returnType = matched.getValueType(programScope)
    if (returnType.isInstanceOf[AnyType.type] || returnType.isInstanceOf[UnResolvedType.type] || returnType == matchCondition.dslType) {
      body.expressions.insert( 0, JavaLocalVarDef( matchCondition.name, returnType,
        Some( Cast( JavaVarRef( matched.refs,returnType ), matchCondition.dslType ) ) ) )
      s"""
         |${matched.refs.mkString( "." )} instanceof ${matchCondition.dslType}
         |""".stripMargin
    } else {
      throw JavaTranslatorException( s"match case about match type defined error because matched type is $returnType match type is ${matchCondition.dslType}" )
    }
  }

  /**
   *translate match try condition into java code
   * <pre><code>
   *     x.b match{                    ===>       if(x.b.failure()){
   *        case Success(a)=> {                         return false;
   *            return a;                         }
   *        }                                     Int  a = x.b.get();
   *                                              return a;
   *
   *        case Failure(e)=>{         ===>       if(x.b.success()){
   *            throw e;                                 return false;
   *        }                                     }
   *     }                                        Throwable e = x.b.getException();
   *                                              throw e;
   * <pre><code>
   */
  private def translateTryMatchCondition(matched: String,
                                         tryType: TryType,
                                         expression: Expression,
                                         body: Block,
                                         conditions: ArrayBuffer[String],
                                         visitor: ExpressionVisitor[String],
                                         counter: AtomicInteger): Unit = {
    expression match {
      case MatchClass( dslType, expressions ) ⇒
        dslType match {
          case _: SuccessType ⇒
            conditions.append(
              s"""
                 |if ($matched.failure()){
                 |     return false;
                 |}
                 |""".stripMargin )
            translateGet(matched,tryType.parameterType,expressions.head,body,conditions,visitor,counter)
//            expressions.head match {
//              case Left( expression ) ⇒
//                conditions.appendAll( translateMatchCondition( s"$matched.get()", tryType.parameterType, expression, body, visitor, counter ) )
//              case Right( name ) ⇒
//                body.expressions.insert( 0, JavaLocalVarDef( name, tryType.parameterType,
//                  Some( JavaVarCall( List( matched ), "get", Array( ) ) ) ) )
//            }
          case FailureType ⇒
            conditions.append(
              s"""
                 |if ($matched.success()){
                 |     return false;
                 |}
                 |""".stripMargin )
            expressions.head match {
              case Right( name ) ⇒
                body.expressions.insert( 0, JavaLocalVarDef( name, ClazzType( "Throwable", Array( ) ),
                  Some( JavaVarCall( List( s"$matched" ), "getException", Array( ),UnResolvedType ) ) ) )
            }
        }
    }
  }


  private def translateGet(matched: String,
                           parameterType: DslType,
                           expression: Either[Expression,String],
                           body: Block,
                           conditions: ArrayBuffer[String],
                           visitor: ExpressionVisitor[String],
                           counter: AtomicInteger): Unit = {
    expression match {
      case Left( expression ) ⇒
        conditions.appendAll( translateMatchCondition(
          s"$matched.get()", parameterType, expression, body, visitor, counter ) )
      case Right( name ) ⇒
        body.expressions.insert( 0, JavaLocalVarDef( name, parameterType,
          Some( JavaVarCall( List( matched ), "get", Array( ) ,UnResolvedType) ) ) )
    }
  }
  /**
   * translate match option condition into java code
   * <pre><code>
   * x.b match{                    ===>       if(!x.b.isPresent()){
   *    case Some(a)=> {                         return false;
   *        return a;                         }
   *    }                                     Int  a = x.b.get();
   *                                          return a;
   *
   *    case None=>{         ===>             if(x.b.isPresent()){
   *        return 10;                                return false;
   *    }                                     }
   * }                                        return 10;
   * <pre><code>
   */
  private def translateOptionMatchCondition(matched: String,
                                            optionType: OptionType,
                                            expression: Expression,
                                            body: Block,
                                            conditions: ArrayBuffer[String],
                                            visitor: ExpressionVisitor[String],
                                            counter: AtomicInteger): Unit = {
    expression match {
      case MatchClass( dslType, expressions ) ⇒
        dslType match {
          case _: SomeType ⇒
            conditions.append(
              s"""
                 |if (!$matched.isPresent()){
                 |     return false;
                 |}
                 |""".stripMargin )
            translateGet(matched,optionType.parameterType,expressions.head,body,conditions,visitor,counter)
//            expressions.head match {
//              case Left( expression ) ⇒
//                conditions.appendAll( translateMatchCondition(
//                  s"$matched.get()", optionType.parameterType, expression, body, visitor, counter ) )
//              case Right( name ) ⇒
//                body.expressions.insert( 0, JavaLocalVarDef( name, optionType.parameterType,
//                  Some( JavaVarCall( List( matched ), "get", Array( ) ) ) ) )
//            }
          case NoneType ⇒
            conditions.append(
              s"""
                 |if ($matched.isPresent()){
                 |     return false;
                 |}
                 |""".stripMargin )
        }
    }
  }

  /**
   * translate match option condition into java code
   * <pre><code>
   *    x.b match{                    ===>       if(x.b.isRight()){
   *      case Left(a)=> {                         return false;
   *          return a;                         }
   *      }                                     Int  a = x.b.left();
   *                                            return a;
   *
   *      case Right(a)=>{         ===>         if(x.b.isLeft())){
   *          return a;                                return false;
   *      }                                     }
   *                                            Int  a = x.b.right();
   * }                                          return a;
   * <pre><code>
   */
  private def translateEitherMatchCondition(matched: String,
                                            eitherType: EitherType,
                                            expression: Expression,
                                            body: Block,
                                            conditions: ArrayBuffer[String],
                                            visitor: ExpressionVisitor[String],
                                            counter: AtomicInteger): Unit = {
    expression match {
      case MatchClass( dslType, expressions ) ⇒
        dslType match {
          case _: LeftType ⇒
            conditions.append(
              s"""
                 |if ($matched.isRight()){
                 |     return false;
                 |}
                 |""".stripMargin )
            expressions.head match {
              case Left( expression ) ⇒
                conditions.appendAll( translateMatchCondition( s"${matched}.left()", eitherType.leftParameterType, expression, body, visitor, counter ) )
              case Right( name ) ⇒
                body.expressions.insert( 0, JavaLocalVarDef( name, eitherType.leftParameterType,
                  Some( JavaVarCall( List( matched ), "left", Array( ) ,UnResolvedType) ) ) )
            }
          case _: RightType ⇒
            conditions.append(
              s"""
                 |if (${matched}.isLeft()){
                 |     return false;
                 |}
                 |""".stripMargin )
            expressions.head match {
              case Left( expression ) ⇒
                conditions.appendAll( translateMatchCondition( s"${matched}.right()", eitherType.rightParameterType, expression, body, visitor, counter ) )
              case Right( name ) ⇒
                body.expressions.insert( 0, JavaLocalVarDef( name, eitherType.rightParameterType,
                  Some( JavaVarCall( List( matched ), "right", Array( ),UnResolvedType ) ) ) )
            }
        }
    }
  }

  private def translateClazzMatchCondition(matched: String,
                                           definitionClazzType: DefinitionClazzType,
                                           condition: Expression,
                                           body: Block,
                                           conditions: ArrayBuffer[String],
                                           visitor: ExpressionVisitor[String],
                                           counter: AtomicInteger): Unit = {

    condition match {
      case MatchClass( dslType, expressions ) ⇒
        dslType match {
          case _: DefinitionClazzType ⇒
            val fieldTypes = definitionClazzType.clazzScope.fields.toList
              .map( v ⇒ (v._1, v._2.dslType) )
            fieldTypes.zip( expressions ).foreach {
              case ((fieldName, valueType), expr) ⇒
                expr match {
                  case Left( expression ) ⇒
                    conditions.appendAll( translateMatchCondition( s"$matched.$fieldName", valueType, expression, body, visitor, counter ) )
                  case Right( name ) ⇒
                    body.expressions.insert( 0, JavaLocalVarDef( name, valueType,
                      Some( JavaVarRef( List( matched, fieldName ),valueType ) ) ) )
                }
            }
        }
    }
  }

  private def testCode(matchedObj: String, matchObj: String, matchedType: String, testOp: String, counter: AtomicInteger): (String, String, String, String) = {
    val varTest =
      s"""
         |if(!($matchedObj $testOp $matchObj){
         |   return false;
         |}
         |""".stripMargin
    val varName = s"__sys_var_${counter.incrementAndGet( )}"
    val varDef = s"$matchedType $varName;"
    val varAssign =
      s"""
         | $varDef = (($matchObj)($matchedObj));
         |""".stripMargin
    (varTest, varDef, varAssign, varName)
  }

  private def listTestCode(matchedObj: String, counter: AtomicInteger): (String, String, String, String) = {
    testCode( matchedObj, "java.util.List", "java.util.List", "instanceof", counter )
  }

  private def translateAnyMatchCondition(matched: String,
                                         matchedCondition: Expression,
                                         body: Block,
                                         conditions: ArrayBuffer[String],
                                         visitor: ExpressionVisitor[String],
                                         counter: AtomicInteger): Unit = {

    matchedCondition match {
      case baseLiteral: BaseLiteral ⇒
        translateLiteralMatchCondition( matched, baseLiteral, conditions, visitor )

      case matchTuple: MatchTuple ⇒

        val tupleClazzName = s"com.dongjiaqiang.jvm.dsl.java.core.tuple.Tuple${matchTuple.expressions.length}"
        val (varTest, varDef, varAssign, varName) = testCode( matched, tupleClazzName, tupleClazzName, "instanceof", counter )
        conditions.append( varTest, varDef, varAssign )

        translateTupleMatchCondition( varName, new TupleType( matchTuple.expressions.indices.map( _ ⇒ AnyType ).toArray ), matchTuple, body, conditions, visitor, counter )

      case matchClass: MatchClass ⇒

        val (varTest, varDef, varAssign, varName) = matchClass.dslType match {
          case _: SomeType ⇒
            testCode( matched, "java.util.Optional.empty()", "java.util.Optional", "!=", counter )
          case _: LeftType ⇒
            testCode( matched, classOf[com.dongjiaqiang.jvm.dsl.java.api.extend.Left[_, _]].getCanonicalName,
              classOf[com.dongjiaqiang.jvm.dsl.java.api.extend.Left[_, _]].getCanonicalName, "instanceof", counter )
          case _: RightType ⇒
            testCode( matched, classOf[com.dongjiaqiang.jvm.dsl.java.api.extend.Right[_, _]].getCanonicalName,
              classOf[com.dongjiaqiang.jvm.dsl.java.api.extend.Right[_, _]].getCanonicalName, "instanceof", counter )
          case _: SuccessType ⇒
            testCode( matched, classOf[com.dongjiaqiang.jvm.dsl.java.api.extend.Success[_]].getCanonicalName,
              classOf[com.dongjiaqiang.jvm.dsl.java.api.extend.Success[_]].getCanonicalName, "instanceof", counter )
          case FailureType ⇒
            testCode( matched, classOf[com.dongjiaqiang.jvm.dsl.java.api.extend.Failure[_]].getCanonicalName,
              classOf[com.dongjiaqiang.jvm.dsl.java.api.extend.Failure[_]].getCanonicalName, "instanceof", counter )
          case clazzType: ClazzType ⇒
            testCode( matched, clazzType.clazzName,
              clazzType.clazzName, "instanceof", counter )
          case _ ⇒
            throw new java.lang.AssertionError( "assertion failed" )
        }
        conditions.append( varTest, varDef, varAssign )
        conditions.appendAll( translateMatchCondition( varName, matchClass.dslType, matchClass, body, visitor, counter ) )

      case _ ⇒
        if (matchedCondition.isInstanceOf[MatchList] || matchedCondition.isInstanceOf[MatchHead]) {
          val (varTest, varDef, varAssign, varName) = listTestCode( matched, counter )
          conditions.append( varTest, varDef, varAssign )
          translateListMatchCondition( varName, new ListType( AnyType ), matchedCondition, body, conditions, visitor, counter )
        } else {
          assert( false )
        }
    }
  }

  private def translateLiteralMatchCondition(matched: String, baseLiteral: BaseLiteral, conditions: ArrayBuffer[String],
                                             visitor: ExpressionVisitor[String]): Unit = {
    conditions.append(
      s"""
         |if("$matched != ${visitor.visit( baseLiteral.asInstanceOf[Expression] )}"){
         |     return false;
         |}
         |""".stripMargin )
  }


  private def translateTupleMatchCondition(matched: String,
                                           matchedType: TupleType,
                                           matchCondition: MatchTuple,
                                           body: Block,
                                           conditions: ArrayBuffer[String],
                                           visitor: ExpressionVisitor[String],
                                           counter: AtomicInteger): Unit = {
    assert( matchedType.parameterTypes.length == matchCondition.expressions.length )
    matchCondition.expressions.zip( matchedType.parameterTypes ).zipWithIndex.foreach {
      case ((either, dslType), index) ⇒
        either match {
          case Left( expression ) ⇒
            conditions.appendAll( translateMatchCondition( s"${matched}_$index", dslType, expression, body, visitor, counter ) )
          case Right( name ) ⇒
            body.expressions.insert( 0, JavaLocalVarDef( name, dslType, Some( JavaVarRef( List( matched, s"_$index" ),dslType ) ) ) )
        }
    }
  }

  private def translateListMatchCondition(matched: String,
                                          listType: ListType,
                                          expression: Expression,
                                          body: Block,
                                          conditions: ArrayBuffer[String],
                                          visitor: ExpressionVisitor[String],
                                          counter: AtomicInteger): Unit = {
    val childExpressions = expression match {
      case matchList: MatchList ⇒
        conditions.append(
          s"""
             |if(${matched}.size()!=${matchList.expressions.length}){
             |   return false;
             |}
             |""".stripMargin )
        matchList.expressions
      case matchHead: MatchHead ⇒
        conditions.append(
          s"""
             |if(${matched}.size() < ${matchHead.head.length}){
             |   return false;
             |}
             |""".stripMargin
        )
        matchHead.head
    }
    childExpressions.zipWithIndex.foreach {
      case (expr, index) ⇒
        expr match {
          case Left( expression ) ⇒
            conditions.appendAll( translateMatchCondition( s"${matched}.get($index)",
              listType.parameterType, expression, body, visitor, counter ) )
          case Right( name ) ⇒
            body.expressions.insert( 0, JavaLocalVarDef( name, listType.parameterType,
              Some( JavaVarCall( List( s"${matched}_$index" ), "get", Array( new IntLiteral( index ) ),UnResolvedType ) ) ) )
        }
    }

    expression match {
      case matchHead: MatchHead ⇒
        val tailLiteral = new ListLiteral( Array( JavaVarCall( List( matched ),
          "subList", Array( new IntLiteral( matchHead.head.length ), JavaVarCall( List( matched ), "size", Array( ),UnResolvedType ) ),UnResolvedType ) ), listType )

        matchHead.tail match {
          case Left( expression ) ⇒
            expression match {
              case matchList: MatchList ⇒
                val varName = s"__sys_var_${counter.incrementAndGet( )}"
                conditions.append(
                  s"""
                     |${api.toJavaType( listType,javaTranslatorContext )} $varName = ${visitor.visit( tailLiteral )}
                     |""".stripMargin )
                conditions.appendAll( translateMatchCondition( varName, listType, matchList, body, visitor, counter ) )
            }
          case Right( name ) ⇒
            body.expressions.insert( 0, JavaLocalVarDef( name, listType,
              Some( tailLiteral ) ) )
        }
      case _ ⇒
    }
  }

  private def translateMatchCondition(matched: String,
                                      matchedType: DslType,
                                      matchCondition: Expression,
                                      body: Block,
                                      visitor: ExpressionVisitor[String], counter: AtomicInteger): ArrayBuffer[String] = {

    val conditions = ArrayBuffer[String]( )
    matchCondition match {
      case matchTuple: MatchTuple ⇒
        matchedType match {
          case tupleType: TupleType ⇒
            translateTupleMatchCondition( matched, tupleType, matchTuple, body, conditions, visitor, counter )
          case AnyType | UnResolvedType ⇒
            translateAnyMatchCondition( matched, matchTuple, body, conditions, visitor, counter )
        }
      case matchClass: MatchClass ⇒
        matchedType match {
          case clazzType: DefinitionClazzType ⇒
            translateClazzMatchCondition( matched, clazzType, matchClass, body, conditions, visitor, counter )
          case eitherType: EitherType ⇒

            assert( matchClass.dslType.isInstanceOf[LeftType] || matchClass.dslType.isInstanceOf[RightType] )
            translateEitherMatchCondition( matched, eitherType, matchClass, body, conditions, visitor, counter )

          case tryType: TryType ⇒

            assert( matchClass.dslType.isInstanceOf[SuccessType] || matchClass.dslType.isInstanceOf[FailureType.type] )
            translateTryMatchCondition( matched, tryType, matchClass, body, conditions, visitor, counter )

          case optionType: OptionType ⇒

            assert( matchClass.dslType.isInstanceOf[SomeType] || matchClass.dslType.isInstanceOf[NoneType.type] )
            translateOptionMatchCondition( matched, optionType, matchClass, body, conditions, visitor, counter )

          case AnyType | UnResolvedType ⇒

            translateAnyMatchCondition( matched, matchClass, body, conditions, visitor, counter )
        }

      case baseLiteral: BaseLiteral ⇒
        translateLiteralMatchCondition( matched, baseLiteral, conditions, visitor )
      case _ ⇒
        if (matchedType.isInstanceOf[MatchHead] || matchCondition.isInstanceOf[MatchList]) {
          matchedType match {
            case listType: ListType ⇒
              translateListMatchCondition( matched, listType, matchCondition, body, conditions, visitor, counter )
            case AnyType | UnResolvedType ⇒
              translateAnyMatchCondition( matched, matchCondition, body, conditions, visitor, counter )
          }
        } else {
          assert( assertion = false, "" )
        }
    }
    conditions
  }


  private def translate(matched: VarRef, matchCondition: Expression, body: Block, visitor: ExpressionVisitor[String]): String = {
    matchCondition match {
      case matchType: MatchType ⇒
        translateMatchType( matched, matchType, body,visitor.programScope )
      case _ ⇒
        val conditions = translateMatchCondition( matched.refs.mkString( "." ),matched.getValueType(visitor.programScope),
          matchCondition, body, visitor, new AtomicInteger( ) )
        conditions.append( "return true" )
        val methodName = s" _sys_match_condition_${javaTranslatorContext.systemVarIndex.incrementAndGet( )}"

        val methodDef =
          s"""
             |public boolean _$methodName(${api.toJavaType( matched.getValueType(visitor.programScope),javaTranslatorContext )} param) throws Exception{
             |   ${conditions.mkString( ";" )}

             |""".stripMargin

        if (visitor.currentClazzScope != null) {
          javaTranslatorContext.systemGenerateMethods.getOrElseUpdate( visitor.currentClazzScope.name, ArrayBuffer( ) )
            .append( methodDef )

        } else {
          javaTranslatorContext.systemGenerateProgramMethods.append( methodDef )
        }
        s"$methodName(${matched.refs.mkString( "." )})"
    }
  }


  override def visit(matchCase: MatchCase, visitor: ExpressionVisitor[String]): String = {
    if (matchCase.cases.isEmpty) {
      assert( matchCase.default.isDefined )
      visitor.visit( matchCase.default.get )
    } else {

      val head = matchCase.cases.head
      val tail = matchCase.cases.tail
      s"""
         |if(${translate( matchCase.matched, head._1, head._2, visitor )})
         |   ${visitor.visit( head._2 )}
         |""".stripMargin + tail.map( `case` ⇒ {
        s"""
           |else if(${translate( matchCase.matched, `case`._1, `case`._2, visitor )})
           |   ${visitor.visit( `case`._2 )}
           |""".stripMargin
      } ).mkString( "\n" ) + matchCase.default.map( block ⇒
        s"""
           |else
           |   ${visitor.visit( block )}
           |""".stripMargin ).getOrElse( "" )
    }
  }

}
