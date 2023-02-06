package com.dongjiaqiang.jvm.dsl.core

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.scope.{FieldScope, ProgramScope, Scope}
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._

package object scope {


  def toDslType(typeContext: TypeContext): DslType = {
    import scala.collection.convert.ImplicitConversions._
    typeContext match {
      case _: IntTypeContext ⇒ IntType
      case _: LongTypeContext ⇒ LongType
      case _: FloatTypeContext ⇒ FloatType
      case _: DoubleTypeContext ⇒ DoubleType
      case _: StringTypeContext ⇒ StringType
      case _: CharTypeContext ⇒ CharType
      case _: BoolTypeContext ⇒ BoolType
      case context: ListTypeContext ⇒
        ListType( toDslType( context.`type`( ) ) )
      case context: SetTypeContext ⇒
        SetType( toDslType( context.`type`( ) ) )
      case context: MapTypeContext ⇒
        MapType( toDslType( context.`type`( 0 ) ), toDslType( context.`type`( 1 ) ) )
      case context: TupleTypeContext ⇒
        TupleType( context.`type`( ).map( `type` ⇒ toDslType( `type` ) ).toArray )
      case context: OptionTypeContext ⇒
        OptionType( toDslType( context.`type`( ) ) )
      case context: FutureTypeContext ⇒
        FutureType( toDslType( context.`type`( ) ) )
      case context: LambdaTypeContext ⇒
        LambdaType( Some( toDslType( context.`type`( ).head ) ), toDslType( context.`type`.last ) )
      case context: SupplierTypeContext ⇒
        LambdaType( None, toDslType( context.`type`( ) ) )
      case context: ParameterizedClassTypeContext ⇒
        ClazzType( context.clazzType( ).getText, context.`type`( ).map( toDslType ).toArray )
      case context: ClassTypeContext ⇒
        if (context.clazzType( ).getText == "Unit") {
          UnitType
        } else if (context.clazzType( ).getText == "Any") {
          AnyType
        } else {
          ClazzType( context.clazzType( ).getText, Array( ) )
        }
    }
  }

  def addScope(scope: Scope, p: ParameterContext, programScope: ProgramScope, belongScope: Scope): Scope = {
    val symbolName = p.localVariable( ).IDENTIFIER( ).getText
    val dslType = toDslType( p.`type`( ) )
    scope.addScope( symbolName, new FieldScope( 0, symbolName, dslType, belongScope, programScope, false ) )
    scope.incStatement( )
    scope
  }

  def addScope(scope: Scope, ps: java.util.List[ParameterContext], belongScope: Scope, programScope: ProgramScope): Scope = {
    import scala.collection.convert.ImplicitConversionsToScala._
    ps.foreach {
      p ⇒ addScope( scope, p, programScope, belongScope )
    }
    scope
  }
}
