package com.trendcore.cache.peertopeer.service;

import com.trendcore.core.domain.Person;

import java.util.stream.Stream;

public interface PersonService {

    void createPersonRegion();

    void insertPersonRecord(String firstName, String lastName);

    Stream<Person> showPersonDataForCurrentDistributedMember();

    void executePersonTransactions(String start);
}
