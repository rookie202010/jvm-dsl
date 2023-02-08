package com.dongjiaqiang.jvm.dsl.core.symbol

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import org.scalatest.funsuite.AnyFunSuite


class SymbolDefParserTestLambdaSuit extends AnyFunSuite{


  test( "define lambda field" ) {
    val input =
      """program {

            (Int,Int)=>Int plusLambda = (i,j)=>{
                (Int,Int)=>Int minusLambda = (i,j)=>{
                    return i-j;
                };
                return i+j+minusLambda(i,j);
            };

            def plus(Int i,Int j)=Int{
                return plusLambda.apply(i,j);
            }

          }
          """
    //define program
    implicit val programScope: ProgramScope = new ProgramScope( )

    programScope members 2 field(
        "plusLambda",0,LambdaType(Some(TupleType(Array(IntType,IntType))),IntType)
    ) method("plus",1,IntType,1) inputParams 2 field (
        "i",0,IntType
    ) field (
        "j",0,IntType
    )belongProgram() lambdaBlock(
      0,4
    ) field(
      "i",0,UnResolvedType
    ) field(
      "j",0,UnResolvedType
    ) field(
      "minusLambda",2,LambdaType(Some(TupleType(Array(IntType,IntType))),IntType)
    ) lambdaBlock(2,3) field(
      "i",0,UnResolvedType
    ) field(
      "j",0,UnResolvedType
    )

    assert(generateProgramScope(input) == programScope)
  }

  test("define two lambda field"){
      val input =
        """
          program{


              (Int,Int)=>Int plusLambda = (i,j)=>{
                      Int d1 = d*20;
                      return i+j+d1;
                  };


              (Int,Int)=>Int minusLambda = (i,j)=>{
                          return i-j+d;
                      };

              def apply(Int i,Int j,Int k)=Int{
                  Int z = k*10;
                  return plusLambda.apply(i,z)*minusLambda.apply(z,j);
               }

               Int d = 100;
          }
          """
    //define program
    implicit val programScope: ProgramScope = new ProgramScope( )

    programScope members 4 field(
      "plusLambda",0,LambdaType(Some(TupleType(Array(IntType,IntType))),IntType)
    ) field(
      "minusLambda",1,LambdaType(Some(TupleType(Array(IntType,IntType))),IntType)
    ) lambdaBlock(
      0,3
    ) field (
      "i",0,UnResolvedType
    ) field (
      "j",0,UnResolvedType
    ) field (
      "d1",0,IntType
    )belongProgram() lambdaBlock(
      1,3
    ) field(
      "i", 0, UnResolvedType
    ) field(
      "j", 0, UnResolvedType
    )belongProgram() method(
      "apply",2,IntType,2
    ) inputParams 3 field(
      "i",0,IntType
    ) field(
      "j",0,IntType
    ) field(
      "k",0,IntType
    ) body() field(
      "z",0,IntType
    ) belongProgram() field(
      "d",3,IntType
    )

    assert( generateProgramScope(input)== programScope )

  }

  test( "define return lambda" ) {
    val input =
      """program {

             Int j = 10;
             Long i = 10L;

             def returnLambda()=()=>String{
                 return ()=>{
                    Int k = 12;
                    return String.format("j=%s,j=%s k=%s",j,i,k);
                 };
             }

             def applyLambda()=String{
                return returnLambda().apply();
             }

           }
           """
    //define program
    implicit val programScope: ProgramScope = new ProgramScope( )

    programScope members 4 field(
      "j",0,IntType,
    ) field(
      "i",1,LongType
    ) method(
      "returnLambda",2,LambdaType(None,StringType),1
    ) body() lambdaBlock (
        0,2
    ) field (
      "k",0,IntType
    )belongProgram() method(
      "applyLambda",3,StringType,1
    )

    assert( generateProgramScope(input) == programScope )
  }

}
