package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate._
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class TranslateUnaryExpressionSuit extends AnyFunSuite {
  def compile(code:String):Class[_]={
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example",clazzName = "Program", javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.load( javaProgram )
  }
  test("unary expression example 1"){
    def func(a:Long,b:Double):Int={
      val c = a.toInt
      val d = b.toInt
      (c+d)*10*(-c)*(-d)
    }
    val code =
      """
        |program{
        |   def eval(Long a,Double b)=Int{
        |       Int c = (Int)a;
        |       Int d = (Int)b;
        |       return (c+d)*10*(-c)*(-d);
        |  }
        |}
        |""".stripMargin
    val clazz = compile(code)
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod("eval",java.lang.Long.TYPE,java.lang.Double.TYPE)
    (0 until 10).foreach(_⇒{
      val a = Random.nextLong()*100
      val b = Random.nextDouble()*100
      assert(method.invoke(instance,Long.box(a),Double.box(b)).equals(func(a,b)))
    })
  }
  test( "unary expression example 2" ) {
    def cond(a:Long):Boolean = a!=10;
    def func(a: Long, b: Double,condition:Boolean): Double = {
      if(a>b && !condition) {
        a
      }else if(!cond(a)){
        b
      }else{
        a+b;
      }
    }
    val code =
      """
        |program{
        |
        |   def cond(Long a)=Boolean{
        |       return a!=10;
        |   }
        |   def eval(Long a,Double b,Bool condition)=Double{
        |       if(a>b && !condition){
        |           return a;
        |       }else if(!cond(a)){
        |           return b;
        |       }else{
        |           return a+b;
        |       }
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", java.lang.Long.TYPE, java.lang.Double.TYPE,java.lang.Boolean.TYPE )
    (0 until 10).foreach( _ ⇒ {
      val a = Random.nextLong( ) * 100
      val b = Random.nextDouble( ) * 100
      val condition = Random.nextBoolean()
      assert( method.invoke( instance, Long.box( a ), Double.box( b ),Boolean.box(condition) ).equals( func( a, b,condition ) ) )
    } )
  }
}
