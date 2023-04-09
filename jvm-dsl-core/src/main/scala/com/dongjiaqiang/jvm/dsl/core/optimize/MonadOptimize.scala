package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MonadMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{ListType, MapType, MonadDslType, TupleType}
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
 * <pre><code>
 */
class MonadOptimize(override val programScope: ProgramScope,val optimizeDslType: OptimizeDslType) extends MonadMethodVisitor[Array[ValueExpression]] {

  override val paramsChecker: Boolean = false

  def transform(calleeType:MonadDslType,
                        param: ValueExpression):ValueExpression={
    param match {
      case lambda: Lambda⇒
        if (lambda.inputs.nonEmpty) {
          calleeType.carryDslType match {
            case TupleType( parameterTypes ) if lambda.inputs.length>1 ⇒
              optimizeDslType.push( lambda.inputs.zip( parameterTypes ).toMap )
              val newParam = optimizeDslType.visit( param )
              optimizeDslType.pop( )
              newParam.asInstanceOf[ValueExpression]
            case _ ⇒
              val map = lambda.inputs.map(input⇒(input,calleeType.carryDslType)).toMap
              optimizeDslType.push(map)
              val newParam = optimizeDslType.visit( param )
              optimizeDslType.pop( )
              newParam.asInstanceOf[ValueExpression]
          }
        } else {
          optimizeDslType.visit( param ).asInstanceOf[ValueExpression]
        }
      case _⇒
        optimizeDslType.visit( param ).asInstanceOf[ValueExpression]
    }
  }

  private def agg(calleeType: MonadDslType,
                  param: ValueExpression) = {
    val lambda = param.asInstanceOf[Lambda]
    optimizeDslType.push( lambda.inputs.map( name ⇒ (name, calleeType.carryDslType) ).toMap )
    val newParam = optimizeDslType.visit( param )
    optimizeDslType.pop( )
    Array( newParam.asInstanceOf[ValueExpression] )
  }
  override def toArray(calleeType: MonadDslType,
                       callee: ValueExpression): Array[ValueExpression] = Array()
  override def toSortedSet(calleeType: MonadDslType,
                           callee: ValueExpression,
                           param: ValueExpression): Array[ValueExpression] = Array(transform(calleeType, param))
  override def toSortedMap(calleeType: MonadDslType,
                           callee: ValueExpression,
                           param: ValueExpression): Array[ValueExpression] = Array(transform(calleeType,param))
  override def reverse(calleeType: MonadDslType,
                       callee: ValueExpression): Array[ValueExpression] = Array()
  override def reduce(calleeType: MonadDslType,
                      callee: ValueExpression,
                      init: ValueExpression,
                      param: ValueExpression): Array[ValueExpression] = Array(optimizeDslType.visit(init).asInstanceOf[ValueExpression],
    transform(calleeType,param))

  override def map(calleeType:MonadDslType,
                   callee: ValueExpression,
                   param: ValueExpression): Array[ValueExpression] = Array(transform(calleeType, param))


  override def mapValue(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): Array[ValueExpression] = {
    calleeType match {
      case mapType: MapType⇒
        Array(transform(ListType(mapType.valueParameterType), param))
      case _⇒
        Array(transform(calleeType, param))
    }
  }

  override def flatten(calleeType:MonadDslType,
                       callee: ValueExpression): Array[ValueExpression] = Array()
  override def flatMap(calleeType:MonadDslType,
                       callee: ValueExpression,
                       param: ValueExpression): Array[ValueExpression] = Array(transform(calleeType, param))
  override def filter(calleeType:MonadDslType,
                      callee: ValueExpression,
                      param: ValueExpression): Array[ValueExpression] = Array(transform(calleeType, param))
  override def filterNot(calleeType:MonadDslType,
                         callee: ValueExpression,
                         param: ValueExpression): Array[ValueExpression] = Array(transform(calleeType, param))
  override def foreach(calleeType:MonadDslType,
                       callee: ValueExpression,
                       param: ValueExpression): Array[ValueExpression] = Array(transform(calleeType, param))
  override def exist(calleeType:MonadDslType,
                     callee: ValueExpression,
                     param: ValueExpression): Array[ValueExpression] = {
    Array(transform(calleeType, param))
  }

  override def find(calleeType:MonadDslType,
                    callee: ValueExpression,
                    param: ValueExpression): Array[ValueExpression] = {
    Array(transform(calleeType, param))
  }

  override def toList(calleeType:MonadDslType,
                      callee: ValueExpression): Array[ValueExpression] = Array()
  override def toSet(calleeType:MonadDslType,
                     callee: ValueExpression): Array[ValueExpression] = Array()

  override def toMap(calleeType:MonadDslType,
                     callee: ValueExpression): Array[ValueExpression] =Array()

  override def toSeqSet(calleeType: MonadDslType,
                        callee: ValueExpression): Array[ValueExpression] =  Array()
  override def toSortedSet(calleeType: MonadDslType,
                           callee: ValueExpression): Array[ValueExpression] =  Array()
  override def toSeqMap(calleeType: MonadDslType,
                        callee: ValueExpression): Array[ValueExpression] =  Array()
  override def toSortedMap(calleeType: MonadDslType,
                           callee: ValueExpression): Array[ValueExpression] =  Array()
  override def sort(calleeType:MonadDslType,
                    callee: ValueExpression,
                    param: ValueExpression): Array[ValueExpression] = {
    Array(transform(calleeType, param))
  }
  override def contains(calleeType:MonadDslType,
                        callee: ValueExpression,
                        param: ValueExpression): Array[ValueExpression] = Array(optimizeDslType.visit(param).asInstanceOf[ValueExpression])
  override def zipWithIndex(calleeType:MonadDslType,
                            callee: ValueExpression): Array[ValueExpression] = Array()
  override def reduce(calleeType:MonadDslType,
                      callee: ValueExpression,
                      param: ValueExpression): Array[ValueExpression] = agg(calleeType,param)
  override def reduceOption(calleeType:MonadDslType,
                            callee: ValueExpression,
                            param: ValueExpression): Array[ValueExpression] = agg(calleeType,param)
  override def isEmpty(calleeType:MonadDslType,
                       callee: ValueExpression): Array[ValueExpression] = Array()
  override def nonEmpty(calleeType:MonadDslType,
                        callee: ValueExpression): Array[ValueExpression] = Array()
  override def length(calleeType:MonadDslType,
                      callee: ValueExpression): Array[ValueExpression] = Array()

  override def mkString(calleeType: MonadDslType, callee: ValueExpression, sep: ValueExpression): Array[ValueExpression] = Array( optimizeDslType.visit( sep ).asInstanceOf[ValueExpression])

  override def mkString(calleeType: MonadDslType, callee: ValueExpression, start: ValueExpression, sep: ValueExpression, end: ValueExpression): Array[ValueExpression] = Array(
    optimizeDslType.visit( start ).asInstanceOf[ValueExpression],
    optimizeDslType.visit( sep ).asInstanceOf[ValueExpression],
    optimizeDslType.visit( end ).asInstanceOf[ValueExpression],
  )

  override def head(calleeType: MonadDslType, callee: ValueExpression): Array[ValueExpression] = Array()

  override def last(calleeType: MonadDslType, callee: ValueExpression): Array[ValueExpression] = Array()

  override def headOption(calleeType: MonadDslType, callee: ValueExpression): Array[ValueExpression] = Array()

  override def lastOption(calleeType: MonadDslType, callee: ValueExpression): Array[ValueExpression] = Array()
}
