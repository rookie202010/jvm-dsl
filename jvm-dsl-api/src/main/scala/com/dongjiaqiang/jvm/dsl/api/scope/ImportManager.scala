package com.dongjiaqiang.jvm.dsl.api.scope

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParserException

import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap, Set ⇒ MSet}

case class ResolveField(name:String,dslType: DslType)
case class ResolveMethod(name:String, params:Array[DslType], returnType:DslType)
case class ResolveClazz(name:String,
                        fields:Map[String,ResolveField],
                        staticFields:Map[String,ResolveField],
                        methods:Map[String,ResolveMethod],
                        staticMethods:Map[String,ResolveMethod])

class ImportManager {

  var classResolver:String⇒ResolveClazz = _

  val classes: ArrayBuffer[String] = ArrayBuffer( )

  val simpleClassRefs:MutableMap[String,MSet[String]] = MutableMap()

  val resolveClasses: MutableMap[String,ResolveClazz] = MutableMap()

  val classesFromPackage: MutableMap[String, String] = MutableMap( )

  val packages: MutableMap[String, String] = MutableMap( )

  override def equals(obj: Any): Boolean = {
      obj match {
        case importManager: ImportManager⇒
            importManager.classes == classes &&
              (importManager.classesFromPackage sameElements  classesFromPackage) &&
              (importManager.packages sameElements  packages)
      }
  }

  def addClass(className: String): Unit = {
    val simpleClazzName = className.split("\\.").last
    classes.append( className )

    simpleClassRefs.getOrElseUpdate(simpleClazzName,
      MSet(className)
    ).add(className)
  }

  def addClass(className:String, packageName:String):Unit={
    val simpleClazzName = className.split("\\.").last
    classesFromPackage.put(className,packageName)

    simpleClassRefs.getOrElseUpdate( simpleClazzName,
      MSet( className )
     ).add( className )
  }

  def addPackage(packageName:String,packageLocation: String): Unit = {
    packages.put(packageName,packageLocation)
  }

  def containClass(clazz:String):Boolean={
      if(simpleClassRefs.contains(clazz)){
         if(simpleClassRefs.get(clazz).size>1){
              throw ExpressionParserException(s"simple clazz $clazz ref more clazz: ${simpleClassRefs.get(clazz).mkString(";")}")
         }else{
              return true
         }
      }
      classes.contains(clazz) || classesFromPackage.contains(clazz)
  }

  def registerClassResolver(resolver:String⇒ResolveClazz):Unit={
      classResolver = resolver
  }

  def resolve(clazz:String):ResolveClazz={
      if(classResolver==null){
        throw ExpressionParserException("class resolver is not register")
      }
      val clazzName = if(simpleClassRefs.contains(clazz)){
          simpleClassRefs(clazz).head
      }else{
          clazz
      }
      resolveClasses.getOrElseUpdate(clazzName,classResolver.apply(clazzName))
  }

}
