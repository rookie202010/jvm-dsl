package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MonadMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{MonadDslType, TupleType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 *MonadOptimize use for determines the input parameters of a method of type Monad that accepts a lambda type
 *
 * for example
 * <pre><code>
 * program{
 *    def method()=Unit{
 *        Array[(Long,Int)] arr= new Array((12,11));
 *        arr.map((e1,e2)=>{ return e1+e2;});  // e1 and e2 is UnResolved type and we should determine e1 to LongType and e2 to IntType
 *    }
 * }
 *
 * <pre><code>
 */
class MonadOptimize(override val programScope: ProgramScope,val optimizeDslType: OptimizeDslType) extends MonadMethodVisitor[Array[ValueExpression]] {

  private def transform(callee: ValueExpression, param: ValueExpression):Array[ValueExpression]={
    val monadDslType = callee.getValueType( programScope ).asInstanceOf[MonadDslType]
    val lambda = param.asInstanceOf[Lambda]
    monadDslType.carryDslType match {
      case TupleType( parameterTypes ) ⇒
        optimizeDslType.push( lambda.inputs.zip( parameterTypes ).toMap )
        val newParam = optimizeDslType.visit( param )
        optimizeDslType.pop( )
        Array( newParam.asInstanceOf[ValueExpression] )
      case _ ⇒
        optimizeDslType.push( Map( lambda.inputs.head → monadDslType.carryDslType ) )
        val newParam  = optimizeDslType.visit( param )
        optimizeDslType.pop( )
        Array( newParam.asInstanceOf[ValueExpression] )
    }
  }
  override def map(callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = transform(callee, param)
  override def flatten(callee: ValueExpression): Array[ValueExpression] = Array()
  override def flatMap(callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = transform(callee, param)
  override def filter(callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = transform(callee, param)
  override def filterNot(callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = transform(callee, param)
  override def foreach(callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = transform(callee, param)
  override def exist(callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = transform(callee, param)

  override def find(callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = transform(callee, param)

  override def toList(callee: ValueExpression): Array[ValueExpression] = Array()
  override def toSet(callee: ValueExpression): Array[ValueExpression] = Array()

  override def toMap(callee: ValueExpression): Array[ValueExpression] =Array()


  override def sort(callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = ???

  override def contains(callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = Array(optimizeDslType.visit(param).asInstanceOf[ValueExpression])
  override def zipWithIndex(callee: ValueExpression): Array[ValueExpression] = Array()
  private def agg(callee: ValueExpression, param: ValueExpression)={
    val monadDslType = callee.getValueType( programScope ).asInstanceOf[MonadDslType]
    val lambda = param.asInstanceOf[Lambda]
    optimizeDslType.push( lambda.inputs.map( name ⇒ (name, monadDslType.carryDslType) ).toMap )
    val newParam = optimizeDslType.visit( param )
    optimizeDslType.pop( )
    Array( newParam.asInstanceOf[ValueExpression] )
  }
  override def reduce(callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = agg(callee, param)
  override def reduceOption(callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = agg(callee, param)
  override def isEmpty(callee: ValueExpression): Array[ValueExpression] = Array()
  override def nonEmpty(callee: ValueExpression): Array[ValueExpression] = Array()
  override def length(callee: ValueExpression): Array[ValueExpression] = Array()

}
