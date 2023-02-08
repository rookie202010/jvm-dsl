package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.core.program.Program
import com.dongjiaqiang.jvm.dsl.core.symbol.generateProgramScope
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}
import scala.language.postfixOps

class ExpressionParserTestBlockExprSuit extends AnyFunSuite {


  test("define try") {

    val input =
      """
        |program{
        |
        | def method1(Double a,Float b)=Double{
        |     try{
        |         if(a>=b){
        |           Double=>Double plus = i=>{ return i+1;};
        |           return plus.apply(a);
        |         }else{
        |           throw new RuntimeException("xx");
        |         }
        |     }catch(Exception e){
        |         return 1d;
        |     }
        | }
        |
        | def method2(Float a,Float b)=Float{
        |    try{
        |      return a;
        |    }finally{
        |       return b;
        |    }
        | }
        |
        | def method3(Int a,Int b)=Int{
        |    try{
        |       return method1(a,b);
        |    }catch(Exception e1){
        |         return 1d;
        |    }catch(Exception e2){
        |         return 2d;
        |    }
        | }
        |
        | def method4(Int c,Int d)=Int{
        |     try{
        |         return method3(c,d);
        |     }catch(Exception e1){
        |         return 1d;
        |     }catch(Exception e2){
        |         return 2d;
        |     }finally{
        |         return 3d;
        |     }
        | }
        |}
        |""".stripMargin
    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program.method(
      "method1"
    ) bodyBlock() tryBlock() expression (scope ⇒ {

        IfCondition( Ge( scope.varRef( "a" ), scope.varRef( "b" ) ), first = true )

      }) ifBlock() expression ( blockScope⇒ {
        blockScope.varDef( "plus", LambdaType( Some( DoubleType ), DoubleType ), Some( Lambda( Array( "i" ), (blockScope lambdaBlock() expression (scope ⇒ {
          Return( Add( scope.varRef( "i" ), 1 int ) )
        })).block ) ) )
      }) expression (blockScope⇒{
        Return( VarCall( blockScope.varRef( "plus" ), "apply", Array( blockScope.varRef( "a" ) ) ) )
      }) belongBlock() ifBlock() expression (_ ⇒ {
        Throw( new ClazzLiteral( Array( "\"xx\"" str ), ClazzType( "RuntimeException" ) ) )
      }) belongBlock() belongBlock() expression( _ ⇒ {
    CatchParameter( "e", ClazzType( "Exception" ) )
  } )  catchBlock() expression (_ ⇒ {
    Return( 1 double )
  })

    program.method(
      "method2"
    ) bodyBlock() tryBlock() expression (blockScope⇒{
        Return(blockScope.varRef("a"))
    }) belongBlock() finallyBlock() expression (blockScope⇒{
      Return(blockScope.varRef("b"))
    })

    program.method(
      "method3"
    ) bodyBlock() tryBlock() expression(blockScope⇒{
      Return(MethodCall(program.programScope.methods.get("method1"),"method1",Array(blockScope.varRef("a"),blockScope.varRef("b"))))
    }) belongBlock() expression(_⇒{
      CatchParameter( "e1", ClazzType( "Exception" ) )
    }) catchBlock() expression(_⇒{
      Return( 1 double )
    }) belongBlock() expression(_⇒{
      CatchParameter( "e2", ClazzType( "Exception" ) )
    }) catchBlock() expression(_⇒{
      Return( 2 double )
    })

    program.method("method4") bodyBlock() tryBlock() expression(blockScope⇒{
      Return(MethodCall(program.programScope.methods.get("method3"),"method3",Array(blockScope.varRef("c"),blockScope.varRef("d"))))
    }) belongBlock() expression (_ ⇒ {
      CatchParameter( "e1", ClazzType( "Exception" ) )
    }) catchBlock() expression (_ ⇒ {
      Return( 1 double )
    }) belongBlock() expression (_ ⇒ {
      CatchParameter( "e2", ClazzType( "Exception" ) )
    }) catchBlock() expression (_ ⇒ {
      Return( 2 double )
    }) belongBlock() finallyBlock() expression(_⇒{
      Return( 3 double )
    })
    assert( program == generateProgram( input ) )
  }

  test("define synchronized"){
    val input =
      """
        |program{
        |   String lock = "lock";
        |   def method2(Int a)=Int{
        |       return a;
        |   }
        |   def method1(Int a)=Int{
        |       synchronized(lock){
        |          return method2(a);
        |       }
        |   }
        |
        |   def method3(Int a)=Int{
        |       synchronized(lock){
        |           return method2(a);
        |       }
        |   }
        |}
        |""".stripMargin

    val program = Program( generateProgramScope(input), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program assign("lock","\"lock\"" str)
    program.method (
      "method2"
    ) bodyBlock() expression(blockScope⇒{
        Return(blockScope.varRef("a"))
    })
    program.method(
      "method1"
    ) bodyBlock() expression(blockScope⇒{
        SyncCondition(blockScope.varRef("lock"))
    }) syncBlock() expression(blockScope⇒{
        Return(MethodCall(program.programScope.methods.get("method2"),"method2",Array(blockScope.varRef("a"))))
    })
    program.method(
      "method3"
    ) bodyBlock() expression (blockScope ⇒ {
      SyncCondition( blockScope.varRef( "lock" ) )
    }) syncBlock() expression (blockScope ⇒ {
      Return( MethodCall( program.programScope.methods.get( "method2" ), "method2", Array( blockScope.varRef( "a" ) ) ) )
    })

    assert( program == generateProgram( input ) )

  }


  test("define if"){
    val input =
      """
        |program{
        |
        |   def method1(Int a,Int b,String str1,String str2)=String{
        |         if(a<b){
        |             return str1;
        |         }else if(a==b){
        |             return str2;
        |         }else{
        |             return "ok";
        |         }
        |   }
        |
        |   def method2(Int a,Int b)=Int{
        |       if(a<b){
        |         return a;
        |       }else if(a==b){
        |         return b;
        |       }
        |       return 1;
        |   }
        |
        |   def method3(Int a,Int b)=Bool{
        |       if(a>b){
        |           return false;
        |       }
        |       return true;
        |   }
        |
        |   def method4(Int a,Int b)=Bool{
        |       if(a>b){
        |           return false;
        |       }else{
        |           return true;
        |       }
        |   }
        |}
        |""".stripMargin

    val program = Program( generateProgramScope(input), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    program.method(
      "method1"
    ) bodyBlock() expression(blockScope⇒{
        IfCondition(Lt(blockScope.varRef("a"),blockScope.varRef("b")),first = true)
    }) expression(blockScope⇒{
        new IfBlock(ArrayBuffer(Return(blockScope.varRef("str1"))))
    }) expression(blockScope⇒{
      IfCondition(Eq(blockScope.varRef("a"),blockScope.varRef("b")),first = false)
    }) expression(blockScope⇒{
      new IfBlock(ArrayBuffer(Return(blockScope.varRef("str2"))))
    }) expression(_⇒{
      new IfBlock(ArrayBuffer(Return("\"ok\"" str)))
    })

    program.method(
      "method2"
    ) bodyBlock() expression(blockScope⇒{
      IfCondition(Lt(blockScope.varRef("a"),blockScope.varRef("b")),first = true)
    }) expression(blockScope⇒{
      new IfBlock(ArrayBuffer(Return(blockScope.varRef("a"))))
    }) expression(blockScope⇒{
      IfCondition(Eq(blockScope.varRef("a"),blockScope.varRef("b")),first = false)
    }) expression(blockScope⇒{
      new IfBlock(ArrayBuffer(Return(blockScope.varRef("b"))))
    }) expression(_⇒{
      Return(1 int)
    })

    program.method(
      "method3"
    ) bodyBlock() expression(blockScope⇒{
        IfCondition(Gt(blockScope.varRef("a"),blockScope.varRef("b")),first = true)
    }) ifBlock() expression(_⇒Return(false bool)) belongBlock() expression(_⇒Return(true bool))

    program.method(
      "method4"
    ) bodyBlock() expression (blockScope ⇒ {
      IfCondition( Gt( blockScope.varRef( "a" ), blockScope.varRef( "b" ) ), first = true )
    }) ifBlock() expression (_ ⇒ Return( false bool )) belongBlock() ifBlock() expression (_ ⇒ Return( true bool ))


    assert( program == generateProgram(input) )

  }

  test( "define try block" ) {
    val input =
      """
        |program{
        |   def method1(Int a,Int b)=Int{
        |       Try[Int] result = Try{
        |
        |           if(a>b){
        |               return a;
        |           }else{
        |               throw new RuntimeException("error happen");
        |           }
        |       };
        |       return result.get();
        |   }
        |}
        |""".stripMargin

    val program = Program( generateProgramScope(input), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program.method(
      "method1"
    ) bodyBlock() expression (blockScope ⇒ {

      val block = blockScope lambdaBlock() expression (scope ⇒ {
        val a = scope.varRef( "a" )
        val b = scope.varRef( "b" )
        IfCondition( Gt( a, b ), first = true )
      }) expression (scope ⇒ {
        val a = scope.varRef( "a" )
        new IfBlock( ArrayBuffer( Return( a ) ) )
      }) expression (_ ⇒ {
        new IfBlock( ArrayBuffer( Throw( new ClazzLiteral( Array( "\"error happen\"" str ), ClazzType( "RuntimeException" ) ) ) ) )
      })

      blockScope.varDef( "result", ClazzType( "Try", Array( IntType ) ), Some( Try( block.block, TryType( UnResolvedType ) ) ) )
    }) expression (blockScope ⇒ {
      val result = blockScope.varRef( "result" )
      Return( VarCall( result, "get", Array( ) ) )
    })
    assert( program == generateProgram(input) )
  }

  test( "define async block" ) {

    val input =
      """
        |program{
        |   ExecutorService es;
        |   def method1(Int a,Int b)=Int{
        |       Future[Int] future = Async(es){
        |           return a+b;
        |       };
        |       return future.get();
        |   }
        |}
        |""".stripMargin

    val program = Program( generateProgramScope(input), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program.method(
      "method1"
    ) bodyBlock() expression (blockScope ⇒ {

      val es = blockScope.varRef( "es" )
      val block = blockScope lambdaBlock() expression (scope ⇒ {
        val a = scope.varRef( "a" )
        val b = scope.varRef( "b" )
        Return( Add( a, b ) )
      })

      blockScope.varDef( "future", FutureType( IntType ), Some( Async( block.block, es.fieldScope, FutureType( UnResolvedType ) ) ) )
    }) expression (blockScope ⇒ {
      val result = blockScope.varRef( "future" )
      Return( VarCall( result, "get", Array( ) ) )
    })

    assert( program == generateProgram(input) )
  }

  test( "define custom block" ) {

    val input =
      """
        |program{
        |
        |   def method2(Int a)=Int{
        |       return a;
        |   }
        |
        |   def method1(Int a,Int b)=Int{
        |       Json json1 = Json{
        |           [1,2,3];
        |       };
        |       Json json2 = Json{
        |           {"a":a,"b":b};
        |       };
        |       Json json3 = Json{
        |           a=a*method2(b);
        |           b=b;
        |           c = json2;
        |       };
        |       return json3.getInt("a");
        |
        |   }
        |}
        |""".stripMargin

    val program = Program( generateProgramScope(input), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program.method(
      "method1"
    ) bodyBlock() expression (blockScope ⇒ {

      val block = blockScope lambdaBlock(true) expression (_ ⇒ {
        new ListLiteral( Array( 1 int, 2 int, 3 int ), ListType( UnResolvedType ) )
      })


      blockScope.varDef( "json1", ClazzType( "Json" ), Some( CustomBlockExpression( "Json", block.block, None ) ) )
    }) expression (blockScope ⇒ {
      val block = blockScope lambdaBlock(true) expression (scope ⇒ {
        new MapLiteral( Array( ("\"a\"" str, scope.varRef( "a" )), ("\"b\"" str, scope.varRef( "b" )) ), MapType( UnResolvedType, UnResolvedType ) )
      })

      blockScope.varDef( "json2", ClazzType( "Json" ), Some( CustomBlockExpression( "Json", block.block, None ) ) )
    }) expression (blockScope ⇒ {


      val block = blockScope lambdaBlock(true) expression (scope ⇒ {
        Assign( VarRef( List( "a" ), None ), Mul(scope.varRef( "a" ),MethodCall(program.programScope.methods.get("method2"),"method2",Array(scope.varRef("b"))) ))
      }) expression (scope ⇒ {
        Assign( VarRef( List( "b" ), None ), scope.varRef( "b" ) )
      }) expression (scope ⇒ {
        Assign( VarRef( List( "c" ), None ), scope.varRef( "json2" ) )
      })

      blockScope.varDef( "json3", ClazzType( "Json" ), Some( CustomBlockExpression( "Json", block.block, None ) ) )
    }) expression (blockScope ⇒ {
      val result = blockScope.varRef( "json3" )
      Return( VarCall( result, "getInt", Array( "\"a\"" str ) ) )
    })

    program.method("method2") bodyBlock() expression(blockScope⇒{
      Return(blockScope.varRef("a"))
    })

    assert( program == generateProgram(input) )
  }

}
