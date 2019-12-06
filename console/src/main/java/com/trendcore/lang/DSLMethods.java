package com.trendcore.lang;

import com.trendcore.console.commands.Command;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DSLMethods {


    public static <T> void ifPresentOrElse(Optional<T> t, Consumer<T> ifPresent,Runnable runnable) {
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


    public static <T> void ifPresentOrElse(boolean expression, Runnable ifPresent,Runnable runnable) {
        if (expression) {
            ifPresent.run();
        } else {
            runnable.run();
        }
    }
}
