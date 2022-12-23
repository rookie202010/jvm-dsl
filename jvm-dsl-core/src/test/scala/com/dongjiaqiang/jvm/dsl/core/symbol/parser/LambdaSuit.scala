package com.dongjiaqiang.jvm.dsl.core.symbol.parser

import com.dongjiaqiang.jvm.dsl.core.{JvmDslLexer, JvmDslParserParser}
import com.dongjiaqiang.jvm.dsl.core.parser.SymbolDefParser
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.scalatest.funsuite.AnyFunSuite

import java.io.StringReader

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/23 
 * */
class LambdaSuit extends AnyFunSuite {


  test("field lambda"){

    val input =
      """program {
            Int i = 10;
            Int=>Int lambda = i=>{

                while(this.i<100){
                    Long j = 200;
                    foo(j);
                    this.i = this.i-1;
                }

                return i*2;
            };

            (Int,Int)=>Long lambda1 = (i,j)=>{

                (Int,Int)=>Int lambda2 = k=>{
                  return k+j+i;
                }

                return lambda2(10);
            };
        }
        """
    val jvmDslLexer = new JvmDslLexer(CharStreams.fromReader(new StringReader(input)))
    val jvmDslParser = new JvmDslParserParser(new CommonTokenStream(jvmDslLexer))
    val symbolDefParser = new SymbolDefParser()
    ParseTreeWalker.DEFAULT.walk(symbolDefParser, jvmDslParser.program())
    assert(symbolDefParser.programScope.fields.isEmpty)
    assert(symbolDefParser.programScope.classes.isEmpty)
    assert(symbolDefParser.programScope.methods.isEmpty)

  }

}
