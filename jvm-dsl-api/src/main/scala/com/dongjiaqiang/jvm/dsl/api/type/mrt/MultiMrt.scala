package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor._
import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.{MethodScope, ProgramScope}

class DefinitionClazzMrt(override val programScope: ProgramScope) extends DefinitionClazzMethodVisitor[DslType]{
  override def visit(methodScope: MethodScope, calleeDslType: DefinitionClazzType, callee: ValueExpression, name: String, params: Array[ValueExpression]): DslType = methodScope.returnType
}
class ClazzMrt(override val programScope: ProgramScope) extends ClazzMethodVisitor[DslType]{
  override def visit(importClazzType: ImportClazzType, importClazzMethod: ImportClazzMethod, callee: ValueExpression, name: String, params: Array[ValueExpression]): DslType = importClazzMethod.returnType
}

class IntMrt(override val programScope: ProgramScope) extends ConvertMrt with RangeMrt with IntMethodVisitor[DslType]
class FloatMrt(override val programScope: ProgramScope) extends ConvertMrt with FloatMethodVisitor[DslType]
class ByteMrt(override val programScope: ProgramScope) extends ConvertMrt with ByteMethodVisitor[DslType]
class CharMrt(override val programScope: ProgramScope) extends ConvertMrt with CharMethodVisitor[DslType]{
  override def toLowerCase(callee: ValueExpression): DslType = CharType
  override def toUpperCase(callee: ValueExpression): DslType = CharType
}

class ListMrt(override val programScope: ProgramScope) extends MonadMrt with SeqMrt with ListMethodVisitor[DslType]

class ArrayMrt(override val programScope: ProgramScope) extends MonadMrt with SeqMrt with ArrayMethodVisitor[DslType]

class MultiMrt(override val programScope: ProgramScope) extends MultiMethodVisitor[DslType]{

  {
    //basic types
    val intMrt = new IntMrt(programScope)
    registerSysVisitor(IntType.getClass.asSubclass(classOf[DslType]),intMrt)
    registerSysVisitor(LongType.getClass.asSubclass(classOf[DslType]),intMrt)
    val floatMrt = new FloatMrt(programScope)
    registerSysVisitor(FloatType.getClass.asSubclass(classOf[DslType]),floatMrt)
    registerSysVisitor(DoubleType.getClass.asSubclass(classOf[DslType]),floatMrt)
    val stringMrt = new StringMrt(programScope)
    registerSysVisitor(StringType.getClass.asSubclass(classOf[DslType]),stringMrt)
    registerSysVisitor(ByteType.getClass.asSubclass(classOf[DslType]),new ByteMrt(programScope))
    registerSysVisitor(CharType.getClass.asSubclass(classOf[DslType]),new CharMrt(programScope))

    //seq types
    registerSysVisitor(classOf[ListType],new ListMrt(programScope))
    registerSysVisitor(classOf[SetType],new SetMrt(programScope))
    registerSysVisitor(classOf[MapType],new MapMrt(programScope))
    registerSysVisitor(classOf[ArrayType],new ArrayMrt(programScope))

    //lambda type
    registerSysVisitor(classOf[LambdaType],new LambdaMrt(programScope))

    //clazz type
    registerSysVisitor(classOf[DefinitionClazzType],new DefinitionClazzMrt(programScope))
    registerSysVisitor(classOf[ClazzType],new ClazzMrt(programScope))

    //try type
    val tryMrt = new TryMrt(programScope)
    registerSysVisitor(classOf[TryType],tryMrt)
    registerSysVisitor(classOf[SuccessType],tryMrt)
    registerSysVisitor(classOf[FailureType],tryMrt)

    //option type
    val optionMrt = new OptionMrt(programScope)
    registerSysVisitor(classOf[OptionType],optionMrt)
    registerSysVisitor(classOf[SomeType],optionMrt)
    registerSysVisitor(NoneType.getClass.asSubclass(classOf[DslType]),optionMrt)

    //either type
    val eitherMrt = new EitherMrt(programScope)
    registerSysVisitor(classOf[EitherType],eitherMrt)
    registerSysVisitor(classOf[LeftType],eitherMrt)
    registerSysVisitor(classOf[RightType],eitherMrt)

    //future type
    registerSysVisitor(classOf[FutureType],eitherMrt)

  }

  override val anyVisitor: AnyMethodVisitor[DslType] = new AnyMrt(programScope)
}
