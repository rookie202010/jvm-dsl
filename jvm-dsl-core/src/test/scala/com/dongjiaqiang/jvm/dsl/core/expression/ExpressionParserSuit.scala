package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.core.parser.{ExpressionParser, SymbolDefParser}
import com.dongjiaqiang.jvm.dsl.core.{JvmDslLexer, JvmDslParserParser}
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.scalatest.funsuite.AnyFunSuite

import java.io.StringReader

class ExpressionParserSuit extends AnyFunSuite {


  test( "define empty program" ) {
    val input =
      """program {



                  Int=>Int lambda = a=>{


                    while(i<10){
                      i+=1;
                    }



                    return a+1+foo1(j=>{
                     return j.toInt() + foo1(z=>{return z.toInt()+122;},"121L"); },"12.1f")+foo1(k=>{
                      return k.toLong();},"2121L");
                  };

                  Int i = 10;

                  def foo1(String=>String format,String str)=String{
                      return format.apply(str);
                  }

                  def foo2(String format,String str)=String{
                            return format+","+str;
                        }

                  def foo()=Int{

                 i  match{

                                  case 12=>{ return 2;}
                                  case (a1,a2,a3)=>{ return a1+a2+a3;}
                                  case i1::i2::i3=>{ return i2;}
                                  default=>{
                                    return 2200;
                                  }
                                };

                      Int j = 2;

                      for(Int k=0;k<100;k+=1){
                        lambda.apply(j);
                      }

                      (Int,Long,Int)=>(Int,Long) lambda1 = (a,b,c)=>{

                          Int sum=0;

                          (Int,Int)=>Int lambda2 = (a,b)=>{
                              return a+b;
                          };

                          for(Int a=0;a<100;a+=1){
                              sum+=lambda2.apply(a,c);
                          }

                          return (sum,b);

                      };

                      return lambda1.apply(1,2,3);
                  }
              }
        """
    val symbolDefParser = new SymbolDefParser( )
    ParseTreeWalker.DEFAULT.walk( symbolDefParser, new JvmDslParserParser( new CommonTokenStream( new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) ) ) )
      .program( ) )

    val expressionParser = new ExpressionParser( symbolDefParser.programScope )
    ParseTreeWalker.DEFAULT.walk( expressionParser, new JvmDslParserParser( new CommonTokenStream( new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) ) ) )
      .program( ) )

    assert( expressionParser.program.methods.isEmpty )
    assert( symbolDefParser.programScope.classes.isEmpty )
    assert( symbolDefParser.programScope.methods.isEmpty )
  }

}
