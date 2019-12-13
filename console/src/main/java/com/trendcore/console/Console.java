package com.trendcore.console;

import com.trendcore.console.commands.Command;
import com.trendcore.console.commands.Context;
import com.trendcore.console.commands.Let;
import com.trendcore.console.commands.Put;
import com.trendcore.lang.DSLMethods;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.trendcore.lang.DSLMethods.*;

public class Console {

    Map<String, Supplier<Command>> commandsMap = new HashMap<>();

    /**
     * Stores variable names and its value
     */
    Context context = new Context();

    Exception previousException;

    private boolean exit;
    private TableGenerator tableGenerator = new TableGenerator();

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
                return "exit;";
            }
        });

        commandsMap.put("commandsList", () ->
                (args, context) -> {
                    List<String> headers = Arrays.asList("Command Name", "Help");

                    List<List<String>> collect = commandsMap.entrySet()
                                        .stream()
                                        .map(entry -> Arrays.asList(entry.getKey(), entry.getValue().get().help()))
                                        .collect(Collectors.toList());

                    System.out.println(tableGenerator.generateTable(headers, collect));
                }


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
        System.out.println("Type commandsList; for available commands. All commands should end with ';'");
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

        Runnable printCommandNotFound = () -> System.out.println("Command not found.");

        ifPresentOrElse(Arrays.stream(query.split(";")).findFirst(), refinedQuery -> {
            String[] s = refinedQuery.split(" ");

            when(s.length >= 1, () -> {
                String command = s[0];
                ifPresentOrElse(Optional.ofNullable(commandsMap.get(command)),
                        commandsSupplier -> {
                            System.out.println("Command :- " + command);
                            executeCommand(s, commandsSupplier);
                        },
                        printCommandNotFound);
            });
        }, printCommandNotFound);

        return exit;
    }

    private void executeCommand(String[] s, Supplier<Command> commandsSupplier) {

        try {
            Command command = commandsSupplier.get();

            String args = Arrays.stream(s).skip(1).collect(Collectors.joining(" "));
            command.execute(args, context);
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
