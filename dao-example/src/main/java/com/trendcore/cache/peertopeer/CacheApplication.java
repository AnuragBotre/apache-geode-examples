package com.trendcore.cache.peertopeer;

import com.trendcore.core.domain.Person;
import com.trendcore.core.lang.IdentifierSequence;
import org.apache.geode.cache.*;
import org.apache.geode.cache.control.RebalanceFactory;
import org.apache.geode.cache.control.RebalanceOperation;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.cache.server.CacheServer;
import org.apache.geode.distributed.DistributedMember;

import java.util.*;
import java.util.stream.Stream;

public class CacheApplication {

    public static final String PERSON_REGION = "Person";
    private final Properties cacheConfiguration;
    private Cache cache;

    private Region<String, Person> region;

    public CacheApplication(Properties cacheConfiguration) {
        this.cacheConfiguration = cacheConfiguration;
    }

    public void init(){
        CacheFactory cacheFactory = new CacheFactory(this.cacheConfiguration);
        cache = cacheFactory.create();

        createPersonRegion();
        createUserRegion();
    }

    private void createUserRegion() {

    }

    private void createPersonRegion() {
        RegionFactory<String, Person> regionFactory = this.cache.createRegionFactory(RegionShortcut.PARTITION);

        /**
         * In case of Partition region partition resolver is required for bulk insert in case of transaction.
         */
        PartitionResolver resolver = new StandardPartitionResolver();

        PartitionAttributesFactory partitionAttributesFactory = new PartitionAttributesFactory();
        //partitionAttributesFactory.setRedundantCopies(2);
        partitionAttributesFactory.setPartitionResolver(resolver);
        PartitionAttributes partitionAttributes = partitionAttributesFactory.create();

        //regionFactory.setEvictionAttributes(EvictionAttributes.createLRUEntryAttributes(200));

        region = regionFactory.setPartitionAttributes(partitionAttributes).create(PERSON_REGION);
    }


    public Stream<CacheServer> getCacheServersStream() {
        List<CacheServer> cacheServers = cache.getCacheServers();
        return cacheServers.stream();
    }

    public void insertPersonRecord(String firstName, String lastName) {
        Person person = createPerson(firstName, lastName);
        region.put(person.getFirstName(), person);
    }

    private Person createPerson(String firstname, String lastname) {
        Person person = new Person(firstname, lastname);
        IdentifierSequence.INSTANCE.setSequentialLongId(person);
        return person;
    }

    public Stream<Person> showPersonDataForCurrentDistributedMember() {
        Region<String, Person> localData = PartitionRegionHelper.getLocalData(region);
        return localData.values().stream();
    }

    public RebalanceOperation performRebalanceOperation(String... regionNames) {
        Set<String> regions = new HashSet<>(Arrays.asList());
        RebalanceFactory rebalanceFactory = cache.getResourceManager()
                .createRebalanceFactory()
                .includeRegions(regions);

        return rebalanceFactory.start();
    }

    public Stream<DistributedMember> getDistributedMembers() {
        return cache.getDistributedSystem().
                getAllOtherMembers().stream();
    }

    public Person getPersonRecord(String firstName) {
        return region.get(firstName);
    }

    public void executePersonTransactions(String start) {

        Map<String, Person> transactionData = new HashMap();
        int id = Integer.parseInt(start);
        for (int i = 0; i < 100; i++) {
            Person person = createPerson("Agent" + (i + id), "");
            transactionData.put(person.getFirstName(), person);
        }

        CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();
        cacheTransactionManager.begin();
        region.putAll(transactionData);
        cacheTransactionManager.commit();
    }
}
