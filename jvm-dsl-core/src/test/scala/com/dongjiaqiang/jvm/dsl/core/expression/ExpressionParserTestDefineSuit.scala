package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.core.program.Program
import com.dongjiaqiang.jvm.dsl.core.symbol.generateProgramScope
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.{ListMap ⇒ MutableMap}
import scala.language.postfixOps

class ExpressionParserTestDefineSuit  extends AnyFunSuite {


  test( "define empty" ) {
    val input =
      """program {

        }
        """
    val program = Program( generateProgramScope(input), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    assert( generateProgram(input) == program )
  }


  test("define assert") {
    val input =
      """
        |program{
        |   def method(Int a)=Unit{
        |       assert a==1;
        |   }
        |}
        |""".stripMargin
    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    program method("method") bodyBlock() expression(blockScope⇒{
      Assert(Eq(blockScope.varRef("a"),1 int))
    })

  }

  test("define methods"){
    val input =
      """
        |program{
        |     import com.xxx.Log;
        |
        |     def method1(Double i,Double j)=Double{
        |         Double=>Double plus = i=>{ return i+j;};
        |         Double=>Double minus;
        |         minus = i=>{ return i-j;};
        |
        |         return plus.apply(i) + minus.apply(i);
        |     }
        |
        |     def method2(Log log,Map[Int,Long] map)=Unit{
        |         map.foreach(kv=>{
        |             log.info("k: %s v: %s",kv._1,kv._2);
        |         });
        |         return;
        |     }
        |}
        |""".stripMargin
    val program = Program( generateProgramScope(input), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    program method(
      "method1"
    ) bodyBlock() expression(blockScope⇒{
       val j = blockScope.varRef("j")
       val body = blockScope lambdaBlock() expression(
          scope⇒{
            Return(Add(scope.varRef("i"),j))
          }
        )
      blockScope.varDef("plus",LambdaType(Some(DoubleType),DoubleType),Some(Lambda(Array("i"),body.block)))
    }) expression(scope⇒ {
      scope.varDef( "minus", LambdaType( Some( DoubleType ), DoubleType ), None )
    }) expression(blockScope⇒{
        val j = blockScope varRef("j")
        val body = blockScope lambdaBlock() expression(scope⇒{
            Return(Sub(scope.varRef("i"),j))
        })
        val minus = blockScope varRef("minus")
        Assign(minus,Lambda(Array("i"),body.block))
    }) expression(blockScope⇒{
        val i = blockScope.varRef("i")
        val plus = blockScope.varRef("plus")
        val minus = blockScope.varRef("minus")
        Return(Add(VarCall(plus,"apply",Array(i)),VarCall(minus,"apply",Array(i))))
    })

    program method (
      "method2"
      ) bodyBlock() expression(
      blockScope⇒{
          val map = blockScope.varRef("map")
          val body = blockScope lambdaBlock() expression(scope⇒{
              val k = scope.varRef("kv","_1")
              val v = scope.varRef("kv","_2")
              val log = blockScope.varRef("log")
              VarCall(log,"info",Array("\"k: %s v: %s\"" str,k,v))
          })
          VarCall(map,"foreach",Array(Lambda(Array("kv"),body.block)))
      }) expression(_⇒{
        Return(UnitLiteral)
    })

    assert( generateProgram(input) == program )

  }

  test( "define classes" ) {
    val input =
      """
        |program{
        |   class A(Int a,Long b){
        |       def foo((Int,Long,String)=>String lambda)=String{
        |           return lambda.apply(1,2L);
        |       }
        |       def bar(Long a)=Int{
        |           return a.toInt();
        |       }
        |
        |       def koo(Int a)=Int{
        |           Int=>Int div = i=> { return a/i;};
        |           return div.apply(a);
        |       }
        |   }
        |}
        |""".stripMargin
    val program = Program( generateProgramScope(input), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program clazz (
      "A"
      ) method (
      "foo"
      ) bodyBlock() expression (blockScope ⇒ {
      val varRef = blockScope.varRef( "lambda" )
      Return( VarCall( varRef, "apply", Array( 1 int, 2 long ) ) )
    }) belongClazzMethod() belongClazz() method (
      "bar"
      ) bodyBlock() expression (blockScope ⇒ {
      val varRef = blockScope.varRef( "a" )
      Return( VarCall( varRef, "toInt", Array( ) ) )
    }) belongClazzMethod() belongClazz() method(
      "koo"
    ) bodyBlock() expression(blockScope⇒{
      val a = blockScope.varRef( "a" )
      val body = (blockScope lambdaBlock() expression (scope⇒{
        val i = scope.varRef( "i" )
        Return(Div(a,i))
      }) ) . block

      blockScope.varDef("div",LambdaType(Some(IntType),IntType),Some(Lambda(Array("i"),body)))
    }) expression(blockScope⇒{
      Return(VarCall(blockScope.varRef( "div" ),"apply",Array(blockScope.varRef( "a" ))))
    })

    assert( program == generateProgram(input) )

  }

  test( "define fields" ) {
    val input =
      """
        |program{
        |            import com.xxx.yyy.Foo;
        |            Int i = 10;
        |            Long j = 200L;
        |            Float k = 10.1f;
        |
        |            A a = new A(10);
        |            String str;
        |            Map[String,Int] ages = {"Tom":12};
        |
        |            Set[Int] set = {1,2,3,4,5};
        |
        |            List[String] list = ["a","b","c"];
        |
        |            (Int,Long,String) tuple3 = (1,10L,"x");
        |
        |            volatile Map[String,String] addresses = {};
        |
        |            char c = 'a';
        |
        |            char d = '\'';
        |
        |            bool b = true;
        |
        |            Long j2 = i*j*10*a.b*a.foo();
        |
        |            (Int,Long)=>Int and = (i,j)=>{ return i+j;};
        |
        |            Int=>Int mul = i=> { return and.apply(i,i)*i;};
        |
        |            (Int,Long)=>String format = (i,j)=>{
        |
        |                 Int a = and.apply(i,j);
        |                 Int b = mul.apply(a);
        |
        |                 Int=>String f = i=>{
        |
        |                   return String.format("%s %s %s",a,b,i);
        |                 };
        |
        |                 return f.apply(b);
        |
        |            };
        |
        |            Int cc = Foo.call(10);
        |         }
        |""".stripMargin
    val program = Program( generateProgramScope(input), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    program assign(
      "i", 10 int
    ) assign(
      "j", 200 long
    ) assign(
      "k", 10.1f float
    ) assign(
      "a", "A" clazz (10 int)
    ) assign(
      "ages", new MapLiteral( Array( ("\"Tom\"" str, 12 int) ), MapType( UnResolvedType, UnResolvedType ) )
    ) assign(
      "set", new SetLiteral( Array( 1 int, 2 int, 3 int, 4 int, 5 int ), SetType( UnResolvedType ) )
    ) assign(
      "list", new ListLiteral( Array( "\"a\"" str, "\"b\"" str, "\"c\"" str ), ListType( UnResolvedType ) )
    ) assign(
      "tuple3", new TupleLiteral( Array( 1 int, 10 long, "\"x\"" str ), TupleType( Array( UnResolvedType, UnResolvedType, UnResolvedType ) ) )
    ) assign(
      "addresses", new SetLiteral( Array( new ListLiteral( Array( ), ListType( UnResolvedType ) ) ), SetType( UnResolvedType ) )
    ) assign(
      "c", 'a' char
    ) assign(
      "d", ''' char
    ) assign(
      "b", true bool
    ) assign(
      "j2", {
      Mul( Mul( Mul( Mul( program.varRef( "i" ), program.varRef( "j" ) ), 10 int ), program.varRef( "a", "b" ) )
        , VarCall( program.varRef( "a" ), "foo", Array( ) ) )
    }
    ) assign(
      "and",
      Lambda( Array( "i", "j" ), (program lambdaBlock() expression (blockScope ⇒ {
        val i = blockScope.varRef( "i" )
        val j = blockScope.varRef( "j" )
        Return( Add( i, j ) )
      })).block )) assign(
      "mul", Lambda( Array( "i" ), (program lambdaBlock() expression (blockScope ⇒ {
      val i = blockScope.varRef( "i" )
      val and = program.varRef( "and" )
      Return( Mul( VarCall( and, "apply", Array( i, i ) ), i ) )
    })).block )
    ) assign(
      "format", Lambda( Array( "i", "j" ), (program lambdaBlock() expression (blockScope ⇒ {
      val i = blockScope.varRef( "i" )
      val j = blockScope.varRef( "j" )
      val and = blockScope.varRef( "and" )
      blockScope.varDef( "a", IntType, Some( VarCall( and, "apply", Array( i, j ) ) ) )
    }) expression (blockScope ⇒ {
      val a = blockScope.varRef( "a" )
      val mul = blockScope.varRef( "mul" )
      blockScope.varDef( "b", IntType, Some( VarCall( mul, "apply", Array( a ) ) ) )
    }) expression (blockScope ⇒ {

      val body = (blockScope lambdaBlock() expression (scope ⇒ {
        val a = scope.varRef( "a" )
        val b = scope.varRef( "b" )
        val i = scope.varRef( "i" )
        Return( StaticCall( StringType, "format", Array( "\"%s %s %s\"" str,a, b, i ) ) )
      })).block

      blockScope.varDef( "f", LambdaType( Some( IntType ), StringType ), Some( Lambda( Array( "i" ), body ) ) )
    }) expression (blockScope ⇒ {
      val f = blockScope.varRef( "f" )
      val b = blockScope.varRef( "b" )
      Return( VarCall( f, "apply", Array( b ) ) )
    }

      )
      ).block )) assign(
      "cc",StaticCall(ClazzType("Foo"),"call",Array(10 int))
    )

    assert( generateProgram(input) == program )


  }

}
