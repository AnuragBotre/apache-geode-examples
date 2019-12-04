package com.trendcore.console;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

public class Console {

    Map<String, Supplier<Command>> commandsMap = new HashMap<>();

    public static void main(String[] args) {

        Console console = new Console();
        console.commandsMap.put("put", () -> new PutCommand());

        boolean isExit = false;
        Scanner scanner = new Scanner(System.in);
        while (!isExit) {
            System.out.print("console>");

            StringBuilder queryBuilder = new StringBuilder();

            boolean flag = true;
            while (flag) {
                String next = scanner.next();
                if (next.endsWith(";")) {
                    queryBuilder.append(next);
                    break;
                } else {
                    queryBuilder.append(next);
                    queryBuilder.append(" ");
                }
            }

            String query = queryBuilder.toString();
            System.out.println(query);
            isExit = console.runQuery(query);
        }
    }

    private boolean runQuery(String query) {
        String[] s = query.split(" ");
        if (s.length > 1) {
            String s1 = s[0];
            Supplier<Command> commandsSupplier = commandsMap.get(s1);
            System.out.println("Command :- " + s1);
            if (commandsSupplier == null) {
                System.out.println("Command not found.");
            } else {
                Command command = commandsSupplier.get();
                for (int i = 1; i < s.length; i++) {
                    String arg = s[i];

                    bindArgument(command, arg);
                }
                command.execute();
            }
        }
        return false;
    }

    Map<Class, Map<String, Field>> cacheFields = new HashMap<>();

    private void bindArgument(Command command, String arg) {


        try {

            String[] nameValue = arg.split("=");

            Field[] declaredFields = command.getClass().getDeclaredFields();

            for (Field field : declaredFields) {
                if (field.getName().equals(nameValue[0])) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    field.set(command,nameValue[1]);
                    field.setAccessible(accessible);
                }
            }

        } catch (IllegalAccessException e) {

        }
    }


}
