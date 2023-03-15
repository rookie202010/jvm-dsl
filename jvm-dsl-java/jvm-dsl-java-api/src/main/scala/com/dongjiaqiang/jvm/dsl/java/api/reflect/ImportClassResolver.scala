package com.dongjiaqiang.jvm.dsl.java.api.reflect

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.java.api.extend.{Either, Try}
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._
import com.dongjiaqiang.jvm.dsl.java.api.tuple._

import java.lang.reflect.{Field, Member, Method, Modifier}
import java.lang.{Boolean ⇒ JBoolean, Byte ⇒ JByte, Character ⇒ JChar, Double ⇒ JDouble, Float ⇒ JFloat, Integer ⇒ JInt, Long ⇒ JLong, Object ⇒ JObject, String ⇒ JString}
import java.util.concurrent.{CompletableFuture ⇒ JFuture}
import java.util.{List ⇒ JList, Map ⇒ JMap, Optional ⇒ JOptional, Set ⇒ JSet}
import scala.reflect.ClassTag
case class ImportClassResolver(classLoader: ClassLoader) {

  private def instanceClazz(clazz: Class[_], targetClazz: Class[_]): Boolean = {
    targetClazz.isAssignableFrom(clazz)
  }

  private def filterMembers[T <: Member : ClassTag](members: Array[T]): (Array[T], Array[T]) = {
    val validMembers = members.filter(field ⇒ (field.getModifiers & Modifier.PUBLIC) != 0)
      .groupBy(field ⇒ {
        (field.getModifiers & Modifier.STATIC) != 0
      })
    (validMembers.get(true) match {
      case Some(v) ⇒ v
      case None ⇒ Array[T]()
    }, validMembers.get(false) match {
      case Some(v) ⇒ v
      case None ⇒ Array[T]()
    })
  }

  private def resolveMethods(clazz:Class[_],
                              methods: Array[Method]) = {
    def help(method: Method): ImportClazzMethod = {
      val methodName = method.getName
      val params = method.getParameterTypes.map(`type` ⇒ if(clazz==`type`) ClazzType(clazz.getCanonicalName,Array()) else resolve(`type`))
      val returnType = resolve(method.getReturnType)
      ImportClazzMethod(methodName, params, returnType)
    }

    val (staticMethods, instanceMethods) = filterMembers(methods)
    (staticMethods.map(help), instanceMethods.map(help))
  }

  private def resolveFields(clazz:Class[_],
                            fields: Array[Field]): (Array[ImportClazzField], Array[ImportClazzField]) = {

    def help(field: Field): ImportClazzField = {
      ImportClazzField(field.getName, resolve(field.getType))
    }

    val (staticFields, instanceFields) = filterMembers(fields)
    (staticFields.map(help), instanceFields.map(help))
  }

  private def resolve(clazz: Class[_]): DslType = {
    clazz match {
      case JBoolean.TYPE ⇒ BoolType
      case JInt.TYPE ⇒ IntType
      case JLong.TYPE ⇒ LongType
      case JDouble.TYPE ⇒ DoubleType
      case JFloat.TYPE ⇒ FloatType
      case JChar.TYPE ⇒ CharType
      case JByte.TYPE ⇒ ByteType
      case Void.TYPE ⇒ UnitType
      case _ ⇒
        if (instanceClazz( clazz, classOf[JString] )) {
          StringType
        } else if (instanceClazz( clazz, classOf[JList[_]] )) {
          ListType( AnyType )
        } else if (instanceClazz( clazz, classOf[JSet[_]] )) {
          SetType( AnyType )
        } else if (instanceClazz( clazz, classOf[JMap[_, _]] )) {
          MapType( AnyType, AnyType )
        } else if (instanceClazz( clazz, classOf[JOptional[_]] )) {
          OptionType( AnyType )
        } else if (instanceClazz( clazz, classOf[Try[_]] )) {
          TryType( AnyType )
        } else if (instanceClazz( clazz, classOf[JFuture[_]] )) {
          FutureType( AnyType )
        } else if (instanceClazz( clazz, classOf[Either[_, _]] )) {
          EitherType( AnyType, AnyType )
        } else if (clazz.isArray) {
          ArrayType( resolve( clazz.getComponentType ) )
        } else if (instanceClazz( clazz, classOf[Tuple2[_, _]] )) {
          TupleType( ((0.until( 2 )).map( _ ⇒ AnyType ).toArray) )
        } else if (instanceClazz( clazz, classOf[Tuple3[_, _, _]] )) {
          TupleType( ((0.until( 3 )).map( _ ⇒ AnyType ).toArray) )
        } else if (instanceClazz( clazz, classOf[Tuple4[_, _, _, _]] )) {
          TupleType( ((0.until( 4 )).map( _ ⇒ AnyType ).toArray) )
        } else if (instanceClazz( clazz, classOf[Tuple5[_, _, _, _, _]] )) {
          TupleType( ((0.until( 5 )).map( _ ⇒ AnyType ).toArray) )
        } else if (instanceClazz( clazz, classOf[Tuple6[_, _, _, _, _, _]] )) {
          TupleType( ((0.until( 6 )).map( _ ⇒ AnyType ).toArray) )
        } else if (instanceClazz( clazz, classOf[Tuple7[_, _, _, _, _, _, _]] )) {
          TupleType( (0.until( 7 )).map( _ ⇒ AnyType ).toArray )
        } else if (instanceClazz( clazz, classOf[Tuple8[_, _, _, _, _, _, _, _]] )) {
          TupleType( (0.until( 8 )).map( _ ⇒ AnyType ).toArray )
        } else if (instanceClazz( clazz, classOf[Tuple9[_, _, _, _, _, _, _, _, _]] )) {
          TupleType( (0.until( 9 )).map( _ ⇒ AnyType ).toArray )
        } else if (instanceClazz( clazz, classOf[Tuple10[_, _, _, _, _, _, _, _, _, _]] )) {
          TupleType( (0.until( 10 )).map( _ ⇒ AnyType ).toArray )
        } else if (clazz == classOf[JObject]) {
          AnyType
        } else if (instanceClazz( clazz, classOf[_1_Consumer[_]] )) {
          LambdaType( Some( AnyType ), UnitType )
        } else if (instanceClazz( clazz, classOf[_2_Consumer[_, _]] )) {
          LambdaType( Some( TupleType( Array( AnyType, AnyType ) ) ), UnitType )
        } else if (instanceClazz( clazz, classOf[_3_Consumer[_, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 3 )).map( _ ⇒ AnyType ).toArray) ) ), UnitType )
        } else if (instanceClazz( clazz, classOf[_4_Consumer[_, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 4 )).map( _ ⇒ AnyType ).toArray) ) ), UnitType )
        } else if (instanceClazz( clazz, classOf[_5_Consumer[_, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 5 )).map( _ ⇒ AnyType ).toArray) ) ), UnitType )
        } else if (instanceClazz( clazz, classOf[_6_Consumer[_, _, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 6 )).map( _ ⇒ AnyType ).toArray) ) ), UnitType )
        } else if (instanceClazz( clazz, classOf[_7_Consumer[_, _, _, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 7 )).map( _ ⇒ AnyType ).toArray) ) ), UnitType )
        } else if (instanceClazz( clazz, classOf[_8_Consumer[_, _, _, _, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 8 )).map( _ ⇒ AnyType ).toArray) ) ), UnitType )
        } else if (instanceClazz( clazz, classOf[_DoubleConsumer] )) {
          LambdaType( Some( DoubleType ), UnitType )
        } else if (instanceClazz( clazz, classOf[_IntConsumer] )) {
          LambdaType( Some( IntType ), UnitType )
        } else if (instanceClazz( clazz, classOf[_LongConsumer] )) {
          LambdaType( Some( LongType ), UnitType )
        } else if (instanceClazz( clazz, classOf[_ObjDoubleConsumer[_]] )) {
          LambdaType( Some( TupleType( Array( AnyType, DoubleType ) ) ), UnitType )
        } else if (instanceClazz( clazz, classOf[_ObjIntConsumer[_]] )) {
          LambdaType( Some( TupleType( Array( AnyType, IntType ) ) ), UnitType )
        } else if (instanceClazz( clazz, classOf[_ObjLongConsumer[_]] )) {
          LambdaType( Some( TupleType( Array( AnyType, LongType ) ) ), UnitType )
        } else if (instanceClazz( clazz, classOf[_Runnable] )) {
          LambdaType( None, UnitType )
        } else if (instanceClazz( clazz, classOf[_1_Function[_, _]] )) {
          LambdaType( Some( AnyType ), AnyType )
        } else if (instanceClazz( clazz, classOf[_2_Function[_, _, _]] )) {
          LambdaType( Some( TupleType( Array( AnyType, AnyType ) ) ), AnyType )
        } else if (instanceClazz( clazz, classOf[_3_Function[_, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 3 )).map( _ ⇒ AnyType ).toArray) ) ), AnyType )
        } else if (instanceClazz( clazz, classOf[_4_Function[_, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 4 )).map( _ ⇒ AnyType ).toArray) ) ), AnyType )
        } else if (instanceClazz( clazz, classOf[_5_Function[_, _, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 5 )).map( _ ⇒ AnyType ).toArray) ) ), AnyType )
        } else if (instanceClazz( clazz, classOf[_6_Function[_, _, _, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 6 )).map( _ ⇒ AnyType ).toArray) ) ), AnyType )
        } else if (instanceClazz( clazz, classOf[_7_Function[_, _, _, _, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 7 )).map( _ ⇒ AnyType ).toArray) ) ), AnyType )
        } else if (instanceClazz( clazz, classOf[_8_Function[_, _, _, _, _, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 8 )).map( _ ⇒ AnyType ).toArray) ) ), AnyType )
        } else if (instanceClazz( clazz, classOf[_DoubleBinaryOperator] )) {
          LambdaType( Some( TupleType( Array( DoubleType, DoubleType ) ) ), DoubleType )
        } else if (instanceClazz( clazz, classOf[_DoubleFunction[_]] )) {
          LambdaType( Some( DoubleType ), AnyType )
        } else if (instanceClazz( clazz, classOf[_DoubleToIntFunction] )) {
          LambdaType( Some( DoubleType ), IntType )
        } else if (instanceClazz( clazz, classOf[_DoubleToLongFunction] )) {
          LambdaType( Some( DoubleType ), LongType )
        } else if (instanceClazz( clazz, classOf[_DoubleUnaryOperator] )) {
          LambdaType( Some( DoubleType ), DoubleType )
        } else if (instanceClazz( clazz, classOf[_IntBinaryOperator] )) {
          LambdaType( Some( TupleType( Array( IntType, IntType ) ) ), IntType )
        } else if (instanceClazz( clazz, classOf[_IntFunction[_]] )) {
          LambdaType( Some( IntType ), AnyType )
        } else if (instanceClazz( clazz, classOf[_IntToDoubleFunction] )) {
          LambdaType( Some( IntType ), DoubleType )
        } else if (instanceClazz( clazz, classOf[_IntToLongFunction] )) {
          LambdaType( Some( IntType ), LongType )
        } else if (instanceClazz( clazz, classOf[_IntUnaryOperator] )) {
          LambdaType( Some( IntType ), IntType )
        } else if (instanceClazz( clazz, classOf[_LongBinaryOperator] )) {
          LambdaType( Some( TupleType( Array( LongType, LongType ) ) ), LongType )
        } else if (instanceClazz( clazz, classOf[_LongFunction[_]] )) {
          LambdaType( Some( LongType ), AnyType )
        } else if (instanceClazz( clazz, classOf[_LongToDoubleFunction] )) {
          LambdaType( Some( LongType ), DoubleType )
        } else if (instanceClazz( clazz, classOf[_LongToIntFunction] )) {
          LambdaType( Some( LongType ), IntType )
        } else if (instanceClazz( clazz, classOf[_LongUnaryOperator] )) {
          LambdaType( Some( LongType ), LongType )
        } else if (instanceClazz( clazz, classOf[_1_Predicate[_]] )) {
          LambdaType( Some( AnyType ), BoolType )
        } else if (instanceClazz( clazz, classOf[_2_Predicate[_, _]] )) {
          LambdaType( Some( TupleType( Array( AnyType, AnyType ) ) ), BoolType )
        } else if (instanceClazz( clazz, classOf[_3_Predicate[_, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 3 )).map( _ ⇒ AnyType ).toArray) ) ), BoolType )
        } else if (instanceClazz( clazz, classOf[_4_Predicate[_, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 4 )).map( _ ⇒ AnyType ).toArray) ) ), BoolType )
        } else if (instanceClazz( clazz, classOf[_5_Predicate[_, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 5 )).map( _ ⇒ AnyType ).toArray) ) ), BoolType )
        } else if (instanceClazz( clazz, classOf[_6_Predicate[_, _, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 6 )).map( _ ⇒ AnyType ).toArray) ) ), BoolType )
        } else if (instanceClazz( clazz, classOf[_7_Predicate[_, _, _, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 7 )).map( _ ⇒ AnyType ).toArray) ) ), BoolType )
        } else if (instanceClazz( clazz, classOf[_8_Predicate[_, _, _, _, _, _, _, _]] )) {
          LambdaType( Some( TupleType( ((0.until( 8 )).map( _ ⇒ AnyType ).toArray) ) ), BoolType )
        } else if (instanceClazz( clazz, classOf[_DoublePredicate] )) {
          LambdaType( Some( DoubleType ), BoolType )
        } else if (instanceClazz( clazz, classOf[_IntPredicate] )) {
          LambdaType( Some( IntType ), BoolType )
        } else if (instanceClazz( clazz, classOf[_LongPredicate] )) {
          LambdaType( Some( LongType ), BoolType )
        } else if (instanceClazz( clazz, classOf[_1_Supplier[_]] )) {
          LambdaType( None, AnyType )
        } else if (instanceClazz( clazz, classOf[_BooleanSupplier] )) {
          LambdaType( None, BoolType )
        } else if (instanceClazz( clazz, classOf[_DoubleSupplier] )) {
          LambdaType( None, DoubleType )
        } else if (instanceClazz( clazz, classOf[_IntSupplier] )) {
          LambdaType( None, IntType )
        } else if (instanceClazz( clazz, classOf[_LongSupplier] )) {
          LambdaType( None, LongType )
        } else {
          ClazzType( clazz.getCanonicalName, Array( ) )
        }
    }
  }

  def resolve(name: String): ImportClazz = {
    val clazz = classLoader.loadClass(name)

    val (staticFields, instanceFields) = resolveFields( clazz, clazz.getFields )
    val (staticMethods, instanceMethods) = resolveMethods( clazz, clazz.getMethods.filterNot( m ⇒ m.getDeclaringClass == classOf[JObject] ) )
    ResolveClazz( name, instanceFields.map( f ⇒ (f.name, f) ).toMap
      , staticFields.map( f ⇒ (f.name, f) ).toMap,
      instanceMethods.map( m ⇒ (m.name, m) ).toMap
      , staticMethods.map( m ⇒ (m.name, m) ).toMap )
  }
}
