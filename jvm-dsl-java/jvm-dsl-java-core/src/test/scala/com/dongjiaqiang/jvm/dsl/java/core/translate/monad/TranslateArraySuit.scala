package com.dongjiaqiang.jvm.dsl.java.core.translate.monad

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate.ProgramTranslator
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

import java.util
import java.util.Optional
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class TranslateArraySuit extends AnyFunSuite{

  def compile(code: String): Class[_] = {
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example",clazzName = "Program", javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.load( javaProgram )
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
        |       List[Long] b2 = b.map(b1=>{ return b1+1; }).filter(b1=> { return b1%2!=0; }).toList();
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
     assert( method.invoke( instance, a,b ).equals( func(a.map(i⇒Int.unbox(i)).toList,b.map(i⇒Long.unbox(i)).toList ) ) )
    } )
  }

  test( "test array 2" ) {
    def func(b:Array[Long],a:Array[Int]):Long={
      ( b.max+a.min)*a.max.toLong
    }
    val code =
      """
        |program{
        |   def eval(Array[Long] b,Array[Int] a)=Long throws Exception{
        |      return (b.max()+a.min())*a.max().toLong();
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval",  classOf[Array[java.lang.Long]],classOf[Array[java.lang.Integer]] )
    (0 until 10).foreach( _ ⇒ {
      val a = Array(Int.box(Random.nextInt(200)),Int.box(Random.nextInt(100)),Int.box(Random.nextInt(300)))
      val b = Array(Long.box(Random.nextLong()),Long.box(Random.nextLong()),Long.box(Random.nextLong()))
      assert( method.invoke( instance,  b,a ).equals(func(b.map(i⇒Long.unbox(i)),a.map(i⇒Int.unbox(i)))))
    } )
  }

  test( "test array 3" ) {
    def func(a: Array[String], b: Array[Int]): Long = {
      val s = a.map(_.toInt)
      val s1 = a.zipWithIndex.map(kv⇒kv._1.toInt+kv._2).sum
      val s2 = b.max
      b.sum+s.sum+s1+s2
    }
    val code =
      """
        |program{
        |   def eval(Array[String] a,Array[Int] b)=Long throws Exception{
        |      return a.map(i=>{ return i.toLong(); }).sum()+b.sum()+ a.zipWithIndex().map((k,v)=> {
        |         return k.toInt()+v;
        |      }).sum().toLong()+ b.toSortedSet((i,j)=>{ return i-j; }).max().toLong();
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[Array[String]],classOf[Array[java.lang.Integer]])
    (0 until 10).foreach( _ ⇒ {
      val a = Array( Random.nextInt( 200 ).toString, Random.nextInt( 100 ).toString, Random.nextInt( 300 ).toString )
      val b = Array(Int.box(Random.nextInt(200)),Int.box(Random.nextInt(100)),Int.box(Random.nextInt(400)))
      assert(method.invoke(instance,a,b) equals func(a,b.map(i⇒Int.unbox(i))))
    } )
  }

  test( "test array 4" ) {
    def func(a: Array[String]): Boolean = {
      a.exists( i ⇒ i.equals( "hello" ) ) && a.exists( i ⇒ i.startsWith( "a" ) )
    }

    val code =
      """
        program{
           def eval(Array[String] a)=Bool throws Exception{
              return a.exist(i=>{
                 return i.equals("hello");
                 }) && a.find(i=>{
                     return i.startsWith("a"); }).isDefine();
          }
        }
        """
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[Array[String]] )
    assert(method.invoke(instance,Array("ba","hello")).equals(func(Array("ba","hello"))))
  }

  test( "test array 5" ) {
    def func(a: Array[String],b:Array[Option[Long]]): Long= {
      a.flatMap(i⇒{
         i.split(",").map(i⇒i.toInt).toSet
      }).sum/b.flatten.sum+b.map( i⇒{
        val j = i.getOrElse(123L)
        Array(j,j,j).sum
      }).sum
    }
    val code =
      """
        |program{
        |   def eval(Array[String] a,Array[Option[Long]] b)=Long throws Exception{
        |       return a.flatMap(i=>{
        |           return i.split(",").map(i=> {return i.toInt();}).toSet();
        |       }).sum().toLong()/b.flatMap(i=>{ return i; }).sum() + b.map(i=>
        |           {
        |              Long j = i.orElse(123L);
        |               return new Array(j,j,j).sum();
        |           }).sum();
        |
        |   }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[Array[String]],classOf[Array[Optional[java.lang.Long]]] )

    (0 until 100).foreach(i⇒{

      val s1 = s"${Random.nextInt(122)},${Random.nextInt(322)},${Random.nextInt(142)}"
      val s2 =  s"${Random.nextInt(122)},${Random.nextInt(322)},${Random.nextInt(142)}"
      val s3 = s"${Random.nextInt(122)},${Random.nextInt(322)},${Random.nextInt(142)}"


      val b = ArrayBuffer[Optional[java.lang.Long]]( )
      b.append( Optional.of( Random.nextLong( ) ) )
      b.append( Optional.of( Random.nextLong( ) ) )

     assert(method.invoke(instance,Array(s1,s2,s3),b.toArray).equals(func(Array(s1,s2,s3),b.map(i⇒Some(Long.unbox(i.get()))).toArray)))
    })
  }

}
