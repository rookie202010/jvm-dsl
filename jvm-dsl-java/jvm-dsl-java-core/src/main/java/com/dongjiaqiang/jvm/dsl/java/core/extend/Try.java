package com.dongjiaqiang.jvm.dsl.java.core.extend;

import com.dongjiaqiang.jvm.dsl.java.core.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.core.lambda.function._1_Function;
import com.dongjiaqiang.jvm.dsl.java.core.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.core.lambda.supplier._1_Supplier;

import java.util.Optional;

public interface Try<T> {

    boolean success();

    boolean failure();

    <K> Try<K> map(_1_Function<? super T, ? extends K> function);

    <K> Try<K> flatMap(_1_Function<? super T, Try<? extends K>> function);


    Try<T> filter(_1_Predicate<? super T> predicate) throws Exception;


    void foreach(_1_Consumer<? super T> consumer) throws Exception;


    <U> U fold(_1_Function<? super T, U> function1, _1_Function<Throwable, U> function2) throws Exception;

    Optional<T> toOption();


    T getOrElse(_1_Supplier<? extends T> supplier) throws Exception;

    Try<T> orElse(_1_Supplier<Try<? extends T>> supplier) throws Exception;

    Either<Throwable, T> toEither();

    T get() throws Throwable;

    static <T> Try<T> apply(_1_Supplier<? extends T> supplier) {

        try {
            return new Success<>(supplier.get());
        } catch (VirtualMachineError | ThreadDeath | InterruptedException | LinkageError e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            return new Failure<>(e);
        }
    }

}

