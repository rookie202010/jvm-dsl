package com.dongjiaqiang.jvm.dsl.java.core

import com.dongjiaqiang.jvm.dsl.api.scope.{ClazzScope, MethodScope}
import com.dongjiaqiang.jvm.dsl.core.optimize.OptimizeExpression
import com.dongjiaqiang.jvm.dsl.core.parser.{ExpressionParser, SymbolDefParser}
import com.dongjiaqiang.jvm.dsl.core.{JvmDslLexer, JvmDslParserParser}
import com.dongjiaqiang.jvm.dsl.java.api
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

import java.io.StringReader
import scala.collection.mutable.{ListMap ⇒ MutableMap}

case class JavaProgram(mainClass:String,definitionClasses:Map[String,String],importClass:Set[String])
package object translate {

  implicit class ProgramTranslator(dsl:String){


    private def translateMethod(methodScope: MethodScope,body:String,javaTranslatorContext: JavaTranslatorContext):String={
        val sync = if(methodScope.sync) "synchronized" else ""
        val returnType = api.toJavaType(methodScope.returnType,javaTranslatorContext)
        val params = methodScope.params.map{
          case (name,scope)⇒s"${api.toJavaType(scope.dslType,javaTranslatorContext)} $name"
        }.mkString(",")
        val throws = if(methodScope.throws.nonEmpty){
            s"throws ${methodScope.throws.map(c⇒api.toJavaType(c,javaTranslatorContext)).mkString(",")}"
        }else{
            ""
        }
        s"""
         |public $sync $returnType ${methodScope.name}($params) $throws
         |        $body
         |""".stripMargin
    }

    private def translateClazz(clazzScope: ClazzScope,clazzResult:MutableMap[String, String],javaTranslatorContext: JavaTranslatorContext): String = {
      val methods = clazzResult.map {
            case (methodName, body) ⇒
              val clazzMethodScope = clazzScope.methods( methodName )
              translateMethod(clazzMethodScope,body,javaTranslatorContext)
          }.mkString("\n")
      val constructorParams = clazzScope.fields.map{
        case (name,scope)⇒s"${api.toJavaType(scope.dslType,javaTranslatorContext)} $name"
      }.mkString(",")
      val clazzFields = clazzScope.fields.map{
        case (name,scope)⇒s"private final ${api.toJavaType(scope.dslType,javaTranslatorContext)} $name;"
      }.mkString("\n")

      s"""
         |class ${clazzScope.name}{
         |
         |    $clazzFields
         |
         |    public ${clazzScope.name}($constructorParams) {
         |
         |    }
         |    $methods
         |}
         |""".stripMargin
    }


    def translate(javaTranslatorContext: JavaTranslatorContext):JavaProgram= {

      //step one: resolve symbol definition in dsl
      val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( dsl ) ) )
      val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
      val symbolDefParser = new SymbolDefParser( )
      ParseTreeWalker.DEFAULT.walk( symbolDefParser, jvmDslParser.program( ) )

      //step two: parse dsl to expression base on symbol
      val expressionParser = new ExpressionParser( symbolDefParser.programScope )
      ParseTreeWalker.DEFAULT.walk( expressionParser, new JvmDslParserParser( new CommonTokenStream( new JvmDslLexer( CharStreams.fromReader( new StringReader( dsl ) ) ) ) )
        .program( ) )

      val program = expressionParser.program
      val programScope = program.programScope
      val defaultReviser = new OptimizeExpression( programScope )

      //step three: optimize expression
      val optimizeProgram = program.revise(defaultReviser)

      //finally step: translate expression to java code
      val javaTranslator = new JavaTranslator( javaTranslatorContext, programScope )
      val (assignedResult, methodResult, clazzResult) = optimizeProgram.visit( javaTranslator )
      clazzResult.map{
        case (clazzName,clazzMethods)⇒
           val clazzScope = programScope.classes(clazzName)
           clazzMethods.map{
             case (clazzMethod,body)⇒{
                val clazzMethodScope = clazzScope.methods(clazzMethod)
             }
           }
      }
      return null
    }
  }


}
