package com.dongjiaqiang.jvm.dsl.java.core.translate.monad

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate.ProgramTranslator
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

import java.util
import scala.util.Random

class TranslateMapSuit extends AnyFunSuite {
  def compile(code: String): Class[_] = {
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example", javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.compile( javaProgram, "com.example" )
  }

  test( "test map 1" ) {
    def func(key: Int): Int = {
      val s = Map( 12 → 11, 21 → 22 ).map( kv ⇒ kv._1 + kv._2 ).sum

      val map = Map( 12 → "112", 13 → "90" )
      val sum = map.keys.map( i ⇒ i + 100 ).sum
      val max = map.values.map( i ⇒ i.toLong ).filter( _ > 90 ).max.toInt
      s + sum + max + key
    }
    val code =
      """
        |program{
        |   def eval(Int key)=Int throws Exception{
        |      Int s = {12:11,21:22}.map((i,j)=>{
        |       return i+j;
        |       }).sum();
        |      Map[Int,String] map = { 12:"112",13:"90"};
        |      return  map.keys().map(i=> { return i+100;}).sum()+ map.values().map(i=>{
        |         return i.toLong();
        |      }).filter(i=> { return i>90; }).max().toInt()+s+key;
        | }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[Int] )
    (0 until 10).foreach( _ ⇒ {
      val key = Random.nextInt( 200 )
      assert( method.invoke( instance, Int.box( key ) ).equals( func( key ) ) )
    } )
  }


  test( "test map 2" ) {
    def func(key: Int): Int = {
      val s = Map( 12 → 11, 21 → 22 ).map( kv ⇒ kv._1 + kv._2 ).sum

      val map = Map( 12 → "112", 13 → "90" )
      val sum = map.keys.map( i ⇒ i + 100 ).sum
      val max = map.values.map( i ⇒ i.toLong ).filter( _ > 90 ).max.toInt
      s + sum + max + key
    }

    val code =
      """
        |program{
        |   def eval(Map[Int,String] map)=Int throws Exception{
        |      return map.toSeqMap().filter((k,v)=> {
        |       return k!=10 ;
        |       }).mapValue(v => { return v.toLong()+1; }).keys().sum().toInt();
        |   }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[java.util.Map[_,_]] )
    (0 until 10).foreach( _ ⇒ {
      val map = new util.HashMap[Integer,String]()
      map.put(Random.nextInt(122),Random.nextInt(1222).toString)
      println(method.invoke(instance,map))
    } )
  }


}
