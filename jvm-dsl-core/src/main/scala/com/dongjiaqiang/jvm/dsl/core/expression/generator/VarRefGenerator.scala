package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.`type`.ClazzType
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.{StaticVarRef, VarRef}
import com.dongjiaqiang.jvm.dsl.api.scope.FieldScope
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.VariableContext
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._
import scala.collection.mutable.{LinkedHashMap⇒ MutableMap}
import scala.language.postfixOps


object VarRefGenerator extends IExpressionGenerator[VariableContext, VarRef,VarGeneratorContext] {
  override def generate(exprContext: ExprContext, ruleContext: VariableContext,generatorContext: VarGeneratorContext = VarGeneratorContext(false)): VarRef = {
    val arrayRefIndexExpressions = MutableMap[Int,List[ValueExpression]]()
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
        VarRef(refs,arrayRefIndexExpressions,None)
    }else {
      val scope = if(exprContext.getContextScope!=null) exprContext.getContextScope else exprContext.getProgramScope
      scope.resolveVarRefs(exprContext.getCurrentExpressionIndex, refs,arrayRefIndexExpressions.keySet.toSet) match {
        case fieldScope: Some[FieldScope] ⇒ VarRef(refs,arrayRefIndexExpressions, fieldScope)
        case None ⇒
            val ref = refs.head
            if(exprContext.getProgramScope.isImportClazz(ref)){
                new StaticVarRef(new ClazzType(ref),exprContext.getProgramScope,refs.tail,arrayRefIndexExpressions)
            }else {
              throw ExpressionParseException( s"can't resolve ${refs.mkString( "." )}" )
            }
        }
      }
    }
}
