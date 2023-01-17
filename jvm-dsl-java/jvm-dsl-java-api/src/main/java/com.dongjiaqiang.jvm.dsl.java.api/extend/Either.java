package com.dongjiaqiang.jvm.dsl.java.api.extend;

import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._1_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._1_Supplier;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public interface Either<L, R> {

    L left();

    R right();

    boolean isLeft();

    boolean isRight();

    Left<L, R> toLeft();

    Right<L, R> toRight();

    default <NR> Either<L, NR> map(_1_Function<? super R, ? extends NR> function) throws Exception {
        if (isRight()) {
            return new Right<>(function.apply(right()));
        } else {
            return new Left<>(left());
        }
    }

    default <NL> Either<NL, R> mapLeft(_1_Function<? super L, ? extends NL> function) throws Exception {
        if (isLeft()) {
            return new Left<>(function.apply(left()));
        } else {
            return new Right<>(right());
        }
    }

    default <NR> Either<L, NR> flatMap(_1_Function<? super R, Either<? extends L, ? extends NR>> function) throws Exception {
        if (isRight()) {
            return (Either<L, NR>) function.apply(right());
        } else {
            return new Left<>(left());
        }
    }

    default <NL, NR> Either<NL, NR> biMap(_1_Function<? super R, ? extends NR> functionR, _1_Function<? super L, ? extends NL> functionL) throws Exception {
        if (isLeft()) {
            return new Left<>(functionL.apply(left()));
        } else {
            return new Right<>(functionR.apply(right()));
        }
    }

    default <U> U fold(_1_Function<? super R, ? extends U> functionR, _1_Function<? super L, ? extends U> functionL) throws Exception {
        if (isLeft()) {
            return functionL.apply(left());
        } else {
            return functionR.apply(right());
        }
    }

    default void foreach(_1_Consumer<? super R> consumer) throws Exception {
        if (isRight()) {
            consumer.accept(right());
        }
    }


    default R getOrElse(_1_Supplier<? extends R> supplier) throws Exception {
        if (isRight()) {
            return right();
        } else {
            return supplier.get();
        }
    }

    default R orElse(R defaultR) {
        if (isRight()) {
            return right();
        } else {
            return defaultR;
        }
    }

    default boolean contains(R r) {
        if (isRight()) {
            return r.equals(right());
        } else {
            return false;
        }
    }

    default Optional<R> toOption() {
        if (isRight()) {
            return Optional.ofNullable(right());
        } else {
            return Optional.empty();
        }
    }

    default Collection<R> toSeq() {
        if (isRight()) {
            return Collections.singletonList(right());
        } else {
            return Collections.emptyList();
        }
    }

    default Either<R, L> swap() {
        if (isRight()) {
            return new Left<>(right());
        } else {
            return new Right<>(left());
        }
    }

    default Try<R> toTry() {
        if (isRight()) {
            return new Success<>(right());
        } else {
            return new Failure<>((Throwable) left());
        }
    }

}
