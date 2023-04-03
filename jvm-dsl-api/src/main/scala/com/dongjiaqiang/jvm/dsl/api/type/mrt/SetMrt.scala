package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.SetMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, MonadDslType, SetType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class SetMrt(override val programScope: ProgramScope) extends MonadMrt with SeqMrt with SetMethodVisitor[DslType]
