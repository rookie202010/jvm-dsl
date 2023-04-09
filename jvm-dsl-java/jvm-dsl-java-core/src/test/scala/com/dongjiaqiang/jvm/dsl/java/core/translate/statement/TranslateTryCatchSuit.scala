package com.dongjiaqiang.jvm.dsl.java.core.translate.statement

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate.ProgramTranslator
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class TranslateTryCatchSuit extends AnyFunSuite {

  def compile(code: String): Class[_] = {
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example", clazzName = "Program", javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )

    javaCompile.load( javaProgram )
  }


  test( "test try catch 1" ) {
//    def eval(k1: Int, k2: List[Int]): Int = {
//      if (k1 / 2 == 0) {
//        if (k2.sum / 2 == 0) {
//          return 10
//        } else {
//          return 30
//        }
//      }
//      20
//    }

    val code =
      """
        |program{
        | def eval(String input)=Int{
        |      try{
        |        return input.split(",").map(score=> { return score.toInt(); }).sum().toInt();
        |     }catch(Exception e){
        |        return -1;
        |     }
        | }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[String] )
    (0 until 10).foreach( _ ⇒ {
      val k1 = Random.nextInt( 20 )



      import scala.collection.convert.ImplicitConversionsToScala._
      println(method.invoke( instance, "12,2121,21,21" ))
      //assert( method.invoke( instance, Int.box( k1 ), a ).equals( eval( k1, a.map( Int.unbox ).toList ) ) )
    } )
  }


}
