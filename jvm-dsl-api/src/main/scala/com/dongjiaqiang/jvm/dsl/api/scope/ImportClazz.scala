package com.dongjiaqiang.jvm.dsl.api.scope

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
case class ImportField(name:String,dslType: DslType)
case class ImportMethod(name:String,params:Array[DslType],returnType:DslType)
case class ImportClazz(name:String,
                       fields:Map[String,ImportField],
                       staticFields:Map[String,ImportField],
                       methods:Map[String,ImportMethod],
                       staticMethods:Map[String,ImportMethod])
