package com.trendcore.console.parsers;

import com.trendcore.console.commands.Command;

import java.lang.reflect.Field;

public class ArgumentParser {

    public void bindArgument(Command command, String arg) {


        try {

            String[] nameValue = arg.split("=");

            Field[] declaredFields = command.getClass().getDeclaredFields();

            for (Field field : declaredFields) {
                if (field.getName().equals(nameValue[0])) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    field.set(command, nameValue[1]);
                    field.setAccessible(accessible);
                }
            }

        } catch (IllegalAccessException e) {

        }
    }
}
