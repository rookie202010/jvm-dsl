package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.{MethodVisitor, MonadMethodVisitor}
import com.dongjiaqiang.jvm.dsl.api.`type`.{ArrayType, DslType, EitherType, LambdaType, ListType, MonadDslType, OptionType, SetType, StringType, TryType, TupleType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaCode
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.{JavaTranslator, MonadLambdaToAnonymousClassTranslator, StringMonadLambdaToAnonymousClassTranslator}

import java.util.Comparator

object MonadMethodJavaTranslator{


  def transform(programScope: ProgramScope,
                javaTranslator: JavaTranslator,
                path: Option[String],
                methodName: String,
                calleeType: MonadDslType,
                callee: ValueExpression,
                params: ValueExpression*): String = {
    val carryDslType = calleeType.carryDslType
    val input = params.map {
      case lambda: Lambda ⇒
        val lambdaType = lambda.getValueType( programScope )
        val newLambdaType = lambdaType.copy( mayInputType = {
          lambdaType.mayInputType match {
            case None ⇒ None
            case Some( TupleType( parameterTypes ) ) ⇒ Some( TupleType( parameterTypes.map( _ ⇒ carryDslType ) ) )
            case Some( _ ) ⇒ Some( carryDslType )
          }
        } )
        calleeType match {
          case StringType ⇒
            StringMonadLambdaToAnonymousClassTranslator.translate( newLambdaType,
              lambda, javaTranslator.javaTranslatorContext,
              javaTranslator, genericErasure = true )
          case _ ⇒
            methodName match {
              case MethodVisitor.SORT | MethodVisitor.TO_SORTED_SET⇒
                s"""
                   new ${classOf[Comparator[_]].getCanonicalName}<Object>(){
                       @Override
                       public int compare(Object ${lambda.inputs.head},Object ${lambda.inputs.last})
                           ${javaTranslator.visit( lambda )}
                   }
                """
              case _⇒
                MonadLambdaToAnonymousClassTranslator.translate( newLambdaType,
                  lambda,
                  javaTranslator.javaTranslatorContext, javaTranslator,
                  genericErasure = true )
            }
        }
      case param ⇒
        javaTranslator.visit( param )
    }.mkString( "," )
    path match {
      case Some( p ) ⇒
        val newInput = if (input.isEmpty) {
          input
        } else {
          s",$input"
        }
        s"""
          $p.$methodName(${javaTranslator.visit( callee )}$newInput)
        """
      case None ⇒
        s"""
          ${javaTranslator.visit( callee )}.$methodName($input)
        """
    }
  }

}
trait MonadMethodJavaTranslator extends MonadMethodVisitor[String]{
  val javaTranslator: JavaTranslator
  val monadPath:Option[String]

  def transform(methodName: String,
                        calleeType: MonadDslType,
                        callee: ValueExpression,
                        params: ValueExpression*): String = {
    MonadMethodJavaTranslator.transform(programScope,javaTranslator,monadPath,methodName,calleeType,callee,params:_*)
  }


  override def mapValue(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    throw new UnsupportedOperationException("unsupported")
  }

  override def map(calleeType:MonadDslType,
                   callee: ValueExpression,
                   param: ValueExpression): String = {
    transform( MAP, calleeType,callee, param )
  }
  override def filter(calleeType:MonadDslType,
                      callee: ValueExpression,
                      param: ValueExpression): String = {
    transform( FILTER,calleeType, callee, param )
  }
  override def filterNot(calleeType:MonadDslType,
                         callee: ValueExpression,
                         param: ValueExpression): String = {
    transform( FILTER_NOT, calleeType,callee, param )
  }
  override def foreach(calleeType:MonadDslType,
                       callee: ValueExpression,
                       param: ValueExpression): String = {
    transform( FOREACH, calleeType,callee, param )
  }
  override def exist(calleeType:MonadDslType,
                     callee: ValueExpression,
                     param: ValueExpression): String = {
    transform( EXIST, calleeType,callee, param )
  }
  override def find(calleeType:MonadDslType
                    ,callee: ValueExpression,
                    param: ValueExpression): String = {
    transform( FIND, calleeType,callee, param )
  }
  override def flatten(calleeType:MonadDslType,
                       callee: ValueExpression): String = {
    val calleeFlag = flag(calleeType)
    val carryFlag = flag(calleeType.carryDslType)
    val inputFlag = s"$carryFlag$calleeFlag"
    transform(s"${FLATTEN}_$inputFlag",calleeType,callee)
  }

  def flag(calleeType: DslType): String = {
    calleeType match {
      case _: ArrayType ⇒ "A"
      case _: EitherType ⇒ "E"
      case _: OptionType ⇒ "O"
      case _: TryType ⇒ "T"
      case _: SetType ⇒ "C"
      case _: ListType ⇒ "C"
      case _ ⇒ ""
    }
  }

  override def flatMap(calleeType:MonadDslType
                       ,callee: ValueExpression,
                       param: ValueExpression): String = {
    val calleeFlag = flag(calleeType)
    val carryFlag = flag(calleeType.carryDslType)
    val lambdaType = param.getValueType( programScope ).asInstanceOf[LambdaType]
    val outFlag = flag(lambdaType.outputType)
    val inputFlag = if(lambdaType.mayInputType.isDefined &&
      lambdaType.mayInputType.get.isInstanceOf[TupleType] &&
      lambdaType.mayInputType.get.asInstanceOf[TupleType].parameterTypes.length>0 && carryFlag!=""){
        val size = lambdaType.mayInputType.get.asInstanceOf[TupleType].parameterTypes.length
        s"T${size}_$calleeFlag"
    }else{
        s"${carryFlag}$calleeFlag"
    }
    transform( s"${FLAT_MAP}_${inputFlag}_$outFlag",calleeType,callee,param)
  }
  override def contains(calleeType:MonadDslType,
                        callee: ValueExpression,
                        param: ValueExpression): String = {
    transform(CONTAINS,calleeType,callee,param)
  }
  override def sort(calleeType:MonadDslType,
                    callee: ValueExpression,
                    param: ValueExpression): String = {
    transform(SORT,calleeType,callee, param)
  }
  override def zipWithIndex(calleeType:MonadDslType,
                            callee: ValueExpression): String = {
    transform(ZIP_WITH_INDEX,calleeType,callee)
  }
  override def reduce(calleeType:MonadDslType,
                      callee: ValueExpression,
                      param: ValueExpression): String = {
    val code = transform( REDUCE, calleeType, callee, param )
    javaTranslator.specifyDslType(javaTranslator.visit( JavaCode( code, calleeType.carryDslType ) ),calleeType.carryDslType)
  }
  override def reduceOption(calleeType:MonadDslType,
                            callee: ValueExpression,
                            param: ValueExpression): String = {
    transform(REDUCE_OPTION,calleeType,callee, param)
  }
  override def isEmpty(calleeType:MonadDslType,
                       callee: ValueExpression): String = {
    transform(IS_EMPTY,calleeType,callee)
  }
  override def nonEmpty(calleeType:MonadDslType,
                        callee: ValueExpression): String = {
    transform(NON_EMPTY,calleeType,callee)
  }
  override def length(calleeType:MonadDslType,
                      callee: ValueExpression): String = {
    transform(LENGTH,calleeType,callee)
  }
  override def toList(calleeType:MonadDslType,
                      callee: ValueExpression): String = {
    transform(TO_LIST,calleeType,callee)
  }
  override def toSet(calleeType:MonadDslType,
                     callee: ValueExpression): String = {
    transform(TO_SET,calleeType,callee)
  }
  override def toMap(calleeType:MonadDslType,
                     callee: ValueExpression): String = {
    transform(TO_MAP,calleeType,callee)
  }
  override def toArray(calleeType: MonadDslType,
                       callee: ValueExpression): String = {
    transform(TO_ARRAY,calleeType,callee)
  }
  override def toSeqSet(calleeType: MonadDslType,
                        callee: ValueExpression): String = {
    transform(TO_SEQ_SET,calleeType,callee)
  }
  override def toSortedSet(calleeType: MonadDslType,
                           callee: ValueExpression): String = {
    transform(TO_SORTED_SET,calleeType,callee)
  }
  override def toSortedSet(calleeType: MonadDslType,
                           callee: ValueExpression,
                           param: ValueExpression): String = {
    transform(TO_SORTED_SET,calleeType,callee,param)
  }
  override def toSeqMap(calleeType: MonadDslType,
                        callee: ValueExpression): String = {
    transform(TO_SEQ_MAP,calleeType,callee)
  }
  override def toSortedMap(calleeType: MonadDslType,
                           callee: ValueExpression): String = {
    transform(TO_SORTED_MAP,calleeType,callee)
  }
  override def toSortedMap(calleeType: MonadDslType,
                           callee: ValueExpression,
                           param: ValueExpression): String = {
    transform(TO_SORTED_MAP,calleeType,callee, param)
  }
  override def reverse(calleeType: MonadDslType,
                       callee: ValueExpression): String = {
    transform("reverse",calleeType,callee)
  }

  override def reduce(calleeType: MonadDslType,
                      callee: ValueExpression,
                      init: ValueExpression,
                      param: ValueExpression): String = {
    val code = transform(REDUCE,calleeType,callee,init,param)
    javaTranslator.specifyDslType(code,calleeType.carryDslType)
  }

  override def mkString(calleeType: MonadDslType, callee: ValueExpression, sep: ValueExpression): String = {
    transform(MK_STRING,calleeType,callee,sep)
  }

  override def mkString(calleeType: MonadDslType, callee: ValueExpression, start: ValueExpression, sep: ValueExpression, end: ValueExpression): String = {
    transform(MK_STRING,calleeType,callee,start,sep,end)
  }

  override def head(calleeType: MonadDslType, callee: ValueExpression): String = {
    val code = transform(HEAD,calleeType,callee)
    javaTranslator.specifyDslType(code,calleeType.carryDslType)
  }

  override def last(calleeType: MonadDslType, callee: ValueExpression): String = {
    val code = transform(LAST,calleeType,callee)
    javaTranslator.specifyDslType(code,calleeType.carryDslType)
  }

  override def headOption(calleeType: MonadDslType, callee: ValueExpression): String = {
    transform(HEAD_OPTION,calleeType,callee)
  }

  override def lastOption(calleeType: MonadDslType, callee: ValueExpression): String = {
    transform(LAST_OPTION,calleeType,callee)
  }
}
