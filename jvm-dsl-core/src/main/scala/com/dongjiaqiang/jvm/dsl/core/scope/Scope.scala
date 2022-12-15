package com.dongjiaqiang.jvm.dsl.core.scope

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.ParameterContext
import com.dongjiaqiang.jvm.dsl.core.`type`.DslType
import com.dongjiaqiang.jvm.dsl.core.exception.SymbolParseException

trait Scope {

  /**
   * 作用域内语句数
   */
  var statements:Int = 0

  def incStatement():Scope={
      statements=statements+1
      this
  }

  def incStatement(i:Int):Scope={
      statements=statements+i
      this
  }

  /**
   * resolve ref in current or outer scope
   * @param index ref index
   * @param refs ref names
   */
  def resolve(index:Int, refs:List[String]):Resolved.Value

  /**
   * 外部作用域索引值
   *
   * index of outer scope
   */
  val outerScopeIndex:Int

  /**
   * 在该作用域内查找给定符号代表的符号类型 类型包括  字段  类 方法
   *
   * @param symbolName symbol name
   * @return the type of symbol
   */
  def getSymbolType(symbolName: String): SymbolType.Value


  def duplicateSymbol(symbolName: String): Unit = {
    val symbolType = getSymbolType( symbolName )
    if (symbolType != SymbolType.UNDEFINED) {
      throw SymbolParseException( f"duplication symbol $symbolName exists type $symbolType in scope $toString" )
    }
  }

  /**
   * 增加字段作用域
   * @param symbolName symbol name
   * @param fieldScope field scope
   */
  def addScope(symbolName:String,fieldScope: FieldScope):Scope = {
    this
  }

  /**
   * 增加类作用域
   * @param symbolName symbol name
   * @param clazzScope class scope
   */
  def addScope(symbolName:String,clazzScope: ClazzScope):Scope={
    this
  }

  /**
   * 增加函数作用域
   * @param symbolName symbol name
   * @param methodScope method scope
   */
  def addScope(symbolName:String,methodScope: MethodScope):Scope={
    this
  }

  /**
   * 增加块作用域
   * @param blockScope block scope
   */
  def addScope(blockScope: BlockScope):Scope={
    this
  }


  def addScope(p: ParameterContext): Scope = {
    val symbolName = p.localVariable( ).IDENTIFIER( ).getText
    val dslType = DslType.unapply( p.`type`( ) )
    addScope( symbolName, new FieldScope( 0,symbolName, dslType, false ) )
    incStatement()
    this
  }

}
