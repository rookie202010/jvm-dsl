package com.dongjiaqiang.jvm.dsl.core.symbol

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.scope._
import com.dongjiaqiang.jvm.dsl.core.parser.SymbolDefParser
import com.dongjiaqiang.jvm.dsl.core.{JvmDslLexer, JvmDslParserParser}
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.scalatest.funsuite.AnyFunSuite

import java.io.StringReader

class SymbolDefParserTestSuite extends AnyFunSuite {

  test("define empty") {
    val input =
      """program {

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

  test("define import clazz"){
    val input =
      """
        program{

          import com.xxx.yyy.Foo;
          import com.xxx.zzz.Bar;
          import com.xxx.zzz.Far from abc;
          using package "https://xxx/abc.jar" as abc;

          def foo(Int a,Int b)=Foo{
            com.xxx.yyy.Foo foo = new com.xxx.yyy.Foo(a,b);
            return foo;
          }
        }
        """.stripMargin
    val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
    val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
    val symbolDefParser = new SymbolDefParser( )

    val parseTreeWalker = new ParseTreeWalker
    parseTreeWalker.walk( symbolDefParser, jvmDslParser.program( ) )

    //define program
    implicit val programScope: ProgramScope = new ProgramScope( )

    programScope.importManager.addClass("com.xxx.yyy.Foo")
    programScope.importManager.addClass("com.xxx.zzz.Bar")
    programScope.importManager.addClass("com.xxx.zzz.Far","abc")
    programScope.importManager.addPackage("abc","https://xxx/abc.jar")

    programScope members 1 method(
      "foo",0,ClazzType("Foo"),2
    ) inputParams 2 field(
      "a",0,IntType
    ) field(
      "b",0,IntType
    ) body() field(
      "foo",0,ClazzType("com.xxx.yyy.Foo")
    )

    assert(symbolDefParser.programScope == programScope)
    assert(symbolDefParser.programScope.isImportClazz("Foo"))
    assert(symbolDefParser.programScope.isImportClazz("com.xxx.yyy.Foo"))
  }


  test( "define block" ) {
    val input =
      """program{

            Closeable closer = new Closeable();

            def xx(Int x)=String throws Ex1,Ex2{
                try{
                    if(x>0){
                      return "ok";
                    }else if(x<0){
                      throw new Ex1();
                    }else{
                      throw new Ex2();
                    }
                }catch(Ex1 e){
                  log.error("error {}",x,e);
                }catch(Ex2 e){
                  String msg = e.toString();
                  log.error("error {} {}",x,msg);
                }finally{
                  closer.close();
                }
            }

          }
          """
    val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
    val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
    val symbolDefParser = new SymbolDefParser( )

    val parseTreeWalker = new ParseTreeWalker
    parseTreeWalker.walk( symbolDefParser, jvmDslParser.program( ) )

    //define program
    implicit val programScope: ProgramScope = new ProgramScope( )

    //define method
    programScope members 2

    //define field
    programScope field(
      "closer", 0, ClazzType("Closeable",Array())
    )

    //define method
    programScope method(
      "xx", outerScopeIndex = 1, StringType, bodyStatements = 6
    ) inputParams 1 field(
      "x", 0, IntType
    ) throwEx (ClazzType("Ex1",Array()),ClazzType("Ex2")) body() block (
      outerScopeIndex = 0,blockStatements = 5
    ) block (
      outerScopeIndex = 1,blockStatements = 1
    ) belongBlock() block (
      outerScopeIndex = 3,blockStatements = 1
    ) belongBlock() block (
      outerScopeIndex = 4,blockStatements = 1
    ) belongBlock()  belongBlock() block(
      outerScopeIndex = 2,blockStatements = 1
    ) field(
      "e",0,ClazzType("Ex1")
    ) belongBlock() block(
      outerScopeIndex = 4,blockStatements = 2
    ) field(
      "e",0,ClazzType("Ex2")
    ) field (
      "msg",0,StringType
    )belongBlock() block(
      outerScopeIndex = 5,blockStatements = 1
    )

    assert( symbolDefParser.programScope == programScope )
  }

  test("define no nesting") {
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
    val jvmDslLexer = new JvmDslLexer(CharStreams.fromReader(new StringReader(input)))
    val jvmDslParser = new JvmDslParserParser(new CommonTokenStream(jvmDslLexer))
    val symbolDefParser = new SymbolDefParser()

    val parseTreeWalker = new ParseTreeWalker
    parseTreeWalker.walk(symbolDefParser, jvmDslParser.program())

    //define program
    implicit val programScope: ProgramScope = new ProgramScope();

    //define method
    programScope members 5 method ("xx",0,StringType,1)

    //define field
    programScope field (
      "ages",1,MapType(StringType, IntType)
    ) field (
      "addresses",2,MapType(StringType, StringType),true
    )

    //define class
    programScope clazz ("Student",3) members 3 field(
      "age",0,IntType
    ) field(
      "address",0,StringType
    ) method(
      "format",2,StringType,1
    ) inputParams 1 field(
      "sep",0,StringType
    )

    //define method
    programScope method(
        "find",4,StringType,3
    ) inputParams 1  field (
      "name",0,StringType
    ) body() field (
      "age",0,IntType
    ) field(
      "address",1,StringType
    )

    assert(symbolDefParser.programScope == programScope)
  }

  test("define match case"){
      val input =
        """
          program{

               def yy(Any a,(Long,Int) tuple)=Int{
                   return a match{
                        case "ok"=> { return 1; }
                        case head::tail=> {
                                if(tail.isEmpty()){
                                    return 2;
                                }else if(tuple._1==10L){
                                    return head match {
                                        case a:Int => { return a;}
                                        default => { return 4;}
                                    };

                              }else{
                                  return 5;
                              }
                             }
                        default=>{ return 5;}
                   };
               }

               class A(Int b,Int c){
                    def foo(Long d)=Int{
                        Int i = 0;
                        while(d>0){
                           i=i+1;
                           d=d-1;
                        }
                        return i;
                    }
               }

               def xx(Int b)  = String throws RuntimeException{

             Try[String] maybe = Try{

                 if(b>0){
                     return "ok";
                 }else{
                     throw RuntimeException("b is small");
                 }
             };

             return maybe.get();
        }
          }
          """


    val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
    val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
    val symbolDefParser = new SymbolDefParser( )

    val parseTreeWalker = new ParseTreeWalker
    parseTreeWalker.walk( symbolDefParser, jvmDslParser.program( ) )

    //define program
    implicit val programScope: ProgramScope = new ProgramScope( )

    //define method
    programScope members 3 method(
      name = "yy",outerScopeIndex = 0,returnType = IntType,bodyStatements = 1
    ) inputParams 2 field (
      "a",0,AnyType
    ) field (
      "tuple",0,TupleType(Array(LongType,IntType))
    ) body() lambdaBlock(
            outerScopeIndex = 0,blockStatements = 1
    ) belongBlock()lambdaBlock(
            outerScopeIndex = 0,blockStatements = 7
    ) field(
      "head",0,UnResolvedType
    ) field(
      "tail",0,UnResolvedType
    ) block (
      outerScopeIndex = 3,blockStatements = 1
    ) belongBlock() block (
      outerScopeIndex = 5,blockStatements = 1
    )  lambdaBlock (
        outerScopeIndex = 0,blockStatements = 2
    ) field (
        "a",0,IntType
    ) belongBlock() lambdaBlock (
        outerScopeIndex = 0,blockStatements = 1
    ) belongBlock() belongBlock() block (
      outerScopeIndex = 6,1
    ) belongBlock() lambdaBlock(
      outerScopeIndex = 6,blockStatements = 1
    )

    //define class
    programScope clazz(
      "A",outerScopeIndex = 1,
    ) members 3 field(
        "b",outerScopeIndex = 0,IntType
    ) field(
        "c",outerScopeIndex = 0,IntType
    ) method(
        "foo",outerScopeIndex = 2,IntType,bodyStatements = 4
    ) inputParams 1  field(
      "d",outerScopeIndex = 0,LongType
    ) body() field(
      "i",outerScopeIndex = 0,IntType
    ) block(2,2)

    //define method

    programScope method(
      "xx",2,StringType,bodyStatements = 2
    ) inputParams 1 throwEx (
       ClazzType("RuntimeException",Array())
    ) field(
      "b",0,IntType
    ) body() field(
      "maybe",0,ClazzType("Try",Array(StringType))
    ) lambdaBlock(
      0,3
    ) block(
      1,1
    ) belongBlock() block(
      2,1
    )

    assert(programScope==symbolDefParser.programScope)
  }

  test("define synchronized"){
      val input =
        """
          program{
              String lock = "lock";
              def xx()=String{
                  synchronized(lock){
                      String unlock = "unlock";
                      return unlock;
                  }
              }
          }
          """.stripMargin
    val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
    val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
    val symbolDefParser = new SymbolDefParser( )

    val parseTreeWalker = new ParseTreeWalker
    parseTreeWalker.walk( symbolDefParser, jvmDslParser.program( ) )
    //define program
    implicit val programScope: ProgramScope = new ProgramScope( )

    programScope members 2 field (
      "lock",0,StringType
    ) method (
      "xx",outerScopeIndex = 1,StringType,bodyStatements = 2
    ) body() block(outerScopeIndex = 1,blockStatements = 2) field(
      "unlock",0,StringType
    )

    assert(symbolDefParser.programScope == programScope)
  }

  test("define for statement") {
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
    val jvmDslLexer = new JvmDslLexer(CharStreams.fromReader(new StringReader(input)))
    val jvmDslParser = new JvmDslParserParser(new CommonTokenStream(jvmDslLexer))
    val symbolDefParser = new SymbolDefParser()

    val parseTreeWalker = new ParseTreeWalker
    parseTreeWalker.walk(symbolDefParser, jvmDslParser.program())

    //define program
    implicit val programScope: ProgramScope = new ProgramScope()

    //define method
    programScope members 5 method(
      "xx",0,StringType,1
    )

    //define field
    programScope field(
      "ages",1,MapType(StringType, IntType)
    ) field(
      "addresses",2,MapType(StringType, StringType),true
    )

    //define class
    programScope clazz(
      "Student",3
    ) members 3 field(
      "age",0,IntType
    ) field(
      "address",0,StringType
    ) method(
      "format",2,StringType,3
    ) inputParams 1 field(
      "sep",0,StringType
    ) body() field(
      "add",0,IntType
    ) block (1,4) field(
        "sep",0,IntType
    ) field(
        "i",2,IntType
    )

    //define method
    programScope method(
      "find",4,StringType,4
    ) inputParams  1 field(
      "name",0,StringType
    )  body() field(
      "age",0,IntType
    ) forBlock (1,6) iField(
        "i",IntType
    ) forBlock(3,4) iField(
        "k",IntType
    ) field(
        "t",3,IntType
    ) belongBlock() field(
        "j",4,IntType
    ) belongBlock() field(
        "address",2,StringType
    )

    assert(symbolDefParser.programScope == programScope)
  }
}
