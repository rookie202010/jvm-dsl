package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.core.`type`.{BoolType, DoubleType, DslType, IntType, LambdaType, LongType, TupleType, UnitType}
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.core
import com.dongjiaqiang.jvm.dsl.java.core.lambda.consumer.{_1_Consumer, _2_Consumer, _DoubleConsumer, _IntConsumer, _LongConsumer, _ObjDoubleConsumer, _ObjIntConsumer, _ObjLongConsumer, _Runnable}
import com.dongjiaqiang.jvm.dsl.java.core.lambda.function.{_IntBinaryOperator, _IntFunction, _IntToDoubleFunction, _IntToLongFunction, _IntUnaryOperator, _LongFunction}
import com.dongjiaqiang.jvm.dsl.java.core.lambda.predicate.{_1_Predicate, _DoublePredicate, _IntPredicate, _LongPredicate}
import com.dongjiaqiang.jvm.dsl.java.core.lambda.supplier.{_1_Supplier, _BooleanSupplier, _DoubleSupplier, _IntSupplier, _LongSupplier}
import com.typesafe.config.Config

import java.util.concurrent.atomic.AtomicInteger
import java.util.function.{DoubleUnaryOperator, IntUnaryOperator, LongUnaryOperator}
import scala.collection.mutable.{ArrayBuffer, Map ⇒ MMap}

case class JavaVarRef(name: List[String]) extends Expression

case class JavaVarCall(varRef: List[String], name: String, params: Array[Expression]) extends Expression

case class JavaLocalVarDef(name: String, dslType: DslType, assigned: Option[Expression]) extends Expression

case class JavaMatchCase(matchedType: DslType, code: String) extends Expression

case class JavaLambda(dslType: LambdaType,lambda: Lambda) extends Expression

/**
 * clazz methods generate by java translator
 *
 * clazzName -> methodName -> methodCode
 *
 * program methods generate by java translator
 *
 * methodName -> methodCode
 */
case class JavaTranslatorContext(packageName: String,
                                 javaTranslateConfig: Config,
                                 systemGenerateMethods: MMap[String, ArrayBuffer[String]] = MMap( ),
                                 systemGenerateProgramMethods: ArrayBuffer[String] = ArrayBuffer( ),
                                 systemVarIndex: AtomicInteger = new AtomicInteger( 0 ))

class JavaTranslator(val javaTranslatorContext: JavaTranslatorContext,
                     override val programScope: ProgramScope) extends ExpressionVisitor[String]
  with UnaryExpressionJavaTranslator
  with BinaryExpressionJavaTranslator
  with VarExpressionJavaTranslator
  with LiteralExpressionJavaTranslator
  with StatementExpressionJavaTranslator
  with MatchCaseExpressionJavaTranslator
  with BlockExpressionJavaTranslator
  with CustomBlockExpressionJavaTranslator {

  override def defaultVisit(expression: Expression, visitor: ExpressionVisitor[String]): String = {
    expression match {
      case JavaVarCall( varRef, name, params ) ⇒
        s"${varRef.mkString( "." )}.${name}(${params.map( visitor.visit ).mkString( "," )})"
      case JavaVarRef( name ) ⇒ name.mkString( "." )
      case JavaLocalVarDef( name, dslType, assigned, ) ⇒
        assigned match {
          case None ⇒ s"${core.toJavaType( dslType )} ${name}"
          case Some( expression ) ⇒ s"${core.toJavaType( dslType )} ${name} = ${visitor.visit( expression )}"
        }
      case JavaLambda(lambdaType: LambdaType,lambda)⇒

        def generateTupleParams(tupleType: TupleType,lambda: Lambda):String={
          tupleType.valueTypes.zip(lambda.inputs).map{
            case (dslType,name)⇒
              s"${core.toJavaType(dslType)} $name"
          }.mkString(",")
        }

        if(javaTranslatorContext.javaTranslateConfig.getBoolean("java.translator.lambda.grammar.enable")) {
          s"(${lambda.inputs.mkString(",")})->${visitor.visit(lambda)}"
        }else {
          lambdaType.inputType match {
            case None ⇒
              lambdaType.outputType match {
                case UnitType ⇒
                  s"""
                     |new ${classOf[_Runnable].getCanonicalName}(){
                     |    @Override
                     |    public void run() throws Exception
                     |          ${visitor.visit(lambda)}
                     |}
                     |""".stripMargin
                case IntType ⇒
                  s"""
                     |new ${classOf[_IntSupplier].getCanonicalName}(){
                     |    @Override
                     |    int getAsInt() throws Exception
                     |        ${visitor.visit(lambda)}
                     |}
                     |""".stripMargin
                case LongType ⇒
                  s"""
                     |new ${classOf[_LongSupplier].getCanonicalName}(){
                     |    @Override
                     |    long getAsLong() throws Exception
                     |      ${visitor.visit(lambda)}
                     |}
                     |""".stripMargin
                case DoubleType⇒
                  s"""
                     |new ${classOf[_DoubleSupplier].getCanonicalName}(){
                     |  @Override
                     |  double getAsDouble() throws Exception
                     |      ${visitor.visit(lambda)}
                     |}
                     |""".stripMargin
                case BoolType⇒
                  s"""
                     |new ${classOf[_BooleanSupplier].getCanonicalName}(){
                     |    @Override
                     |    boolean getAsBoolean() throws Exception
                     |      ${visitor.visit(lambda)}
                     |}
                     |""".stripMargin
                case _⇒
                  s"""
                     |new ${classOf[_1_Supplier[_]].getCanonicalName}<${core.toJavaType(lambdaType.outputType)}>(){
                     |    @Override
                     |    ${core.toJavaType(lambdaType.outputType)} get() throws Exception
                     |      ${visitor.visit(lambda)}
                     |}
                     |""".stripMargin
              }
            case Some(inputType)⇒
                lambdaType.outputType match {
                  case BoolType⇒
                      inputType match {
                        case IntType⇒
                            s"""
                             |new ${classOf[_IntPredicate].getCanonicalName}(){
                             |    @Override
                             |    boolean test(int ${lambda.inputs.head}) throws Exception
                             |        ${visitor.visit(lambda)}
                             |}
                             |""".stripMargin
                        case LongType⇒
                          s"""
                             |new ${classOf[_LongPredicate].getCanonicalName}(){
                             |    @Override
                             |    boolean test(long ${lambda.inputs.head}) throws Exception
                             |        ${visitor.visit(lambda)}
                             |}
                             |""".stripMargin
                        case DoubleType⇒
                          s"""
                             |new ${classOf[_DoublePredicate].getCanonicalName}(){
                             |    @Override
                             |    boolean test(double ${lambda.inputs.head}) throws Exception
                             |        ${visitor.visit(lambda)}
                             |}
                             |""".stripMargin
                        case tupleType: TupleType⇒
                            val predicateClazzName=
                              s"""
                                |com.dongjiaqiang.jvm.dsl.java.core.lambda.predicate._${tupleType.valueTypes.length}_Predicate<${tupleType.valueTypes.map(`core`.toJavaType).mkString(",")}>
                                |""".stripMargin
                            val inputs =   generateTupleParams(tupleType,lambda)
                            s"""
                             |new $predicateClazzName(){
                             |    @Override
                             |    boolean test($inputs) throws Exception
                             |       ${visitor.visit(lambda)}
                             |}
                             |""".stripMargin
                        case _⇒
                          s"""
                             |new ${classOf[_1_Predicate[_]].getCanonicalName}<${core.toJavaType(inputType)}>(){
                             |    @Override
                             |    boolean test(${lambda.inputs.head}) throws Exception
                             |      ${visitor.visit(lambda)}
                             |}
                             |""".stripMargin
                      }
                  case UnitType⇒
                      inputType match {
                        case IntType⇒
                            s"""
                             |new ${classOf[_IntConsumer]}(){
                             |    @Override
                             |    void accept(int ${lambda.inputs.head}) throws Exception
                             |         ${visitor.visit(lambda)}
                             |}
                             |""".stripMargin
                        case DoubleType⇒
                            s"""
                             |new ${classOf[_DoubleConsumer]}(){
                             |    @Override
                             |    void accept(double ${lambda.inputs.head}) throws Exception
                             |         ${visitor.visit(lambda)}
                             |}
                             |""".stripMargin
                        case LongType⇒
                            s"""
                             |new ${classOf[_LongConsumer]}(){
                             |    @Override
                             |    void accept(long ${lambda.inputs.head}) throws Exception
                             |       ${visitor.visit(lambda)}
                             |}
                             |""".stripMargin
                        case tupleType: TupleType⇒
                          if(tupleType.valueTypes.length==2){
                              tupleType.valueTypes.last match {
                                case DoubleType⇒
                                    s"""
                                     |new ${classOf[_ObjDoubleConsumer[_]].getCanonicalName}<${core.toJavaType(tupleType.valueTypes.head)}>(){
                                     |  @Override
                                     |  void accept(${core.toJavaType(tupleType.valueTypes.head)} ${lambda.inputs.head}, double ${lambda.inputs.last}) throws Exception
                                     |      ${visitor.visit(lambda)}
                                     |}
                                     |""".stripMargin
                                case IntType⇒
                                  s"""
                                     |new ${classOf[_ObjIntConsumer[_]].getCanonicalName}<${core.toJavaType(tupleType.valueTypes.head)}>(){
                                     |  @Override
                                     |  void accept(${core.toJavaType(tupleType.valueTypes.head)} ${lambda.inputs.head}, int ${lambda.inputs.last}) throws Exception
                                     |      ${visitor.visit(lambda)}
                                     |}
                                     |""".stripMargin
                                case LongType⇒
                                  s"""
                                     |new ${classOf[_ObjLongConsumer[_]].getCanonicalName}<${core.toJavaType(tupleType.valueTypes.head)}>(){
                                     |  @Override
                                     |  void accept(${core.toJavaType(tupleType.valueTypes.head)} ${lambda.inputs.head}, int ${lambda.inputs.last}) throws Exception
                                     |      ${visitor.visit(lambda)}
                                     |}
                                     |""".stripMargin
                                case _⇒
                                  s"""
                                     |new ${classOf[_2_Consumer[_,_]].getCanonicalName}<${core.toJavaType(tupleType.valueTypes.head)},${core.toJavaType(tupleType.valueTypes.last)}>(){
                                     |  @Override
                                     |  void accept(${core.toJavaType(tupleType.valueTypes.head)} ${lambda.inputs.head}, i${core.toJavaType(tupleType.valueTypes.last)} ${lambda.inputs.last}) throws Exception
                                     |      ${visitor.visit(lambda)}
                                     |}
                                     |""".stripMargin
                              }
                          }else{
                            val consumerClazzName=
                                s"""
                               |com.dongjiaqiang.jvm.dsl.java.core.lambda.consumer._${tupleType.valueTypes.length}_Consumer<${tupleType.valueTypes.map(`core`.toJavaType).mkString(",")}>
                               |""".stripMargin
                            val inputs =   generateTupleParams(tupleType,lambda)
                            s"""
                               |new $consumerClazzName(){
                               |    @Override
                               |    void accept($inputs) throws Exception
                               |       ${visitor.visit(lambda)}
                               |}
                               |""".stripMargin
                          }
                        case _⇒
                          s"""
                             |new ${classOf[_1_Consumer[_]].getCanonicalName}<${core.toJavaType(inputType)}>(){
                             |  @Override
                             |  void accept(${core.toJavaType(inputType)} ${lambda.inputs.head}) throws Exception
                             |      ${visitor.visit(lambda)}
                             |}
                             |""".stripMargin

                      }
                  case outputType: DslType⇒
                        inputType match {
                          case IntType⇒
                            outputType match {
                              case IntType ⇒
                                s"""
                                   |new ${classOf[_IntUnaryOperator].getCanonicalName}(){
                                   |    @Override
                                   |    int  apply(int ${lambda.inputs.head}) throws Exception
                                   |      ${visitor.visit(lambda)}
                                   |}
                                   |""".stripMargin
                              case DoubleType⇒
                                s"""
                                   |new ${classOf[_IntToDoubleFunction].getCanonicalName}(){
                                   |    @Override
                                   |    double applyAsDouble(int ${lambda.inputs.head}) throws Exception
                                   |      ${visitor.visit(lambda)}
                                   |}
                                   |""".stripMargin
                              case LongType⇒
                                  s"""
                                     |new ${classOf[_IntToLongFunction].getCanonicalName}(){
                                     |    @Override
                                     |    long applyAsDouble(int ${lambda.inputs.head}) throws Exception
                                     |      ${visitor.visit(lambda)}
                                     |}
                                   |""".stripMargin
                              case tupleType: TupleType⇒
                                  if(tupleType.valueTypes.length==2 && tupleType.valueTypes.head.isInstanceOf[IntType.type ] && tupleType.valueTypes.last.isInstanceOf[IntType.type ]) {
                                      s"""
                                      |new ${classOf[_IntBinaryOperator[_,_]]}(){
                                      |   @Override
                                      |   int applyAsInt(int ${lambda.inputs.head}, int ${lambda.inputs.last}) throws Exception
                                      |       ${visitor.visit(lambda)}
                                      |}
                                      |""".stripMargin
                                  }else{
                                    val functionClazzName=
                                      s"""
                                         |com.dongjiaqiang.jvm.dsl.java.core.lambda.function._${tupleType.valueTypes.length}_Function<${tupleType.valueTypes.map(`core`.toJavaType).mkString(",")}>
                                         |""".stripMargin

                                  }
                              case _ ⇒
                                s"""
                                   |new ${classOf[_IntFunction[_]].getCanonicalName}<${core.toJavaType(outputType)}>(){
                                   |    @Override
                                   |    ${core.toJavaType(outputType)}  apply(int ${lambda.inputs.head}) throws Exception
                                   |            ${visitor.visit(lambda)}
                                   |}
                                   |""".stripMargin
                            }
                          case LongType⇒
                            outputType match {
                              case LongType ⇒
                                s"""
                                   |new ${classOf[LongUnaryOperator].getCanonicalName}<${core.toJavaType(outputType)}>(){
                                   |      @Override
                                   |      long applyAsLong(long ${lambda.inputs.head})
                                   |}
                                   |""".stripMargin
                              case _ ⇒
                                s"""
                                   |new ${classOf[_LongFunction[_]].getCanonicalName}<${core.toJavaType(outputType)}>(){
                                   |    ${core.toJavaType(outputType)}  apply(long ${lambda.inputs.head}) throws Exception
                                   |
                                   |}
                                   |""".stripMargin
                            }
                          case DoubleType⇒
                              outputType match{
                                  case DoubleType⇒
                                      s"""
                                       |new ${classOf[DoubleUnaryOperator].getCanonicalName}<${core.toJavaType(outputType)}>(){
                                       |    @Override
                                       |    double applyAsLong(double ${lambda.inputs.head})
                                       |}
                                       |""".stripMargin
                                  case _⇒
                                      s"""
                                      |new ${classOf[_IntFunction[_]].getCanonicalName}<${core.toJavaType(outputType)}>(){
                                      |    ${core.toJavaType(outputType)}  apply(double ${lambda.inputs.head}) throws Exception;
                                      |}
                                      |""".stripMargin
                              }
                          }

        }
      case JavaMatchCase( matchedType, code ) ⇒
        matchedType match {
          case IntType ⇒
            s"""
               |new ${classOf[_IntSupplier].getCanonicalName}(){
               |            @Override
               |            public int getAsInt() throws Exception {
               |               $code
               |            }
               |}.getAsInt()
               |""".stripMargin
          case LongType ⇒
            s"""
               |new ${classOf[_LongSupplier].getCanonicalName}(){
               |            @Override
               |            public int getAsLong() throws Exception {
               |               $code
               |            }
               |}.getAsLong()
               |""".stripMargin
          case DoubleType ⇒
            s"""
               |new ${classOf[_DoubleSupplier].getCanonicalName}(){
               |            @Override
               |            public double getAsDouble() throws Exception {
               |               $code
               |            }
               |}.getAsDouble()
               |""".stripMargin
          case BoolType ⇒
            s"""
               |new ${classOf[_BooleanSupplier].getCanonicalName}(){
               |            @Override
               |            public boolean getAsBoolean() throws Exception {
               |               $code
               |            }
               |}.getAsBoolean
               |""".stripMargin
          case _ ⇒
            s"""
               |new ${classOf[_1_Supplier[_]].getCanonicalName}<${core.toJavaType( matchedType )}>(){
               |            @Override
               |            public ${core.toJavaType( matchedType )} get() throws Exception {
               |               $code
               |            }
               |}.get()
               |""".stripMargin
        }
    }
  }
}
