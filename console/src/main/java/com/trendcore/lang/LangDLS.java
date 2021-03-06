package com.trendcore.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class LangDLS {


    public static <T> void ifPresentOrElse(Optional<T> t, Consumer<T> ifPresent, Runnable runnable) {
        if (t.isPresent()) {
            T t1 = t.get();
            ifPresent.accept(t1);
        } else {
            runnable.run();
        }
    }

    public static <T> void when(boolean expression, Runnable ifPresent) {
        if (expression) {
            ifPresent.run();
        }
    }


    public static <T> void ifPresentOrElse(boolean expression, Runnable ifPresent, Runnable runnable) {
        if (expression) {
            ifPresent.run();
        } else {
            runnable.run();
        }
    }

    public static <T> void notNull(T t, Consumer<T> consumer) {
        if (t != null) {
            consumer.accept(t);
        }
    }

    public static <T> void isTrue(boolean expression, Runnable ifPresent,Runnable orElse) {
        ifPresentOrElse(expression,ifPresent,orElse);
    }

    public static <T> void notNull(T t, Consumer<T> consumer, Runnable runnable) {
        if (t != null) {
            consumer.accept(t);
        } else {
            runnable.run();
        }
    }
}
