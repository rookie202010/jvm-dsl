package com.dongjiaqiang.jvm.dsl.java.core.lambda.consumer;

@FunctionalInterface
public interface _ObjLongConsumer<T> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t     the first input argument
     * @param value the second input argument
     */
    void accept(T t, long value) throws Exception;
}