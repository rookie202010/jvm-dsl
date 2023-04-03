package com.dongjiaqiang.jvm.dsl.java.core.compile

import com.dongjiaqiang.jvm.dsl.java.core.JavaProgram
import org.codehaus.commons.compiler.CompilerFactoryFactory
import org.codehaus.janino.util.ResourceFinderClassLoader
import org.codehaus.janino.{ClassLoaderIClassLoader, CompilerFactory, SimpleCompiler}
import org.codehaus.janino.util.resource.{MapResourceCreator, MapResourceFinder, Resource, StringResource}

import java.util
import scala.collection.mutable.ArrayBuffer

trait JavaCompiler{
  def compile(javaProgram: JavaProgram,packageName:String):Class[_]
}

class JaninoCompiler(val clazzLoader:ClassLoader) extends JavaCompiler {
  private val compiler: org.codehaus.janino.Compiler = new org.codehaus.janino.Compiler( new MapResourceFinder( ), new ClassLoaderIClassLoader(clazzLoader ) )

  def compile(javaProgram: JavaProgram,packageName:String):Class[_]={

    val classes = new util.HashMap[String,Array[Byte]]()
    compiler.setClassFileCreator( new MapResourceCreator( classes ) )

    val resources = ArrayBuffer[Resource]()

    resources.appendAll(javaProgram.definitionClasses.map{
      case (clazzName,clazzCode)⇒ new StringResource(s"$packageName/$clazzName.java",
        clazzCode)
    })
    resources.append(new StringResource(s"$packageName/Program.java",javaProgram.mainClass))
    compiler.compile(resources.toArray)

    new ResourceFinderClassLoader(new MapResourceFinder(classes),clazzLoader).loadClass(s"${packageName}.Program")
  }
}
