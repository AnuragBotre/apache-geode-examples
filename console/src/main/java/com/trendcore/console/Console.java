package com.trendcore.console;

import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.PutCommand;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Supplier;

import static com.trendcore.lang.DSLMethods.ifTrue;
import static com.trendcore.lang.DSLMethods.ifPresentOrElse;

public class Console {

    Map<String, Supplier<Command>> commandsMap = new HashMap<>();

    /**
     * Stores variable names and its value
     */
    Map<String, Object> context = new HashMap<>();


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

        ifTrue(s.length > 1, () -> {
            String command = s[0];
            ifPresentOrElse(Optional.of(commandsMap.get(command)),
                    commandsSupplier -> {
                        System.out.println("Command :- " + command);
                        executeCommand(s, commandsSupplier);
                    },
                    () -> System.out.println("Command not found.")
            );
        });

        return false;
    }

    private void executeCommand(String[] s, Supplier<Command> commandsSupplier) {
        Command command = commandsSupplier.get();

        String args = "";
        for (int i = 1; i < s.length; i++) {
            String arg = s[i];
            args = args + arg;
        }
        command.execute(args, context);
    }


    private void bindArgument(Command command, String arg) {


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
