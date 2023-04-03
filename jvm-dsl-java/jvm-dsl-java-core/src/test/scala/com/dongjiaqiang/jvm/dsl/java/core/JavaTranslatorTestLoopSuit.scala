package com.dongjiaqiang.jvm.dsl.java.core

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate._
import com.typesafe.config.{ConfigFactory, ConfigValueFactory}
import org.scalatest.funsuite.AnyFunSuite

class JavaTranslatorTestLoopSuit extends AnyFunSuite {


  test("define loop"){


    val input =
      """
        |
        |program{
        |
        | class A(Int x){
        |
        |   def foo(Int x,Int y)=Int{
        |       return x+y;
        |   }
        | }
        |
        | def method(Int x,Int y)=Long throws Exception{
        |       A a = new A(19);
        |
        |       List[List[Int]] list = [[1],[2,3],[3,1]];
        |
        |      List[Int] newList =  list.map(i=>{
        |
        |         Int sum = i.reduce((k1,k2)=>{ return k1+k2;});
        |
        |
        |      return sum+10;
        |
        |      });
        |
        |
        |
        |      Option[Long] opp = ?10L;
        |
        |       Int ddd  = (Int)opp.getOrElse(()=>{
        |
        |           Option[Long] oppo = ?20L;
        |           return oppo.orElse(30L);
        |       });
        |
        |
        |      Option[Option[Long]] opop1 = ?opp;
        |
        |      long opp1 =  opp.map(i=>{
        |
        |
        |            Option[Int] opx = ?5;
        |
        |      return i+10+ (Long)(opx.get());
        |
        |      }).orElse(100L);
        |
        |
        |      Int sum = 10;
        |      for(Int i=10;i<10;i=i+1){
        |         sum  = sum+i*2;
        |      }
        |
        |      long i1 = 10;
        |      return x+(y+sum)*2 + sum+a.x+(long)(newList.get(0))+opp1+ddd+opop1.flatten().get();
        | }
        |
        |}
        |""".stripMargin

    val javaProgram = input.translate(JavaTranslatorContext(packageName = "xx",javaTranslateConfig = ConfigFactory.empty()))

    val javaCompile = new JaninoCompiler(this.getClass.getClassLoader)

    val clazz = javaCompile.compile(javaProgram,"xx")
    val instance = clazz.getConstructors.head.newInstance()

    println(instance)


    println(clazz.getMethod("method",java.lang.Integer.TYPE,java.lang.Integer.TYPE).invoke(instance,new Integer(1),new Integer(10)))
    //println(clazz.getMethods.head.invoke(instance,1,2))
  }
}
