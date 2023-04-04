package com.dongjiaqiang.jvm.dsl.core

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.scope.{FieldScope, ProgramScope, Scope}
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._

package object scope {


  def toDslType(typeContext: TypeContext,programScope: ProgramScope): DslType = {
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
        ListType( toDslType( context.`type`( ),programScope ) )
      case context: SetTypeContext ⇒
        SetType( toDslType( context.`type`( ),programScope ) )
      case context: MapTypeContext ⇒
        MapType( toDslType( context.`type`( 0 ),programScope ), toDslType( context.`type`( 1 ),programScope ) )
      case context: TupleTypeContext ⇒
        TupleType( context.`type`( ).map( `type` ⇒ toDslType( `type`,programScope ) ).toArray )
      case context: OptionTypeContext ⇒
        new OptionType( toDslType( context.`type`( ) ,programScope) )
      case context: FutureTypeContext ⇒
        FutureType( toDslType( context.`type`( ) ,programScope) )
      case context: LambdaTypeContext ⇒
        LambdaType( Some( toDslType( context.`type`( ).head,programScope ) ), toDslType( context.`type`.last,programScope ) )
      case context: SupplierTypeContext ⇒
        LambdaType( None, toDslType( context.`type`( ),programScope ) )
      case context: ParameterizedClassTypeContext ⇒
        val clazzName = context.clazzType().getText
        clazzName match {
          case "Array" ⇒ ArrayType(toDslType(context.`type`().head,programScope))
          case "Some"⇒SomeType(toDslType(context.`type`().head,programScope))
          case "Left"⇒
            if(context.`type`.size()==1) {
              LeftType( leftParameterType = toDslType( context.`type`( ).head, programScope ) )
            }else{
              LeftType( leftParameterType = toDslType( context.`type`( ).head, programScope ),
                rightParameterType = toDslType( context.`type`( ).last, programScope ) )
            }
          case "Right"⇒
            if(context.`type`.size()==1) {
              RightType( rightParameterType = toDslType( context.`type`( ).head, programScope ) )
            }else{
              RightType( leftParameterType = toDslType( context.`type`( ).head, programScope ),
                rightParameterType = toDslType( context.`type`( ).last, programScope ) )
            }
          case "None"⇒NoneType
          case _⇒  new ClazzType( clazzName, context.`type`( ).map( t⇒toDslType(t,programScope) ).toArray )
        }
      case context: ClassTypeContext ⇒
        if (context.clazzType( ).getText == "Unit") {
          UnitType
        } else if (context.clazzType( ).getText == "Any") {
          AnyType
        } else {
          if(programScope.classes.contains(context.clazzType().getText)){
            DefinitionClazzType(context.clazzType().getText,programScope.classes(context.clazzType().getText))
          }else {
            new ClazzType( context.clazzType( ).getText, Array( ) )
          }
        }
    }
  }

  def addScope(scope: Scope, p: ParameterContext, programScope: ProgramScope, belongScope: Scope): Scope = {
    val symbolName = p.localVariable( ).IDENTIFIER( ).getText
    val dslType = toDslType( p.`type`( ),programScope )
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
