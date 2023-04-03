package com.dongjiaqiang.jvm.dsl.java.core.translate.monad

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate.ProgramTranslator
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

class TranslateMapSuit extends AnyFunSuite{
  def compile(code: String): Class[_] = {
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example", javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.compile( javaProgram, "com.example" )
  }

  test( "test seq 4" ) {
    val code =
      """
        |program{
        |   def eval(Int key)=Int throws Exception{
        |       Map[Int,String] map = { 12:"112",13:"90"};
        |      return  map.keys().map(i=> { return i+100;}).sum()+ map.values().map(i=>{
        |         return i.toLong();
        |      }).filter(i=> { return i>90; }).max().toInt();
        |
        | }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval",classOf[Int] )
    println( method.invoke( instance,Int.box(12) ) )
    //    (0 until 10).foreach( _ ⇒ {
    //      val a = new util.ArrayList[String]( )
    //      a.add( Random.nextInt( 20 ).toString )
    //      a.add( Random.nextInt( 30 ).toString )
    //      a.add( Random.nextInt( 100 ).toString )
    //      println( method.invoke( instance, a ) )
    //    } )
  }

}
