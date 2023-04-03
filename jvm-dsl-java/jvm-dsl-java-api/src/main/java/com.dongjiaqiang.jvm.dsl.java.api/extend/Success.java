package com.dongjiaqiang.jvm.dsl.java.api.extend;

import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._1_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._2_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._1_Supplier;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple2;

import java.util.*;

public final class Success<T> implements Try<T> {

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
    public <K> Try<K> flatMap(_1_Function<? super T, Try<K>> function) {
        try {
            return function.apply(value);
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    @Override
    public boolean exist(_1_Predicate<? super T> predicate) throws Exception {
        return predicate.test(value);
    }

    @Override
    public Optional<T> find(_1_Predicate<? super T> predicate) throws Exception {
        if(predicate.test(value)){
            return toOption();
        }else {
            return Optional.empty();
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
    public boolean contains(Try<T> tTry, T t) {
        return value.equals(t);
    }

    @Override
    public List<Tuple2<T, Integer>> zipWithIndex() {
        List<Tuple2<T,Integer>> list = new ArrayList<>();
        list.add(new Tuple2<>(value,0));
        return list;
    }

    @Override
    public T reduce(_2_Function<? super T, ? super T, T> reducer) {
        return value;
    }

    @Override
    public <U> U fold(_1_Function<? super T, U> function1, _1_Function<Throwable, U> function2) throws Exception {
        return function1.apply(value);
    }

    @Override
    public T getOrElse(_1_Supplier<T> supplier) {
        return value;
    }

    @Override
    public T orElse(T t) throws Exception {
        return value;
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

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        list.add(value);
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] toArray() {
        Object[] arr = new Object[1];
        arr[0] = value;
        return (T[]) arr;
    }

    @Override
    public Set<T> toSet() {
        Set<T> set = new HashSet<>();
        set.add(value);
        return set;
    }

    @Override
    public Set<T> toSeqSet() {
        Set<T> set = new LinkedHashSet<>();
        set.add(value);
        return set;
    }

    @Override
    public Set<T> toSortedSet(Try<T> tTry, Comparator<T> comparator) {
        Set<T> set = new TreeSet<>(comparator);
        set.add(value);
        return set;
    }
}
