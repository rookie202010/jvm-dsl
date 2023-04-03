package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, NoneType}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

object NoneLiteral extends Literal[None.type, NoneType.type ]( None ) {

  override def toString: String = s"${dslType.name}"


  override val dslType: NoneType.type = NoneType

  override def equals(obj: Any): Boolean = {
    obj == NoneLiteral
  }

  override def getValueType(programScope: ProgramScope): NoneType.type = dslType
}
