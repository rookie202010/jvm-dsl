package com.dongjiaqiang.jvm.dsl.java.api.extend;

import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._1_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._2_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._1_Supplier;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple2;

import java.util.*;
import java.util.concurrent.CompletableFuture;

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
    public <K> Try<K> flatMap(_1_Function<? super T, Try<K>> function) {
        return new Failure<>(exception);
    }

    @Override
    public Try<T> filter(_1_Predicate<? super T> predicate) {
        return this;
    }

    @Override
    public boolean exist(_1_Predicate<? super T> predicate) throws Exception {
        return false;
    }

    @Override
    public Optional<T> find(_1_Predicate<? super T> predicate) throws Exception {
        return Optional.empty();
    }

    @Override
    public void foreach(_1_Consumer<? super T> consumer) {

    }

    @Override
    public boolean contains(Try<T> tTry, T t) {
        return false;
    }

    @Override
    public List<Tuple2<T, Integer>> zipWithIndex() {
        return new ArrayList<>();
    }

    @Override
    public T reduce(_2_Function<? super T, ? super T, T> reducer) {
       throw new UnsupportedOperationException("empty reduce");
    }

    @Override
    public <U> U fold(_1_Function<? super T, U> function1, _1_Function<Throwable, U> function2) throws Exception {
        return function2.apply(exception);
    }

    @Override
    public T getOrElse(_1_Supplier<T> supplier) throws Exception {
        return supplier.get();
    }

    @Override
    public T orElse(T t) {
        return t;
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

    @Override
    public List<T> toList() {
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] toArray() {
        return (T[]) new Object[]{};
    }

    @Override
    public Set<T> toSet() {
        return new HashSet<>();
    }

    @Override
    public Set<T> toSeqSet() {
        return new LinkedHashSet<>();
    }

    @Override
    public Set<T> toSortedSet(Try<T> tTry, Comparator<T> comparator) {
        return new TreeSet<>(comparator);
    }
}
