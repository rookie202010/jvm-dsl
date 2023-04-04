package com.dongjiaqiang.jvm.dsl.java.core.translate.monad

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate.ProgramTranslator
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2023/4/4 
 * */
class TranslateTupleSuit  extends AnyFunSuite {
  def compile(code: String): Class[_] = {
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example", javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.compile( javaProgram, "com.example" )
  }

  test("test tuple 1"){
    val code =
      """
        |program{
        |     def eval(Int i,Long j)=Int{
        |         Array[(Int,Array[Array[(Long,Int)]])] t = null;
        |         return t[1]._1[0][1]._1;
        |     }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[Int],classOf[Long])
    println(method.invoke(instance,Int.box(1),Long.box(2)))
  }
}
