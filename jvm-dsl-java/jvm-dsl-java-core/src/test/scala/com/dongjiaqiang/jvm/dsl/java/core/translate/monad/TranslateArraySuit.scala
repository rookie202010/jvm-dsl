package com.dongjiaqiang.jvm.dsl.java.core.translate.monad

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate.ProgramTranslator
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

import java.util
import scala.util.Random

class TranslateArraySuit extends AnyFunSuite{

  def compile(code: String): Class[_] = {
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example", javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.compile( javaProgram, "com.example" )
  }

  test( "test array 1" ) {
    def exist(b:List[Long]):Int = {
        if(b.exists(b1⇒{b1/2==0})){
          10
        }else{
          20
        }
    }
    def func(a:List[Int],b:List[Long]): Long = {
      a.sum+a.min+a.max+a.flatMap(x⇒{
        List(List(x,x,x).sum+ List(x+x,x*x).flatMap(x1⇒{
          List(x1+10,x1+20)
        }).sum)
      }).sum+exist(b)+b.head
    }
    val code =
      """
        |program{
        |   def exist(Array[Long] b)=Int throws Exception{
        |
        |       List[Long] b2 = b.map(b1=>{ return b1+1; }).filter(b1=> { return b1%2!=0; }).toList();
        |
        |       if(b.exist(b1=>{ return b1/2==0; })){
        |           return 10 + b2.sum().toInt();
        |       }else{
        |           return 20;
        |       }
        |   }
        |   def eval(Array[Int] a,Array[Long] b)=Long throws Exception{
        |       Int max = a.max();
        |       Int sum = a.sum();
        |       Int min = a.min();
        |       return max+sum+min+a.flatMap(a1=>{
        |          return [[a1,a1,a1].sum() + [a1+a1,a1*a1].flatMap(b1=> {
        |               return [b1+10,b1+20];
        |           }).sum()];
        |       }).sum()+exist(b)+b.get(0);
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval",classOf[Array[java.lang.Integer]],classOf[Array[java.lang.Long]] )
    (0 until 10).foreach( _ ⇒ {
      val a = Array(
      Int.box(Random.nextInt(20)),
      Int.box(Random.nextInt(30)),
      Int.box(Random.nextInt(100)))

      val b = Array(
      Long.box(Random.nextLong()),
      Long.box(Random.nextLong()),
      Long.box(Random.nextLong()))
      import scala.collection.convert.ImplicitConversionsToScala._
     assert( method.invoke( instance, a,b ).equals( func(a.toList.map(Int.unbox),b.toList.map(Long.unbox) ) ) )
    } )
  }

  test( "test array 2" ) {
    val code =
      """
        |program{
        |   def eval(Array[Long] b)=Long throws Exception{
        |      return b.map(a1=>{
        |          return a1+20;
        |       }).sum();
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval",  classOf[Array[java.lang.Long]] )
    (0 until 10).foreach( _ ⇒ {
//      val a = new util.ArrayList[Integer]( )
//      a.add( Int.box( Random.nextInt( 20 ) ) )
//      a.add( Int.box( Random.nextInt( 30 ) ) )
//      a.add( Int.box( Random.nextInt( 100 ) ) )


//      val b = new util.ArrayList[java.lang.Long]( )
//      b.add( Long.box( Random.nextLong( ) ) )
//      b.add( Long.box( Random.nextLong( ) ) )
//      b.add( Long.box( Random.nextLong( ) ) )

      val b = Array(Long.box(Random.nextLong()),Long.box(Random.nextLong()),Long.box(Random.nextLong()))
      import scala.collection.convert.ImplicitConversionsToScala._
      println( method.invoke( instance,  b ))
    } )
  }

}
