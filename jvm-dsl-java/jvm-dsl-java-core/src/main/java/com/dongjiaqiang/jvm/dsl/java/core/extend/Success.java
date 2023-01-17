package com.dongjiaqiang.jvm.dsl.java.core.extend;

import com.dongjiaqiang.jvm.dsl.java.core.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.core.lambda.function._1_Function;
import com.dongjiaqiang.jvm.dsl.java.core.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.core.lambda.supplier._1_Supplier;

import java.util.NoSuchElementException;
import java.util.Optional;

public class Success<T> implements Try<T> {

    private final T value;

    public Success(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean success() {
        return true;
    }

    @Override
    public boolean failure() {
        return false;
    }

    @Override
    public <K> Try<K> map(_1_Function<? super T, ? extends K> function) {
        return Try.apply(() ->
                function.apply(value)
        );
    }

    @Override
    public <K> Try<K> flatMap(_1_Function<? super T, Try<? extends K>> function) {
        try {
            return (Try<K>) function.apply(value);
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    @Override
    public Try<T> filter(_1_Predicate<? super T> predicate) throws Exception {
        if (predicate.test(value)) {
            return this;
        } else {
            return new Failure<>(new NoSuchElementException("Predicate does not hold for " + value));
        }
    }

    @Override
    public Optional<T> toOption() {
        return Optional.ofNullable(value);
    }

    @Override
    public void foreach(_1_Consumer<? super T> consumer) throws Exception {
        consumer.accept(value);
    }

    @Override
    public <U> U fold(_1_Function<? super T, U> function1, _1_Function<Throwable, U> function2) throws Exception {
        return function1.apply(value);
    }

    @Override
    public T getOrElse(_1_Supplier<? extends T> supplier) {
        return value;
    }

    @Override
    public Try<T> orElse(_1_Supplier<Try<? extends T>> supplier) throws Exception {
        return (Try<T>) supplier.get();
    }

    @Override
    public Throwable getException() {
        throw new IllegalStateException("no failure!");
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public Either<Throwable, T> toEither() {
        return new Right<>(value);
    }


}
