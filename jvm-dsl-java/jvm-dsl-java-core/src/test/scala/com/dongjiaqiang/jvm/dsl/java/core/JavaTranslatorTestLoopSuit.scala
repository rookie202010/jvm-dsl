package com.dongjiaqiang.jvm.dsl.java.core

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.translate._
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

class JavaTranslatorTestLoopSuit extends AnyFunSuite {


  test("define loop"){


    val input =
      """
        |
        |program{
        |
        | def method(Ing x,Int y)=Int{
        |      Int sum = 10;
        |      for(Int i=10;i<10;i=i+1){
        |         sum  = sum+i*2;
        |      }
        |      return x+(y+sum)*2 + sum;
        | }
        |
        |}
        |""".stripMargin

    input.translate(JavaTranslatorContext(packageName = "xx",javaTranslateConfig = ConfigFactory.empty() ))
  }
}
