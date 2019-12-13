package com.trendcore.console.commands;

import com.trendcore.console.TableGenerator;
import com.trendcore.lang.DSLMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.trendcore.lang.DSLMethods.ifPresentOrElse;

public class IterableResult implements Result {

    TableGenerator tableGenerator = new TableGenerator();

    String columns[];

    Stream<List<String>> rows;

    @Override
    public void showResult() {
        List<List<String>> emptyList = new ArrayList<>();
        List<List<String>> collect = Optional.ofNullable(rows).orElse(emptyList.stream()).collect(Collectors.toList());

        System.out.println(tableGenerator.generateTable(Arrays.asList(columns), collect));
    }

    public IterableResult columns(String... columns) {
        this.columns = columns;
        return this;
    }

    public void data(Stream<List<String>> rows) {
        this.rows = rows;
    }
}
