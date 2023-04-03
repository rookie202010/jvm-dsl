package com.dongjiaqiang.jvm.dsl.java.core.translate.statement

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate.ProgramTranslator
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2023/4/3 
 * */
class TranslateWhileSuit extends AnyFunSuite {
  def compile(code: String): Class[_] = {
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example", javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.compile( javaProgram, "com.example" )
  }
  test("test loop 1") {
    def eval(key: Int): Int = {
      var sum = 0
      (0 until key).foreach(i⇒{
        if(i==20){
            return i
        }
        if(i!=30){
            sum+=i
        }
      })
      sum
    }
    val code =
      """
        |program{
        |   def eval(Int key)=Int{
        |       Int sum = 0;
        |       for(Int i=0;i<key;i=i+1){
        |           if(i==20){
        |               Int j = i;
        |               return j;
        |           }
        |           if(i==30){
        |               continue;
        |           }
        |           sum = sum + i;
        |       }
        |       return sum;
        | }
        |}
        |""".stripMargin
    val clazz = compile(code)
    val instance = clazz.getConstructors.head.newInstance()
    val method = clazz.getMethod("eval", classOf[Int])
    (0 until 10).foreach(_ ⇒ {
      val key = Random.nextInt(100)
      assert(method.invoke(instance, Int.box(key)).equals(eval(key)))
    })
  }
  test("test loop 2") {
    def eval(key: Int): Int = {
      var sum = 0
      (0 until key).foreach(i⇒{
        if(i==20){
          return i
        }
        if(i!=30){
          sum+=i
        }
      })
      sum
    }
    val code =
      """
        |program{
        |   def eval(List[Int] key)=Int{
        |       Int sum = 0;
        |       for(Int i:key){
        |           if(i==20){
        |               Int j = i;
        |               return j;
        |           }
        |           if(i==30){
        |               continue;
        |           }
        |           sum = sum + i;
        |       }
        |       return sum;
        | }
        |}
        |""".stripMargin
    val clazz = compile(code)
    val instance = clazz.getConstructors.head.newInstance()
    val method = clazz.getMethod("eval", classOf[Int])
    (0 until 10).foreach(_ ⇒ {
      val key = Random.nextInt(100)
      assert(method.invoke(instance, Int.box(key)).equals(eval(key)))
    })
  }

}
