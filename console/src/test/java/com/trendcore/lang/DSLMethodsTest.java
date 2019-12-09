package com.trendcore.lang;

import org.junit.Test;

import static com.trendcore.lang.DSLMethodsTest.IfElse.ifTrue;
import static com.trendcore.lang.DSLMethodsTest.later.later;
import static com.trendcore.lang.DSLMethodsTest.orElse.orElse;
import static com.trendcore.lang.DSLMethodsTest.then.then;

public class DSLMethodsTest {

    @Test
    public void testIfElse() {

        IfElse ifElse = ifTrue(11 < 10,
                then(() -> System.out.println("1 is less than 10")),
                orElse(() -> System.out.println("10 is less than 11"))
        );


        eager(ifElse);

        later later = later(ifElse);
        later.execute();

    }

    public static void eager(ExpressionEvaluator expressionEvaluator) {
        expressionEvaluator.execute();
    }

    interface ExpressionEvaluator {
        void execute();
    }

    static class then implements ExpressionEvaluator {

        private Runnable runnable;

        public static then then(Runnable runnable) {
            then then = new then(runnable);
            return then;
        }

        public then(Runnable runnable) {
            this.runnable = runnable;
        }

        public void execute() {
            runnable.run();
        }
    }

    static class orElse implements ExpressionEvaluator {

        private Runnable runnable;

        public static orElse orElse(Runnable runnable) {
            orElse orElse = new orElse(runnable);
            return orElse;
        }

        public orElse(Runnable runnable) {
            this.runnable = runnable;
        }

        public void execute() {
            runnable.run();
        }
    }


    public static class IfElse implements ExpressionEvaluator {

        boolean b;
        then then;
        orElse orElse;

        public static IfElse ifTrue(boolean b) {
            IfElse ifElse = new IfElse();
            ifElse.b = b;
            return ifElse;
        }


        public static IfElse ifTrue(boolean b, then then, orElse orElse) {
            IfElse ifElse = new IfElse();
            ifElse.b = b;
            ifElse.then = then;
            ifElse.orElse = orElse;

            return ifElse;
        }

        public void execute() {
            if (b) {
                then.execute();
            } else {
                orElse.execute();
            }
        }

    }


    public static class later implements ExpressionEvaluator {

        ExpressionEvaluator expression;


        public static later later(ExpressionEvaluator expression){
            later later = new later();
            later.expression = expression;
            return later;
        }

        @Override
        public void execute() {
            this.expression.execute();
        }
    }
}