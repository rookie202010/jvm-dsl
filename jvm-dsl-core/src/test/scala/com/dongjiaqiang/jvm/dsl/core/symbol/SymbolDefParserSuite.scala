package com.dongjiaqiang.jvm.dsl.core.symbol

import com.dongjiaqiang.jvm.dsl.core.`type`.{IntType, MapType, StringType}
import com.dongjiaqiang.jvm.dsl.core.scope.{BlockScope, ClazzScope, FieldScope, MethodScope, ProgramScope, Scope}
import com.dongjiaqiang.jvm.dsl.core.{JvmDslLexer, JvmDslParserLexer, JvmDslParserParser}
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CodePointBuffer, CodePointCharStream, CommonTokenStream}
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.{ListMap ⇒ MutableMap}
import java.io.StringReader

class SymbolDefParserSuite extends AnyFunSuite {

  test( "define empty package" ) {
    val input ="""program {

        }
        """
    val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader(new StringReader(input)))
    val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
    val symbolDefParser = new SymbolDefParser( )
    ParseTreeWalker.DEFAULT.walk( symbolDefParser, jvmDslParser.program( ) )
    assert( symbolDefParser.programScope.fields.isEmpty )
    assert( symbolDefParser.programScope.classes.isEmpty )
    assert( symbolDefParser.programScope.methods.isEmpty )
  }

  test( "define one package" ) {
    val input = """program{

          def xx()=String{
          return "xx";
          }

           Map[String,Int] ages = {"Tom":12};
           volatile Map[String,String] addresses = {};

           class Student(Int age,String address){
             def format(String sep)=String{
                 return format("%s %s %s",age,sep,address);
             }
           }

           def find(String name)=String{
             Int age = ages.get(name);
             String address = addresses.get(name);
             return format("-->");
           }
        }
        """
    val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader(new StringReader(input)))
    val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
    val symbolDefParser = new SymbolDefParser( )

    val parseTreeWalker = new ParseTreeWalker
    parseTreeWalker.walk( symbolDefParser, jvmDslParser.program() )

    val programScope = new ProgramScope()

    //define filed ages
    val agesField = new FieldScope( "ages", new MapType( StringType, IntType ))

    //define field addresses
    val addressesField = new FieldScope( "addresses", new MapType( StringType, StringType ),true)

    programScope.addScope("ages",agesField)
    programScope.addScope("addresses",addressesField)

    //define class Student
    val studentClazz = new ClazzScope("Student")
    val studentFormatMethod = new MethodScope("format",studentClazz,StringType)
    studentFormatMethod.addScope(new BlockScope(studentFormatMethod))

    studentFormatMethod.addScope("sep",new FieldScope("sep",StringType))

    studentClazz.addScope("age",new FieldScope("age",IntType))
    studentClazz.addScope("address",new FieldScope("address",StringType))
    studentClazz.addScope("format",studentFormatMethod)

    programScope.addScope("Student",studentClazz)

    //define xx method
    val xxMethod = new MethodScope("xx",programScope,StringType)
    xxMethod.addScope(new BlockScope(xxMethod))

    programScope.addScope("xx",xxMethod)

    //define find method
    val findMethod = new MethodScope("find",programScope,StringType)
    findMethod.addScope(new BlockScope(findMethod))

    findMethod.addScope("name",new FieldScope("name",StringType))

    findMethod.blockScope.addScope("age",new FieldScope("age",IntType))
    findMethod.blockScope.addScope("address",new FieldScope("address",StringType))

    programScope.addScope("find",findMethod)

    assert(symbolDefParser.programScope==programScope)
  }
}
