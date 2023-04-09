package com.dongjiaqiang.jvm.dsl.java.core.translate.statement

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate.ProgramTranslator
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

import java.util
import scala.util.Random

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2023/4/3 
 * */
class TranslateIfSuit extends AnyFunSuite {
  def compile(code: String): Class[_] = {
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example",clazzName = "Program", javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.load( javaProgram )
  }
  test("test if 1") {
    def eval(key: Int): Int = {
      if (key / 2 == 0) {10} else {20}
    }
    val code =
      """
        |program{
        |   def foo(Int key)=Bool{
        |       return key/2==0;
        |   }
        |   def eval(Int key)=Int{
        |       if(foo(key)){
        |           return 10;
        |       }else{
        |           return 20;
        |       }
        | }
        |}
        |""".stripMargin
    val clazz = compile(code)
    val instance = clazz.getConstructors.head.newInstance()
    val method = clazz.getMethod("eval", classOf[Int])
    (0 until 10).foreach(_ ⇒ {
      val key = Random.nextInt(20)
      assert(method.invoke(instance, Int.box(key)).equals(eval(key)))
    })
  }
  test("test if 2") {
    def eval(k1: Int,k2:List[Int]): Int = {
      if (k1/2 == 0) {
        if(k2.sum/2==0){
            return 10
        }else{
          return 30
        }
      }
      20
    }
    val code =
      """
        |program{
        |   def foo(Int key)=Bool{
        |       return key/2==0;
        |   }
        |   def eval(Int k1,List[Int] k2)=Int{
        |       if(foo(k1)){
        |           if(foo(k2.sum())){
        |               return 10;
        |           }else{
        |               return 30;
        |           }
        |       }
        |       return 20;
        | }
        |}
        |""".stripMargin
    val clazz = compile(code)
    val instance = clazz.getConstructors.head.newInstance()
    val method = clazz.getMethod("eval", classOf[Int],classOf[util.List[_]])
    (0 until 10).foreach(_ ⇒ {
      val k1 = Random.nextInt(20)

      val a = new util.ArrayList[Integer]()
      a.add(Int.box(Random.nextInt(20)))
      a.add(Int.box(Random.nextInt(30)))
      a.add(Int.box(Random.nextInt(100)))

      import scala.collection.convert.ImplicitConversionsToScala._
      assert(method.invoke(instance, Int.box(k1),a).equals(eval(k1,a.map(Int.unbox).toList)))
    })
  }
  test("test if 3") {
    def bar(k:Int):Int={
      if(k>20){
        return k+20
      }else if(k==20){
        return k
      }
      k*2
    }
    def eval(k1: Int,k2:List[Int]): Int = {
      if (k1/2 == 0) {
        if(k2.sum/2==0){
          10
        }else{
          30
        }
      }else if((bar(k1)/2+3)*10>20){
          bar(k1)
      }else {
        20
      }
    }
    val code =
      """
        |program{
        |   def foo(Int key)=Bool{
        |       return key/2==0;
        |   }
        |   def bar(Int key)=Int{
        |       if(key>20){
        |           return key+20;
        |       }else if(key==20){
        |           return key;
        |       }
        |       return key*2;
        |   }
        |   def eval(Int k1,List[Int] k2)=Int{
        |       if(foo(k1)){
        |           if(foo(k2.sum())){
        |               return 10;
        |           }else{
        |               return 30;
        |           }
        |       }else if((bar(k1)/2+3)*10>20){
        |           return bar(k1);
        |       }else{
        |           return 20;
        |       }
        | }
        |}
        |""".stripMargin
    val clazz = compile(code)
    val instance = clazz.getConstructors.head.newInstance()
    val method = clazz.getMethod("eval", classOf[Int],classOf[util.List[_]])
    (0 until 10).foreach(_ ⇒ {
      val k1 = Random.nextInt(20)
      val a = new util.ArrayList[Integer]()
      a.add(Int.box(Random.nextInt(20)))
      a.add(Int.box(Random.nextInt(30)))
      a.add(Int.box(Random.nextInt(100)))
      import scala.collection.convert.ImplicitConversionsToScala._
      assert(method.invoke(instance, Int.box(k1),a).equals(eval(k1,a.map(Int.unbox).toList)))
    })
  }
}
