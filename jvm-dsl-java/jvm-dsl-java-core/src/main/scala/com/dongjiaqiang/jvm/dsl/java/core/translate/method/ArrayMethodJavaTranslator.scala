package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.{ArrayType, DslType, IntType, LambdaType, ListType, MonadDslType, SetType, TupleType}
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor.{MAP, ZIP_WITH_INDEX}
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.SetMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.codes.{_SYS_ARRAY_CODES, _SYS_SET_CODES}
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaCode
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator

class ArrayMethodJavaTranslator(override val programScope: ProgramScope,
                                val javaTranslator: JavaTranslator) extends MonadMethodJavaTranslator with SeqMethodJavaTranslator with SetMethodVisitor[String]{

  override val monadPath: Option[String]= Some( _SYS_ARRAY_CODES.CLAZZ_NAME )
  override val seqPath: String = _SYS_ARRAY_CODES.CLAZZ_NAME

  def transform(callee: ValueExpression, arrayType: ArrayType): ValueExpression = {
      val calleeCode = javaTranslator.visit( callee )
      val param = s"new ${com.dongjiaqiang.jvm.dsl.java.api.toJavaType(arrayType.carryDslType,javaTranslator.javaTranslatorContext)}[${calleeCode}.length]"
      JavaCode( s"$calleeCode,$param",arrayType)
  }

  override def indexOf(calleeDslType: MonadDslType, callee: ValueExpression, element: ValueExpression): String = super.indexOf( calleeDslType, callee, element )

  override def get(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression): String = {
    val code =
      s"${javaTranslator.visit( callee )}[${javaTranslator.visit( index )}]"
    javaTranslator.specifyDslType( code, calleeDslType.carryDslType )
  }

  override def map(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    val returnType = ArrayType(param.getValueType( programScope ).asInstanceOf[LambdaType].outputType )
    transform( MAP, calleeType, transform( callee,returnType), param )
  }

  override def filter(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    val code = super.filter( calleeType, callee, param )
    s"$code.toArray(new ${com.dongjiaqiang.jvm.dsl.java.api.toJavaType(calleeType.carryDslType,javaTranslator.javaTranslatorContext)}[]{})"
  }

  override def flatMap(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    val lambdaType = param.getValueType( programScope ).asInstanceOf[LambdaType]
    val outputType  = lambdaType.outputType.asInstanceOf[MonadDslType].carryDslType
    val code = super.flatMap( calleeType, callee, param )
    s"$code.toArray(new ${com.dongjiaqiang.jvm.dsl.java.api.toJavaType(outputType,javaTranslator.javaTranslatorContext)}[]{})"
  }

  override def length(calleeType: MonadDslType, callee: ValueExpression): String = {
      s"${javaTranslator.visit( callee )}.length"
  }

  override def splitAt(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression): String = super.splitAt( calleeDslType, callee, index )

  override def add(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): String = {
    throw new UnsupportedOperationException("un supported")
  }

  override def add(calleeDslType: MonadDslType, callee: ValueExpression, element: ValueExpression): String = {
    throw new UnsupportedOperationException( "un supported" )
  }

  override def addAll(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): String = {
    throw new UnsupportedOperationException( "un supported" )
  }

  override def addAll(calleeDslType: MonadDslType, callee: ValueExpression, element: ValueExpression): String = {
    throw new UnsupportedOperationException( "un supported" )
  }

  override def remove(calleeDslType: MonadDslType, callee: ValueExpression, indexOrElement: ValueExpression): String = {
    throw new UnsupportedOperationException( "un supported" )
  }

  override def update(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): String = {
    s"${javaTranslator.visit( callee )}[${javaTranslator.visit( index )}]=${javaTranslator.visit( element )}"
  }
}
