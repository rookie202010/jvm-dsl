package com.dongjiaqiang.jvm.dsl.api.scope

import com.dongjiaqiang.jvm.dsl.api.exception.SymbolParseException

trait Scope {

  /**
   * 作用域内语句数
   */
  var statements: Int = 0

  def incStatement(): Scope = {
    statements = statements + 1
    this
  }

  def incStatement(i: Int): Scope = {
    statements = statements + i
    this
  }

  /**
   * resolve ref in current or outer scope
   *
   * @param index ref index
   * @param arrayRefsIndex array refs index
   * @param refs  ref names
   */
  def resolveVarRefs(index: Int, refs: List[String], arrayRefsIndex:Set[Int]): Option[FieldScope]

  /**
   * resolve method in current scope
   */
  def resolveMethod(name: String): Option[MethodScope] = {
    throw new UnsupportedOperationException( s"can not resolve method $name in current scope" )
  }

  /**
   * 外部作用域索引值
   *
   * index of outer scope
   */
  val outerScopeIndex: Int

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
   *
   * @param symbolName symbol name
   * @param fieldScope field scope
   */
  def addScope(symbolName: String, fieldScope: FieldScope): Scope = {
    this
  }

  /**
   * 增加类作用域
   *
   * @param symbolName symbol name
   * @param clazzScope class scope
   */
  def addScope(symbolName: String, clazzScope: ClazzScope): Scope = {
    this
  }

  /**
   * 增加函数作用域
   *
   * @param symbolName  symbol name
   * @param methodScope method scope
   */
  def addScope(symbolName: String, methodScope: MethodScope): Scope = {
    this
  }

  /**
   * 增加块作用域
   *
   * @param blockScope block scope
   */
  def addScope(blockScope: BlockScope): Scope = {
    this
  }
}
