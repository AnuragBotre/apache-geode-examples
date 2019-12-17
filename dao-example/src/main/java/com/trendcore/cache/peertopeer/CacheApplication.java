package com.trendcore.cache.peertopeer;

import com.trendcore.cache.peertopeer.models.Role;
import com.trendcore.cache.peertopeer.models.User;
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
    private Region<String, Person> personRegion;

    private final Properties cacheConfiguration;
    private Cache cache;

    private Region<Long, User> userRegion;
    public static final String USER_REGION = "User";

    private Region<Long, Role> roleRegion;
    public static final String ROLE_REGION = "Role";

    public CacheApplication(Properties cacheConfiguration) {
        this.cacheConfiguration = cacheConfiguration;
    }

    public void init(){
        CacheFactory cacheFactory = new CacheFactory(this.cacheConfiguration);
        cache = cacheFactory.create();

        createPersonRegion();
        createUserRegion();
        createRoleRegion();
    }

    private void createUserRegion() {
        RegionFactory<Long, User> regionFactory = this.cache.createRegionFactory(RegionShortcut.PARTITION);
        userRegion = regionFactory.create(USER_REGION);
    }

    private void createRoleRegion() {
        RegionFactory<Long, Role> regionFactory = this.cache.createRegionFactory(RegionShortcut.PARTITION);
        roleRegion = regionFactory.create(ROLE_REGION);
    }

    private void createPersonRegion() {
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


    public Stream<CacheServer> getCacheServersStream() {
        List<CacheServer> cacheServers = cache.getCacheServers();
        return cacheServers.stream();
    }

    public void insertPersonRecord(String firstName, String lastName) {
        Person person = createPerson(firstName, lastName);
        personRegion.put(person.getFirstName(), person);
    }

    private Person createPerson(String firstname, String lastname) {
        Person person = new Person(firstname, lastname);
        IdentifierSequence.INSTANCE.setSequentialLongId(person);
        return person;
    }

    public Stream<Person> showPersonDataForCurrentDistributedMember() {
        Region<String, Person> localData = PartitionRegionHelper.getLocalData(personRegion);
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

    public Object getRecord(String region, Object key) {
        Region<Object, Object> region1 = cache.getRegion(region);
        return region1.get(key);
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
        personRegion.putAll(transactionData);
        cacheTransactionManager.commit();
    }

    public void executeUserTransaction(String start) {
        Map<Long, User> transactionData = new HashMap();
        int id = Integer.parseInt(start);
        for (int i = 0; i < 100; i++) {
            User user = createUser("Agent" + (i + id), "");
            user.setId((long) (i+id));
            transactionData.put(user.getId(), user);
        }


        transactionData.forEach((userId, user) -> {
            CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();
            cacheTransactionManager.begin();
            userRegion.put(userId,user);
            cacheTransactionManager.commit();
        });

    }

    private User createUser(String username, String firstname) {
        User user = new User();
        user.setFirstName(username);
        user.setFirstName(firstname);
        //IdentifierSequence.INSTANCE.setSequentialLongId(user);
        return user;
    }

    public void updatingUserBatch(String start) {
        Map<Long, User> transactionData = new HashMap();
        int id = Integer.parseInt(start);
        for (int i = 0; i < 10 ; i++) {
            User user = createUser("Agent" + (i + id), "");
            user.setId((long) (i+id));
            user.setFirstName("Updated" + (i+id));
            transactionData.put(user.getId(), user);
        }

        CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();
        cacheTransactionManager.begin();
        transactionData.forEach((key, user) -> {
            User user1 = userRegion.get(key);
            DistributedMember primaryMemberForKey = PartitionRegionHelper.getPrimaryMemberForKey(userRegion, key);

        });
        cacheTransactionManager.commit();
    }
}
