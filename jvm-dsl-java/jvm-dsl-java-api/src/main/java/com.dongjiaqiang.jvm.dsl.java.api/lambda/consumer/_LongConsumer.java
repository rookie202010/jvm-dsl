package com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer;

import java.util.Objects;

@FunctionalInterface
public interface _LongConsumer {

    /**
     * Performs this operation on the given argument.
     *
     * @param value the input argument
     */
    void accept(long value) throws Exception;

    /**
     * Returns a composed {@code LongConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code LongConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default _LongConsumer andThen(_LongConsumer after) {
        Objects.requireNonNull(after);
        return (long t) -> {
            accept(t);
            after.accept(t);
        };
    }
}