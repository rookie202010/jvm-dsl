package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.Lambda
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate.{_1_Predicate, _DoublePredicate, _IntPredicate, _LongPredicate}
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._
import com.dongjiaqiang.jvm.dsl.java.core

object LambdaToAnonymousClassTranslator {

    def generateTupleParams(tupleType: TupleType, lambda: Lambda): String = {
        tupleType.valueTypes.zip( lambda.inputs ).map {
            case (dslType, name) ⇒
                s"${core.toJavaType( dslType )} $name"
        }.mkString( "," )
    }

    def generateTupleTypes(tupleType: TupleType): String = {
        tupleType.valueTypes.map( core.toJavaType ).mkString( "," )
    }

    def translateSupplier(outputType: DslType, lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
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
                   |new ${classOf[_1_Supplier[_]].getCanonicalName}<${core.toJavaType( outputType )}>(){
                   |    @Override
                   |    ${core.toJavaType( outputType )} get() throws Exception
                   |      ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
        }
    }

    def translatePredicate(inputType: DslType, lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
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
                       |com.dongjiaqiang.jvm.dsl.java.core.lambda.predicate._${tupleType.valueTypes.length}_Predicate<${tupleType.valueTypes.map( `core`.toJavaType ).mkString( "," )}>
                       |""".stripMargin
                val inputs = generateTupleParams( tupleType, lambda )
                s"""
                   |new $predicateClazzName<${generateTupleTypes( tupleType )}>(){
                   |    @Override
                   |    boolean test($inputs) throws Exception
                   |       ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
            case _ ⇒
                s"""
                   |new ${classOf[_1_Predicate[_]].getCanonicalName}<${core.toJavaType( inputType )}>(){
                   |    @Override
                   |    boolean test(${lambda.inputs.head}) throws Exception
                   |      ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin
        }
    }

    def translateConsumer(inputType: DslType, lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
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
                if (tupleType.valueTypes.length == 2) {
                    tupleType.valueTypes.last match {
                        case DoubleType ⇒
                            s"""
                               |new ${classOf[_ObjDoubleConsumer[_]].getCanonicalName}<${core.toJavaType( tupleType.valueTypes.head )}>(){
                               |  @Override
                               |  void accept(${core.toJavaType( tupleType.valueTypes.head )} ${lambda.inputs.head}, double ${lambda.inputs.last}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case IntType ⇒
                            s"""
                               |new ${classOf[_ObjIntConsumer[_]].getCanonicalName}<${core.toJavaType( tupleType.valueTypes.head )}>(){
                               |  @Override
                               |  void accept(${core.toJavaType( tupleType.valueTypes.head )} ${lambda.inputs.head}, int ${lambda.inputs.last}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case LongType ⇒
                            s"""
                               |new ${classOf[_ObjLongConsumer[_]].getCanonicalName}<${core.toJavaType( tupleType.valueTypes.head )}>(){
                               |  @Override
                               |  void accept(${core.toJavaType( tupleType.valueTypes.head )} ${lambda.inputs.head}, int ${lambda.inputs.last}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case _ ⇒
                            s"""
                               |new ${classOf[_2_Consumer[_, _]].getCanonicalName}<${core.toJavaType( tupleType.valueTypes.head )},${core.toJavaType( tupleType.valueTypes.last )}>(){
                               |  @Override
                               |  void accept(${core.toJavaType( tupleType.valueTypes.head )} ${lambda.inputs.head}, i${core.toJavaType( tupleType.valueTypes.last )} ${lambda.inputs.last}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                    }
                } else {
                    val consumerClazzName =
                        s"""
                           |com.dongjiaqiang.jvm.dsl.java.core.lambda.consumer._${tupleType.valueTypes.length}_Consumer<${tupleType.valueTypes.map( `core`.toJavaType ).mkString( "," )}>
                           |""".stripMargin
                    val inputs = generateTupleParams( tupleType, lambda )
                    s"""
                       |new $consumerClazzName<${generateTupleTypes( tupleType )}>(){
                       |    @Override
                       |    void accept($inputs) throws Exception
                       |       ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                }
            case _ ⇒
                s"""
                   |new ${classOf[_1_Consumer[_]].getCanonicalName}<${core.toJavaType( inputType )}>(){
                   |  @Override
                   |  void accept(${core.toJavaType( inputType )} ${lambda.inputs.head}) throws Exception
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
        outputType == targetType && inputType.valueTypes.length == 2 && inputType.valueTypes.head == targetType && inputType.valueTypes.last == targetType
    }


    def translateFunction(inputType: DslType, outputType: DslType, lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
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
                           |new ${classOf[_IntFunction[_]].getCanonicalName}<${core.toJavaType( outputType )}>(){
                           |     @Override
                           |     ${core.toJavaType( outputType )} apply(int ${lambda.inputs.head}) throws Exception
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
                           |new ${classOf[_LongFunction[_]].getCanonicalName}<${core.toJavaType( outputType )}>(){
                           |     @Override
                           |     ${core.toJavaType( outputType )} apply(long ${lambda.inputs.head}) throws Exception
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
                           |new ${classOf[_DoubleFunction[_]].getCanonicalName}<${core.toJavaType( outputType )}>(){
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
                           |com.dongjiaqiang.jvm.dsl.java.core.lambda.function._${tupleType.valueTypes.length}_Function<${tupleType.valueTypes.map( `core`.toJavaType ).mkString( "," )}>
                           |""".stripMargin
                    val inputs = generateTupleParams( tupleType, lambda )
                    s"""
                       |new $functionClazzName<${generateTupleTypes( tupleType )}>(){
                       |    @Override
                       |    ${core.toJavaType( outputType )} apply($inputs) throws Exception
                       |       ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                }
            case _ ⇒
                s"""
                   |new ${classOf[_1_Function[_, _]].getCanonicalName}<${core.toJavaType( inputType )},${core.toJavaType( outputType )}>(){
                   |     @Override
                   |     ${core.toJavaType( outputType )} apply(${core.toJavaType( inputType )} ${lambda.inputs.head}) throws Exception
                   |         ${visitor.visit( lambda )}
                   |}
                   |""".stripMargin

        }
    }


    def translate(lambdaType: LambdaType, lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
        lambdaType.inputType match {
            case None ⇒
                translateSupplier( lambdaType.outputType, lambda, visitor )
            case Some( inputType ) ⇒
                lambdaType.outputType match {
                    case BoolType ⇒
                        translatePredicate( inputType, lambda, visitor )
                    case UnitType ⇒
                        translateConsumer( inputType, lambda, visitor )
                    case _ ⇒
                        translateFunction( inputType, lambdaType.outputType, lambda, visitor )
                }
        }
    }
}
