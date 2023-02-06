package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.core.parser.{ExpressionParser, SymbolDefParser}
import com.dongjiaqiang.jvm.dsl.core.program.Program
import com.dongjiaqiang.jvm.dsl.core.{JvmDslLexer, JvmDslParserParser}
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.scalatest.funsuite.AnyFunSuite

import java.io.StringReader
import scala.collection.mutable.{ListMap ⇒ MutableMap}
import scala.language.postfixOps

class ExpressionParserTestDefineSuit  extends AnyFunSuite {


  test( "define empty" ) {
    val input =
      """program {

        }
        """
    val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
    val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
    val symbolDefParser = new SymbolDefParser( )
    ParseTreeWalker.DEFAULT.walk( symbolDefParser, jvmDslParser.program( ) )

    val expressionParser = new ExpressionParser( symbolDefParser.programScope )
    ParseTreeWalker.DEFAULT.walk( expressionParser, new JvmDslParserParser( new CommonTokenStream( new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) ) ) )
      .program( ) )

    val program = Program(symbolDefParser.programScope , assigned = MutableMap(), classes =  MutableMap(), methods =  MutableMap())
    assert(expressionParser.program == program)
  }

  test("define classes") {
    val input =
      """
        |program{
        |   class A(Int a,Long b){
        |       def foo((Int,Long,String)=>String lambda)=String{
        |           return lambda.apply(1,2L);
        |       }
        |       def bar(Long a)=Int{
        |           return a.toInt();
        |       }
        |   }
        |}
        |""".stripMargin

    val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
    val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
    val symbolDefParser = new SymbolDefParser( )
    ParseTreeWalker.DEFAULT.walk( symbolDefParser, jvmDslParser.program( ) )


    val expressionParser = new ExpressionParser( symbolDefParser.programScope )
    ParseTreeWalker.DEFAULT.walk( expressionParser, new JvmDslParserParser( new CommonTokenStream( new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) ) ) )
      .program( ) )

    val program = Program( symbolDefParser.programScope, assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program clazz(
      "A"
    ) method(
      "foo"
    ) bodyBlock() expression(blockScope⇒{
      val varRef = blockScope.varRef("lambda")
      Return(VarCall(varRef,"apply",Array(1 int,2 long)))
    }) belongClazzMethod() belongClazz() method(
      "bar"
    ) bodyBlock() expression(blockScope⇒{
        val varRef = blockScope.varRef("a")
      Return(VarCall(varRef,"toInt",Array()))
    })

    assert(program== expressionParser.program)

  }

  test("define fields"){
    val input =
      """
        |program{
        |            Int i = 10;
        |            Long j = 200L;
        |            Float k = 10.1f;
        |
        |            A a = new A(10);
        |            String str;
        |            Map[String,Int] ages = {"Tom":12};
        |
        |            Set[Int] set = {1,2,3,4,5};
        |
        |            List[String] list = ["a","b","c"];
        |
        |            (Int,Long,String) tuple3 = (1,10L,"x");
        |
        |            volatile Map[String,String] addresses = {};
        |
        |            char c = 'a';
        |
        |            char d = '\'';
        |
        |            bool b = true;
        |
        |            Long j2 = i*j*10*a.b*a.foo();
        |         }
        |""".stripMargin
    val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
    val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
    val symbolDefParser = new SymbolDefParser( )
    ParseTreeWalker.DEFAULT.walk( symbolDefParser, jvmDslParser.program( ) )


    val expressionParser = new ExpressionParser( symbolDefParser.programScope )
    ParseTreeWalker.DEFAULT.walk( expressionParser, new JvmDslParserParser( new CommonTokenStream( new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) ) ) )
      .program( ) )

    val program = Program( symbolDefParser.programScope, assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    program assign (
      "i",10 int
    ) assign (
      "j",200 long
    ) assign (
      "k",10.1f float
    )assign (
      "a","A" clazz (10 int)
    ) assign(
      "ages",new MapLiteral(Array(("\"Tom\"" str,12 int)),MapType(UnResolvedType,UnResolvedType))
    ) assign (
      "set",new SetLiteral(Array(1 int,2 int,3 int,4 int,5 int),SetType(UnResolvedType))
    ) assign (
      "list",new ListLiteral(Array("\"a\"" str,"\"b\"" str,"\"c\"" str),ListType(UnResolvedType))
    ) assign (
      "tuple3",new TupleLiteral(Array(1 int,10 long,"\"x\"" str),TupleType(Array(UnResolvedType,UnResolvedType,UnResolvedType)))
    )assign(
      "addresses",new SetLiteral(Array(new ListLiteral(Array(),ListType(UnResolvedType))),SetType(UnResolvedType))
    ) assign(
      "c",'a' char
    ) assign(
      "d",''' char
    ) assign(
      "b",true bool
    ) assign(
      "j2",{
        Mul(Mul(Mul(Mul(program.varRef("i"),program.varRef("j")),10 int),program.varRef("a","b"))
     , VarCall(program.varRef("a"),"foo",Array()))

    }
    )

    assert( expressionParser.program == program )


  }



}
