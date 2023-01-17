package com.dongjiaqiang.jvm.dsl.java.api.extend;

import java.util.NoSuchElementException;

public class Left<L, R> implements Either<L, R> {

    private final L left;

    public Left(L left) {
        this.left = left;
    }

    @Override
    public L left() {
        return left;
    }

    @Override
    public R right() {
        throw new NoSuchElementException("Left has no right value");
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public Left<L, R> toLeft() {
        return this;
    }

    @Override
    public Right<L, R> toRight() {
        throw new IllegalStateException("Left is not Right");
    }


}
