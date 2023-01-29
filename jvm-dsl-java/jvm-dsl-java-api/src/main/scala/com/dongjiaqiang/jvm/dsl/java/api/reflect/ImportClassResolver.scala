package com.dongjiaqiang.jvm.dsl.java.api.reflect

import com.dongjiaqiang.jvm.dsl.api.`type`.{AnyType, ArrayType, BoolType, ByteType, CharType, ClazzType, DoubleType, DslType, EitherType, FloatType, FutureType, IntType, LambdaType, ListType, LongType, MapType, OptionType, SetType, StringType, TryType, TupleType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.scope.{ImportClazz, ImportField, ImportMethod}
import com.dongjiaqiang.jvm.dsl.java.api.extend.{Either, Try}
import com.dongjiaqiang.jvm.dsl.java.api.tuple._

import java.lang.reflect.{Field, Member, Method, Modifier}
import java.lang.{Boolean ⇒ JBoolean}
import java.lang.{Integer ⇒ JInt}
import java.lang.{Long ⇒ JLong}
import java.lang.{Double ⇒ JDouble}
import java.lang.{Float ⇒ JFloat}
import java.lang.{String ⇒ JString}
import java.lang.{Character ⇒ JChar}
import java.lang.{Byte ⇒ JByte}
import java.util.{List ⇒ JList}
import java.util.{Set ⇒ JSet}
import java.util.{Map ⇒ JMap}
import java.lang.{Object ⇒ JObject}
import java.util.{Optional ⇒ JOptional}
import java.util.concurrent.{CompletableFuture ⇒ JFuture}
import scala.collection.mutable.{ListMap ⇒ MutableMap}
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

  private def resolveMethods(methods: Array[Method], importedClasses: MutableMap[String, ImportClazz]): (Array[ImportMethod], Array[ImportMethod]) = {
    def help(method: Method): ImportMethod = {
      val methodName = method.getName
      val params = method.getParameterTypes.map(`type` ⇒ resolve(`type`, importedClasses))
      val returnType = resolve(method.getReturnType, importedClasses)
      ImportMethod(methodName, params, returnType)
    }

    val (staticMethods, instanceMethods) = filterMembers(methods)
    (staticMethods.map(help), instanceMethods.map(help))
  }

  private def resolveFields(fields: Array[Field],
                            importedClasses: MutableMap[String, ImportClazz]): (Array[ImportField], Array[ImportField]) = {

    def help(field: Field): ImportField = {
      ImportField(field.getName, resolve(field.getType, importedClasses))
    }

    val (staticFields, instanceFields) = filterMembers(fields)
    (staticFields.map(help), instanceFields.map(help))
  }

  private def resolve(clazz: Class[_], importedClasses: MutableMap[String, ImportClazz]): DslType = {
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
        if (instanceClazz(clazz, classOf[JString])) {
          StringType
        } else if (instanceClazz(clazz, classOf[JList[_]])) {
          ListType(AnyType)
        } else if (instanceClazz(clazz, classOf[JSet[_]])) {
          SetType(AnyType)
        } else if (instanceClazz(clazz, classOf[JMap[_, _]])) {
          MapType(AnyType, AnyType)
        } else if (instanceClazz(clazz, classOf[JOptional[_]])) {
          OptionType(AnyType)
        } else if (instanceClazz(clazz, classOf[Try[_]])) {
          TryType(AnyType)
        } else if (instanceClazz(clazz, classOf[JFuture[_]])) {
          FutureType(AnyType)
        } else if (instanceClazz(clazz, classOf[Either[_, _]])) {
          EitherType(AnyType, AnyType)
        } else if (clazz.isArray) {
          ArrayType(resolve(clazz.getComponentType, importedClasses))
        } else if (instanceClazz(clazz, classOf[Tuple2[_, _]])) {
          TupleType(((0.until(2)).map(_ ⇒ AnyType).toArray))
        } else if (instanceClazz(clazz, classOf[Tuple3[_, _, _]])) {
          TupleType(((0.until(3)).map(_ ⇒ AnyType).toArray))
        } else if (instanceClazz(clazz, classOf[Tuple4[_, _, _, _]])) {
          TupleType(((0.until(4)).map(_ ⇒ AnyType).toArray))
        } else if (instanceClazz(clazz, classOf[Tuple5[_, _, _, _, _]])) {
          TupleType(((0.until(5)).map(_ ⇒ AnyType).toArray))
        } else if (instanceClazz(clazz, classOf[Tuple6[_, _, _, _, _, _]])) {
          TupleType(((0.until(6)).map(_ ⇒ AnyType).toArray))
        } else if (instanceClazz(clazz, classOf[Tuple7[_, _, _, _, _, _, _]])) {
          TupleType((0.until(7)).map(_ ⇒ AnyType).toArray)
        } else if (instanceClazz(clazz, classOf[Tuple8[_, _, _, _, _, _, _, _]])) {
          TupleType((0.until(8)).map(_ ⇒ AnyType).toArray)
        } else if (instanceClazz(clazz, classOf[Tuple9[_, _, _, _, _, _, _, _, _]])) {
          TupleType((0.until(9)).map(_ ⇒ AnyType).toArray)
        } else if (instanceClazz(clazz, classOf[Tuple10[_, _, _, _, _, _, _, _, _, _]])) {
          TupleType((0.until(10)).map(_ ⇒ AnyType).toArray)
        } else if (clazz==classOf[JObject]) {
          AnyType
        } else {
          val clazzName = clazz.getCanonicalName
          val (staticFields, instanceFields) = resolveFields(clazz.getFields, importedClasses)
          val (staticMethods, instanceMethods) = resolveMethods(clazz.getMethods.filterNot(m⇒m.getDeclaringClass==classOf[JObject]), importedClasses)
          val importClazz = ImportClazz(clazzName, instanceFields.map(f ⇒ (f.name, f)).toMap
            , staticFields.map(f ⇒ (f.name, f)).toMap,
            instanceMethods.map(m ⇒ (m.name, m)).toMap
            , staticMethods.map(m ⇒ (m.name, m)).toMap)
          importedClasses.put(importClazz.name, importClazz)
          ClazzType(clazzName, Array())
        }
    }
  }

  def resolve(name: String): Map[String, ImportClazz] = {
    val clazz = classLoader.loadClass(name)
    val importedClasses = MutableMap[String, ImportClazz]()
    resolve(clazz,importedClasses)
    importedClasses.toMap
  }
}
