package com.dongjiaqiang.jvm.dsl.java.api.enhance

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, IntType, ListType, LongType, StringType}

import scala.collection.mutable.ArrayBuffer

case class Method(name:String,
                  inputParameters:Array[DslType],
                  outputParameter:DslType,
                  body:Option[()⇒String])
trait DslTypeEnhance {

  val dslType:DslType
  val sysMethods:Array[Method]
  val extendMethods:ArrayBuffer[Method] = ArrayBuffer()

  def addMethod(method: Method):Unit = extendMethods.append(method)
}

class IntTypeEnhance extends DslTypeEnhance {
  override val dslType: DslType = IntType
  override val sysMethods: Array[Method] = Array(
    Method("until",Array(IntType),ListType(IntType),None),
    Method("until",Array(IntType,IntType),ListType(IntType),None),
    Method("to",Array(IntType),ListType(IntType),None),
    Method("to",Array(IntType,IntType),ListType(IntType),None),
  )
}

class LongTypeEnhance extends DslTypeEnhance {
  override val dslType: DslType = LongType
  override val sysMethods: Array[Method] = Array(
    Method("until",Array(LongType),ListType(LongType),None),
    Method("until",Array(LongType,LongType),ListType(LongType),None),
    Method("to",Array(LongType),ListType(LongType),None),
    Method("to",Array(LongType,LongType),ListType(LongType),None),
  )
}

class StringTypeEnhance extends DslTypeEnhance{
  {
    com.dongjiaqiang.jvm.dsl.java.api.util.CodeUtils.mkString("xxss","##")
    "".mkString(".")
    "xsss".toLong
    "{} {}".format("x","y")
  }
  override val dslType: DslType = StringType
  override val sysMethods: Array[Method] = Array(
    Method("mkString",Array(StringType),StringType,None)
  )
}
