package com.trendcore.core.domain;

public interface Identifiable<T> {
    T getId();

    void setId(T id);
}
