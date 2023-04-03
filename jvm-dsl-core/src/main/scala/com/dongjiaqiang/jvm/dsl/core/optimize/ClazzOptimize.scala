package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.ClazzMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

import scala.collection.mutable.ArrayBuffer
import scala.util.Success



object ClazzOptimize{

  def visit(calleeType:DslType,callee:ValueExpression,optimizeDslType: OptimizeDslType,params:ArrayBuffer[ValueExpression]):Unit={
    callee match {
      case Lambda( inputs, _ ) ⇒
        calleeType match {
          case LambdaType( Some( inputType ), _ ) ⇒
            inputType match {
              case TupleType( parameterTypes ) ⇒
                optimizeDslType.push( inputs.zip( parameterTypes ).toMap )
              case _ ⇒
                optimizeDslType.push( Map( inputs.head → inputType ) )
            }
            val newParam = optimizeDslType.visit( callee )
            params.append( newParam.asInstanceOf[ValueExpression] )
            optimizeDslType.pop( )
          case _ ⇒
            params.append( optimizeDslType.visit( callee ).asInstanceOf[ValueExpression] )
        }
      case _ ⇒
        params.append( optimizeDslType.visit( callee ).asInstanceOf[ValueExpression] )
    }
  }
}


/**
 *ClazzOptimize use for determines the input parameters of a method of type ClazzType that accepts a lambda type
 *
 * for example
 * <pre><code>
 * program{
 *    import static.B
 *    def method()=Unit{
 *        B b = new B(20);
 *        b.method(e=> { return e.toLong; }); //e is UnResolvedType and we should determine e to IntType;
 *    }
 * }
 *
 * <pre><code>
 */
class ClazzOptimize(override val programScope: ProgramScope,val optimizeDslType: OptimizeDslType)  extends ClazzMethodVisitor[Array[ValueExpression]] {

  override def visit(importClazzType: ImportClazzType, importClazzMethod: ImportClazzMethod, callee: ValueExpression, name: String, params: Array[ValueExpression]): Array[ValueExpression] = {
    val newParams = ArrayBuffer[ValueExpression]( )
    val zips = importClazzMethod.params.toList.zip( params )
    zips.foreach {
      case (dslType, param) ⇒
          ClazzOptimize.visit(dslType,param,optimizeDslType,newParams)
    }
    newParams.toArray
  }

  override def visit(calleeDslType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[Array[ValueExpression]] = {
    val clazzType = calleeDslType.asInstanceOf[ClazzType]
    val resolvedClazzType = programScope.importManager.resolve( clazzType.clazzName )
    resolvedClazzType.methods.get( name )
      .flatMap( method ⇒ {
          Some( visit( resolvedClazzType, method, callee, name, params ) )
        } )
  }
}
