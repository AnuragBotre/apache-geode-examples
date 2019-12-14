package com.trendcore.console;

import com.trendcore.console.commands.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.trendcore.lang.LangDLS.*;

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
                (args, context) -> {
                    final Result[] r = {null};
                    notNull(previousException,
                            e -> r[0] = new StackTraceResult(e),
                            () -> r[0] = new SimpleResult("No Exception present")
                    );
                    return r[0];
                }
        );


        commandsMap.put("exit", () -> new Command() {
            @Override
            public Result execute(String args, Context context1) {
                exit = true;
                return new SimpleResult("Exiting From console.");
            }

            @Override
            public String help() {
                return "exit;";
            }
        });

        commandsMap.put("commandsList", () ->
                (args, context) -> {

                    Stream<List<String>> listStream = commandsMap.entrySet()
                            .stream()
                            .map(entry -> Arrays.asList(entry.getKey(), entry.getValue().get().help()));

                    IterableResult iterableResult = new IterableResult();
                    iterableResult.columns("Command Name", "Help").data(listStream);
                    return iterableResult;
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
                            Result result = executeCommand(s, commandsSupplier);
                            result.showResult();
                        },
                        printCommandNotFound);
            });
        }, printCommandNotFound);

        return exit;
    }

    private Result executeCommand(String[] s, Supplier<Command> commandsSupplier) {

        try {
            Command command = commandsSupplier.get();

            String args = Arrays.stream(s).skip(1).collect(Collectors.joining(" "));
            return command.execute(args, context);
        } catch (Exception e) {
            previousException = e;
            return new SimpleResult("Error occurred while invoking command. Type 'showPreviousException' for showing exception");
        }
    }


    public void addCommand(String commandName, Supplier<Command> commandSupplier) {
        ifPresentOrElse(commandsMap.containsKey(commandName),
                () -> System.out.println("Command name already present."),
                () -> commandsMap.put(commandName, commandSupplier));
    }
}
