package com.trendcore.console.parsers;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ArgumentParser {

    public void bindArgument(Object object, String args) {

        Arrays.stream(args.split(" ")).forEach(arg -> {

            String[] nameValue = arg.split("=");
            Field[] declaredFields = object.getClass().getDeclaredFields();

            Arrays.stream(declaredFields)
                    .filter(field -> field.getName().equals(nameValue[0]))
                    .forEach(field -> setValue(object, nameValue[1], field));
        });


    }

    private void setValue(Object object, String value, Field field) {
        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            field.set(object, value);
            field.setAccessible(accessible);
        } catch (IllegalAccessException e) {

        }
    }
}
