package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.{ClazzType, IntType, StringType}
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.core.program.Program
import com.dongjiaqiang.jvm.dsl.core.symbol.generateProgramScope
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.{ListMap ⇒ MutableMap}
import scala.language.postfixOps

class ExpressionParserTestLoopSuit extends AnyFunSuite {


  test("define for"){
    val input =
      """
        |program{
        |   def method()=Int{
        |       Int j = 0;
        |       for(Int i=0;i<100;i=i+1){
        |           j = j+i;
        |       }
        |       return j;
        |   }
        |
        |   def method1(List[Int] list)=Int{
        |       Int j = 0;
        |       for(Int i:list){
        |           j = j+i;
        |       }
        |       return j;
        |   }
        |
        |   def method2(Map[String,String] map)=String{
        |       StringBuilder sb = new StringBuilder();
        |       for(String key,String value:map){
        |           sb.append(key);
        |           sb.append(',');
        |           sb.append(value);
        |       }
        |       return sb.toString();
        |   }
        |}
        |""".stripMargin

    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    program method (
      "method"
      ) bodyBlock() updateVarDef("j", IntType, Some( 0 int )) forLoopBlock(
        scope⇒scope.varDef("i",IntType,Some(0 int)),
        scope⇒Lt(scope.varRef("i"),100 int),
        scope⇒Assign(scope.varRef("i"),Add(scope.varRef("i"),1 int))
    ) expression(blockScope⇒{
        Assign(blockScope.varRef("j"),Add(blockScope.varRef("j"),blockScope.varRef("i")))
    }) belongBlock() expression(blockScope⇒{
        Return(blockScope.varRef("j"))
    })
    program method(
      "method1"
    ) bodyBlock() updateVarDef("j", IntType, Some( 0 int )) forCollectionBlock(
      scope⇒scope.varDef("i",IntType,None),
      scope⇒scope.varRef("list")
    ) expression(blockScope⇒{
      Assign(blockScope.varRef("j"),Add(blockScope.varRef("j"),blockScope.varRef("i")))
    }) belongBlock() expression (blockScope ⇒ {
      Return( blockScope.varRef( "j" ) )
    })

    program method(
      "method2"
    ) bodyBlock() updateVarDef("sb", ClazzType("StringBuilder"), Some( new ClazzLiteral(Array(),ClazzType("StringBuilder")) )) forMapBlock(
      scope⇒ scope.varDef("key",StringType,None),
      scope⇒ scope.varDef("value",StringType,None),
        scope⇒scope.varRef("map")) expression(blockScope⇒{
        VarCall(blockScope.varRef("sb"),"append",Array(blockScope.varRef("key")))
    }) expression (blockScope ⇒ {
      VarCall( blockScope.varRef( "sb" ), "append", Array(  '.' char) )
    }) expression (blockScope ⇒ {
      VarCall( blockScope.varRef( "sb" ), "append", Array( blockScope.varRef( "value" ) ) )
    }) belongBlock() expression(blockScope⇒{
        Return(VarCall(blockScope.varRef("sb"),"toString",Array()))
    })
    val i =generateProgram( input )

    assert( generateProgram( input ) == program )

  }

  test("define while"){
    val input =
      """
        |program{
        |   def method()=Int{
        |       Int i=100;
        |       while(i>0){
        |         i = i%10;
        |       }
        |       return i;
        |   }
        |}
        |""".stripMargin

    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    program method (
      "method"
      ) bodyBlock() updateVarDef("i", IntType, Some( 100 int )) expression(blockScope⇒{
        WhileCondition(Gt(blockScope.varRef("i"),0 int))
    }) whileBlock() expression(blockScope⇒{
        Assign(blockScope.varRef("i"),Mod(blockScope.varRef("i"),10 int))
    }) belongBlock() expression(blockScope⇒{
        Return(blockScope.varRef("i"))
    })

    assert( generateProgram( input ) == program )
  }

  test("define do while"){
      val input =
        """
          |program{
          |   def method()=Int{
          |       Int i=0;
          |       do{
          |         i=i+1;
          |       }while(i<100)
          |       return i;
          |   }
          |}
          |""".stripMargin
    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    program method(
      "method"
    ) bodyBlock() updateVarDef("i",IntType,Some(0 int)) doWhileBlock() expression(blockScope⇒{
        val i = blockScope.varRef("i")
        Assign(i,Add(i,1 int))
    }) belongBlock() expression(blockScope⇒{
        DoWhileCondition(Lt(blockScope.varRef("i"),100 int))
    }) expression(blockScope⇒{
        Return(blockScope.varRef("i"))
    })

    assert( generateProgram(input) == program )
  }
}
