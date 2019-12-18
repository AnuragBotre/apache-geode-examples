package com.trendcore.cache.peertopeer.service;

import com.trendcore.cache.peertopeer.StandardPartitionResolver;
import com.trendcore.core.domain.Person;
import com.trendcore.core.lang.IdentifierSequence;
import org.apache.geode.cache.*;
import org.apache.geode.cache.partition.PartitionRegionHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class PersonServiceImpl implements PersonService {

    private Cache cache;

    public static final String PERSON_REGION = "Person";
    private Region<String, Person> personRegion;

    public PersonServiceImpl(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void createPersonRegion() {
        RegionFactory<String, Person> regionFactory = this.cache.createRegionFactory(RegionShortcut.PARTITION);

        /**
         * In case of Partition personRegion partition resolver is required for bulk insert in case of transaction.
         */
        PartitionResolver resolver = new StandardPartitionResolver();

        PartitionAttributesFactory partitionAttributesFactory = new PartitionAttributesFactory();
        //partitionAttributesFactory.setRedundantCopies(2);
        partitionAttributesFactory.setPartitionResolver(resolver);
        PartitionAttributes partitionAttributes = partitionAttributesFactory.create();

        //regionFactory.setEvictionAttributes(EvictionAttributes.createLRUEntryAttributes(200));

        personRegion = regionFactory.setPartitionAttributes(partitionAttributes).create(PERSON_REGION);
    }

    @Override
    public void insertPersonRecord(String firstName, String lastName) {
        Person person = createPerson(firstName, lastName);
        CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();
        try {
            cacheTransactionManager.begin();
            personRegion.put(person.getFirstName(), person);
            cacheTransactionManager.commit();
        } catch (Exception e) {
            cacheTransactionManager.rollback();
        }
    }

    private Person createPerson(String firstname, String lastname) {
        Person person = new Person(firstname, lastname);
        IdentifierSequence.INSTANCE.setSequentialLongId(person);
        return person;
    }

    @Override
    public Stream<Person> showPersonDataForCurrentDistributedMember() {
        Region<String, Person> localData = PartitionRegionHelper.getLocalData(personRegion);
        return localData.values().stream();
    }

    @Override
    public void executePersonTransactions(String start) {
        Map<String, Person> transactionData = new HashMap();
        int id = Integer.parseInt(start);
        for (int i = 0; i < 100; i++) {
            Person person = createPerson("Agent" + (i + id), "");
            transactionData.put(person.getFirstName(), person);
        }
        CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();
        try {
            cacheTransactionManager.begin();
            personRegion.putAll(transactionData);
            cacheTransactionManager.commit();
        } catch (Exception e) {
            cacheTransactionManager.rollback();
        }
    }
}
