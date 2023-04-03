package com.dongjiaqiang.jvm.dsl.java.core.translate.monad

import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.compile.JaninoCompiler
import com.dongjiaqiang.jvm.dsl.java.core.translate.ProgramTranslator
import com.typesafe.config.ConfigFactory
import org.apache.commons.lang3.StringUtils
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class TranslateStringSuit extends AnyFunSuite {
  def compile(code: String): Class[_] = {
    val javaProgram = code.translate( JavaTranslatorContext( packageName = "com.example", javaTranslateConfig = ConfigFactory.empty( ) ) )
    val javaCompile = new JaninoCompiler( this.getClass.getClassLoader )
    javaCompile.compile( javaProgram, "com.example" )
  }

  test( "test string 1" ) {
    def func(a: String, b: String, e: String): Double = {
      a.toLong + b.toDouble * e.toInt
    }

    val code =
      """
        |program{
        |   def eval(String a,String b,String e)=Double{
        |       Long c = a.toLong();
        |       Double d = b.toDouble();
        |       return c+d*e.toInt();
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[String], classOf[String], classOf[String] )
    (0 until 10).foreach( _ ⇒ {
      val a = Random.nextLong( ).toString
      val b = (Random.nextDouble( ) * 100).toString
      val c = Random.nextInt( 300 ).toString
      assert( method.invoke( instance, a, b, c ).equals( func( a, b, c ) ) )
    } )
  }
  test( "test string 2" ) {
    def func(a: Int, b: Double, c: String, d: Long): String = {
      val str = "a: %s,b: %s,c: %s,d: %s %s %s".format( a, b, c, d, 100, c.toUpperCase( ) )
      if (str.isEmpty) {
        "%s %s %s %s".format( str.substring( 1, 10 ), str.trim, str.reverse, StringUtils.isNumeric( str ) )
      } else {
        val fields = "a,b,c".split( "," );
        "ok" + 10d + fields( 0 )
      }
    }

    val code =
      """
        |program{
        |   def eval(Int a,Double b,String c,Long d)=String{
        |       String str =  "a: %s,b: %s,c: %s,d: %s %s %s".format(a,b,c,d,100,c.toUpperCase());
        |       if(str.isEmpty()){
        |           return "%s %s %s %s".format(str.subString(1,10),str.trim(),str.reverse(),str.isNumeric());
        |       }else{
        |           Array[String] fields = "a,b,c".split(",");
        |           return "ok"+10d+fields[0];
        |       }
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", java.lang.Integer.TYPE, java.lang.Double.TYPE, classOf[String], java.lang.Long.TYPE )
    (0 until 10).foreach( _ ⇒ {
      val a = Random.nextInt( 100 )
      val b = Random.nextDouble( ) * 100
      val c = Random.nextString( 10 )
      val d = Random.nextLong( )
      assert( method.invoke( instance, Int.box( a ), Double.box( b ), c, Long.box( d ) ).equals( func( a, b, c, d ) ) )
    } )
  }
  test( "test string 3" ) {
    def func(a: String): String = {
      val chars = a.map( _ ⇒ 10 )
      val b = a.map( c ⇒ c.toLower )
      val c = b.map( k ⇒ {
        if (k == 'c') {
          'b'
        } else {
          val kk = k.toInt + 10
          val fb = "father".filter( bc ⇒ bc != 'a' )
          if (kk == 10) {
            kk.toChar
          } else {
            fb.head
          }
        }
      } )
      chars.mkString( a.map( c ⇒ c.toUpper ) ) + b + c
    }

    val code =
      """
        |program{
        |   def method(String a)=String{
        |     return a;
        |   }
        |   def eval(String a)=String throws Exception{
        |       List[Int] chars = a.map(c=> { return 10;});
        |       String b = a.map(c=> { return c.toLowerCase(); });
        |       String c = b.map(k=> {
        |         if(k=='c'){
        |           return 'b';
        |         }else{
        |           Int kk = k.toInt()+10;
        |           String fb = "father".filter( bc =>{
        |            return bc!='a';
        |            });
        |            if(kk==10){
        |               return kk.toChar();
        |            }else{
        |               return fb.head();
        |            }
        |         }
        |       });
        |       return chars.mkString(a.map(c=> { return c.toUpperCase(); }))+b+c;
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[String] )
    (0 until 10).foreach( _ ⇒ {
      val c = Random.nextString( Random.nextInt( 25 ) )
      assert( method.invoke( instance, c ).equals( func( c ) ) )
    } )
  }

  test( "test string 4" ) {
    def func(a: String): Char = {
      a.reduce( (c1, c2) ⇒ {
        val c3 = c1.toInt + c2.toInt
        c3.toChar
      } )
    }

    val code =
      """
        |program{
        |   def eval(String a)=Char throws Exception{
        |       Char s = a.reduce((c1,c2)=>{
        |           Int b = c1.toInt()+c2.toInt();
        |           return b.toChar();
        |       });
        |       return s;
        |  }
        |}
        |""".stripMargin
    val clazz = compile( code )
    val instance = clazz.getConstructors.head.newInstance( )
    val method = clazz.getMethod( "eval", classOf[String] )
    (0 until 10).foreach( _ ⇒ {
      val a = Random.nextLong( ).toString
      println(method.invoke( instance, a ),func( a ))
      assert( method.invoke( instance, a ).equals( func( a ) ) )
    } )
  }
}
