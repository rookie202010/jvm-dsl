package com.dongjiaqiang.jvm.dsl.java.core.compile

import com.dongjiaqiang.jvm.dsl.java.core.JavaProgram
import org.codehaus.janino.ClassLoaderIClassLoader
import org.codehaus.janino.util.resource.{MapResourceCreator, MapResourceFinder, Resource, StringResource}

import java.util
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Set
trait JavaCompiler{
  def load(javaProgram: JavaProgram):Class[_]={
      compile(mutable.Set(javaProgram)).
        loadClass(s"${javaProgram.packageName}.${javaProgram.clazzName}")
  }

  def compile(javaProgram: JavaProgram):ClassLoader={
      compile(mutable.Set(javaProgram))
  }

  def compile(javaPrograms:mutable.Set[JavaProgram]):ClassLoader
}

class JaninoCompiler(val clazzLoader:ClassLoader) extends JavaCompiler {
  private val compiler: org.codehaus.janino.Compiler = new org.codehaus.janino.Compiler( new MapResourceFinder( ), new ClassLoaderIClassLoader(clazzLoader ) )

  override def compile(javaPrograms: mutable.Set[JavaProgram]): ClassLoader = {
    val classes = new util.HashMap[String, Array[Byte]]( )
    compiler.setClassFileCreator( new MapResourceCreator( classes ) )
    val resources = ArrayBuffer[Resource]( )
    javaPrograms.foreach(javaProgram⇒{
      val packageName = javaProgram.packageName
      val clazzName = javaProgram.clazzName
      resources.appendAll( javaProgram.definitionClasses.map {
        case (definitionClazzName, clazzCode) ⇒ new StringResource( s"$packageName/$clazzName$definitionClazzName.java",
          clazzCode )
      } )
      resources.append( new StringResource( s"$packageName/$clazzName.java", javaProgram.mainClass ) )
    })
    compiler.compile( resources.toArray )
    new JaninoResourceFinderClassLoader( new MapResourceFinder( classes ), clazzLoader )
  }
}
