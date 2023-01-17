package com.dongjiaqiang.jvm.dsl.java.api.extend;

import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._1_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._1_Supplier;

import java.util.Optional;

public class Failure<T> implements Try<T> {

    private final Throwable exception;

    public Failure(Throwable e) {
        this.exception = e;
    }


    @Override
    public Throwable getException() {
        return exception;
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public boolean failure() {
        return true;
    }

    @Override
    public <K> Try<K> map(_1_Function<? super T, ? extends K> function) {
        return new Failure<>(exception);
    }

    @Override
    public <K> Try<K> flatMap(_1_Function<? super T, Try<? extends K>> function) {
        return new Failure<>(exception);
    }

    @Override
    public Try<T> filter(_1_Predicate<? super T> predicate) {
        return this;
    }

    @Override
    public void foreach(_1_Consumer<? super T> consumer) {

    }

    @Override
    public <U> U fold(_1_Function<? super T, U> function1, _1_Function<Throwable, U> function2) throws Exception {
        return function2.apply(exception);
    }

    @Override
    public T getOrElse(_1_Supplier<? extends T> supplier) throws Exception {
        return supplier.get();
    }

    @Override
    public Try<T> orElse(_1_Supplier<Try<? extends T>> supplier) {
        return this;
    }

    @Override
    public Either<Throwable, T> toEither() {
        return new Left<>(exception);
    }

    @Override
    public T get() throws Throwable {
        throw exception;
    }

    @Override
    public Optional<T> toOption() {
        return Optional.empty();
    }


}
