package com.dongjiaqiang.jvm.dsl.java.core.lambda.consumer;

@FunctionalInterface
public interface _ObjIntConsumer<T> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t     the first input argument
     * @param value the second input argument
     */
    void accept(T t, int value) throws Exception;
}