package com.dongjiaqiang.jvm.dsl.java.core.translate.monad

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate.ProgramTranslator
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

import java.util
import scala.util.Random

class TranslateOptionSuit extends AnyFunSuite{
  def compile(code: String): Class[_] = {
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example",clazzName = "Program",  javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.load( javaProgram )
  }

  test( "test option 1" ) {
    def func(a:Option[String]): Long = {
      a.flatMap(i⇒{
        val j = i.toLong
        val list = List("1","2","3","4")
        val k = list.map(_.toLong ).sum+j
        Some(k)
      }).filter(_>100).getOrElse(200L)*a.toList.map(_.toInt).sum
    }
    val code =
      """
        |program{
        |   def eval(Option[String] op)=Long throws Exception{
        |       return op.flatMap(i=> {
        |        Long j = i.toLong();
        |        List[String] list = ["1","2","3","4"];
        |        Long k = list.map(i=> { return i.toLong(); }).sum() + j;
        |        return ?k;
        |        }).filter(i=>{ return i > 100; }).orElse(200L)*op.toList().map(i=>{ return i.toInt();}).sum();
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval",classOf[java.util.Optional[_]])
    (0 until 10).foreach( _ ⇒ {
      val i = Random.nextInt(300)
      if(i%100==10) {
        method.invoke( instance, java.util.Optional.empty() ).equals( func( None ) )
      }else{
        method.invoke( instance, java.util.Optional.of( i.toString ) ).equals( func( Some( i.toString ) ) )
      }
    } )
  }

  test( "test option 2" ) {
    def func(a: Option[String]): Long = {
      a.flatMap( i ⇒ {
        val j = i.toLong
        val list = List( "1", "2", "3", "4" )
        val k = list.map( _.toLong ).sum + j
        Some( k )
      } ).filter( _ > 100 ).getOrElse( 200L ) * a.toList.map( _.toInt ).sum
    }

    val code =
      """
        |program{
        |   def eval(Option[String] op)=Int throws Exception{
        |       return op.toList().map(i=>{ return i.toInt();}).sum();
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[java.util.Optional[_]] )
    (0 until 10).foreach( _ ⇒ {
      val i = Random.nextInt( 300 )
      if (i % 100 == 10) {
        method.invoke( instance, java.util.Optional.empty( ) ).equals( func( None ) )
      } else {
        method.invoke( instance, java.util.Optional.of( i.toString ) ).equals( func( Some( i.toString ) ) )
      }
    } )
  }
}
