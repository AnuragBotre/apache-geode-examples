package com.trendcore.console;

import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Let;
import com.trendcore.console.commands.Put;

import java.util.*;
import java.util.function.Supplier;

import static com.trendcore.lang.DSLMethods.*;

public class Console {

    Map<String, Supplier<Command>> commandsMap = new HashMap<>();

    /**
     * Stores variable names and its value
     */
    Context context = new Context();

    Exception previousException;

    private boolean exit;

    public Console() {


        commandsMap.put("showPreviousException", () ->
                (args, context) ->
                        notNull(previousException,
                                e -> e.printStackTrace(),
                                () -> System.out.println("No Exception present")
                        )
        );


        commandsMap.put("exit", () -> new Command() {
            @Override
            public void execute(String args, Context context1) {
                exit = true;
            }

            @Override
            public String help() {
                return "Exit from console";
            }
        });

        commandsMap.put("commandsList", () ->
                (args, context) ->
                        commandsMap.forEach((s, commandSupplier) ->
                                System.out.println(s + " " + commandSupplier.get().help())
                        )
        );

    }

    public static void main(String[] args) {

        Console console = new Console();
        console.commandsMap.put("put", () -> new Put());
        console.commandsMap.put("let", () -> new Let());

        console.start();
    }

    public void start() {


        boolean isExit = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type commandsList for available commands");
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
            isExit = runQuery(query);
        }
    }


    boolean runQuery(String query) {

        ifPresentOrElse(Arrays.stream(query.split(";")).findFirst(),refinedQuery -> {
            String[] s = refinedQuery.split(" ");

            when(s.length >= 1, () -> {
                String command = s[0];
                ifPresentOrElse(Optional.ofNullable(commandsMap.get(command)),
                        commandsSupplier -> {
                            System.out.println("Command :- " + command);
                            executeCommand(s, commandsSupplier);
                        },
                        () -> System.out.println("Command not found.")
                );
            });
        },() -> System.out.println("Command not found."));



        return exit;
    }

    private void executeCommand(String[] s, Supplier<Command> commandsSupplier) {

        try {
            Command command = commandsSupplier.get();

            StringBuilder args = new StringBuilder();
            when(s.length > 1 , () -> {
                for (int i = 1; i < s.length; i++) {
                    String arg = s[i];
                    args.append(arg);
                }
            });

            command.execute(args.toString(), context);
        } catch (Exception e) {
            previousException = e;
            System.out.println("Error occurred while invoking command. Type 'showPreviousException' for showing exception");
        }
    }




    public void addCommand(String commandName, Supplier<Command> commandSupplier) {
        ifPresentOrElse(commandsMap.containsKey(commandName),
                () -> System.out.println("Command name already present."),
                () -> commandsMap.put(commandName, commandSupplier));
    }
}
