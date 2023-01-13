package com.dongjiaqiang.jvm.dsl.java.core.lambda.function;

@FunctionalInterface
public interface _IntBinaryOperator {

    /**
     * Applies this operator to the given operands.
     *
     * @param left  the first operand
     * @param right the second operand
     * @return the operator result
     */
    int applyAsInt(int left, int right) throws Exception;
}