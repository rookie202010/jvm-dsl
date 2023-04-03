package com.dongjiaqiang.jvm.dsl.api.scope

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, ImportClazzType}
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException

import scala.collection.mutable.{ArrayBuffer, LinkedHashMap ⇒ MutableMap, Set ⇒ MSet}


trait ImportRegister{

  /**
   * add import class
   *
   * @param className import class name
   */
  def addClass(className: String): Unit

  /**
   * add import class from package
   *
   * @param className   import class name
   * @param packageName package name
   */
  def addClass(className:String, packageName:String):Unit

  /**
   * add package
   *
   * @param packageName     package name
   * @param packageLocation package location
   */
  def addPackage(packageName:String,packageLocation: String): Unit

  def registerDefaultClassResolver(resolver:String⇒ImportClazzType):Unit

  def registerDslTypeResolver(resolver:DslType⇒Option[ImportClazzType]):Unit

  def registerPackageClassResolver(`package`:String,resolver:String⇒ImportClazzType):Unit
}

trait ImportResolver{

  /**
   * contain import class the name can be simple name or full name
   *
   * @param className import class name
   * @return contain class
   */
  def containClass(className:String):Boolean


  def resolve(className:String):ImportClazzType
}

class ImportManager extends ImportRegister with ImportResolver {

  private var defaultClassResolver:String⇒ImportClazzType = _

  private var dslTypeResolver:DslType⇒Option[ImportClazzType] = _

  private val classes: ArrayBuffer[String] = ArrayBuffer( )

  private val simpleClassRefs:MutableMap[String,MSet[String]] = MutableMap()

  private val resolveClasses: MutableMap[String,ImportClazzType] = MutableMap()

  private val dslTypeClasses: MutableMap[DslType,Option[ImportClazzType]] = MutableMap()

  private val classesFromPackage: MutableMap[String, String] = MutableMap( )

  private val packages: MutableMap[String, String] = MutableMap( )

  private val packageClassResolvers:MutableMap[String,String⇒ImportClazzType] = MutableMap()

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


  def containClass(className:String):Boolean={
      if(simpleClassRefs.contains(className)){
         if(simpleClassRefs.get(className).size>1){
              throw ExpressionParseException(s"simple clazz $className ref more clazz: ${simpleClassRefs.get(className).mkString(";")}")
         }else{
              return true
         }
      }
      classes.contains(className) || classesFromPackage.contains(className)
  }

  def registerDefaultClassResolver(resolver:String⇒ImportClazzType):Unit={
      if(defaultClassResolver==null) {
        defaultClassResolver = resolver
      }else{
        throw new IllegalStateException("default class resolver already registered!")
      }
  }

  def registerDslTypeResolver(resolver:DslType⇒Option[ImportClazzType]):Unit={
      if(dslTypeResolver==null){
          dslTypeResolver = resolver
      }else{
          throw new IllegalStateException("dsl type resolver already registered!")
      }
  }

  def registerPackageClassResolver(`package`:String,resolver:String⇒ImportClazzType):Unit={
      if(packageClassResolvers.contains(`package`)) {
        packageClassResolvers.put( `package`, resolver )
      }else{
        throw new IllegalStateException(s"${`package`} package class resolver")
      }
  }

  def resolve(dslType: DslType): Option[ImportClazzType] = {
     dslTypeClasses.getOrElseUpdate( dslType, dslTypeResolver.apply( dslType ) )
  }

  def resolve(className:String):ImportClazzType= {
    val clazzName = if (simpleClassRefs.contains( className )) {
      if (simpleClassRefs.get( className ).size > 1) {
        throw ExpressionParseException( s"simple clazz $className ref more clazz: ${simpleClassRefs.get( className ).mkString( ";" )}" )
      }
      simpleClassRefs( className ).head
    } else {
      className
    }

    if (classes.contains( clazzName )) {
      if (defaultClassResolver != null) {
        resolveClasses.getOrElseUpdate( clazzName, defaultClassResolver.apply( clazzName ) )
      } else {
        throw new IllegalStateException( "default class resolver is not register" )
      }
    } else if (classesFromPackage.contains( clazzName )) {
      val packageName = classesFromPackage( clazzName )
      packageClassResolvers.get( packageName ) match {
        case None ⇒
          throw new IllegalStateException( f"package $packageName class resolver is not register" )
        case Some( resolver ) ⇒
          resolveClasses.getOrElseUpdate( clazzName, resolver.apply( clazzName ) )
      }
    } else {
      throw new IllegalArgumentException( f"class $className not imported" )
    }
  }

}
