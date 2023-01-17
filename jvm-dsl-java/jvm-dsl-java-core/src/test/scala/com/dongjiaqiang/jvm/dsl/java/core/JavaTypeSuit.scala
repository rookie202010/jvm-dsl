package com.dongjiaqiang.jvm.dsl.java.core

import com.dongjiaqiang.jvm.dsl.api.`type`._
import org.scalatest.funsuite.AnyFunSuite

class JavaTypeSuit extends AnyFunSuite {


  test( "basic type" ) {


    Either
    assert( toJavaType( IntType ) == "Integer" )
    assert( toJavaType( LongType ) == "Long" )
    assert( toJavaType( FloatType ) == "Float" )
    assert( toJavaType( DoubleType ) == "Double" )
    assert( toJavaType( BoolType ) == "Boolean" )
  }

  test( "collection type" ) {
    assert( toJavaType( new ListType( IntType ) ) == "java.util.ArrayList<Integer>" )
    assert( toJavaType( new ListType( new ListType( DoubleType ) ) ) == "java.util.ArrayList<java.util.ArrayList<Double>>" )
    assert( toJavaType( new MapType( IntType, new SetType( IntType ) ) ) == "java.util.HashMap<Integer,java.util.HashSet<Integer>>" )

    assert( toJavaType( new OptionType( new ClazzType( "Foo", Array( IntType, new ListType( LongType ) ) ) ) ) == "java.util.Optional<Foo<Integer,java.util.ArrayList<Long>>>" )

  }

  test( "lambda type" ) {
    assert( toJavaType( new LambdaType( None, IntType ) ) == "java.util.function.IntSupplier" )
    assert( toJavaType( new LambdaType( None, new ListType( IntType ) ) ) == "java.util.function.Supplier<java.util.ArrayList<Integer>>" )
    assert( toJavaType( new LambdaType( None, new TupleType( Array( IntType, LongType ) ) ) ) == "java.util.function.Supplier<com.dongjiaqiang.jvm.dsl.java.core.tuple.Tuple2<Integer,Long>>" )
    assert( toJavaType( new LambdaType( Some( IntType ), LongType ) ) == "java.util.function.IntToLongFunction" )
    assert( toJavaType( new LambdaType( Some( new TupleType( Array( IntType, StringType ) ) ), StringType ) ) == "java.util.function.BiFunction<Integer,String,String>" )
    assert( toJavaType( new LambdaType( Some( IntType ), UnitType ) ) == "java.util.function.IntConsumer" )
  }
}
