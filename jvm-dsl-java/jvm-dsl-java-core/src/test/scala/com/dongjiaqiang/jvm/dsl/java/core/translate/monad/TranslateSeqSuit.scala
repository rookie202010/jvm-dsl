package com.dongjiaqiang.jvm.dsl.java.core.translate.monad

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate.ProgramTranslator
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

import java.util
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class TranslateSeqSuit extends AnyFunSuite{

  def compile(code: String): Class[_] = {
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example",clazzName = "Program",  javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.load( javaProgram )
  }

  test( "test seq 1" ) {
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
        |   def exist(List[Long] b)=Int throws Exception{
        |       if(b.exist(b1=>{ return b1/2==0; })){
        |           return 10;
        |       }else{
        |           return 20;
        |       }
        |   }
        |   def eval(List[Int] a,List[Long] b)=Long throws Exception{
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
    val method = clazz.getMethod( "eval",classOf[java.util.List[_]],classOf[java.util.List[_]] )
    (0 until 10).foreach( _ ⇒ {
      val a = new util.ArrayList[Integer]()
      a.add(Int.box(Random.nextInt(20)))
      a.add(Int.box(Random.nextInt(30)))
      a.add(Int.box(Random.nextInt(100)))

      val b = new util.ArrayList[java.lang.Long]()
      b.add(Long.box(Random.nextLong()))
      b.add(Long.box(Random.nextLong()))
      b.add(Long.box(Random.nextLong()))
      import scala.collection.convert.ImplicitConversionsToScala._
     assert( method.invoke( instance, a,b ).equals( func(a.toList.map(Int.unbox),b.toList.map(Long.unbox) ) ) )
    } )
  }

  test( "test seq 2" ) {
    def func(a:List[Int],b:List[Long]): Int = {
        val a1 = a.sorted
        a1.zipWithIndex.map({
          case (i,j)⇒ i+j
        }).sum+b.sum.toInt
    }
    val code =
      """
        |program{
        |   def eval(List[Int] a,List[Long] b)=Int throws Exception{
        |       List[Int] a1 = a.sort((i,j)=>{
        |           return i-j;
        |       });
        |       List[(Int,Int)] a2 = a1.zipWithIndex();
        |       return a2.map((i,j)=> { return i+j; }).sum()+b.reduce((b1,b2)=> { return b1+b2; }).toInt();
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[java.util.List[_]], classOf[java.util.List[_]] )
    (0 until 10).foreach( _ ⇒ {
      val a = new util.ArrayList[Integer]( )
      a.add( Int.box( Random.nextInt( 20 ) ) )
      a.add( Int.box( Random.nextInt( 30 ) ) )
      a.add( Int.box( Random.nextInt( 100 ) ) )

      val b = new util.ArrayList[java.lang.Long]( )
      b.add( Long.box( Random.nextLong( ) ) )
      b.add( Long.box( Random.nextLong( ) ) )
      b.add( Long.box( Random.nextLong( ) ) )
      import scala.collection.convert.ImplicitConversionsToScala._
      assert( method.invoke( instance, a, b ).equals( func( a.toList.map( Int.unbox ), b.toList.map( Long.unbox ) ) ) )
    } )
  }

  test( "test seq 3" ) {
    def func(a:List[String]):Long={
        a.map(i⇒i.toInt).sum.toLong
    }
    val code =
      """
        |program{
        |   def eval(List[String] a)=Long throws Exception{
        |       return a.toList().map(i=>{ return i.toInt(); }).sum().toLong();
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[java.util.List[_]] )
    (0 until 10).foreach( _ ⇒ {
      val a = new util.ArrayList[String]( )
      a.add(  Random.nextInt( 20 ).toString)
      a.add( Random.nextInt( 30  ).toString )
      a.add(  Random.nextInt( 100 ).toString )
      import scala.collection.convert.ImplicitConversionsToScala._
      assert( method.invoke( instance,a ).equals(func(a.toList)))
    } )
  }

  test( "test seq 4" ) {
    def func():Long={
        val a = Set(3,2,3)
        a.last.toLong+Set(3,12,3,11,1).map(i⇒i+10).filter(j⇒j>10).sum.toLong
    }
    val code =
      """
        |program{
        |   def eval()=Long throws Exception{
        |       Set[Int] a =  {3,2,3}.toSortedSet( (i,j)=>{
        |           return j-i;
        |           });
        |
        |       return a.last().toLong() + {3,12,3,11,1}.toSortedSet().map(i=>{ return i+10;}).filter(j=> { return j>10;}).toSet().sum().toLong();
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval" )
    assert( method.invoke( instance ).equals(func()) )
  }

  test( "test seq 5" ) {
    def func(a: List[Long], b: Set[Option[Long]]): Int = {
      val s1 = a.map( _.toInt ).sum
      val s2 = b.flatten
      val min = if(s2.isEmpty) 10 else s2.min
      val sum = s2.sum
      s1 + sum.toInt+ min.toInt
    }
    val code =
      """
        |program{
        |   def eval(List[Long] a,Set[Option[Long]] b)=Int throws Exception{
        |       Set[Long] bb = b.flatten();
        |       Long min = 10;
        |       if(bb.nonEmpty()){
        |           min = bb.min();
        |       }
        |
        |       return a.map(i=>{ return i.toInt(); }).sum()+b.flatMap(i=> { return i; }).sum().toInt() + min.toInt();
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[java.util.List[_]],
      classOf[java.util.Set[java.util.Optional[java.lang.Long]]] )
    (0 until 10).foreach( _ ⇒ {
      val a = new util.ArrayList[Long]( )
      a.add( Random.nextLong( ) )
      a.add( Random.nextLong( ) )
      a.add( Random.nextLong( ) )
      val b = new util.HashSet[java.util.Optional[Long]]( )
      val b1 = ArrayBuffer[Option[Long]]( )
      if (Random.nextBoolean( )) {
        b.add( java.util.Optional.empty( ) )
        b1.append( None )
      } else {
        val i = Random.nextLong( )
        b.add( java.util.Optional.of( i ) )
        b1.append( Some( i ) )
      }
      import scala.collection.convert.ImplicitConversionsToScala._
      assert( method.invoke( instance, a, b ).equals( func( a.toList, b1.toSet ) ) )
    } )
  }
}
