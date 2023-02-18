package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParserException
import com.dongjiaqiang.jvm.dsl.api.expression
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, VarRef}
import com.dongjiaqiang.jvm.dsl.api.scope.FieldScope
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.VariableContext
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._
import scala.collection.mutable.{ListMap ⇒ MutableMap}
import scala.language.postfixOps


object VarRefGenerator extends IExpressionGenerator[VariableContext, VarRef,VarGeneratorContext] {
  override def generate(exprContext: ExprContext, ruleContext: VariableContext,generatorContext: VarGeneratorContext = VarGeneratorContext(false)): VarRef = {

    val arrayRefIndexExpressions = MutableMap[Int,List[Expression]]()
    val refs = ruleContext.localVarOrArrayVar().zipWithIndex.map {
      case (variableContext, index) ⇒
        if (variableContext.localVariable( ) != null) {
          variableContext.localVariable( ).IDENTIFIER( ).getText
        } else {

          val expressions = variableContext.localArrayVariable().conditionalOrExpression()
            .map(c⇒{
              OrGenerator.generate( exprContext,
                c, generatorContext )
            })
          arrayRefIndexExpressions.put( index, expressions.toList)

          variableContext.localArrayVariable( ).localVariable( ).getText
        }
    }.toList
    if(generatorContext.ignoreVarRefResolved && exprContext.getCurrentBlock!=null && exprContext.getCurrentBlock.ignoreVarRefResolved){
        expression.VarRef(refs,arrayRefIndexExpressions,None)
    }else {
      (if(exprContext.getContextScope!=null) exprContext.getContextScope else exprContext.getProgramScope
      ).resolveVarRefs(exprContext.getCurrentExpressionIndex, refs,arrayRefIndexExpressions.keySet.toSet) match {
        case fieldScope: Some[FieldScope] ⇒ expression.VarRef(refs,arrayRefIndexExpressions, fieldScope)
        case None ⇒
            throw ExpressionParserException(s"can't resolve ${refs.mkString(",")}")
        }
      }
    }
}
