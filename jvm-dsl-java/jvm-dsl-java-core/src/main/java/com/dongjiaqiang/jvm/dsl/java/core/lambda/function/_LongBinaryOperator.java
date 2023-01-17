package com.dongjiaqiang.jvm.dsl.java.core.lambda.function;

@FunctionalInterface
public interface _LongBinaryOperator {

    /**
     * Applies this operator to the given operands.
     *
     * @param left  the first operand
     * @param right the second operand
     * @return the operator result
     */
    long applyAsLong(long left, long right) throws Exception;
}