package com.dongjiaqiang.jvm.dsl.core.symbol

import com.dongjiaqiang.jvm.dsl.core.`type`.{IntType, MapType, StringType}
import com.dongjiaqiang.jvm.dsl.core.parser.SymbolDefParser
import com.dongjiaqiang.jvm.dsl.core.scope.{BlockScope, ClazzScope, FieldScope, ForStatementBlockScope, MethodScope, ProgramScope, Scope}
import com.dongjiaqiang.jvm.dsl.core.{JvmDslLexer, JvmDslParserLexer, JvmDslParserParser}
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CodePointBuffer, CodePointCharStream, CommonTokenStream}
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.{ListMap ⇒ MutableMap}
import java.io.StringReader

class SymbolDefParserSuite extends AnyFunSuite {

    test( "define empty program" ) {
        val input =
            """program {

        }
        """
        val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
        val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
        val symbolDefParser = new SymbolDefParser( )
        ParseTreeWalker.DEFAULT.walk( symbolDefParser, jvmDslParser.program( ) )
        assert( symbolDefParser.programScope.fields.isEmpty )
        assert( symbolDefParser.programScope.classes.isEmpty )
        assert( symbolDefParser.programScope.methods.isEmpty )
    }

    test( "define program no nesting" ) {
        val input =
            """program{

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
        val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
        val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
        val symbolDefParser = new SymbolDefParser( )

        val parseTreeWalker = new ParseTreeWalker
        parseTreeWalker.walk( symbolDefParser, jvmDslParser.program( ) )

        val programScope = new ProgramScope( )
        programScope.incStatement(5)


        programScope.addScope( "ages", new FieldScope( 1,"ages", new MapType( StringType, IntType ) ) )
          .addScope( "addresses", new FieldScope( 2,"addresses", new MapType( StringType, StringType ), true ) )
          .addScope("Student",new ClazzScope(3, "Student" )

            .addScope("format",new MethodScope( "format",2, studentClazz, StringType ))
          )

        //define class Student
        val studentClazz = new ClazzScope(3, "Student" )
        studentClazz.incStatement(3)

        val studentFormatMethod = new MethodScope( "format",2, studentClazz, StringType )
        studentFormatMethod.incStatement(1)

        studentFormatMethod.addScope( new BlockScope(0, studentFormatMethod ) )
        studentFormatMethod.blockScope.incStatement()

        studentFormatMethod.addScope( "sep", new FieldScope( 0,"sep", StringType ) )

        studentClazz.addScope( "age", new FieldScope( 0,"age", IntType ) )
        studentClazz.addScope( "address", new FieldScope( 0,"address", StringType ) )
        studentClazz.addScope( "format", studentFormatMethod )

        programScope.addScope( "Student", studentClazz )

        //define xx method
        val xxMethod = new MethodScope( "xx",0, programScope, StringType )
        xxMethod.addScope( new BlockScope(0, xxMethod ) )
        xxMethod.blockScope.incStatement()

        programScope.addScope( "xx", xxMethod )

        //define find method
        val findMethod = new MethodScope( "find",4, programScope, StringType )
        findMethod.incStatement(1)
        findMethod.addScope( new BlockScope(0, findMethod ) )
        findMethod.blockScope.incStatement(3)

        findMethod.addScope( "name", new FieldScope( 0,"name", StringType ) )

        findMethod.blockScope.addScope( "age", new FieldScope( 0,"age", IntType ) )
        findMethod.blockScope.addScope( "address", new FieldScope( 1,"address", StringType ) )

        programScope.addScope( "find", findMethod )

        assert( symbolDefParser.programScope == programScope )
    }

    test( "define program one level of nesting" ) {
        val input =
            """program{

              def xx()=String{
              return "xx";
              }

               Map[String,Int] ages = {"Tom":12};
               volatile Map[String,String] addresses = {};

               class Student(Int age,String address){
                 def format(String sep)=String{
                     Int add = 100;
                     {
                        Int sep = 100;
                        sep+=100;

                        Int i = sep;
                        add+=i;

                     }
                     return format("%s %s %s",add,sep,address);
                 }
               }

               def find(String name)=String{
                 Int age = ages.get(name);

                 for(Int i=0;i<100;i=i+1){
                      for(Int k=0;i<10;k=k+i){
                          Int t = k;
                      }
                      Int j = t;
                      age=age+i;
                 }

                 String address = addresses.get(name);
                 return format("-->");
               }
            }
            """
        val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
        val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
        val symbolDefParser = new SymbolDefParser( )

        val parseTreeWalker = new ParseTreeWalker
        parseTreeWalker.walk( symbolDefParser, jvmDslParser.program( ) )

        val programScope = new ProgramScope( )
        programScope.incStatement(5)

        //define filed ages
        val agesField = new FieldScope( 1,"ages", new MapType( StringType, IntType ) )
        //define field addresses
        val addressesField = new FieldScope( 2,"addresses", new MapType( StringType, StringType ), true )

        programScope.addScope( "ages", agesField )
        programScope.addScope( "addresses", addressesField )

        //define class Student
        val studentClazz = new ClazzScope( 3,"Student" )
        studentClazz.incStatement(3)

        val studentFormatMethod = new MethodScope( "format",2, studentClazz, StringType )
        studentFormatMethod.incStatement()

        studentFormatMethod.addScope( new BlockScope( 0,studentFormatMethod ) )
        studentFormatMethod.blockScope.incStatement(3)

        studentFormatMethod.addScope( "sep", new FieldScope( 0,"sep", StringType ) )

        studentFormatMethod.blockScope.addScope("add",new FieldScope(0,"add",IntType))

        val studentFormatMethodBlock_1 = new BlockScope(1,studentFormatMethod.blockScope)
        studentFormatMethodBlock_1.incStatement(4)
        studentFormatMethodBlock_1.addScope("sep",new FieldScope(0,"sep",IntType))
        studentFormatMethodBlock_1.addScope("i",new FieldScope(2,"i",IntType))

        studentFormatMethod.blockScope.addScope(studentFormatMethodBlock_1)

        studentClazz.addScope( "age", new FieldScope( 0,"age", IntType ) )
        studentClazz.addScope( "address", new FieldScope( 0,"address", StringType ) )
        studentClazz.addScope( "format", studentFormatMethod )

        programScope.addScope( "Student", studentClazz )

        //define xx method
        val xxMethod = new MethodScope( "xx",0, programScope, StringType )
        xxMethod.addScope( new BlockScope(0, xxMethod ) )
        xxMethod.blockScope.incStatement()

        programScope.addScope( "xx", xxMethod )

        //define find method
        val findMethod = new MethodScope( "find",4, programScope, StringType )
        findMethod.incStatement()
        findMethod.addScope( new BlockScope(0, findMethod ) )
        findMethod.blockScope.incStatement(4)

        findMethod.addScope( "name", new FieldScope( 0,"name", StringType ) )

        findMethod.blockScope.addScope( "age", new FieldScope( 0,"age", IntType ) )
        findMethod.blockScope.addScope( "address", new FieldScope( 2,"address", StringType ) )

        //forStatement block
        val findMethodBlock_1 = new ForStatementBlockScope(1,MutableMap("i"→new FieldScope(0,"i",IntType)),findMethod.blockScope)
        findMethodBlock_1.incStatement(6)

        val findMethodBlock_11 = new ForStatementBlockScope(3,MutableMap("k"→new FieldScope(0,"k",IntType)),findMethodBlock_1)
        findMethodBlock_11.incStatement(4)

        findMethodBlock_11.addScope("t",new FieldScope(3,"t",IntType))
        findMethodBlock_1.addScope(findMethodBlock_11)

        findMethodBlock_1.addScope("j",new FieldScope(4,"j",IntType))
        findMethod.blockScope.addScope(findMethodBlock_1)



        programScope.addScope( "find", findMethod )

        assert( symbolDefParser.programScope == programScope )
    }
}
