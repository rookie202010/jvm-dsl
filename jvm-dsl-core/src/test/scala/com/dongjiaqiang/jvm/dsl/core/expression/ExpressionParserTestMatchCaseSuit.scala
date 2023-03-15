package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.binary.{Add, Mul}
import com.dongjiaqiang.jvm.dsl.api.expression.block._
import com.dongjiaqiang.jvm.dsl.api.expression.call.{MethodCall, VarCall}
import com.dongjiaqiang.jvm.dsl.api.expression.statement.{Return, Throw}
import com.dongjiaqiang.jvm.dsl.core.optimize.OptimizeExpression
import com.dongjiaqiang.jvm.dsl.core.program.Program
import com.dongjiaqiang.jvm.dsl.core.symbol.generateProgramScope
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.{ListMap ⇒ MutableMap}
import scala.language.postfixOps
class ExpressionParserTestMatchCaseSuit extends AnyFunSuite {

  test("define match list"){
      val input =
        """
          |program{
          |
          |   def method(List[Int] a)=Int{
          |       return a match{
          |           case [1,2,b] => { return b; }
          |           case [a,c,d] => { return a+c+d;}
          |       };
          |   }
          |}
          |""".stripMargin

    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    program method("method") bodyBlock() expression(blockScope⇒{

      val caseOne = (blockScope.lambdaBlock( ) expression (blockScope ⇒ {
        Return( Some(blockScope.varRef( "b" )))
      })).block

      val caseTwo = (blockScope.lambdaBlock( ) expression (blockScope ⇒ {
        Return( Some(Add(binary.Add(blockScope.varRef("a"),blockScope.varRef( "c" )), blockScope.varRef("d") ) ))
      })).block

      val caseOneCondition = MatchList( Array( Left[Expression, String]( 1 int ), Left[Expression, String]( 2 int ), Right[Expression, String]( "b" ) ))
      val caseTwoCondition = MatchList( Array( Right[Expression, String]( "a" ), Right[Expression, String]( "c" ), Right[Expression, String]( "d" ) ) )

      Return( Some(MatchCase( blockScope.varRef( "a" ), Array( (caseOneCondition, caseOne),(caseTwoCondition, caseTwo) ), None ) ))


    })

    val generateP = generateProgram( input )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == program )


  }


  test("define match tuple"){
    val input =
      """
        |program{
        |
        |   class A(Int a,Int b){}
        |
        |   def method((Int,Long,String) tuple)=Int{
        |       return tuple match{
        |         case (1,2L,"xx")=>{ return 1;}
        |         case (a,b,c)=> { return b;}
        |       };
        |   }
        |
        |   def method1(Any tuple)=Int{
        |       return tuple match{
        |           case (1,A(a,b),"xx")=> { return a+b;}
        |           default=> { return 1;}
        |       };
        |   }
        |
        |
        |   def method2(Any any)=Int{
        |       return any match{
        |           case (a,b,c)::d::e=> { return 1;}
        |           case a::[b,c,d]::e => { return 2;}
        |       };
        |   }
        |}
        |""".stripMargin

    val programScope = generateProgramScope(input)

    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program clazz "A"

    program method (
      "method2"
    ) bodyBlock() expression (blockScope⇒{

      val caseOneBlock = (blockScope.lambdaBlock( ) expression (_ ⇒ Return( Some(1 int )))).block
      val caseTwoBlock = (blockScope.lambdaBlock( ) expression (_ ⇒ Return(Some(2 int)))).block

      val caseOneCondition = MatchHead( Array( Left[Expression, String]( MatchTuple(
        Array(

          Right[Expression, String]( "a" ),
          Right[Expression, String]( "b" ),
          Right[Expression, String]( "c" )
        )) ),
        Right[Expression, String]( "d" )),
        Right[Expression,String]("e")  )

      val caseTwoCondition = MatchHead( Array( Right[Expression, String]( "a" ),

        Left[Expression, String](MatchList(Array(

          Right[Expression, String]( "b" ),
          Right[Expression, String]( "c" ),
        Right[Expression, String]( "d" )

        )))),
        Right[Expression, String]( "e" ) )


      Return( Some(block.MatchCase( blockScope.varRef( "any" ), Array( (caseOneCondition, caseOneBlock), (caseTwoCondition, caseTwoBlock) ), None )) )}
      )

    program method(
      "method"
    ) bodyBlock() expression(blockScope⇒{

      val caseOneBlock = (blockScope.lambdaBlock( ) expression (_⇒Return(Some(1 int)))).block
      val caseTwoBlock = (blockScope.lambdaBlock( ) expression (scope⇒{ Return(Some(scope.varRef("b")))})).block

      val caseOneCondition = MatchTuple(Array(Left[Expression,String](1 int),Left[Expression,String](2 long),Left[Expression,String]("\"xx\"" str)))
      val caseTwoCondition = MatchTuple(Array(Right[Expression,String]("a"),Right[Expression,String]("b"),Right[Expression,String]("c")))

      Return(Some(block.MatchCase(blockScope.varRef("tuple"),Array((caseOneCondition,caseOneBlock),(caseTwoCondition,caseTwoBlock)),None)))

    })

    program method (
      "method1"
      ) bodyBlock() expression(blockScope⇒{

      val caseOneBlock = (blockScope.lambdaBlock( ) expression (scope⇒Return(Some(binary.Add(scope.varRef("a"),scope.varRef("b")))))).block
      val caseOneCondition = MatchTuple(Array(Left[Expression,String](1 int),
        Left[Expression,String](MatchClass(DefinitionClazzType("A",programScope.classes("A")),Array(Right[Expression,String]("a"),Right[Expression,String]("b")))),
        Left[Expression,String]("\"xx\"" str)))


      Return(Some(block.MatchCase(blockScope.varRef("tuple"),Array((caseOneCondition,caseOneBlock)),Some((blockScope lambdaBlock() expression(_⇒Return(Some(1 int)))).block))))
    })

    val generateP = generateProgram( input )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == program )

  }



  test("define match type"){
      val input =
        """
          |program{
          |   def method(any Any)=Int{
          |       Int i =  any match{
          |           case i:Int=>{ Int j = i;return j;}
          |           case i:Long=>{ return 2;}
          |           case "xxx"=>{  return 3;}
          |       };
          |       return i;
          |   }
          |
          |   def method1(any Any)=Int{
          |       return any match{
          |         case i:Int=>{ return i;}
          |         default=>{ return 1;}
          |       };
          |   }
          |}
          |""".stripMargin
      val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

      program method (
        "method1"
      ) bodyBlock() expression (blockScope⇒{

        val `case` = (blockScope.lambdaBlock( ) expression (blockScope ⇒ {
          Return( Some(blockScope.varRef( "i" ) ))
        })).block

        val `default` = (blockScope lambdaBlock() expression(_⇒Return(Some(1 int)))).block
        Return(Some(block.MatchCase(blockScope.varRef("any"),Array((MatchType("i",IntType),`case`)),Some(`default`))))
      })


      program method(
        "method"
      ) bodyBlock() expression(blockScope⇒{

        blockScope.varDef("i",IntType,Some(block.MatchCase(blockScope.varRef("any"),{

          val caseOne = (blockScope.lambdaBlock() expression(blockScope⇒blockScope.varDef("j",IntType,Some(blockScope.varRef("i")))) expression(blockScope⇒{
              Return(Some(blockScope.varRef("j")))
          })).block
          val caseTwo = (blockScope.lambdaBlock() expression(_⇒Return( Some(2 int)))).block
          val caseThree = (blockScope.lambdaBlock() expression(_⇒Return(Some(3 int)))).block

          Array((block.MatchType("i",IntType),caseOne),(block.MatchType("i",LongType),caseTwo),("\"xxx\"" str,caseThree))

        },None)))
      }) expression(blockScope⇒Return(Some(blockScope.varRef("i"))))
    val generateP = generateProgram( input )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == program )

  }

  test("define match clazz"){

    val input =
      """
        |program{
        |
        |   class A(Int a,B b){}
        |
        |   class B(Int c,Int d){}
        |
        |   def method3(A a)=Int{
        |      return a match{
        |           case A(a,b)=>{ return a+b.c;}
        |           case A(a,B(c,d))=> { return a+c*d;}
        |       };
        |   }
        |
        |   def method4(A a)=Int{
        |       return a match{
        |           case A(a,10)=>{ return a+10;}
        |           case A(10,b)=>{ return b+10;}
        |       };
        |   }
        |
        |   def method(Try[Int] v)=Int{
        |       return v match{
        |         case Success(s)=> { return s;}
        |         case Failure(e)=> { throw e;}
        |       };
        |   }
        |
        |def method2(Either[Int,Long] either)=Long{
        |       return either match{
        |         case Left(l)=> { return l.toLong();}
        |         case Right(r)=> { return r;}
        |       };
        |   }
        |def method1(Option[String] o)=String{
        |       String str = o match{
        |         case Some(s)=>{return s;}
        |         case None=>{ return "none";}
        |       };
        |       return str;
        |   }
        |
        |}
        |""".stripMargin

    val programScope = generateProgramScope(input)

    val program = Program( programScope, assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    val programOptimize = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program clazz "A"
    program clazz "B"
    program method (
      "method4"
      ) bodyBlock() expression(blockScope⇒{

      val caseOne = (blockScope.lambdaBlock( ) expression (blockScope ⇒ {
        Return( Some(binary.Add(blockScope.varRef( "a" ),10 int )))
      })).block

      val caseTwo = (blockScope.lambdaBlock( ) expression (blockScope ⇒ {
        Return( Some(binary.Add( blockScope.varRef( "b" ),10 int ) ))
      })).block

      Return(Some(block.MatchCase(blockScope.varRef("a"),Array(
        (block.MatchClass(DefinitionClazzType("A",programScope.classes("A")),Array(Right[Expression,String]("a"),Left[Expression,String](10 int))),caseOne)
      ,(block.MatchClass(DefinitionClazzType("A",programScope.classes("A")),Array(Left[Expression,String](10 int),Right[Expression,String]("b"))),caseTwo)),None)))
    })

    programOptimize method (
      "method4"
      ) bodyBlock() expression (blockScope ⇒ {

      val caseOne = (blockScope.lambdaBlock( ) expression (blockScope ⇒ {
        Return( Some(binary.Add( blockScope.varRef( "a" ), 10 int ) ))
      })).block

      val caseTwo = (blockScope.lambdaBlock( ) expression (blockScope ⇒ {
        Return( Some(binary.Add( blockScope.varRef( "b" ), 10 int ) ))
      })).block

      Return( Some(block.MatchCase( blockScope.varRef( "a" ), Array(
        (block.MatchClass( DefinitionClazzType("A",programScope.classes("A")), Array( Right[Expression, String]( "a" ), Left[Expression, String]( 10 int ) ) ), caseOne)
        , (block.MatchClass( DefinitionClazzType("A",programScope.classes("A")), Array( Left[Expression, String]( 10 int ), Right[Expression, String]( "b" ) ) ), caseTwo) ), None ) ))
    })

    program method (
      "method3"
      ) bodyBlock() expression (blockScope ⇒ {

      val caseOne = (blockScope.lambdaBlock( ) expression (blockScope ⇒ {
        Return(Some( binary.Add( blockScope.varRef( "a" ), blockScope.varRef( "b","c" ) ) ))
      })).block

      val caseTwo = (blockScope.lambdaBlock() expression(blockScope⇒{
        Return(Some(binary.Add(blockScope.varRef("a"),Mul(blockScope.varRef("c"),blockScope.varRef("d")))))
      })).block

      Return( Some(block.MatchCase( blockScope.varRef( "a" ),
        Array( (block.MatchClass( DefinitionClazzType("A",programScope.classes("A")),
          Array( Right[Expression, String]( "a" ),
            Right[Expression, String]( "b" ) ) ), caseOne),
          (block.MatchClass( DefinitionClazzType("A",programScope.classes("A")),
            Array( Right[Expression, String]( "a" ),
              Left[Expression, String](
                block.MatchClass(DefinitionClazzType("B",programScope.classes("B")),Array(
                  Right[Expression, String]( "c" ),
                Right[Expression, String]( "d" )
                )) ) ) ), caseTwo)), None ) ))
    })

    programOptimize method (
      "method3"
      ) bodyBlock() expression (blockScope ⇒ {

      val caseOne = (blockScope.lambdaBlock( ) expression (blockScope ⇒ {
        Return( Some(binary.Add( blockScope.varRef( "a" ), blockScope.varRef( "b", "c" ) ) ))
      })).block

      val caseTwo = (blockScope.lambdaBlock( ) expression (blockScope ⇒ {
        Return( Some(binary.Add( blockScope.varRef( "a" ), binary.Mul( blockScope.varRef( "c" ), blockScope.varRef( "d" ) ) ) ))
      })).block

      Return( Some(block.MatchCase( blockScope.varRef( "a" ),
        Array( (block.MatchClass( DefinitionClazzType("A",programScope.classes("A")),
          Array( Right[Expression, String]( "a" ),
            Right[Expression, String]( "b" ) ) ), caseOne),
          (block.MatchClass( DefinitionClazzType("A",programScope.classes("A")),
            Array( Right[Expression, String]( "a" ),
              Left[Expression, String](
                block.MatchClass( DefinitionClazzType("B",programScope.classes("B")), Array(
                  Right[Expression, String]( "c" ),
                  Right[Expression, String]( "d" )
                ) ) ) ) ), caseTwo) ), None ) ))
    })

    program method (
      "method2"
    ) bodyBlock() expression (blockScope⇒{
        val caseOne = blockScope.lambdaBlock() expression(scope⇒Return(Some(VarCall(scope.varRef("l"),"toLong",Array()))))
        val caseTwo = blockScope.lambdaBlock() expression(scope⇒Return(Some(scope.varRef("r"))))

        Return(Some(block.MatchCase(blockScope.varRef("either"),Array((block.MatchClass(LeftType(UnResolvedType),Array(Right[Expression,String]("l"))),caseOne.block),
          (block.MatchClass(RightType(UnResolvedType),Array(Right[Expression,String]("r"))),caseTwo.block)),None)))
    })

    programOptimize method (
      "method2"
      ) bodyBlock() expression (blockScope ⇒ {
      val caseOne = blockScope.lambdaBlock( ) expression (scope ⇒ Return( Some(call.VarCall( scope.varRef( "l" ), "toLong", Array( ) ) )))
      val caseTwo = blockScope.lambdaBlock( ) expression (scope ⇒ Return( Some(scope.varRef( "r" ) )))

      Return(Some(block.MatchCase( blockScope.varRef( "either" ), Array( (block.MatchClass( LeftType(UnResolvedType), Array( Right[Expression, String]( "l" ) ) ), caseOne.block),
        (block.MatchClass( RightType(UnResolvedType), Array( Right[Expression, String]( "r" ) ) ), caseTwo.block) ), None ) ))
    })

    program method(
      "method"
    ) bodyBlock() expression(blockScope⇒{
        val caseOne = blockScope.lambdaBlock() expression(blockScope⇒Return(Some(blockScope.varRef("s"))))
        val caseTwo = blockScope.lambdaBlock() expression(blockScope⇒Throw(blockScope.varRef("e")))

        Return(Some(block.MatchCase(blockScope.varRef("v"),Array((block.MatchClass(SuccessType(UnResolvedType),Array(Right[Expression,String]("s"))),caseOne.block),
          (block.MatchClass(FailureType,Array(Right[Expression,String]("e"))),caseTwo.block)),None)))
    })

    programOptimize method (
      "method"
      ) bodyBlock() expression (blockScope ⇒ {
      val caseOne = blockScope.lambdaBlock( ) expression (blockScope ⇒ Return( Some(blockScope.varRef( "s" ) )))
      val caseTwo = blockScope.lambdaBlock( ) expression (blockScope ⇒ statement.Throw( blockScope.varRef( "e" ) ))

      Return( Some(block.MatchCase( blockScope.varRef( "v" ), Array( (block.MatchClass( SuccessType(UnResolvedType), Array( Right[Expression, String]( "s" ) ) ), caseOne.block),
        (block.MatchClass( FailureType, Array( Right[Expression, String]( "e" ) ) ), caseTwo.block) ), None ) ))
    })

    program method (
      "method1"
      ) bodyBlock() expression (blockScope ⇒ {
      val caseOne = blockScope.lambdaBlock( ) expression (blockScope ⇒ Return( Some(blockScope.varRef( "s" ) )))
      val caseTwo = blockScope.lambdaBlock( ) expression (_ ⇒ Return(Some("\"none\"" str )))

      blockScope.varDef("str",StringType,Some( block.MatchCase( blockScope.varRef( "o" ), Array( (block.MatchClass( SomeType(UnResolvedType), Array( Right[Expression, String]( "s" ) ) ), caseOne.block),
        (block.MatchClass(NoneType,Array()), caseTwo.block) ), None ) ))
    }) expression(blockScope⇒{
        Return(Some(blockScope.varRef("str")))
    })

    programOptimize method (
      "method1"
      ) bodyBlock() expression (blockScope ⇒ {
      val caseOne = blockScope.lambdaBlock( ) expression (blockScope ⇒ Return( Some(blockScope.varRef( "s" ) )))
      val caseTwo = blockScope.lambdaBlock( ) expression (_ ⇒ Return( Some("\"none\"" str )))

      blockScope.varDef( "str", StringType, Some( block.MatchCase( blockScope.varRef( "o" ), Array( (block.MatchClass( SomeType(UnResolvedType), Array( Right[Expression, String]( "s" ) ) ), caseOne.block),
       ( block.MatchClass(NoneType,Array()), caseTwo.block) ), None ) ) )
    }) expression (blockScope ⇒ {
      Return( Some(blockScope.varRef( "str" ) ))
    })

    val generateP = generateProgram( input )
  //  assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == program )
  }

  test("define match head"){

    val input =
      """
        |program{
        |   def method(List[Long] list)=Long{
        |       return list match{
        |           case head::tail=>{ return head + method(tail);}
        |           case head::Nil=> { return head;}
        |       };
        |   }
        |
        |   def method1(List[Long] list)=Long{
        |       return list match{
        |           case 1L::2L::3L=>{ return 1L;}
        |           case 2L::tail => { return 2L;}
        |       };
        |   }
        |}
        |""".stripMargin

    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program method (
      "method1"
      ) bodyBlock() expression (blockScope ⇒ {

      val caseOne = blockScope.lambdaBlock( ) expression (_ ⇒ Return( Some(1 long)))
      val caseTwo = blockScope.lambdaBlock( ) expression (_⇒ Return(Some(2 long )))

      Return( Some(block.MatchCase( blockScope.varRef( "list" ),
        Array(
          (

            MatchHead( Array(
              Left[Expression, String]( 1 long ),
              Left[Expression, String]( 2 long )),
              Left[Expression, String]( 3 long  )
            ),
            caseOne.block
          ),

          (MatchHead( Array( Left[Expression, String]( 2 long )), Right[Expression, String]( "tail" ) ),
            caseTwo.block
          )

        ), None ) ))

    })

    program method(
      "method"
    ) bodyBlock() expression(blockScope⇒{

      val caseOne = blockScope.lambdaBlock( ) expression (blockScope ⇒ Return( Some(binary.Add(blockScope.varRef("head"),
        MethodCall(program.programScope.methods.get("method"),"method",Array(blockScope.varRef("tail")))) )))

      val caseTwo = blockScope.lambdaBlock( ) expression (blockScope ⇒ Return(Some(blockScope.varRef("head")) ))

      Return( Some(block.MatchCase(blockScope.varRef("list"),
        Array(
          (

          MatchHead(Array(Right[Expression,String]("head")),Right[Expression,String]("tail")),
          caseOne.block
          ),

          (MatchHead( Array( Right[Expression, String]( "head" ) ), Right[Expression, String]( "Nil" ) ),
          caseTwo.block
          )

        ),None )))

    })
    val generateP = generateProgram( input )
    assert( generateP == program )
    assert( generateP.revise( new OptimizeExpression( generateP.programScope ) ) == program )
  }
}
