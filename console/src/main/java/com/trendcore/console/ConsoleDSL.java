package com.trendcore.console;

import com.trendcore.console.commands.IterableResult;

import java.util.List;
import java.util.stream.Stream;

public class ConsoleDSL {

    public static IterableResult iterableResults(Stream<List<String>> listStream, String... columns) {
        IterableResult iterableResult = new IterableResult();
        iterableResult.data(listStream);
        iterableResult.columns(columns);
        return iterableResult;
    }

}
