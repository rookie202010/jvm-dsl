package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.{MethodVisitor, SetMethodVisitor}
import com.dongjiaqiang.jvm.dsl.api.`type`.{ArrayType, DslType, LambdaType, MonadDslType, SetType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.codes.{_SYS_COL_CODES, _SYS_SET_CODES}
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaCode
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.{JavaTranslator, LambdaToAnonymousClassTranslator}

class SetMethodJavaTranslator(override val programScope: ProgramScope,
                              val javaTranslator: JavaTranslator) extends MonadMethodJavaTranslator with SeqMethodJavaTranslator with SetMethodVisitor[String]{

  override val monadPath: Option[String]= Some( _SYS_SET_CODES.CLAZZ_NAME )
  override val seqPath: String = _SYS_COL_CODES.CLAZZ_NAME


  def transform(callee:ValueExpression,setType:SetType,carryDslType:DslType):ValueExpression={
    if(setType.seq){
        JavaCode(s"${javaTranslator.visit(callee)},new java.util.LinkedHashSet<${com.dongjiaqiang.jvm.dsl.java.api.toJavaType(carryDslType,javaTranslator.javaTranslatorContext)}>()",setType)
    }else if(setType.sorted){
        setType.sorter match {
          case None⇒
            JavaCode(s"${javaTranslator.visit(callee)},new java.util.TreeSet<${com.dongjiaqiang.jvm.dsl.java.api.toJavaType(carryDslType,javaTranslator.javaTranslatorContext)}>()",setType)
          case Some(lambda)⇒
            val comparator = LambdaToAnonymousClassTranslator.translateComparator(javaTranslator,javaTranslator.javaTranslatorContext,lambda)
            JavaCode(s"${javaTranslator.visit(callee)},new java.util.TreeSet<${com.dongjiaqiang.jvm.dsl.java.api.toJavaType(carryDslType,javaTranslator.javaTranslatorContext)}>($comparator)",setType)
        }
    }else{
        JavaCode(s"${javaTranslator.visit(callee)},new java.util.HashSet<${com.dongjiaqiang.jvm.dsl.java.api.toJavaType(carryDslType,javaTranslator.javaTranslatorContext)}>()",setType)
    }
  }
  override def map(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    val returnType = ArrayType(param.getValueType( programScope ).asInstanceOf[LambdaType].outputType )
    transform( MAP, calleeType, transform(callee, calleeType.asInstanceOf[SetType],returnType), param )
  }

  override def filter(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    transform( FILTER, calleeType, transform(callee, calleeType.asInstanceOf[SetType],calleeType.carryDslType), param )
  }

  override def filterNot(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    transform( FILTER_NOT, calleeType, transform(callee, calleeType.asInstanceOf[SetType],calleeType.carryDslType), param )
  }

  override def flatten(calleeType: MonadDslType, callee: ValueExpression): String = {
    val newCallee = transform( callee, calleeType.asInstanceOf[SetType],
      calleeType.carryDslType.asInstanceOf[MonadDslType].carryDslType )
    super.flatten( calleeType, newCallee )
  }

  override def flatMap(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    val lambdaType = param.getValueType( programScope ).asInstanceOf[LambdaType]
    val outputType = lambdaType.outputType.asInstanceOf[MonadDslType].carryDslType

    val newCallee = transform(callee, calleeType.asInstanceOf[SetType],outputType)
    super.flatMap(  calleeType, newCallee, param )
  }

  override def isEmpty(calleeType: MonadDslType, callee: ValueExpression): String = {
    MonadMethodJavaTranslator.transform(programScope,javaTranslator,None,MethodVisitor.IS_EMPTY,calleeType,callee)
  }

  override def nonEmpty(calleeType: MonadDslType, callee: ValueExpression): String = {
    val code = MonadMethodJavaTranslator.transform(programScope,javaTranslator,None,MethodVisitor.IS_EMPTY,calleeType,callee)
    s"!$code"
  }
}
