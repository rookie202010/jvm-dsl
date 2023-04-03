package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import org.scalatest.funsuite.AnyFunSuite
import com.dongjiaqiang.jvm.dsl.java.core.translate._
import com.typesafe.config.ConfigFactory

import scala.util.Random
class TranslateBinaryExpressionSuit extends AnyFunSuite {
  def compile(code:String):Class[_]={
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example", javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.compile( javaProgram, "com.example" )
  }
  test("binary expression example 1"){
    def func(a:Int,b:Int):Int={
        a+b;
    }
    val code =
      """
        |program{
        |   def eval(Int a,Int b)=Int{
        |       return a+b;
        |  }
        |}
        |""".stripMargin
    val clazz = compile(code)
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod("eval",java.lang.Integer.TYPE,java.lang.Integer.TYPE)
    (0 until 10).foreach(_⇒{
      val a = Random.nextInt(100)
      val b = Random.nextInt(100)
      assert(method.invoke(instance,Int.box(a),Int.box(b)).equals(func(a,b)))
    })
  }
  test( "binary expression example 2" ) {
    def func(a: Int, b: Int,d:Long,c:Long): Long = {
      a - b*a+d*c;
    }

    val code =
      """
        |program{
        |   def eval(Int a,Int b,Long d,Long c)=Long{
        |       return a-b*a+d*c;
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", java.lang.Integer.TYPE, java.lang.Integer.TYPE,java.lang.Long.TYPE,java.lang.Long.TYPE )
    (0 until 10).foreach( _ ⇒ {
      val a = Random.nextInt( 100 )
      val b = Random.nextInt( 100 )
      val c = Random.nextLong()
      val d = Random.nextLong();
      assert(method.invoke( instance, Int.box( a ), Int.box( b ),Long.box(d),Long.box(c) ).equals( func( a, b,d,c ) ))
    } )
  }
  test( "binary expression example 3" ) {
    def func(a: Int, b: Int): Long = {
      if(a>b){
        a
      }else if(a==b){
        a+b
      }else{
        b
      }
    }
    val code =
      """
        |program{
        |   def eval(Int a,Int b)=Long{
        |       if(a>b){
        |           return a;
        |       }else if(a==b){
        |           return a+b;
        |       }else{
        |           return b;
        |       }
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", java.lang.Integer.TYPE, java.lang.Integer.TYPE)
    (0 until 10).foreach( _ ⇒ {
      val a = Random.nextInt( 100 )
      val b = Random.nextInt( 100 )
     assert(method.invoke( instance, Int.box( a ), Int.box( b )).equals( func( a, b ) ))
    } )
  }
  test( "binary expression example 4" ) {
    def func(a: Int, b: Int,c:Int): Long = {
      if(a>b || a<c){
        return c%a
      }else if(a==b && a==c){
        return a+b+c;
      }
      a%c+(a+c)*b;
    }
    val code =
      """
        |program{
        |   def eval(Int a,Int b,Int c)=Long{
        |       if(a> b || a < c){
        |           return c%a;
        |       }else if(a==b && a==c){
        |           return a+b+c;
        |       }
        |       return a%c+(a+c)*b;
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", java.lang.Integer.TYPE, java.lang.Integer.TYPE,java.lang.Integer.TYPE )
    (0 until 10).foreach( _ ⇒ {
      val a = Random.nextInt( 100 )
      val b = Random.nextInt( 100 )
      val c = Random.nextInt( 100 )
      assert(method.invoke( instance, Int.box( a ), Int.box( b ),Int.box(c) ).equals( func( a, b,c ) ))
    } )
  }
}
