package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api
import com.dongjiaqiang.jvm.dsl.java.api.codes._SYS_STR_CODES.{_2_CharToCharFunction, _CharConsumer, _CharPredicate, _CharToCharFunction, _CharToObjectFunction}
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate.{_1_Predicate, _DoublePredicate, _IntPredicate, _LongPredicate}
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._

import java.util.Comparator


trait LambdaToAnonymousClassTranslator{
    def translate(lambdaType: LambdaType,
                  lambda: Lambda,
                  javaTranslatorContext: JavaTranslatorContext,
                  visitor: ExpressionVisitor[String],
                  genericErasure:Boolean = false):String
}


object MonadLambdaToAnonymousClassTranslator extends LambdaToAnonymousClassTranslator{
    override def translate(lambdaType: LambdaType,
                           lambda: Lambda,
                           javaTranslatorContext: JavaTranslatorContext,
                           visitor: ExpressionVisitor[String], genericErasure: Boolean): String = {
        LambdaToAnonymousClassTranslator.translate(lambdaType,lambda,javaTranslatorContext,visitor,genericErasure,unpack = false)
    }
}
object StringMonadLambdaToAnonymousClassTranslator extends LambdaToAnonymousClassTranslator {

    override def translate(lambdaType: LambdaType,
                           lambda: Lambda,
                           javaTranslatorContext: JavaTranslatorContext,
                           visitor: ExpressionVisitor[String],
                           genericErasure: Boolean = false): String = {
        lambdaType.mayInputType match {
            case Some( inputDslType ) ⇒
                lambdaType.outputType match {
                    case BoolType ⇒
                        //translator predicate lambda expression
                        s"""
                           |new ${classOf[_CharPredicate].getCanonicalName}(){
                           |    @Override
                           |    public boolean test(char ${lambda.inputs.head}) throws Exception
                           |        ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                    case UnitType ⇒
                        //translator consumer lambda expression
                        s"""
                           |new ${classOf[_CharConsumer].getCanonicalName}(){
                           |  @Override
                           |  public void accept(char ${lambda.inputs.head}) throws Exception
                           |      ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin
                    //translate function lambda expression
                    case CharType ⇒
                        inputDslType match {
                            case TupleType( _ ) ⇒
                                s"""
                                   |new ${classOf[_2_CharToCharFunction].getCanonicalName}(){
                                   |    @Override
                                   |    public char apply(char ${lambda.inputs.head},char ${lambda.inputs.last}) throws Exception
                                   |         ${visitor.visit( lambda )}
                                   |}
                                   |"""
                            case _ ⇒
                                s"""
                                   |new ${classOf[_CharToCharFunction].getCanonicalName}(){
                                   |    @Override
                                   |    public char apply(char ${lambda.inputs.head}) throws Exception
                                   |         ${visitor.visit( lambda )}
                                   |}
                                   |""".stripMargin
                        }
                    //translate function lambda expression
                    case _ ⇒
                        val inputType = if (genericErasure) {
                            "Object"
                        } else {
                            api.toJavaType( inputDslType, javaTranslatorContext )
                        }
                        s"""
                           |new ${classOf[_CharToObjectFunction[_]].getCanonicalName}<$inputType>(){
                           |    @Override
                           |    public $inputType apply(char ${lambda.inputs.head}) throws Exception
                           |         ${visitor.visit( lambda )}
                           |}
                           |""".stripMargin

                }
        }
    }
}

object LambdaToAnonymousClassTranslator {

    def translateComparator(visitor: ExpressionVisitor[String],
                            javaTranslatorContext: JavaTranslatorContext,
                            lambda: Lambda,
                            calleeType: DslType = AnyType): String = {
        s"""
           |new ${classOf[Comparator[_]].getCanonicalName}<Object>(){
           |    @Override
           |    public int compare(${api.toJavaType( calleeType, javaTranslatorContext )} ${lambda.inputs.head},${api.toJavaType( calleeType, javaTranslatorContext )} ${lambda.inputs.last})
           |        ${visitor.visit( lambda )}
           |}
           |""".stripMargin
    }

    private def generateTupleParams(tupleType: TupleType, lambda: Lambda, javaTranslatorContext: JavaTranslatorContext, genericErasure: Boolean): String = {
        tupleType.parameterTypes.zip( lambda.inputs ).map {
            case (dslType, name) ⇒
                val typeStr = generateType(dslType,javaTranslatorContext,genericErasure)
                s"$typeStr $name"
        }.mkString( "," )
    }

    private def generateTupleTypes(tupleType: TupleType, javaTranslatorContext: JavaTranslatorContext, genericErasure: Boolean): String = {
        tupleType.parameterTypes.map( t ⇒ {
            if (genericErasure) {
                "Object"
            } else {
                api.toJavaType( t, javaTranslatorContext )
            }
        } ).mkString( "," )
    }

    private def generateType(dslType: DslType,javaTranslatorContext: JavaTranslatorContext,genericErasure:Boolean):String={
        if(genericErasure){
            "Object"
        }else{
            api.toJavaType( dslType, javaTranslatorContext )
        }
    }

    private def translateSupplier(outputType: DslType,
                                  lambda: Lambda,
                                  javaTranslatorContext: JavaTranslatorContext,
                                  visitor: ExpressionVisitor[String],
                                  genericErasure: Boolean = false,
                                  unpack: Boolean = true): String = {
        def translateGenericSupplier(): String = {
            outputType match {
                case UnitType⇒
                    s"""
                       |new ${classOf[_Runnable].getCanonicalName}(){
                       |    @Override
                       |    public void run() throws Exception
                       |          ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case _⇒
                    val typeStr = generateType(outputType,javaTranslatorContext,genericErasure)
                    s"""
                       |new ${classOf[_1_Supplier[_]].getCanonicalName}<$typeStr>(){
                       |    @Override
                       |    public $typeStr get() throws Exception
                       |      ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
            }
        }
        if (unpack) {
            outputType match {
                case IntType ⇒
                    s"""
                       |new ${classOf[_IntSupplier].getCanonicalName}(){
                       |    @Override
                       |    public int getAsInt() throws Exception
                       |        ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case LongType ⇒
                    s"""
                       |new ${classOf[_LongSupplier].getCanonicalName}(){
                       |    @Override
                       |    public long getAsLong() throws Exception
                       |      ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case DoubleType ⇒
                    s"""
                       |new ${classOf[_DoubleSupplier].getCanonicalName}(){
                       |  @Override
                       |  public double getAsDouble() throws Exception
                       |      ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case BoolType ⇒
                    s"""
                       |new ${classOf[_BooleanSupplier].getCanonicalName}(){
                       |    @Override
                       |    public boolean getAsBoolean() throws Exception
                       |      ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case _ ⇒
                    translateGenericSupplier()
            }
        } else {
            translateGenericSupplier()
        }
    }

    private def translatePredicate(inputType: DslType,
                                   lambda: Lambda,
                                   javaTranslatorContext: JavaTranslatorContext,
                                   visitor: ExpressionVisitor[String],
                                   genericErasure: Boolean,
                                   unpack: Boolean): String = {
        def translateGenericPredicate(): String = {
            inputType match {
                case tupleType: TupleType ⇒
                    val predicateClazzName =
                        s"""
                           |com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._${tupleType.parameterTypes.length}_Predicate
                           |""".stripMargin
                    val inputs = generateTupleParams( tupleType, lambda, javaTranslatorContext, genericErasure )
                    s"""
                       |new $predicateClazzName<${generateTupleTypes( tupleType, javaTranslatorContext, genericErasure )}>(){
                       |    @Override
                       |    public boolean test($inputs) throws Exception
                       |       ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case _ ⇒
                    val typeStr = generateType(inputType,javaTranslatorContext, genericErasure)
                    s"""
                       |new ${classOf[_1_Predicate[_]].getCanonicalName}<$typeStr>(){
                       |    @Override
                       |    public boolean test($typeStr ${lambda.inputs.head}) throws Exception
                       |      ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
            }
        }
        if (unpack) {
            inputType match {
                case IntType ⇒
                    s"""
                       |new ${classOf[_IntPredicate].getCanonicalName}(){
                       |    @Override
                       |    public boolean test(int ${lambda.inputs.head}) throws Exception
                       |        ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case LongType ⇒
                    s"""
                       |new ${classOf[_LongPredicate].getCanonicalName}(){
                       |    @Override
                       |    public boolean test(long ${lambda.inputs.head}) throws Exception
                       |        ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case DoubleType ⇒
                    s"""
                       |new ${classOf[_DoublePredicate].getCanonicalName}(){
                       |    @Override
                       |    public boolean test(double ${lambda.inputs.head}) throws Exception
                       |        ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case _ ⇒ translateGenericPredicate( )
            }
        } else {
            translateGenericPredicate( )
        }
    }

    private def translateConsumer(inputType: DslType,
                                  lambda: Lambda,
                                  javaTranslatorContext: JavaTranslatorContext,
                                  visitor: ExpressionVisitor[String],
                                  genericErasure: Boolean,
                                  unpack: Boolean): String = {
        def translateTuple2Consumer(dslType1: DslType, dslType2: DslType): String = {
            val type1 = generateType(dslType1,javaTranslatorContext, genericErasure)
            val type2 = generateType(dslType2,javaTranslatorContext, genericErasure)
            s"""
               |new ${classOf[_2_Consumer[_, _]].getCanonicalName}<$type1,$type2>(){
               |  @Override
               |  public void accept($type1, $type2) throws Exception
               |      ${visitor.visit( lambda )}
               |}
               |""".stripMargin
        }
        def translateTupleConsumer(tupleType:TupleType): String = {
            val consumerClazzName =
                s"""
                   |com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._${tupleType.parameterTypes.length}_Consumer
                   |""".stripMargin
            val inputs = generateTupleParams( tupleType, lambda, javaTranslatorContext, genericErasure )
            s"""
               |new $consumerClazzName<${generateTupleTypes( tupleType, javaTranslatorContext, genericErasure )}>(){
               |    @Override
               |    public void accept($inputs) throws Exception
               |       ${visitor.visit( lambda )}
               |}
               |""".stripMargin
        }
        def translateGenericConsumer():String={
            val typeStr = generateType(inputType,javaTranslatorContext, genericErasure)
            s"""
               |new ${classOf[_1_Consumer[_]].getCanonicalName}<$typeStr>(){
               |  @Override
               |  public void accept($typeStr ${lambda.inputs.head}) throws Exception
               |      ${visitor.visit( lambda )}
               |}
               |""".stripMargin
        }
        if(unpack) {
            inputType match {
                case IntType ⇒
                    s"""
                       |new ${classOf[_IntConsumer]}(){
                       |    @Override
                       |    public void accept(int ${lambda.inputs.head}) throws Exception
                       |         ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case DoubleType ⇒
                    s"""
                       |new ${classOf[_DoubleConsumer]}(){
                       |    @Override
                       |    public void accept(double ${lambda.inputs.head}) throws Exception
                       |         ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case LongType ⇒
                    s"""
                       |new ${classOf[_LongConsumer]}(){
                       |    @Override
                       |    public void accept(long ${lambda.inputs.head}) throws Exception
                       |       ${visitor.visit( lambda )}
                       |}
                       |""".stripMargin
                case tupleType: TupleType ⇒
                    if (tupleType.parameterTypes.length == 2) {
                        val typeStr = generateType(tupleType.parameterTypes.head,javaTranslatorContext, genericErasure)
                        tupleType.parameterTypes.last match {
                            case DoubleType ⇒
                                s"""
                                   |new ${classOf[_ObjDoubleConsumer[_]].getCanonicalName}<$typeStr>(){
                                   |  @Override
                                   |  public void accept($typeStr ${lambda.inputs.head}, double ${lambda.inputs.last}) throws Exception
                                   |      ${visitor.visit( lambda )}
                                   |}
                                   |""".stripMargin
                            case IntType ⇒
                                s"""
                                   |new ${classOf[_ObjIntConsumer[_]].getCanonicalName}<$typeStr>(){
                                   |  @Override
                                   |  public void accept($typeStr ${lambda.inputs.head}, int ${lambda.inputs.last}) throws Exception
                                   |      ${visitor.visit( lambda )}
                                   |}
                                   |""".stripMargin
                            case LongType ⇒
                                s"""
                                   |new ${classOf[_ObjLongConsumer[_]].getCanonicalName}<$typeStr>(){
                                   |  @Override
                                   |  public void accept($typeStr ${lambda.inputs.head}, int ${lambda.inputs.last}) throws Exception
                                   |      ${visitor.visit( lambda )}
                                   |}
                                   |""".stripMargin
                            case _ ⇒
                                translateTuple2Consumer( tupleType.parameterTypes.head, tupleType.parameterTypes.last )
                        }
                    } else {
                        translateTupleConsumer( tupleType )
                    }
                case _ ⇒
                    translateGenericConsumer()
            }
        }else{
            inputType match {
                case tupleType: TupleType⇒ translateTupleConsumer(tupleType)
                case _⇒ translateGenericConsumer()
            }
        }
    }

    private def translateDoubleBinaryOperator(lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
        s"""
           |new ${classOf[_DoubleBinaryOperator].getCanonicalName}(){
           |     @Override
           |     public double applyAsDouble(double ${lambda.inputs.head}, double ${lambda.inputs.last}) throws Exception
           |         ${visitor.visit( lambda )}
           |}
           |""".stripMargin
    }

    private def translateIntBinaryOperator(lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
        s"""
           |new ${classOf[_IntBinaryOperator].getCanonicalName}(){
           |     @Override
           |     public int applyAsInt((int ${lambda.inputs.head}, int ${lambda.inputs.last}) throws Exception
           |         ${visitor.visit( lambda )}
           |}
           |""".stripMargin
    }

    private def translateLongBinaryOperator(lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
        s"""
           |new ${classOf[_LongBinaryOperator].getCanonicalName}(){
           |     @Override
           |     public long applyAsLong(long ${lambda.inputs.head}, long ${lambda.inputs.last}) throws Exception
           |         ${visitor.visit( lambda )}
           |}
           |""".stripMargin
    }

    private def basicBinaryOperator(inputType: TupleType, outputType: DslType, targetType: DslType): Boolean = {
        outputType == targetType && inputType.parameterTypes.length == 2 && inputType.parameterTypes.head == targetType && inputType.parameterTypes.last == targetType
    }

    private def translateFunction(inputType: DslType,
                                  outputType: DslType,
                                  lambda: Lambda,
                                  javaTranslatorContext: JavaTranslatorContext,
                                  visitor: ExpressionVisitor[String],
                                  genericErasure: Boolean,
                                  unpack: Boolean): String = {
        def translateTupleFunction(tupleType: TupleType):String={
            val functionClazzName =
                s"""
                   |com.dongjiaqiang.jvm.dsl.java.api.lambda.function._${tupleType.parameterTypes.length}_Function
                   |""".stripMargin
            val inputs = generateTupleParams( tupleType, lambda, javaTranslatorContext, genericErasure )
            val typeStr = generateType(outputType,javaTranslatorContext, genericErasure)
            s"""
               |new $functionClazzName<${generateTupleTypes(  TupleType(tupleType.parameterTypes:+outputType), javaTranslatorContext, genericErasure )}>(){
               |    @Override
               |    public $typeStr apply($inputs) throws Exception
               |       ${visitor.visit( lambda )}
               |}
               |""".stripMargin
        }
        def translateGenericFunction():String={
            val typeIn = generateType(inputType,javaTranslatorContext, genericErasure)
            val typeOut = generateType(outputType,javaTranslatorContext, genericErasure)
            s"""
               |new ${classOf[_1_Function[_, _]].getCanonicalName}<$typeIn,$typeOut>(){
               |     @Override
               |     public $typeOut apply($typeIn ${lambda.inputs.head}) throws Exception
               |         ${visitor.visit( lambda )}
               |}
               |""".stripMargin
        }
        if(unpack) {
            inputType match {
                case IntType ⇒
                    outputType match {
                        case IntType ⇒
                            s"""
                               |new ${classOf[_IntUnaryOperator].getCanonicalName}(){
                               |    @Override
                               |    public int applyAsInt(int ${lambda.inputs.head}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case DoubleType ⇒
                            s"""
                               |new ${classOf[_IntToDoubleFunction].getCanonicalName}(){
                               |    @Override
                               |    public double applyAsDouble(int ${lambda.inputs.head}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case LongType ⇒
                            s"""
                               |new ${classOf[_IntToLongFunction].getCanonicalName}(){
                               |    @Override
                               |    public long applyAsLong(int ${lambda.inputs.head}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case _ ⇒
                            val typeStr = generateType( outputType, javaTranslatorContext, genericErasure )
                            s"""
                               |new ${classOf[_IntFunction[_]].getCanonicalName}<$typeStr>(){
                               |     @Override
                               |     public $typeStr apply(int ${lambda.inputs.head}) throws Exception
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
                               |    public long applyAsLong(long ${lambda.inputs.head}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case DoubleType ⇒
                            s"""
                               |new ${classOf[_LongToDoubleFunction].getCanonicalName}(){
                               |    @Override
                               |    public double applyAsDouble(int ${lambda.inputs.head}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case LongType ⇒
                            s"""
                               |new ${classOf[_LongUnaryOperator].getCanonicalName}(){
                               |    @Override
                               |    public long applyAsLong(int ${lambda.inputs.head}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case _ ⇒
                            val typeStr = generateType( outputType, javaTranslatorContext, genericErasure )
                            s"""
                               |new ${classOf[_LongFunction[_]].getCanonicalName}<$typeStr>(){
                               |     @Override
                               |     public $typeStr apply(long ${lambda.inputs.head}) throws Exception
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
                               |    public int applyAsInt(double ${lambda.inputs.head}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case DoubleType ⇒
                            s"""
                               |new ${classOf[_DoubleUnaryOperator].getCanonicalName}(){
                               |    @Override
                               |    public double applyAsDouble(double ${lambda.inputs.head}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case LongType ⇒
                            s"""
                               |new ${classOf[_DoubleToLongFunction].getCanonicalName}(){
                               |    @Override
                               |    public long applyAsLong(double ${lambda.inputs.head}) throws Exception
                               |      ${visitor.visit( lambda )}
                               |}
                               |""".stripMargin
                        case _ ⇒
                            val typeStr = generateType( outputType, javaTranslatorContext, genericErasure )
                            s"""
                               |new ${classOf[_DoubleFunction[_]].getCanonicalName}<$typeStr>(){
                               |     @Override
                               |     public $typeStr apply(double ${lambda.inputs.head}) throws Exception
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
                        translateTupleFunction( tupleType )
                    }
                case _ ⇒
                    translateGenericFunction( )
            }
        }else{
            inputType match {
                case tupleType: TupleType⇒ translateTupleFunction(tupleType)
                case _⇒ translateGenericFunction()
            }
        }
    }


    /**
     * translate lambda expression into java anonymous class implement
     * @param lambdaType lambda type
     * @param lambda lambda expression
     * @param javaTranslatorContext java translator context
     * @param visitor visitor
     * @param genericErasure should erasure generic type
     * @param unpack should unpack basic type
     * @return
     */
    def translate(lambdaType: LambdaType,
                  lambda: Lambda,
                  javaTranslatorContext: JavaTranslatorContext,
                  visitor: ExpressionVisitor[String],
                  genericErasure:Boolean = false,
                  unpack:Boolean = true): String = {
        lambdaType.mayInputType match {
            case None ⇒
                //translator supplier lambda expression
                translateSupplier( lambdaType.outputType, lambda, javaTranslatorContext,visitor,genericErasure,unpack )
            case Some( inputType ) ⇒
                lambdaType.outputType match {
                    case BoolType ⇒
                        //translator predicate lambda expression
                        translatePredicate( inputType, lambda,javaTranslatorContext, visitor ,genericErasure,unpack)
                    case UnitType ⇒
                        //translator consumer lambda expression
                        translateConsumer( inputType, lambda,javaTranslatorContext, visitor,genericErasure,unpack )
                    case _ ⇒
                        //translate function lambda expression
                        translateFunction( inputType, lambdaType.outputType, lambda, javaTranslatorContext,visitor,genericErasure, unpack )
                }
        }
    }
}
