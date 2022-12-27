package com.dongjiaqiang.jvm.dsl.core.symbol.parser

import com.dongjiaqiang.jvm.dsl.core.parser.SymbolDefParser
import com.dongjiaqiang.jvm.dsl.core.{JvmDslLexer, JvmDslParserParser}
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
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


            Int kk= kk match {

                       case 12=>{ return 2;}
                       case (a1,a2,a3)=>{ return a1+a2+a3;}
                 case k1::kTail=>{ return 1;}
                       default=>{
                         return 2200;
                       }
                     };


        }
        """
    val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
    val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
    val symbolDefParser = new SymbolDefParser( )
    ParseTreeWalker.DEFAULT.walk( symbolDefParser, jvmDslParser.program( ) )
    assert( symbolDefParser.programScope.fields.isEmpty )
    // assert(symbolDefParser.programScope.classes.isEmpty)
    //  assert(symbolDefParser.programScope.methods.isEmpty)

  }

}
