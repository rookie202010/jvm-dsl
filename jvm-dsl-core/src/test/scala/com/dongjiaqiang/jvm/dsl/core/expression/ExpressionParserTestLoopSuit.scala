package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.api
import com.dongjiaqiang.jvm.dsl.api.`type`.{ClazzType, IntType, ListType, StringType}
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.Assign
import com.dongjiaqiang.jvm.dsl.api.expression.binary._
import com.dongjiaqiang.jvm.dsl.api.expression.call.VarCall
import com.dongjiaqiang.jvm.dsl.api.expression.literal.ClazzLiteral
import com.dongjiaqiang.jvm.dsl.api.expression.statement.{Break, Continue, Return}
import com.dongjiaqiang.jvm.dsl.core.optimize.OptimizeExpression
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
        |def method()=Int{
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
        |
        |   def method3(List[List[Int]] list)=Int{
        |       Int sum = 0;
        |       for(List[Int] i:list){
        |           for(Int j:i){
        |               sum = sum+j;
        |           }
        |       }
        |       return sum;
        |   }
        |
        |
        |}
        |""".stripMargin

    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    val programOptimize = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )


    program method (
      "method"
      ) bodyBlock() updateVarDef("j", IntType, Some( 0 int )) forLoopBlock(
        scope⇒scope.varDef("i",IntType,Some(0 int)),
        scope⇒Lt(scope.varRef("i"),100 int),
        scope⇒Assign(scope.varRef("i"),Add(scope.varRef("i"),1 int))
    ) expression(blockScope⇒{
        api.expression.`var`.Assign(blockScope.varRef("j"),binary.Add(blockScope.varRef("j"),blockScope.varRef("i")))
    }) belongBlock() expression(blockScope⇒{
        Return(Some(blockScope.varRef("j")))
    })


    programOptimize method(
      "method"
    ) bodyBlock() updateVarDef("j", IntType, Some( 0 int )) expression(blockScope⇒{

      val scope = blockScope statementBlock(false)

      val loopVarDef = scope.varDef("i",IntType,Some(0 int))
      val loopVarCondition = binary.Lt(scope.varRef("i"),100 int)
      val loopVarUpdate = api.expression.`var`.Assign(scope.varRef("i"),binary.Add(scope.varRef("i"),1 int))

      val body = scope expression(blockScope⇒{
        api.expression.`var`.Assign(blockScope.varRef("j"),binary.Add(blockScope.varRef("j"),blockScope.varRef("i")))
      })

      For(loopVarDef,loopVarCondition,loopVarUpdate,body.block)
    }) expression(scope⇒{
      Return(Some(scope.varRef("j")))
    })

    program method(
      "method1"
    ) bodyBlock() updateVarDef("j", IntType, Some( 0 int )) forCollectionBlock(
      scope⇒scope.varDef("i",IntType,None),
      scope⇒scope.varRef("list")
    ) expression(blockScope⇒{
      api.expression.`var`.Assign(blockScope.varRef("j"),binary.Add(blockScope.varRef("j"),blockScope.varRef("i")))
    }) belongBlock() expression (blockScope ⇒ {
      Return( Some(blockScope.varRef( "j" ) ))
    })

    programOptimize method (
      "method1"
      ) bodyBlock() updateVarDef("j", IntType, Some( 0 int )) expression (blockScope ⇒ {

      val scope = blockScope statementBlock(false)

      val loopVarDef = scope.varDef( "i", IntType, None )
      val looped = scope.varRef("list")

      val body = scope expression (blockScope ⇒ {
        api.expression.`var`.Assign( blockScope.varRef( "j" ), binary.Add( blockScope.varRef( "j" ), blockScope.varRef( "i" ) ) )
      })

      ForCollection( loopVarDef, looped, body.block )
    }) expression (scope ⇒ {
      Return( Some(scope.varRef( "j" ) ))
    })

    program method(
      "method2"
    ) bodyBlock() updateVarDef("sb", ClazzType("StringBuilder"), Some( new ClazzLiteral(Array(),ClazzType("StringBuilder")) )) forMapBlock(
      scope⇒ scope.varDef("key",StringType,None),
      scope⇒ scope.varDef("value",StringType,None),
        scope⇒scope.varRef("map")) expression(blockScope⇒{
        VarCall(blockScope.varRef("sb"),"append",Array(blockScope.varRef("key")))
    }) expression (blockScope ⇒ {
      call.VarCall( blockScope.varRef( "sb" ), "append", Array(  ',' char) )
    }) expression (blockScope ⇒ {
      call.VarCall( blockScope.varRef( "sb" ), "append", Array( blockScope.varRef( "value" ) ) )
    }) belongBlock() expression(blockScope⇒{
        Return(Some(call.VarCall(blockScope.varRef("sb"),"toString",Array())))
    })

    programOptimize method (
      "method2"
      ) bodyBlock() updateVarDef("sb", ClazzType("StringBuilder"),
      Some( new ClazzLiteral(Array(),ClazzType("StringBuilder")) ))  expression (blockScope ⇒ {

      val scope = blockScope statementBlock(false)

      val loopKeyDef = scope.varDef("key",StringType,None)
      val loopValueDef = scope.varDef("value",StringType,None)
      val looped = scope.varRef("map")

      val body = scope expression (blockScope ⇒ {
        call.VarCall( blockScope.varRef( "sb" ), "append", Array( blockScope.varRef( "key" ) ) )
      }) expression (blockScope ⇒ {
        call.VarCall( blockScope.varRef( "sb" ), "append", Array( ',' char ) )
      }) expression (blockScope ⇒ {
        call.VarCall( blockScope.varRef( "sb" ), "append", Array( blockScope.varRef( "value" ) ) )
      })

      ForMap( loopKeyDef,loopValueDef,looped,body.block )
    }) expression (blockScope ⇒ {
      Return( Some(call.VarCall( blockScope.varRef( "sb" ), "toString", Array( ) ) ))
    })


    program method (
      "method3"
      ) bodyBlock() updateVarDef("sum", IntType, Some( 0 int )) forCollectionBlock(
      scope ⇒ {
        scope.varDef( "i", ListType( IntType ), None )
      },
      scope ⇒ {
        scope.varRef( "list" )
      }) forCollectionBlock(
      scope ⇒ scope.varDef( "j", IntType, None ),
      scope ⇒ scope.varRef( "i" )
    ) expression (blockScope ⇒ {
      api.expression.`var`.Assign( blockScope.varRef( "sum" ), binary.Add( blockScope.varRef( "sum" ), blockScope.varRef( "j" ) ) )
    }) belongBlock() belongBlock() expression (blockScope ⇒ {
      Return( Some(blockScope.varRef( "sum" ) ))
    })

    programOptimize method (
      "method3"
      ) bodyBlock() updateVarDef("sum", IntType, Some( 0 int ))  expression (blockScope⇒ {

      val scope = blockScope statementBlock(false)

      val localVarDef = scope.varDef( "i", ListType( IntType ), None )
      val looped = scope.varRef( "list" )

      val body = scope expression(blockScope⇒{

        val scope = blockScope statementBlock(false)

        val localVarDef = scope.varDef( "j", IntType, None )
        val looped = scope.varRef( "i" )
        val body = scope expression(scope⇒{
          api.expression.`var`.Assign( scope.varRef( "sum" ), binary.Add( scope.varRef( "sum" ), scope.varRef( "j" ) ) )
        })
        ForCollection(localVarDef,looped,body.block)
      })

      ForCollection(localVarDef,looped,body.block)
    }) expression (blockScope ⇒ {
      Return( Some(blockScope.varRef( "sum" ) ))
    })

    val generateP = generateProgram( input )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == programOptimize )

  }

  test("define while"){
    val input =
      """
        |program{

        | def method()=Int{
        |       Int i=100;
        |       while(i>0){
        |         i = i%10;
        |       }
        |       return i;
        |   }
        |
        |   def method1()=Int{
        |       Int j = 100;
        |       while(j<100){
        |           if(j>=10){
        |               continue;
        |           }
        |           j = j-1;
        |       }
        |       return j;
        |   }
        |
        |   def method2()=Int{
        |       Int j = 0;
        |       Int i = 0;
        |       Int sum = 0;
        |       while(j<100){
        |           while(i<100){
        |               sum = sum+i;
        |               i = i + 1;
        |           }
        |           sum = sum + j;
        |           j = j + 1;
        |       }
        |   }
        |}
        |""".stripMargin

    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    val programOptimize = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program method(
      "method2"
    ) bodyBlock() updateVarDef ("j",IntType,Some(0 int)) updateVarDef ("i",IntType,Some(0 int)) updateVarDef ("sum",IntType,Some((0 int))) expression (blockScope⇒{
        WhileCondition(binary.Lt(blockScope.varRef("j"),100 int))
    }) whileBlock() expression (blockScope⇒{
        WhileCondition(binary.Lt(blockScope.varRef("i"),100 int))
    }) whileBlock() expression (blockScope⇒{
        api.expression.`var`.Assign(blockScope.varRef("sum"),binary.Add(blockScope.varRef("sum"),blockScope.varRef("i")))
    }) expression (blockScope⇒{
      api.expression.`var`.Assign(blockScope.varRef("i"),binary.Add(blockScope.varRef("i"),1 int))
    }) belongBlock() expression(blockScope⇒{
      api.expression.`var`.Assign(blockScope.varRef("sum"),binary.Add(blockScope.varRef("sum"),blockScope.varRef("j")))
    }) expression(blockScope ⇒ {
      api.expression.`var`.Assign( blockScope.varRef( "j" ), binary.Add( blockScope.varRef( "j" ), 1 int ) )
    })

    programOptimize method(
      "method2"
    ) bodyBlock() updateVarDef (
      "j",IntType,Some(0 int)) updateVarDef (
      "i",IntType,Some(0 int)) updateVarDef (
      "sum",IntType,Some((0 int))) expression(blockScope⇒{

      val condition1 = binary.Lt(blockScope.varRef("j"),100 int)
      val childScope = blockScope statementBlock(false)
      val childScope1 = childScope statementBlock(false)

      val condition2 = binary.Lt(childScope.varRef("i"),100 int)
      val body2 = childScope1 expression(blockScope⇒{
        api.expression.`var`.Assign(blockScope.varRef("sum"),binary.Add(blockScope.varRef("sum"),blockScope.varRef("i")))
      }) expression (blockScope ⇒ {
        api.expression.`var`.Assign( blockScope.varRef( "i" ), binary.Add( blockScope.varRef( "i" ), 1 int ) )
      })


      val body1 = childScope expression (_⇒ {
        While(condition2,body2.block)
      })expression (blockScope ⇒ {
          api.expression.`var`.Assign( blockScope.varRef( "sum" ), binary.Add( blockScope.varRef( "sum" ), blockScope.varRef( "j" ) ) )
        }) expression (blockScope ⇒ {
          api.expression.`var`.Assign( blockScope.varRef( "j" ), binary.Add( blockScope.varRef( "j" ), 1 int ) )
        })

      While(condition1,body1.block)
    })

    program method (
      "method"
      ) bodyBlock() updateVarDef("i", IntType, Some( 100 int )) expression(blockScope⇒{
        WhileCondition(Gt(blockScope.varRef("i"),0 int))
    }) whileBlock() expression(blockScope⇒{
        api.expression.`var`.Assign(blockScope.varRef("i"),Mod(blockScope.varRef("i"),10 int))
    }) belongBlock() expression(blockScope⇒{
        Return(Some(blockScope.varRef("i")))
    })

    programOptimize method(
      "method"
    ) bodyBlock() updateVarDef("i", IntType, Some( 100 int )) expression(blockScope⇒{
        val condition = binary.Gt(blockScope.varRef("i"),0 int)
        val childScope = blockScope statementBlock(false)
        val body = childScope expression (blockScope ⇒ {
          api.expression.`var`.Assign( blockScope.varRef( "i" ), binary.Mod( blockScope.varRef( "i" ), 10 int ) )
        })
      While(condition,body.block)
    }) expression (blockScope ⇒ {
      Return( Some(blockScope.varRef( "i" ) ))
    })

    program method(
      "method1"
    ) bodyBlock() updateVarDef("j", IntType, Some( 100 int )) expression(blockScope⇒{
      WhileCondition(binary.Lt(blockScope.varRef("j"),100 int))
    }) whileBlock() expression(blockScope⇒{
        IfCondition(Ge(blockScope.varRef("j"),10 int),first = true)
    }) ifBlock() expression(_⇒Continue) belongBlock() expression(blockScope⇒{
        api.expression.`var`.Assign(blockScope.varRef("j"),Sub(blockScope.varRef("j"),1 int))
    }) belongBlock() expression(blockScope⇒Return(Some(blockScope.varRef("j"))))

    programOptimize method(
      "method1"
    ) bodyBlock() updateVarDef("j", IntType, Some( 100 int )) expression(blockScope⇒{

      val whileCondition = binary.Lt(blockScope.varRef("j"),100 int)
      val childScope = blockScope statementBlock(false)
      val childScope1 = childScope statementBlock(false)

      val ifCondition = binary.Ge(childScope.varRef("j"),10 int)
      val ifBlock = childScope1 expression(_⇒Continue)

      val whileBody = childScope expression(_⇒{
        If(Array((ifCondition,ifBlock.block)),None)
      }) expression (blockScope ⇒ {
        api.expression.`var`.Assign( blockScope.varRef( "j" ), binary.Sub( blockScope.varRef( "j" ), 1 int ) )
      })

      While(whileCondition,whileBody.block)
    }) expression(blockScope⇒Return(Some(blockScope.varRef("j"))))

    val generateP = generateProgram( input )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == programOptimize )

  }

  test("define do while"){
      val input =
        """
          |program{

          |def method()=Int{
          |       Int i=0;
          |       do{
          |         i=i+1;
          |       }while(i<100)
          |       return i;
          |   }
          |
          |   def method1()=Int{
          |       Int i=0;
          |       do{
          |         i=i+1;
          |         if(i==30){
          |           break;
          |         }
          |       }while(i<100)
          |       return i;
          |   }
          |
          |def method2()=Int{
          |      Int i=0;
          |      Int j=0;
          |      Int sum = 0;
          |      do{
          |         do{
          |           sum=sum+j;
          |           j = j+1;
          |         }while(j<100)
          |
          |         sum=sum+i;
          |         i = i+1;
          |
          |      }while(i<100)
          |   }
          |
          |}
          |""".stripMargin
    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    val programOptimize = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )


    program method (
      "method2"
    ) bodyBlock() updateVarDef("i", IntType, Some( 0 int )) updateVarDef("j", IntType, Some( 0 int )) updateVarDef("sum", IntType, Some( (0 int) )) doWhileBlock(

    ) doWhileBlock(

    ) expression(blockScope⇒{
        api.expression.`var`.Assign(blockScope.varRef("sum"),binary.Add(blockScope.varRef("sum"),blockScope.varRef("j")))
    }) expression(blockScope⇒{
        api.expression.`var`.Assign(blockScope.varRef("j"),binary.Add(blockScope.varRef("j"),1 int))
    }) belongBlock() expression(blockScope⇒{
        DoWhileCondition(binary.Lt(blockScope.varRef("j"),100 int))
    }) expression(blockScope⇒{
      api.expression.`var`.Assign(blockScope.varRef("sum"),binary.Add(blockScope.varRef("sum"),blockScope.varRef("i")))
    }) expression (blockScope ⇒ {
      api.expression.`var`.Assign( blockScope.varRef( "i" ), binary.Add( blockScope.varRef( "i" ), 1 int ) )
    }) belongBlock() expression (blockScope ⇒ {
      DoWhileCondition( binary.Lt( blockScope.varRef( "i" ), 100 int ) )
    })

    programOptimize method(
      "method2"
    ) bodyBlock() updateVarDef("i", IntType, Some( 0 int )) updateVarDef("j", IntType, Some( 0 int )) updateVarDef("sum", IntType, Some( (0 int) )) expression(blockScope⇒{

      val doWhileBlockScope1 = blockScope statementBlock(false)

      val doWhileBlockScope2 = doWhileBlockScope1 statementBlock(false)

      val doWhileBody2 = doWhileBlockScope2 expression (blockScope ⇒ {
        api.expression.`var`.Assign( blockScope.varRef( "sum" ), binary.Add( blockScope.varRef( "sum" ), blockScope.varRef( "j" ) ) )
      }) expression (blockScope ⇒ {
        api.expression.`var`.Assign( blockScope.varRef( "j" ), binary.Add( blockScope.varRef( "j" ), 1 int ) )
      })

      val doWhileCondition2 = binary.Lt(doWhileBlockScope1.varRef("j"),100 int)

      val doWhile2 = DoWhile(doWhileCondition2,doWhileBody2.block)

      val doWhileBody1 = doWhileBlockScope1 expression(_⇒doWhile2) expression (blockScope ⇒ {
        api.expression.`var`.Assign( blockScope.varRef( "sum" ), binary.Add( blockScope.varRef( "sum" ), blockScope.varRef( "i" ) ) )
      }) expression (blockScope ⇒ {
        api.expression.`var`.Assign( blockScope.varRef( "i" ), binary.Add( blockScope.varRef( "i" ), 1 int ) )
      })

      val doWhileCondition1 = binary.Lt( blockScope.varRef( "i" ), 100 int )

      DoWhile(doWhileCondition1,doWhileBody1.block)
    })

    program method(
      "method"
    ) bodyBlock() updateVarDef("i",IntType,Some(0 int)) doWhileBlock() expression(blockScope⇒{
        val i = blockScope.varRef("i")
        api.expression.`var`.Assign(i,binary.Add(i,1 int))
    }) belongBlock() expression(blockScope⇒{
        DoWhileCondition(binary.Lt(blockScope.varRef("i"),100 int))
    }) expression(blockScope⇒{
        Return(Some(blockScope.varRef("i")))
    })

    programOptimize method(
      "method"
    ) bodyBlock() updateVarDef("i",IntType,Some(0 int)) expression(blockScope⇒{

      val childScope = blockScope statementBlock(false)
      val doWhileCondition = binary.Lt(blockScope.varRef("i"),100 int)

      val doWhileBody = childScope expression (blockScope ⇒ {
        val i = blockScope.varRef( "i" )
        api.expression.`var`.Assign( i, binary.Add( i, 1 int ) )
      })

      DoWhile(doWhileCondition,doWhileBody.block)
    }) expression (blockScope ⇒ {
      Return( Some(blockScope.varRef( "i" ) ))
    })


    program method (
      "method1"
      ) bodyBlock() updateVarDef("i", IntType, Some( 0 int )) doWhileBlock() expression (blockScope ⇒ {
      val i = blockScope.varRef( "i" )
      api.expression.`var`.Assign( i, binary.Add( i, 1 int ) )
    }) expression (blockScope⇒IfCondition(Eq(blockScope.varRef("i"),30 int),first = true)) ifBlock() expression (_⇒
      Break) belongBlock() belongBlock() expression (blockScope ⇒ {
      DoWhileCondition( binary.Lt( blockScope.varRef( "i" ), 100 int ) )
    }) expression (blockScope ⇒ {
      Return( Some(blockScope.varRef( "i" ) ))
    })

    programOptimize method(
      "method1"
    ) bodyBlock() updateVarDef("i", IntType, Some( 0 int )) expression(blockScope⇒{

      val doWhileCondition = binary.Lt( blockScope.varRef( "i" ), 100 int )

      val doWhileBlockScope = blockScope statementBlock(false)
      val ifBlockScope = doWhileBlockScope statementBlock(false)

      val ifCondition = binary.Eq(doWhileBlockScope.varRef("i"),30 int)
      val ifBody = ifBlockScope expression (_ ⇒ Break)

      val doWhileBody = doWhileBlockScope expression (blockScope ⇒ {
        val i = blockScope.varRef( "i" )
        api.expression.`var`.Assign( i, binary.Add( i, 1 int ) )
      }) expression(_⇒If(Array((ifCondition,ifBody.block)),None))

      DoWhile(doWhileCondition,doWhileBody.block)

    }) expression (blockScope ⇒ {
      Return( Some(blockScope.varRef( "i" ) ))
    })


    val generateP = generateProgram( input )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == programOptimize )

  }
}
