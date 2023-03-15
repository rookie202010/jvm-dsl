package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.api
import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.VarRef
import com.dongjiaqiang.jvm.dsl.api.expression.binary.Add
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.api.expression.call.{FuncCallChain, MethodCall, VarCall}
import com.dongjiaqiang.jvm.dsl.api.expression.literal.ClazzLiteral
import com.dongjiaqiang.jvm.dsl.api.expression.statement.Return
import com.dongjiaqiang.jvm.dsl.core.optimize.OptimizeExpression
import com.dongjiaqiang.jvm.dsl.core.program.Program
import com.dongjiaqiang.jvm.dsl.core.symbol.generateProgramScope
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.{ListMap ⇒ MutableMap}
import scala.language.postfixOps

class ExpressionParserTestCallSuit extends AnyFunSuite {


  test("test call chain 1"){
    val input =
      """
        |
        |program{
        |   class B(Int c){}
        |
        |   class A(Int a,Int b){
        |       def toB()=B{
        |           return new B(a+b);
        |       }
        |   }
        |
        |   def until(Int a)=B{
        |       B b= new B(a.until(10).get(10));
        |       return b;
        |   }
        |   }
        |
        |   """.stripMargin
    val program = Program( generateProgramScope(input), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program clazz ("B")
    program clazz ("A") method (
      "toB"
      ) bodyBlock() expression (blockScope ⇒ {
      Return( Some(new ClazzLiteral( Array( Add( blockScope.varRef( "a" ), blockScope.varRef( "b" ) ) ), DefinitionClazzType( "B",program.programScope.classes("B") ) ) ))
    })

    program method ("until") bodyBlock() expression (blockScope ⇒ {

      val a = blockScope.varRef( "a" )

      blockScope.varDef( "b", DefinitionClazzType( "B",program.programScope.classes("B")), Some( new ClazzLiteral( Array( FuncCallChain( VarCall( a, "until", Array( 10 int ) ), List(
        MethodCall( None, "get", Array( 10 int ) )
      ) ) ), DefinitionClazzType( "B",program.programScope.classes("B")) ) ) )
    }) expression (blockScope ⇒ Return( Some(blockScope.varRef( "b" ) )))

    val generateP = generateProgram( input )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == program )
  }

  test("test call chain 2"){
    val input =
      """
        |program{
        |
        |   def size(List[Int] a)=Int{
        |       return a.size();
        |   }
        |
        |   def get(Map[String,Int] map)=Int{
        |       return map.get("key");
        |   }
        |
        |   def range(Int a)=Int{
        |       return a.to(100).slice(20,30).sum();
        |   }
        |
        |   def method(()=>C c)=Int{
        |       return c.apply().map(i=>{
        |
        |           Int=>Int plus = j=> { return j+i;};
        |
        |           return plus.apply(i);
        |       }).c.d.bar("xx") + c.apply().e.f.g;
        |   }
        |
        |
        |}
        |""".stripMargin

    val program = Program( generateProgramScope(input), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program method(
      "size"
    ) bodyBlock() expression(blockScope⇒{
        Return(Some(call.VarCall(blockScope.varRef("a"),"size",Array())))
    })

    program method(
      "get"
    ) bodyBlock() expression(blockScope⇒{
        Return(Some(call.VarCall(blockScope.varRef("map"),"get",Array("\"key\"" str))))
    })

    program method(
      "range"
    ) bodyBlock() expression(blockScope⇒{
        Return(Some(FuncCallChain(call.VarCall(blockScope.varRef("a"),"to",Array(100 int)),List(MethodCall(None,"slice",Array(20 int,30 int)),
          MethodCall(None,"sum",Array())))))
    })

    program method(
      "method"
    ) bodyBlock() expression(blockScope⇒{

        Return(Some(binary.Add( {
          val map = Lambda( Array( "i" ), (blockScope lambdaBlock() expression (scope ⇒ {

            val plus = Lambda( Array( "j" ), (scope lambdaBlock() expression (scope1 ⇒ {
              Return( Some(binary.Add( scope1.varRef( "j" ), scope1.varRef( "i" ) ) ))
            })).block )

            scope varDef("plus", LambdaType( Some( IntType ), IntType ), Some( plus ))

          }) expression (scope ⇒ {
            Return( Some(call.VarCall( scope.varRef( "plus" ), "apply", Array( scope.varRef( "i" ) ) ) ))
          })).block )

          val c = blockScope.varRef( "c" )
          FuncCallChain( call.VarCall( c, "apply", Array( ) ), List( MethodCall( None, "map", Array( map )), VarRef( List("c","d"),MutableMap(),None ), MethodCall( None, "bar", Array( "\"xx\"" str ) ) ) )
        },{
          val c = blockScope.varRef( "c" )
          FuncCallChain(call.VarCall(c,"apply",Array()),List(api.expression.`var`.VarRef(List("e","f","g"),MutableMap(),None)))
        })))
    })

    val generateP = generateProgram( input )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == program )
  }

  test("test call chain 3"){
    val input =
      """
        |
        |program{
        |
        |   class A(B b,Int d){}
        |   class B(Int c,C e){}
        |   class E(Int e,F f){}
        |
        |
        |   A a= new A(new B(10),12);
        |
        |
        |   def method(A a)=Int{
        |       return a.b.c+a.d;
        |   }
        |
        |   def method1(C c)=Int{
        |       return c.c1.c2 + a.b.c;
        |   }
        |
        |   def method2(Option[A] optional)=Long{
        |       return optional.map(a=>{ return a.b; }).get().e.e;
        |   }
        |
        |   def method3(F f)=Int{
        |       return f.a.c.d.foo(1,20).e.f.map(i=>{ return i+1;}).length();
        |   }
        |
        |}
        |""".stripMargin

    val programScope = generateProgramScope(input)

    val dslTypeResolver:DslType⇒Option[ImportClazzType] = dslType ⇒ {
      val clazzType = dslType.asInstanceOf[ClazzType]
      clazzType.clazzName match {
        case "C" ⇒
          Some( ImportClazzType( "C", fields = Map( "c1" → ImportClazzField( "c1", ClazzType( "C1" ) ) ) ) )
        case "C1" ⇒
          Some( ImportClazzType( "C1", fields = Map( "c2" → ImportClazzField( "c2", IntType ) ) ) )
        case "F" ⇒
          Some( ImportClazzType( "F", fields = Map( "a" → ImportClazzField( "a", ClazzType( "F1" ) ) ) ) )
        case "F1" ⇒
          Some( ImportClazzType( "F2", fields = Map( "c" → ImportClazzField( "c", ClazzType( "F2" ) ) ) ) )
        case "F2" ⇒
          Some( ImportClazzType( "F3", fields = Map( "d" → ImportClazzField( "d", ClazzType( "F4" ) ) ) ) )
      }
    }

    programScope.importManager.registerDslTypeResolver {
     dslTypeResolver
    }

    val program = Program(programScope, assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program clazz("A")
    program clazz("B")
    program clazz("E")

    program assign("a",new ClazzLiteral(Array(new ClazzLiteral(Array(10 int),DefinitionClazzType( "B",program.programScope.classes("B") )),12 int),DefinitionClazzType( "A",program.programScope.classes("A") )))

    program.method("method") bodyBlock() expression(blockScope⇒{

      Return(Some(binary.Add(blockScope.varRef("a","b","c"),blockScope.varRef("a","d"))))
    })

    program.method("method1") bodyBlock() expression(blockScope⇒{

      Return(Some(binary.Add(blockScope.varRef("c","c1","c2"),blockScope.varRef("a","b","c"))))
    })

    program.method("method2") bodyBlock() expression(blockScope⇒{

        val lambda = Lambda(Array("a"), (blockScope lambdaBlock() expression (blockScope⇒{

          val ab = blockScope.varRef("a","b")
          Return(Some(ab))
        })).block)

        Return(Some(FuncCallChain(call.VarCall(blockScope.varRef("optional"),"map",Array(lambda)),List(MethodCall(None,"get",Array()),api.expression.`var`.VarRef(List("e","e"),MutableMap(),None)))))
    })

    program.method("method3") bodyBlock() expression(blockScope⇒{

      val lambda = Lambda( Array( "i" ), (blockScope lambdaBlock() expression (blockScope ⇒ {

        val i = blockScope.varRef( "i" )
        Return( Some(binary.Add(i,1 int) ))
      })).block )

      Return(Some(FuncCallChain(call.VarCall(blockScope.varRef("f","a","c","d"),"foo",Array(1 int,20 int)),List(api.expression.`var`.VarRef(List("e","f"),MutableMap(),None),MethodCall(None,"map",Array(lambda)),MethodCall(None,"length",Array())))))

    })

    val generateP = generateProgram( input,dslTypeResolver )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == program )
  }

}
