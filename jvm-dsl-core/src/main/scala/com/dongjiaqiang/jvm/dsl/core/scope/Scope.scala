package com.dongjiaqiang.jvm.dsl.core.scope

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.ParameterContext
import com.dongjiaqiang.jvm.dsl.core.`type`.DslType
import com.dongjiaqiang.jvm.dsl.core.symbol.SymbolParseException

import scala.collection.mutable.ArrayBuffer

trait Scope {

  /**
   * 作用域内语句数
   */
  var statements:Int = 0

  def incStatement():Unit={
      statements=statements+1
  }

  def incStatement(i:Int):Unit={
      statements=statements+i
  }

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
  def addScope(symbolName:String,fieldScope: FieldScope):Unit = {}

  /**
   * 增加类作用域
   * @param symbolName symbol name
   * @param clazzScope class scope
   */
  def addScope(symbolName:String,clazzScope: ClazzScope):Unit={}

  /**
   * 增加函数作用域
   * @param symbolName symbol name
   * @param methodScope method scope
   */
  def addScope(symbolName:String,methodScope: MethodScope):Unit={}

  /**
   * 增加块作用域
   * @param blockScope block scope
   */
  def addScope(blockScope: BlockScope):Unit={}


  def addScope(p: ParameterContext): Unit = {
    val symbolName = p.localVariable( ).IDENTIFIER( ).getText
    val dslType = DslType.unapply( p.`type`( ) )
    addScope( symbolName, new FieldScope( 0,symbolName, dslType, false ) )
    incStatement()
  }

}
