package com.dongjiaqiang.jvm.dsl.java.api.extend;

import java.util.NoSuchElementException;

public class Right<L, R> implements Either<L, R> {


    private final R right;

    public Right(R right) {
        this.right = right;
    }

    @Override
    public L left() {
        throw new NoSuchElementException("Right has no left value");
    }

    @Override
    public R right() {
        return right;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public Left<L, R> toLeft() {
        throw new IllegalStateException("Right is not Left");
    }

    @Override
    public Right<L, R> toRight() {
        return this;
    }

}
