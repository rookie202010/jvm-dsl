package com.dongjiaqiang.jvm.dsl.java.api.tuple;

import java.util.Objects;

public class Tuple6<T1, T2, T3, T4, T5, T6> {

    public T1 _1;
    public T2 _2;

    public T3 _3;

    public T4 _4;

    public T5 _5;

    public T6 _6;

    public Tuple6(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
        this._5 = _5;
        this._6 = _6;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tuple6<?, ?, ?, ?, ?, ?> tuple6 = (Tuple6<?, ?, ?, ?, ?, ?>) o;
        return Objects.equals(_1, tuple6._1) && Objects.equals(_2, tuple6._2) && Objects.equals(_3, tuple6._3) && Objects.equals(_4, tuple6._4) && Objects.equals(_5, tuple6._5) && Objects.equals(_6, tuple6._6);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4, _5, _6);
    }
}
