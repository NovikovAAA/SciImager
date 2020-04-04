package com.visualipcv.controller.binding;

@FunctionalInterface
public interface FactoryFunction <T, A> {
    T create(A arg);
}
