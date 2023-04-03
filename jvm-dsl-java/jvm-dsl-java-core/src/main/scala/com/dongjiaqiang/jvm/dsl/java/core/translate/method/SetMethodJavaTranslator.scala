package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.SetMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{MonadDslType, SetType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.codes.{_SYS_COL_CODES, _SYS_SET_CODES}
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaCode
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.{JavaTranslator, LambdaToAnonymousClassTranslator}

class SetMethodJavaTranslator(override val programScope: ProgramScope,
                              val javaTranslator: JavaTranslator) extends MonadMethodJavaTranslator with SeqMethodJavaTranslator with SetMethodVisitor[String]{

  override val monadPath: Option[String]= Some( _SYS_SET_CODES.CLAZZ_NAME )
  override val seqPath: String = _SYS_COL_CODES.CLAZZ_NAME


  def transform(callee:ValueExpression,setType:SetType):ValueExpression={
    if(setType.seq){
        JavaCode(s"${javaTranslator.visit(callee)},new java.util.LinkedHashSet<${com.dongjiaqiang.jvm.dsl.java.api.toJavaType(setType.carryDslType,javaTranslator.javaTranslatorContext)}>()",setType)
    }else if(setType.sorted){
        setType.sorter match {
          case None⇒
            JavaCode(s"${javaTranslator.visit(callee)},new java.util.TreeSet<${com.dongjiaqiang.jvm.dsl.java.api.toJavaType(setType.carryDslType,javaTranslator.javaTranslatorContext)}>()",setType)
          case Some(lambda)⇒
            val comparator = LambdaToAnonymousClassTranslator.translateComparator(javaTranslator,javaTranslator.javaTranslatorContext,lambda)
            JavaCode(s"${javaTranslator.visit(callee)},new java.util.TreeSet<${com.dongjiaqiang.jvm.dsl.java.api.toJavaType(setType.carryDslType,javaTranslator.javaTranslatorContext)}>($comparator)",setType)
        }
    }else{
        JavaCode(s"${javaTranslator.visit(callee)},new java.util.HashSet<${com.dongjiaqiang.jvm.dsl.java.api.toJavaType(setType.carryDslType,javaTranslator.javaTranslatorContext)}>()",setType)
    }
  }
  override def map(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    transform( MAP, calleeType, transform(callee, calleeType.asInstanceOf[SetType]), param )
  }

  override def filter(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    transform( FILTER, calleeType, transform(callee, calleeType.asInstanceOf[SetType]), param )
  }

  override def filterNot(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    transform( FILTER_NOT, calleeType, transform(callee, calleeType.asInstanceOf[SetType]), param )
  }

  override def flatten(calleeType: MonadDslType, callee: ValueExpression): String = {
    transform( FLATTEN, calleeType, transform(callee, calleeType.asInstanceOf[SetType]) )
  }

  override def flatMap(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    transform( FLAT_MAP, calleeType, transform(callee, calleeType.asInstanceOf[SetType]), param )
  }
}
