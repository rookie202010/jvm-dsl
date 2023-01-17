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
        new ListType( toDslType( context.`type`( ) ) )
      //      case context: ArrayTypeContext ⇒
      //       new ArrayType( unapply( context ) )
      case context: SetTypeContext ⇒
        new SetType( toDslType( context.`type`( ) ) )
      case context: MapTypeContext ⇒
        new MapType( toDslType( context.`type`( 0 ) ), toDslType( context.`type`( 1 ) ) )
      case context: TupleTypeContext ⇒
        new TupleType( context.`type`( ).map( `type` ⇒ toDslType( `type` ) ).toArray )
      case context: OptionTypeContext ⇒
        new OptionType( toDslType( context.`type`( ) ) )
      case context: FutureTypeContext ⇒
        new FutureType( toDslType( context.`type`( ) ) )
      case context: LambdaTypeContext ⇒
        new LambdaType( Some( toDslType( context.`type`( ).head ) ), toDslType( context.`type`.last ) )
      case context: SupplierTypeContext ⇒
        new LambdaType( None, toDslType( context.`type`( ) ) )
      case context: ParameterizedClassTypeContext ⇒
        new ClazzType( context.clazzType( ).IDENTIFIER( ).getText, context.`type`( ).map( toDslType ).toArray )
      case context: ClassTypeContext ⇒
        if (context.clazzType( ).IDENTIFIER( ).getText == "Unit") {
          UnitType
        } else if (context.clazzType( ).IDENTIFIER( ).getText == "Any") {
          AnyType
        } else {
          new ClazzType( context.clazzType( ).IDENTIFIER( ).getText, Array( ) )
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
