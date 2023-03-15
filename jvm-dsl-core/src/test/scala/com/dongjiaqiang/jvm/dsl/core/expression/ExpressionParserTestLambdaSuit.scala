package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.{DefinitionClazzType, IntType, LambdaType}
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.binary.{Add, Div}
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

class ExpressionParserTestLambdaSuit extends AnyFunSuite{


  test("define lambda 1"){

    val  input =
      """
        |program{
        |
        |   class A((Int,Int)=>Int lambda,Int a){
        |       def foo(Int b)=Int{
        |            return lambda.apply(a,b);
        |       }
        |   }
        |
        |   def method()=Int{
        |       A a = new A((i,j)=>{ return i/j;},10);
        |       return a.foo(20);
        |   }
        |
        |   class B(Int a,C c){}
        |
        |   class C(Array[Int] c){}
        |
        |   def method1(Array[B] ass,Int  index)=Int{
        |       return ass[index].a;
        |   }
        |
        |   def method2(Array[Array[B]] ass,Int index)=Int{
        |       return ass[index][index].a;
        |   }
        |
        |   def method3(Array[Array[Array[B]]] ass,Int index)=Int{
        |       return ass[index][index][index+1].c[10];
        |   }
        |
        |
        |}
        |""".stripMargin

    val programScope = generateProgramScope(input)
    val program = Program( programScope, assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program clazz("A") method("foo") bodyBlock() expression(blockScope⇒{
      Return(Some(VarCall(blockScope.varRef("lambda"),"apply",Array(blockScope.varRef("a"),blockScope.varRef("b")))))
    })

    program.clazz("B")
    program.clazz("C")
    program method("method") bodyBlock() expression(blockScope⇒{

      val lambda = Lambda(Array("i","j"), (blockScope lambdaBlock() expression(scope⇒{
        Return(Some(Div(scope.varRef("i"),scope.varRef("j"))))
      })).block)

      blockScope.varDef("a",DefinitionClazzType("A",programScope.classes("A")),Some(new ClazzLiteral(Array(lambda,10 int),
        DefinitionClazzType("A",programScope.classes("A")))))

    }) expression(blockScope⇒{

      Return(Some(call.VarCall(blockScope.varRef("a"),"foo",Array(20 int))))
    })

    program method("method1") bodyBlock() expression(blockScope⇒{
       Return(Some(blockScope.resolveVarRef(MutableMap(0→List(blockScope.varRef("index"))),"ass","a")))
    })

    program method ("method2") bodyBlock() expression (blockScope ⇒ {
      Return( Some(blockScope.resolveVarRef( MutableMap( 0 → List( blockScope.varRef( "index" ),
        blockScope.varRef( "index" )) ), "ass", "a" ) ))
    })

    program method ("method3") bodyBlock() expression (blockScope ⇒ {
      Return( Some(blockScope.resolveVarRef( MutableMap( 0 → List( blockScope.varRef( "index" ),
        blockScope.varRef( "index" ),Add(blockScope.varRef("index"),1 int) ),1→List(10 int) ), "ass", "c" ) ))
    })

    val generateP = generateProgram( input )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == program )
  }

  test("define lambda 2"){

    val input =
      """
        |program{
        |
        |   Log log;
        |
        |   def method(List[Int] list,Int k)=Unit{
        |       list.map(i=>{
        |
        |            Int=>Int plus = j => { return j+i; };
        |
        |            return plus.apply(k);
        |
        |       }).foreach(i=>{
        |
        |             log.info(i);
        |       });
        |   }
        |}
        |""".stripMargin

    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    program method("method") bodyBlock()  expression(blockScope⇒{

        val varCall = call.VarCall(blockScope.varRef("list"),"map",Array(Lambda(Array("i"),(blockScope lambdaBlock() expression(scope⇒{

           scope.varDef("plus",LambdaType(Some(IntType),IntType),Some(Lambda(Array("j"),(scope lambdaBlock() expression(scope1⇒{
            Return(Some(binary.Add(scope1.varRef("j"),scope1.varRef("i"))))
          })).block)))

        }) expression(scope⇒ Return(Some(call.VarCall(scope.varRef("plus"),"apply",Array(scope.varRef("k"))))))).block)))

        val parts = List(MethodCall(None,"foreach",Array({


          Lambda(Array("i"),(blockScope lambdaBlock() expression(scope⇒{
            call.VarCall(scope.varRef("log"),"info",Array(scope.varRef("i")))
          })).block)

        })))

        FuncCallChain(varCall,parts)

    })

    val generateP = generateProgram( input )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == program )

  }
}
