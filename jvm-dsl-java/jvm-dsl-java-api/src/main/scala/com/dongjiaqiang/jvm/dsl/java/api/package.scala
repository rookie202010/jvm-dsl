package com.dongjiaqiang.jvm.dsl.java

import com.dongjiaqiang.jvm.dsl.api.`type`.{AnyType, ArrayType, BoolType, ByteType, CharType, ClazzType, DoubleType, DslType, FloatType, FutureType, IntType, LambdaType, ListType, LongType, MapType, OptionType, SetType, StringType, TupleType, UnitType}
import com.dongjiaqiang.jvm.dsl.java.api.exception.JavaTranslatorException
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer.{_1_Consumer, _2_Consumer, _3_Consumer, _4_Consumer, _5_Consumer, _6_Consumer, _7_Consumer, _8_Consumer, _DoubleConsumer, _IntConsumer, _LongConsumer, _ObjDoubleConsumer, _ObjIntConsumer, _ObjLongConsumer}
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function.{_1_Function, _2_Function, _3_Function, _4_Function, _5_Function, _6_Function, _7_Function, _8_Function, _DoubleBinaryOperator, _DoubleFunction, _DoubleToIntFunction, _DoubleToLongFunction, _DoubleUnaryOperator, _IntBinaryOperator, _IntFunction, _IntToDoubleFunction, _IntToLongFunction, _IntUnaryOperator, _LongBinaryOperator, _LongFunction, _LongToDoubleFunction, _LongToIntFunction, _LongUnaryOperator}
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate.{_1_Predicate, _2_Predicate, _3_Predicate, _4_Predicate, _5_Predicate, _6_Predicate, _7_Predicate, _8_Predicate, _DoublePredicate, _IntPredicate, _LongPredicate}
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier.{_1_Supplier, _BooleanSupplier, _DoubleSupplier, _IntSupplier, _LongSupplier}

import java.util.Optional
import java.util.concurrent.CompletableFuture

package object api {

  def toBasicType(dslType: DslType, javaTranslatorContext: JavaTranslatorContext): String = {
    dslType match {
      case IntType ⇒ "int"
      case LongType ⇒ "long"
      case DoubleType ⇒ "double"
      case FloatType ⇒ "float"
      case BoolType ⇒ "boolean"
      case ByteType ⇒ "byte"
      case CharType ⇒ "char"
      case UnitType ⇒ "void"
      case _ ⇒ toJavaType(dslType, javaTranslatorContext: JavaTranslatorContext)
    }
  }

  def toJavaType(dslType: DslType, javaTranslatorContext: JavaTranslatorContext): String = {
    dslType match {
      case AnyType ⇒ "Object"
      case IntType ⇒ "Integer"
      case LongType ⇒ "Long"
      case FloatType ⇒ "Float"
      case DoubleType ⇒ "Double"
      case CharType ⇒ "Character"
      case StringType ⇒ "String"
      case ByteType ⇒ "Byte"
      case BoolType ⇒ "Boolean"
      case UnitType ⇒ "Void"


      case `type`: ListType ⇒
        String.format("%s<%s>", classOf[java.util.ArrayList[_]].getCanonicalName, toJavaType(`type`.parameterType, javaTranslatorContext))
      case `type`: SetType ⇒
        String.format("%s<%s>", classOf[java.util.HashSet[_]].getCanonicalName, toJavaType(`type`.parameterType, javaTranslatorContext))
      case `type`: MapType ⇒
        String.format("%s<%s,%s>", classOf[java.util.HashMap[_, _]].getCanonicalName, toJavaType(`type`.keyParameterType, javaTranslatorContext),
          toJavaType(`type`.valueParameterType, javaTranslatorContext))
      case `type`: OptionType ⇒
        String.format("%s<%s>", classOf[Optional[_]].getCanonicalName, toJavaType(`type`.parameterType, javaTranslatorContext))
      case `type`: TupleType ⇒
        val sb = new StringBuffer()
        sb.append("com.dongjiaqiang.jvm.dsl.java.core.tuple.Tuple")
        sb.append(`type`.parameterTypes.length)
        sb.append("<")
        sb.append(`type`.parameterTypes.map(`type` ⇒ {
          toJavaType(`type`, javaTranslatorContext)
        }).mkString(","))
        sb.append(">")
        sb.toString
      case `type`: ArrayType ⇒
        String.format("%s[]", toJavaType(`type`.parameterType, javaTranslatorContext))
      case `type`: FutureType ⇒
        String.format("%s<%s>", classOf[CompletableFuture[_]].getCanonicalName,
          toJavaType(`type`.parameterType, javaTranslatorContext))


      case `type`: ClazzType ⇒
        if (`type`.parameterTypes.isEmpty) {
          `type`.clazzName
        } else {
          new StringBuffer().append(`type`.clazzName).append("<").append(`type`.parameterTypes.map(`type` ⇒ {
            toJavaType(`type`, javaTranslatorContext)
          }).mkString(",")).append(">").toString
        }


      case `type`: LambdaType ⇒

        def toSupplierType(dslType: DslType): String = {
          dslType match {
            case IntType ⇒
              classOf[_IntSupplier].getCanonicalName
            case LongType ⇒
              classOf[_LongSupplier].getCanonicalName
            case DoubleType ⇒
              classOf[_DoubleSupplier].getCanonicalName
            case BoolType ⇒
              classOf[_BooleanSupplier].getCanonicalName
            case dslType: DslType ⇒
              String.format("%s<%s>", classOf[_1_Supplier[_]].getCanonicalName,
                toJavaType(dslType, javaTranslatorContext))
          }
        }

        def toFunctionType(tupleType: TupleType, resultType: DslType): String = {
          val clazzType = tupleType.parameterTypes.length match {
            case 2 ⇒ classOf[_2_Function[_, _, _]].getCanonicalName
            case 3 ⇒ classOf[_3_Function[_, _, _, _]].getCanonicalName
            case 4 ⇒ classOf[_4_Function[_, _, _, _, _]].getCanonicalName
            case 5 ⇒ classOf[_5_Function[_, _, _, _, _, _]].getCanonicalName
            case 6 ⇒ classOf[_6_Function[_, _, _, _, _, _, _]].getCanonicalName
            case 7 ⇒ classOf[_7_Function[_, _, _, _, _, _, _, _]].getCanonicalName
            case 8 ⇒ classOf[_8_Function[_, _, _, _, _, _, _, _, _]].getCanonicalName
          }
          String.format("%s<%s,%s>", clazzType, tupleType.parameterTypes.map(t ⇒ toJavaType(t, javaTranslatorContext)).mkString(","),
            toJavaType(resultType, javaTranslatorContext))
        }

        `type`.mayInputType match {
          case Some(inputType) ⇒
            inputType match {
              //supplier
              case UnitType ⇒ toSupplierType(`type`.outputType)
              case _ ⇒
                `type`.outputType match {
                  case UnitType ⇒
                    //consumer
                    inputType match {
                      case IntType ⇒
                        classOf[_IntConsumer].getCanonicalName
                      case LongType ⇒
                        classOf[_LongConsumer].getCanonicalName
                      case DoubleType ⇒
                        classOf[_DoubleConsumer].getCanonicalName
                      case dslType: TupleType ⇒
                        dslType.parameterTypes.length match {
                          case 2 ⇒
                            dslType.parameterTypes.last match {
                              case IntType ⇒
                                String.format("%s<%s>", classOf[_ObjIntConsumer[_]].getCanonicalName,
                                  toJavaType(dslType.parameterTypes.head, javaTranslatorContext))
                              case DoubleType ⇒
                                String.format("%s<%s>", classOf[_ObjDoubleConsumer[_]].getCanonicalName,
                                  toJavaType(dslType.parameterTypes.head, javaTranslatorContext))
                              case LongType ⇒
                                String.format("%s<%s>", classOf[_ObjLongConsumer[_]].getCanonicalName,
                                  toJavaType(dslType.parameterTypes.head, javaTranslatorContext))
                              case _ ⇒
                                String.format("%s<%s,%s>", classOf[_2_Consumer[_, _]].getCanonicalName,
                                  toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                                  toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                                )
                            }
                          case 3 ⇒
                            String.format("%s<%s,%s,%s>", classOf[_3_Consumer[_, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                          case 4 ⇒
                            String.format("%s<%s,%s,%s,%s>", classOf[_4_Consumer[_, _, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(2), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                          case 5 ⇒
                            String.format("%s<%s,%s,%s,%s,%s>", classOf[_5_Consumer[_, _, _, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(2), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(3), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                          case 6 ⇒
                            String.format("%s<%s,%s,%s,%s,%s,%s>", classOf[_6_Consumer[_, _, _, _, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(2), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(3), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(4), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                          case 7 ⇒
                            String.format("%s<%s,%s,%s,%s,%s,%s,%s>", classOf[_7_Consumer[_, _, _, _, _, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(2), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(3), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(4), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(5), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                          case 8 ⇒
                            String.format("%s<%s,%s,%s,%s,%s,%s,%s,%s>", classOf[_8_Consumer[_, _, _, _, _, _, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(2), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(3), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(4), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(5), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(6), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                        }
                      case dslType: DslType ⇒
                        String.format("%s<%s>", classOf[_1_Consumer[_]].getCanonicalName, toJavaType(dslType, javaTranslatorContext))
                    }
                  case BoolType ⇒
                    //predicate
                    inputType match {
                      case DoubleType ⇒
                        classOf[_DoublePredicate].getCanonicalName
                      case IntType ⇒
                        classOf[_IntPredicate].getCanonicalName
                      case LongType ⇒
                        classOf[_LongPredicate].getCanonicalName
                      case dslType: TupleType ⇒
                        dslType.parameterTypes.length match {
                          case 2 ⇒
                            String.format("%s<%s,%s>", classOf[_2_Predicate[_, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                          case 3 ⇒
                            String.format("%s<%s,%s,%s>", classOf[_3_Predicate[_, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                          case 4 ⇒
                            String.format("%s<%s,%s,%s,%s>", classOf[_4_Predicate[_, _, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(2), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                          case 5 ⇒
                            String.format("%s<%s,%s,%s,%s,%s>", classOf[_5_Predicate[_, _, _, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(2), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(3), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                          case 6 ⇒
                            String.format("%s<%s,%s,%s,%s,%s,%s>", classOf[_6_Predicate[_, _, _, _, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(2), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(3), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(4), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                          case 7 ⇒
                            String.format("%s<%s,%s,%s,%s,%s,%s,%s>", classOf[_7_Predicate[_, _, _, _, _, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(2), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(3), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(4), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(5), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                          case 8 ⇒
                            String.format("%s<%s,%s,%s,%s,%s,%s,%s,%s>", classOf[_8_Predicate[_, _, _, _, _, _, _, _]].getCanonicalName,
                              toJavaType(dslType.parameterTypes.head, javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(1), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(2), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(3), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(4), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(5), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes(6), javaTranslatorContext),
                              toJavaType(dslType.parameterTypes.last, javaTranslatorContext)
                            )
                        }
                      case dslType: DslType ⇒
                        String.format("%s<%s>", classOf[_1_Predicate[_]].getCanonicalName, toJavaType(dslType, javaTranslatorContext))
                    }
                  case IntType ⇒
                    inputType match {
                      case IntType ⇒
                        classOf[_IntUnaryOperator].getCanonicalName
                      case LongType ⇒
                        classOf[_LongToIntFunction].getCanonicalName
                      case DoubleType ⇒
                        classOf[_DoubleToIntFunction].getCanonicalName
                      case tupleType: TupleType ⇒
                        if (tupleType.parameterTypes.length == 2 && tupleType.parameterTypes.head == IntType && tupleType.parameterTypes.last == IntType) {
                          classOf[_IntBinaryOperator].getCanonicalName
                        } else {
                          toFunctionType(tupleType, IntType)
                        }
                      case _ ⇒
                        String.format("%s<%s,%s>", classOf[_1_Function[_, _]].getCanonicalName, toJavaType(inputType, javaTranslatorContext),
                          toJavaType(IntType, javaTranslatorContext))
                    }
                  case LongType ⇒
                    inputType match {
                      case LongType ⇒
                        classOf[_LongUnaryOperator].getCanonicalName
                      case IntType ⇒
                        classOf[_IntToLongFunction].getCanonicalName
                      case DoubleType ⇒
                        classOf[_DoubleToLongFunction].getCanonicalName
                      case tupleType: TupleType ⇒
                        if (tupleType.parameterTypes.length == 2 && tupleType.parameterTypes.head == LongType && tupleType.parameterTypes.last == LongType) {
                          classOf[_LongBinaryOperator].getCanonicalName
                        } else {
                          toFunctionType(tupleType, LongType)
                        }
                      case _ ⇒
                        String.format("%s<%s,%s>", classOf[_1_Function[_, _]].getCanonicalName,
                          toJavaType(inputType, javaTranslatorContext), toJavaType(LongType, javaTranslatorContext))
                    }
                  case DoubleType ⇒
                    inputType match {
                      case DoubleType ⇒
                        classOf[_DoubleUnaryOperator].getCanonicalName
                      case IntType ⇒
                        classOf[_IntToDoubleFunction].getCanonicalName
                      case LongType ⇒
                        classOf[_LongToDoubleFunction].getCanonicalName
                      case tupleType: TupleType ⇒
                        if (tupleType.parameterTypes.length == 2 && tupleType.parameterTypes.head == DoubleType && tupleType.parameterTypes.last == DoubleType) {
                          classOf[_DoubleBinaryOperator].getCanonicalName
                        } else {
                          toFunctionType(tupleType, DoubleType)
                        }
                      case _ ⇒
                        String.format("%s<%s,%s>", classOf[_1_Function[_, _]].getCanonicalName,
                          toJavaType(inputType, javaTranslatorContext),
                          toJavaType(DoubleType, javaTranslatorContext))
                    }
                  case _ ⇒
                    inputType match {
                      case IntType ⇒
                        String.format("%s<%s>", classOf[_IntFunction[_]].getCanonicalName,
                          toJavaType(`type`.outputType, javaTranslatorContext))
                      case LongType ⇒
                        String.format("%s<%s>", classOf[_LongFunction[_]].getCanonicalName,
                          toJavaType(`type`.outputType, javaTranslatorContext))
                      case DoubleType ⇒
                        String.format("%s<%s>", classOf[_DoubleFunction[_]].getCanonicalName,
                          toJavaType(`type`.outputType, javaTranslatorContext))
                      case tupleType: TupleType ⇒
                        toFunctionType(tupleType, `type`.outputType)
                      case _ ⇒
                        String.format("%s<%s,%s>", classOf[_1_Function[_, _]].getCanonicalName,
                          toJavaType(inputType, javaTranslatorContext), toJavaType(`type`.outputType, javaTranslatorContext))
                    }
                }
            }
          case None ⇒
            //supplier
            toSupplierType(`type`.outputType)
        }
      case _⇒ javaTranslatorContext.customDslTypeTranslator.get(dslType) match {
        case Some(translator)⇒translator.translate()
        case _⇒ throw JavaTranslatorException(s"dslType ${dslType} cannot parse")
      }
    }
  }
}
