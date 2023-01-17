package com.dongjiaqiang.jvm.dsl.java

import com.dongjiaqiang.jvm.dsl.core.`type`._
import com.dongjiaqiang.jvm.dsl.java.core.lambda.consumer._
import com.dongjiaqiang.jvm.dsl.java.core.lambda.function._
import com.dongjiaqiang.jvm.dsl.java.core.lambda.predicate._
import com.dongjiaqiang.jvm.dsl.java.core.lambda.supplier._

import java.util.Optional
import java.util.concurrent.CompletableFuture


package object core {

  def toBasicType(dslType: DslType): String = {
    dslType match {
      case IntType ⇒ "int"
      case LongType ⇒ "long"
      case DoubleType ⇒ "double"
      case FloatType ⇒ "float"
      case BoolType ⇒ "boolean"
      case ByteType ⇒ "byte"
      case CharType ⇒ "char"
      case UnitType ⇒ "void"
      case _ ⇒ toJavaType( dslType )
    }
  }

  def toJavaType(dslType: DslType): String = {
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
        String.format( "%s<%s>", classOf[java.util.ArrayList[_]].getCanonicalName, toJavaType( `type`.valueType ) )
      case `type`: SetType ⇒
        String.format( "%s<%s>", classOf[java.util.HashSet[_]].getCanonicalName, toJavaType( `type`.valueType ) )
      case `type`: MapType ⇒
        String.format( "%s<%s,%s>", classOf[java.util.HashMap[_, _]].getCanonicalName, toJavaType( `type`.keyType ), toJavaType( `type`.valueType ) )
      case `type`: OptionType ⇒
        String.format( "%s<%s>", classOf[Optional[_]].getCanonicalName, toJavaType( `type`.valueType ) )
      case `type`: TupleType ⇒
        val sb = new StringBuffer( )
        sb.append( "com.dongjiaqiang.jvm.dsl.java.core.tuple.Tuple" )
        sb.append( `type`.valueTypes.length )
        sb.append( "<" )
        sb.append( `type`.valueTypes.map( `type` ⇒ {
          toJavaType( `type` )
        } ).mkString( "," ) )
        sb.append( ">" )
        sb.toString
      case `type`: ArrayType ⇒
        String.format( "%s[]", toJavaType( `type`.valueType ) )
      case `type`: FutureType ⇒
        String.format( "%s<%s>", classOf[CompletableFuture[_]].getCanonicalName, toJavaType( `type`.valueType ) )


      case `type`: ClazzType ⇒
        if (`type`.valueTypes.isEmpty) {
          `type`.clazzName
        } else {
          new StringBuffer( ).append( `type`.clazzName ).append( "<" ).append( `type`.valueTypes.map( `type` ⇒ {
            toJavaType( `type` )
          } ).mkString( "," ) ).append( ">" ).toString
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
              String.format( "%s<%s>", classOf[_1_Supplier[_]].getCanonicalName, toJavaType( dslType ) )
          }
        }

        def toFunctionType(tupleType: TupleType, resultType: DslType): String = {
          val clazzType = tupleType.valueTypes.length match {
            case 2 ⇒ classOf[_2_Function[_, _, _]].getCanonicalName
            case 3 ⇒ classOf[_3_Function[_, _, _, _]].getCanonicalName
            case 4 ⇒ classOf[_4_Function[_, _, _, _, _]].getCanonicalName
            case 5 ⇒ classOf[_5_Function[_, _, _, _, _, _]].getCanonicalName
            case 6 ⇒ classOf[_6_Function[_, _, _, _, _, _, _]].getCanonicalName
            case 7 ⇒ classOf[_7_Function[_, _, _, _, _, _, _, _]].getCanonicalName
            case 8 ⇒ classOf[_8_Function[_, _, _, _, _, _, _, _, _]].getCanonicalName
          }
          String.format( "%s<%s,%s>", clazzType, tupleType.valueTypes.map( toJavaType ).mkString( "," ), toJavaType( resultType ) )
        }

        `type`.inputType match {
          case Some( inputType ) ⇒
            inputType match {
              //supplier
              case UnitType ⇒ toSupplierType( `type`.outputType )
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
                        dslType.valueTypes.length match {
                          case 2 ⇒
                            dslType.valueTypes.last match {
                              case IntType ⇒
                                String.format( "%s<%s>", classOf[_ObjIntConsumer[_]].getCanonicalName,
                                  toJavaType( dslType.valueTypes.head ) )
                              case DoubleType ⇒
                                String.format( "%s<%s>", classOf[_ObjDoubleConsumer[_]].getCanonicalName,
                                  toJavaType( dslType.valueTypes.head ) )
                              case LongType ⇒
                                String.format( "%s<%s>", classOf[_ObjLongConsumer[_]].getCanonicalName,
                                  toJavaType( dslType.valueTypes.head ) )
                              case _ ⇒
                                String.format( "%s<%s,%s>", classOf[_2_Consumer[_, _]].getCanonicalName,
                                  toJavaType( dslType.valueTypes.head ),
                                  toJavaType( dslType.valueTypes.last )
                                )
                            }
                          case 3 ⇒
                            String.format( "%s<%s,%s,%s>", classOf[_3_Consumer[_, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                          case 4 ⇒
                            String.format( "%s<%s,%s,%s,%s>", classOf[_4_Consumer[_, _, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes( 2 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                          case 5 ⇒
                            String.format( "%s<%s,%s,%s,%s,%s>", classOf[_5_Consumer[_, _, _, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes( 2 ) ),
                              toJavaType( dslType.valueTypes( 3 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                          case 6 ⇒
                            String.format( "%s<%s,%s,%s,%s,%s,%s>", classOf[_6_Consumer[_, _, _, _, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes( 2 ) ),
                              toJavaType( dslType.valueTypes( 3 ) ),
                              toJavaType( dslType.valueTypes( 4 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                          case 7 ⇒
                            String.format( "%s<%s,%s,%s,%s,%s,%s,%s>", classOf[_7_Consumer[_, _, _, _, _, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes( 2 ) ),
                              toJavaType( dslType.valueTypes( 3 ) ),
                              toJavaType( dslType.valueTypes( 4 ) ),
                              toJavaType( dslType.valueTypes( 5 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                          case 8 ⇒
                            String.format( "%s<%s,%s,%s,%s,%s,%s,%s,%s>", classOf[_8_Consumer[_, _, _, _, _, _, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes( 2 ) ),
                              toJavaType( dslType.valueTypes( 3 ) ),
                              toJavaType( dslType.valueTypes( 4 ) ),
                              toJavaType( dslType.valueTypes( 5 ) ),
                              toJavaType( dslType.valueTypes( 6 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                        }
                      case dslType: DslType ⇒
                        String.format( "%s<%s>", classOf[_1_Consumer[_]].getCanonicalName, toJavaType( dslType ) )
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
                        dslType.valueTypes.length match {
                          case 2 ⇒
                            String.format( "%s<%s,%s>", classOf[_2_Predicate[_, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes.last )
                            )
                          case 3 ⇒
                            String.format( "%s<%s,%s,%s>", classOf[_3_Predicate[_, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                          case 4 ⇒
                            String.format( "%s<%s,%s,%s,%s>", classOf[_4_Predicate[_, _, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes( 2 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                          case 5 ⇒
                            String.format( "%s<%s,%s,%s,%s,%s>", classOf[_5_Predicate[_, _, _, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes( 2 ) ),
                              toJavaType( dslType.valueTypes( 3 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                          case 6 ⇒
                            String.format( "%s<%s,%s,%s,%s,%s,%s>", classOf[_6_Predicate[_, _, _, _, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes( 2 ) ),
                              toJavaType( dslType.valueTypes( 3 ) ),
                              toJavaType( dslType.valueTypes( 4 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                          case 7 ⇒
                            String.format( "%s<%s,%s,%s,%s,%s,%s,%s>", classOf[_7_Predicate[_, _, _, _, _, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes( 2 ) ),
                              toJavaType( dslType.valueTypes( 3 ) ),
                              toJavaType( dslType.valueTypes( 4 ) ),
                              toJavaType( dslType.valueTypes( 5 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                          case 8 ⇒
                            String.format( "%s<%s,%s,%s,%s,%s,%s,%s,%s>", classOf[_8_Predicate[_, _, _, _, _, _, _, _]].getCanonicalName,
                              toJavaType( dslType.valueTypes.head ),
                              toJavaType( dslType.valueTypes( 1 ) ),
                              toJavaType( dslType.valueTypes( 2 ) ),
                              toJavaType( dslType.valueTypes( 3 ) ),
                              toJavaType( dslType.valueTypes( 4 ) ),
                              toJavaType( dslType.valueTypes( 5 ) ),
                              toJavaType( dslType.valueTypes( 6 ) ),
                              toJavaType( dslType.valueTypes.last )
                            )
                        }
                      case dslType: DslType ⇒
                        String.format( "%s<%s>", classOf[_1_Predicate[_]].getCanonicalName, toJavaType( dslType ) )
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
                        if (tupleType.valueTypes.length == 2 && tupleType.valueTypes.head == IntType && tupleType.valueTypes.last == IntType) {
                          classOf[_IntBinaryOperator].getCanonicalName
                        } else {
                          toFunctionType( tupleType, IntType )
                        }
                      case _ ⇒
                        String.format( "%s<%s,%s>", classOf[_1_Function[_, _]].getCanonicalName, toJavaType( inputType ), toJavaType( IntType ) )
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
                        if (tupleType.valueTypes.length == 2 && tupleType.valueTypes.head == LongType && tupleType.valueTypes.last == LongType) {
                          classOf[_LongBinaryOperator].getCanonicalName
                        } else {
                          toFunctionType( tupleType, LongType )
                        }
                      case _ ⇒
                        String.format( "%s<%s,%s>", classOf[_1_Function[_, _]].getCanonicalName, toJavaType( inputType ), toJavaType( LongType ) )
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
                        if (tupleType.valueTypes.length == 2 && tupleType.valueTypes.head == DoubleType && tupleType.valueTypes.last == DoubleType) {
                          classOf[_DoubleBinaryOperator].getCanonicalName
                        } else {
                          toFunctionType( tupleType, DoubleType )
                        }
                      case _ ⇒
                        String.format( "%s<%s,%s>", classOf[_1_Function[_, _]].getCanonicalName, toJavaType( inputType ), toJavaType( DoubleType ) )
                    }
                  case _ ⇒
                    inputType match {
                      case IntType ⇒
                        String.format( "%s<%s>", classOf[_IntFunction[_]].getCanonicalName, toJavaType( `type`.outputType ) )
                      case LongType ⇒
                        String.format( "%s<%s>", classOf[_LongFunction[_]].getCanonicalName, toJavaType( `type`.outputType ) )
                      case DoubleType ⇒
                        String.format( "%s<%s>", classOf[_DoubleFunction[_]].getCanonicalName, toJavaType( `type`.outputType ) )
                      case tupleType: TupleType ⇒
                        toFunctionType( tupleType, `type`.outputType )
                      case _ ⇒
                        String.format( "%s<%s,%s>", classOf[_1_Function[_, _]].getCanonicalName, toJavaType( inputType ), toJavaType( `type`.outputType ) )
                    }
                }
            }
          case None ⇒
            //supplier
            toSupplierType( `type`.outputType )
        }
    }
  }
}
