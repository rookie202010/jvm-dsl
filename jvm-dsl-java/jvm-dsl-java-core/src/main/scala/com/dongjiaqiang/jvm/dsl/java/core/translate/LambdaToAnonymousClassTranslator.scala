package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.Lambda
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate.{_1_Predicate, _DoublePredicate, _IntPredicate, _LongPredicate}
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._

object LambdaToAnonymousClassTranslator {

    def generateTupleParams(tupleType: TupleType, lambda: Lambda,javaTranslatorContext: JavaTranslatorContext): String = {
        tupleType.parameterTypes.zip( lambda.inputs ).map {
            case (dslType, name) ⇒
                s"${api.toJavaType( dslType,javaTranslatorContext )} $name"
        }.mkString( "," )
    }

    def generateTupleTypes(tupleType: TupleType,javaTranslatorContext: JavaTranslatorContext): String = {
        tupleType.parameterTypes.map(t⇒api.toJavaType(t,javaTranslatorContext) ).mkString( "," )
    }

    def translateSupplier(outputType: DslType, lambda: Lambda, javaTranslatorContext: JavaTranslatorContext,visitor: ExpressionVisitor[String]): String = {
        outputType match {
            case UnitType ⇒
                s"""
                   |new ${classOf[_Runnable].getCanonicalName}(){
                   |    @Override
                   |    public void run() throws Exception
                   |          ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case IntType ⇒
                s"""
                   |new ${classOf[_IntSupplier].getCanonicalName}(){
                   |    @Override
                   |    int getAsInt() throws Exception
                   |        ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case LongType ⇒
                s"""
                   |new ${classOf[_LongSupplier].getCanonicalName}(){
                   |    @Override
                   |    long getAsLong() throws Exception
                   |      ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case DoubleType ⇒
                s"""
                   |new ${classOf[_DoubleSupplier].getCanonicalName}(){
                   |  @Override
                   |  double getAsDouble() throws Exception
                   |      ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case BoolType ⇒
                s"""
                   |new ${classOf[_BooleanSupplier].getCanonicalName}(){
                   |    @Override
                   |    boolean getAsBoolean() throws Exception
                   |      ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case _ ⇒
                s"""
                   |new ${classOf[_1_Supplier[_]].getCanonicalName}<${api.toJavaType( outputType,javaTranslatorContext )}>(){
                   |    @Override
                   |    ${api.toJavaType( outputType,javaTranslatorContext )} get() throws Exception
                   |      ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
        }
    }

    def translatePredicate(inputType: DslType, lambda: Lambda,javaTranslatorContext: JavaTranslatorContext, visitor: ExpressionVisitor[String]): String = {
        inputType match {
            case IntType ⇒
                s"""
                   |new ${classOf[_IntPredicate].getCanonicalName}(){
                   |    @Override
                   |    boolean test(int ${lambda.inputs.head}) throws Exception
                   |        ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case LongType ⇒
                s"""
                   |new ${classOf[_LongPredicate].getCanonicalName}(){
                   |    @Override
                   |    boolean test(long ${lambda.inputs.head}) throws Exception
                   |        ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case DoubleType ⇒
                s"""
                   |new ${classOf[_DoublePredicate].getCanonicalName}(){
                   |    @Override
                   |    boolean test(double ${lambda.inputs.head}) throws Exception
                   |        ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case tupleType: TupleType ⇒
                val predicateClazzName =
                    s"""
                       |com.dongjiaqiang.jvm.dsl.java.core.lambda.predicate._${tupleType.parameterTypes.length}_Predicate<${tupleType.parameterTypes.map(t⇒api.toJavaType(t,javaTranslatorContext) ).mkString( "," )}>
                       |""".stripMargin
                val inputs = generateTupleParams( tupleType, lambda,javaTranslatorContext)
                s"""
                   |new $predicateClazzName<${generateTupleTypes( tupleType ,javaTranslatorContext)}>(){
                   |    @Override
                   |    boolean test($inputs) throws Exception
                   |       ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case _ ⇒
                s"""
                   |new ${classOf[_1_Predicate[_]].getCanonicalName}<${api.toJavaType( inputType,javaTranslatorContext )}>(){
                   |    @Override
                   |    boolean test(${lambda.inputs.head}) throws Exception
                   |      ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
        }
    }

    def translateConsumer(inputType: DslType, lambda: Lambda, javaTranslatorContext: JavaTranslatorContext,visitor: ExpressionVisitor[String]): String = {
        inputType match {
            case IntType ⇒
                s"""
                   |new ${classOf[_IntConsumer]}(){
                   |    @Override
                   |    void accept(int ${lambda.inputs.head}) throws Exception
                   |         ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case DoubleType ⇒
                s"""
                   |new ${classOf[_DoubleConsumer]}(){
                   |    @Override
                   |    void accept(double ${lambda.inputs.head}) throws Exception
                   |         ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case LongType ⇒
                s"""
                   |new ${classOf[_LongConsumer]}(){
                   |    @Override
                   |    void accept(long ${lambda.inputs.head}) throws Exception
                   |       ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case tupleType: TupleType ⇒
                if (tupleType.parameterTypes.length == 2) {
                    tupleType.parameterTypes.last match {
                        case DoubleType ⇒
                            s"""
                               |new ${classOf[_ObjDoubleConsumer[_]].getCanonicalName}<${api.toJavaType( tupleType.parameterTypes.head,javaTranslatorContext)}>(){
                               |  @Override
                               |  void accept(${api.toJavaType( tupleType.parameterTypes.head,javaTranslatorContext )} ${lambda.inputs.head}, double ${lambda.inputs.last}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case IntType ⇒
                            s"""
                               |new ${classOf[_ObjIntConsumer[_]].getCanonicalName}<${api.toJavaType( tupleType.parameterTypes.head,javaTranslatorContext )}>(){
                               |  @Override
                               |  void accept(${api.toJavaType( tupleType.parameterTypes.head,javaTranslatorContext )} ${lambda.inputs.head}, int ${lambda.inputs.last}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case LongType ⇒
                            s"""
                               |new ${classOf[_ObjLongConsumer[_]].getCanonicalName}<${api.toJavaType( tupleType.parameterTypes.head,javaTranslatorContext )}>(){
                               |  @Override
                               |  void accept(${api.toJavaType( tupleType.parameterTypes.head ,javaTranslatorContext)} ${lambda.inputs.head}, int ${lambda.inputs.last}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case _ ⇒
                            s"""
                               |new ${classOf[_2_Consumer[_, _]].getCanonicalName}<${api.toJavaType( tupleType.parameterTypes.head,javaTranslatorContext )},${api.toJavaType( tupleType.parameterTypes.last,javaTranslatorContext )}>(){
                               |  @Override
                               |  void accept(${api.toJavaType( tupleType.parameterTypes.head,javaTranslatorContext )} ${lambda.inputs.head}, i${api.toJavaType( tupleType.parameterTypes.last,javaTranslatorContext )} ${lambda.inputs.last}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                    }
                } else {
                    val consumerClazzName =
                        s"""
                           |com.dongjiaqiang.jvm.dsl.java.core.lambda.consumer._${tupleType.parameterTypes.length}_Consumer<${tupleType.parameterTypes.map(t⇒api.toJavaType(t,javaTranslatorContext) ).mkString( "," )}>
                           |""".stripMargin
                    val inputs = generateTupleParams( tupleType, lambda,javaTranslatorContext )
                    s"""
                       |new $consumerClazzName<${generateTupleTypes( tupleType,javaTranslatorContext )}>(){
                       |    @Override
                       |    void accept($inputs) throws Exception
                       |       ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                }
            case _ ⇒
                s"""
                   |new ${classOf[_1_Consumer[_]].getCanonicalName}<${api.toJavaType( inputType,javaTranslatorContext )}>(){
                   |  @Override
                   |  void accept(${api.toJavaType( inputType,javaTranslatorContext )} ${lambda.inputs.head}) throws Exception
                   |      ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin

        }
    }

    def translateDoubleBinaryOperator(lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
        s"""
           |new ${classOf[_DoubleBinaryOperator].getCanonicalName}(){
           |     @Override
           |     double applyAsDouble(double ${lambda.inputs.head}, double ${lambda.inputs.last}) throws Exception
           |         ${visitor.visit( lambda )}
           |}
           |""".stripMargin
    }

    def translateIntBinaryOperator(lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
        s"""
           |new ${classOf[_IntBinaryOperator].getCanonicalName}(){
           |     @Override
           |     int applyAsInt((int ${lambda.inputs.head}, int ${lambda.inputs.last}) throws Exception
           |         ${visitor.visit( lambda )}
           |}
           |""".stripMargin
    }

    def translateLongBinaryOperator(lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
        s"""
           |new ${classOf[_LongBinaryOperator].getCanonicalName}(){
           |     @Override
           |     long applyAsLong(long ${lambda.inputs.head}, long ${lambda.inputs.last}) throws Exception
           |         ${visitor.visit( lambda )}
           |}
           |""".stripMargin
    }

    def basicBinaryOperator(inputType: TupleType, outputType: DslType, targetType: DslType): Boolean = {
        outputType == targetType && inputType.parameterTypes.length == 2 && inputType.parameterTypes.head == targetType && inputType.parameterTypes.last == targetType
    }


    def translateFunction(inputType: DslType, outputType: DslType, lambda: Lambda, javaTranslatorContext: JavaTranslatorContext,visitor: ExpressionVisitor[String]): String = {
        inputType match {
            case IntType ⇒
                outputType match {
                    case IntType ⇒
                        s"""
                           |new ${classOf[_IntUnaryOperator].getCanonicalName}(){
                           |    @Override
                           |    int  applyAsInt(int ${lambda.inputs.head}) throws Exception
                           |      ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                    case DoubleType ⇒
                        s"""
                           |new ${classOf[_IntToDoubleFunction].getCanonicalName}(){
                           |    @Override
                           |    double applyAsDouble(int ${lambda.inputs.head}) throws Exception
                           |      ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                    case LongType ⇒
                        s"""
                           |new ${classOf[_IntToLongFunction].getCanonicalName}(){
                           |    @Override
                           |    long applyAsLong(int ${lambda.inputs.head}) throws Exception
                           |      ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                    case _ ⇒
                        s"""
                           |new ${classOf[_IntFunction[_]].getCanonicalName}<${api.toJavaType( outputType,javaTranslatorContext )}>(){
                           |     @Override
                           |     ${api.toJavaType( outputType,javaTranslatorContext )} apply(int ${lambda.inputs.head}) throws Exception
                           |            ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                }
            case LongType ⇒
                outputType match {
                    case IntType ⇒
                        s"""
                           |new ${classOf[_LongUnaryOperator].getCanonicalName}(){
                           |    @Override
                           |    long  applyAsLong(long ${lambda.inputs.head}) throws Exception
                           |      ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                    case DoubleType ⇒
                        s"""
                           |new ${classOf[_LongToDoubleFunction].getCanonicalName}(){
                           |    @Override
                           |    double applyAsDouble(int ${lambda.inputs.head}) throws Exception
                           |      ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                    case LongType ⇒
                        s"""
                           |new ${classOf[_LongUnaryOperator].getCanonicalName}(){
                           |    @Override
                           |    long applyAsLong(int ${lambda.inputs.head}) throws Exception
                           |      ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                    case _ ⇒
                        s"""
                           |new ${classOf[_LongFunction[_]].getCanonicalName}<${api.toJavaType( outputType,javaTranslatorContext )}>(){
                           |     @Override
                           |     ${api.toJavaType( outputType,javaTranslatorContext )} apply(long ${lambda.inputs.head}) throws Exception
                           |            ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                }
            case DoubleType ⇒
                outputType match {
                    case IntType ⇒
                        s"""
                           |new ${classOf[_DoubleToIntFunction].getCanonicalName}(){
                           |    @Override
                           |    int  applyAsInt(double ${lambda.inputs.head}) throws Exception
                           |      ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                    case DoubleType ⇒
                        s"""
                           |new ${classOf[_DoubleUnaryOperator].getCanonicalName}(){
                           |    @Override
                           |    double applyAsDouble(double ${lambda.inputs.head}) throws Exception
                           |      ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                    case LongType ⇒
                        s"""
                           |new ${classOf[_DoubleToLongFunction].getCanonicalName}(){
                           |    @Override
                           |    long applyAsLong(double ${lambda.inputs.head}) throws Exception
                           |      ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                    case _ ⇒
                        s"""
                           |new ${classOf[_DoubleFunction[_]].getCanonicalName}<${api.toJavaType( outputType,javaTranslatorContext )}>(){
                           |     @Override
                           |     R apply(double ${lambda.inputs.head}) throws Exception
                           |            ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                }
            case tupleType: TupleType ⇒
                if (basicBinaryOperator( tupleType, outputType, IntType )) {
                    translateIntBinaryOperator( lambda, visitor )
                } else if (basicBinaryOperator( tupleType, outputType, LongType )) {
                    translateLongBinaryOperator( lambda, visitor )
                } else if (basicBinaryOperator( tupleType, outputType, DoubleType )) {
                    translateDoubleBinaryOperator( lambda, visitor )
                } else {
                    val functionClazzName =
                        s"""
                           |com.dongjiaqiang.jvm.dsl.java.core.lambda.function._${tupleType.parameterTypes.length}_Function<${tupleType.parameterTypes.map(t⇒api.toJavaType(t,javaTranslatorContext) ).mkString( "," )}>
                           |""".stripMargin
                    val inputs = generateTupleParams( tupleType, lambda,javaTranslatorContext )
                    s"""
                       |new $functionClazzName<${generateTupleTypes( tupleType,javaTranslatorContext )}>(){
                       |    @Override
                       |    ${api.toJavaType( outputType,javaTranslatorContext )} apply($inputs) throws Exception
                       |       ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                }
            case _ ⇒
                s"""
                   |new ${classOf[_1_Function[_, _]].getCanonicalName}<${api.toJavaType( inputType,javaTranslatorContext )},${api.toJavaType( outputType,javaTranslatorContext )}>(){
                   |     @Override
                   |     ${api.toJavaType( outputType,javaTranslatorContext )} apply(${api.toJavaType( inputType,javaTranslatorContext )} ${lambda.inputs.head}) throws Exception
                   |         ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin

        }
    }


    def translate(lambdaType: LambdaType, lambda: Lambda, javaTranslatorContext: JavaTranslatorContext,visitor: ExpressionVisitor[String]): String = {
        lambdaType.mayInputType match {
            case None ⇒
                translateSupplier( lambdaType.outputType, lambda, javaTranslatorContext,visitor )
            case Some( inputType ) ⇒
                lambdaType.outputType match {
                    case BoolType ⇒
                        translatePredicate( inputType, lambda,javaTranslatorContext, visitor )
                    case UnitType ⇒
                        translateConsumer( inputType, lambda,javaTranslatorContext, visitor )
                    case _ ⇒
                        translateFunction( inputType, lambdaType.outputType, lambda, javaTranslatorContext,visitor )
                }
        }
    }
}
