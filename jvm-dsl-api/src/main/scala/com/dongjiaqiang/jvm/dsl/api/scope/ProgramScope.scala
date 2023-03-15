package com.dongjiaqiang.jvm.dsl.api.scope

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.`type`.mrt.MultiMrt
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}
import com.dongjiaqiang.jvm.dsl.api.scope

import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}

class ProgramScope(val fields: MutableMap[String, FieldScope],
                   val classes: MutableMap[String, ClazzScope],
                   val methods: MutableMap[String, MethodScope],
                   val importManager: ImportManager,
                   val lambdaScopes: ArrayBuffer[BlockScope] = ArrayBuffer( ),
                   val customExprTypeResolver:MutableMap[String,Expression⇒DslType] = MutableMap()
                  ) extends Scope  {

  val multiMrt: MultiMrt = new MultiMrt(this)

  

  def callType(valueExpression:ValueExpression,name:String,params:Array[ValueExpression]):DslType={
    multiMrt.visit( valueExpression.getValueType( this ),valueExpression, name, params ) match {
      case Some( dslType ) ⇒ dslType
      case None ⇒ throw ExpressionParseException( s"method $name in expression $valueExpression doest not accept " +
        s"params ${params.map( _.getValueType( this ).toString ).mkString( "Array(", ", ", ")" )}" )
    }
  }

  override def toString:String = {
    val classStr =  if(classes.isEmpty) "" else "classes:\n  "+classes.toList.map(_._2.toString).mkString("\n")
    val fieldStr = if(fields.isEmpty) "" else "fields:\n  "+fields.toList.map(_._2.toString).mkString("\n")
    val methodStr = if(methods.isEmpty) "" else "methods:\n  "+methods.toList.map(_._2.toString).mkString("\n")
    val lambdaBlockStr = if(lambdaScopes.isEmpty) "" else "lambdaBlocks:\n  "+lambdaScopes.mkString("\n")
    s"""
      ProgramScope:
      statements $statements
      $fieldStr
      $classStr
      $methodStr
      $lambdaBlockStr
      """
  }

  override val outerScopeIndex: Int = 0

  def this() {
    this( MutableMap( ), MutableMap( ), MutableMap( ),new ImportManager() )
  }

  def isImportClazz(name:String):Boolean={
      importManager.containClass(name)
  }

  override def addScope(symbolName: String, fieldScope: FieldScope): ProgramScope = {
    duplicateSymbol( symbolName )
    fields.put( symbolName, fieldScope )
    this
  }

  override def addScope(symbolName: String, clazzScope: ClazzScope): ProgramScope = {
    duplicateSymbol( symbolName )
    classes.put( symbolName, clazzScope )
    this
  }


  override def addScope(symbolName: String, methodScope: MethodScope): ProgramScope = {
    duplicateSymbol( symbolName )
    methods.put( symbolName, methodScope )
    this
  }

  override def getSymbolType(symbolName: String): SymbolType.Value = {
    if (fields.contains( symbolName )) {
      SymbolType.FIELD
    } else if (classes.contains( symbolName )) {
      SymbolType.CLAZZ
    } else if (methods.contains( symbolName )) {
      SymbolType.METHOD
    } else {
      SymbolType.UNDEFINED
    }
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case programScope: ProgramScope ⇒
        (fields sameElements programScope.fields) &&
          (classes sameElements programScope.classes) &&
          (methods sameElements programScope.methods) &&
        statements == programScope.statements
    }

  /**
   * resolve var refs in program (defined fields)
   */
  override def resolveVarRefs(index: Int, refs: List[String], arrayRefsIndex:Set[Int]): Option[FieldScope] = {
    scope.resolveVarRefs(index,refs,arrayRefsIndex,this, fields,skipCurrentScope = false,backRef = true,None)
  }

  /**
   * resolve method in current scope
   */
  override def resolveMethod(name: String): Option[MethodScope] = { methods.get(name)}
}
