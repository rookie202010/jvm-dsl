package com.dongjiaqiang.jvm.dsl.core.symbol

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import org.scalatest.funsuite.AnyFunSuite


class SymbolDefParserTestBlockExprSuit extends AnyFunSuite{
  test( "define try block" ) {
    val input =
      """
        |program {
        |          def xx(Int b)  = String throws RuntimeException  {
        |             Try[String] maybe = Try{
        |                 if(b>0){
        |                     String ok = "ok";
        |                     return ok;
        |                 }else{
        |                     throw RuntimeException("b is small");
        |                 }
        |             };
        |            return maybe.get();
        |          }
        |
        |          def yy(Int b)  = Try[String] {
        |                 return Try{
        |                     if(b>0){
        |                         return "ok";
        |                     }else{
        |                         throw RuntimeException("b is small");
        |                     }
        |                 };
        |          }
        |       }""".stripMargin
    //define program
    implicit val programScope: ProgramScope = new ProgramScope( )

    programScope members 2 method(
      "xx",0,StringType,2
    ) inputParams 1  field(
      "b",0,IntType
    )throwEx(
      ClazzType("RuntimeException")
      ) body() field(
      "maybe",0,ClazzType("Try",Array(StringType))
    ) lambdaBlock(
        0,3
    ) block(
        1,2
    ) field(
      "ok",0,StringType
    ) belongBlock() block(
      2,1
    )

    programScope method(
      "yy",1,ClazzType("Try",Array(StringType)),1
    ) inputParams 1 field(
      "b",0,IntType
    ) body() lambdaBlock(
      0,3
    ) block(
      1,1
    ) belongBlock() block(
      2,1
    )

    assert(programScope==generateProgramScope(input))
  }

  test("define async block"){
      val input =
        """
          |program {
          |         Executor executor;
          |          def xx(Int b)  = Long  {
          |             Async[Long] async ;
          |             async = Async(executor){
          |                 if(b>0){
          |                     return 10L;
          |                 }else{
          |                     return 20L;
          |                 }
          |             };
          |            return async.get();
          |          }
          |
          |          def yy(Int b)  = Unit {
          |                 Async{
          |
          |                     Int=>Int lambda = i=>{ return i+1;} ;
          |                     lambda.apply(1);
          |
          |                 };
          |          }
          |       }
          """.stripMargin
    //define program
    implicit val programScope: ProgramScope = new ProgramScope( )

    programScope members 3 field(
      "executor",0,ClazzType("Executor")
    ) method(
      "xx",1,LongType,3
    ) inputParams 1 field (
      "b",0,IntType
    )body() field(
      "async",0,ClazzType("Async",Array(LongType))
    ) lambdaBlock(
      1,3
    ) block(
      1,1
    ) belongBlock() block(
      2,1
    )

    programScope method(
      "yy",2,UnitType,1
    ) inputParams 1 field(
      "b", 0, IntType
    ) body() lambdaBlock(
      0,2
    ) field(
      "lambda",0,LambdaType(Some(IntType),IntType)
    ) lambdaBlock(
      0,1
    ) field(
      "i",0,UnResolvedType
    )

    assert(programScope==generateProgramScope(input))
  }

  test( "define custom block" ) {
    val input =
      """
        |program{
        |
        |   def json()=Json{
        |
        |       Json json = Json {
        |           i=10;
        |           j=20;
        |       };
        |
        |       return json;
        |
        |   }
        |
        |}
        |""".stripMargin
    //define program
    implicit val programScope: ProgramScope = new ProgramScope( )

    programScope members 1 method(
      "json",0,ClazzType("Json"),2
    ) body() field(
      "json",0,ClazzType("Json")
    ) lambdaBlock(
      0,2
    )

    assert( generateProgramScope(input) == programScope )
  }

}
