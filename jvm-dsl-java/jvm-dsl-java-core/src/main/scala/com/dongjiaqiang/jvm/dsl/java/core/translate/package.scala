package com.dongjiaqiang.jvm.dsl.java.core

import com.dongjiaqiang.jvm.dsl.api.scope.{ClazzScope, MethodScope}
import com.dongjiaqiang.jvm.dsl.core.optimize.{OptimizeDslType, OptimizeExpression}
import com.dongjiaqiang.jvm.dsl.core.parser.{ExpressionParser, SymbolDefParser}
import com.dongjiaqiang.jvm.dsl.core.{JvmDslLexer, JvmDslParserParser}
import com.dongjiaqiang.jvm.dsl.java.api
import com.dongjiaqiang.jvm.dsl.java.api.codes.{_SYS_ARRAY_CODES, _SYS_COL_CODES, _SYS_GEN_CODES, _SYS_LIST_CODES, _SYS_MAP_CODES, _SYS_OPTION_CODES, _SYS_SET_CODES, _SYS_STR_CODES}
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator
import com.dongjiaqiang.jvm.dsl.java.core.translate.method.MultiMethodJavaTranslator
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

import java.io.StringReader
import scala.collection.mutable.{LinkedHashMap ⇒ MutableMap}

case class JavaProgram(mainClass:String,definitionClasses:Map[String,String],importClass:Set[String])
package object translate {

  implicit class ProgramTranslator(dsl:String){

    /**
     * assemble java code into a java method
     */
    private def translateMethod(methodScope: MethodScope,body:String,javaTranslatorContext: JavaTranslatorContext):String={
        val sync = if(methodScope.sync) "synchronized" else ""
        val returnType = api.toBasicType(methodScope.returnType,javaTranslatorContext)
        val params = methodScope.params.map{
          case (name,scope)⇒s"${api.toBasicType(scope.dslType,javaTranslatorContext)} $name"
        }

        val throws = if(methodScope.throws.nonEmpty){
            s"throws ${methodScope.throws.map(c⇒api.toJavaType(c,javaTranslatorContext)).mkString(",")}"
        }else{
            ""
        }
        s"""
         |public $sync $returnType ${methodScope.name}(${params.mkString(",")}) $throws
         |        $body
         |""".stripMargin
    }

    /**
     * assemble java code into java clazz
     */
    private def translateClazz(clazzScope: ClazzScope,clazzResult:MutableMap[String, String],javaTranslatorContext: JavaTranslatorContext): String = {
      val methods = clazzResult.map {
            case (methodName, body) ⇒
              val clazzMethodScope = clazzScope.methods( methodName )
              translateMethod(clazzMethodScope,body,javaTranslatorContext)
          }.mkString("\n")
      val constructorParams = clazzScope.fields.map{
        case (name,scope)⇒s"${api.toBasicType(scope.dslType,javaTranslatorContext)} $name"
      }.mkString(",")
      val clazzFields = clazzScope.fields.map{
        case (name,scope)⇒s"public ${api.toBasicType(scope.dslType,javaTranslatorContext)} $name;"
      }.mkString("\n")

      val constructBody = clazzScope.fields.keys.map( name⇒{
        s"this.$name=$name;"
      }).mkString("\n")

      s"""
         |package ${javaTranslatorContext.packageName};
         |public class ${clazzScope.name}{
         |    $clazzFields
         |    public ${clazzScope.name}($constructorParams) {
         |          $constructBody
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
      val optimizeExpression = new OptimizeExpression( programScope )

      //step three: optimize expression
      val optimizeExpressionProgram = program.revise(optimizeExpression)

      //step four: optimize dslType
      val optimizeDsl = new OptimizeDslType(programScope)

      val optimizeDslProgram = optimizeExpressionProgram.revise(optimizeDsl)

      //finally step: translate expression to java code
      val javaTranslator = new JavaTranslator( javaTranslatorContext,programScope)
      val (assignedResult, methodResult, clazzResult) = optimizeDslProgram.visit( javaTranslator )

      val definitionClasses = clazzResult.map{
        case (clazzName,clazzMethods)⇒
          (clazzName,translateClazz(programScope.classes(clazzName),clazzMethods,javaTranslatorContext))
      }.toMap

      val mainMethods = methodResult.map{
        case (methodName,body)⇒
            translateMethod(programScope.methods(methodName),body,javaTranslatorContext)
      }.mkString("\n")

      val mainFields= programScope.fields.map {
        case (name, scope) ⇒ s"public ${api.toBasicType( scope.dslType, javaTranslatorContext )} $name = ${assignedResult(name)};"
      }.mkString( "\n" )

      val importDefinitionClasses = (if(definitionClasses.isEmpty){
        ""
      }else {
        definitionClasses.keys.map( name ⇒ {
          s"""
             |import ${javaTranslator.javaTranslatorContext.packageName}.$name
             |""".stripMargin
        } ).mkString( "", ";\n", ";" )
      }).addString(
        new StringBuilder().append(
          s"""
             |import ${classOf[_SYS_ARRAY_CODES].getCanonicalName};
             |import ${classOf[_SYS_COL_CODES].getCanonicalName};
             |import ${classOf[_SYS_GEN_CODES].getCanonicalName};
             |import ${classOf[_SYS_LIST_CODES].getCanonicalName};
             |import ${classOf[_SYS_MAP_CODES].getCanonicalName};
             |import ${classOf[_SYS_OPTION_CODES].getCanonicalName};
             |import ${classOf[_SYS_SET_CODES].getCanonicalName};
             |import ${classOf[_SYS_STR_CODES].getCanonicalName};
             |""".stripMargin)).toString()

      val mainClazz =
        s"""
           |package ${javaTranslatorContext.packageName};
           |
           |$importDefinitionClasses
           |
           |public class Program{
           |
           |    public Program(){}
           |    $mainFields
           |    $mainMethods
           |}
           |""".stripMargin
      JavaProgram(mainClazz,definitionClasses,Set())
    }
  }


}
