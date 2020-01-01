package com.trendcore.cache.peertopeer;

import com.trendcore.cache.peertopeer.models.Role;
import com.trendcore.cache.peertopeer.models.User;
import com.trendcore.cache.peertopeer.service.PersonService;
import com.trendcore.cache.peertopeer.service.PersonServiceImpl;
import com.trendcore.cache.peertopeer.service.UserService;
import com.trendcore.cache.peertopeer.service.UserServiceImpl;
import com.trendcore.core.domain.Person;
import org.apache.geode.cache.*;
import org.apache.geode.cache.control.RebalanceFactory;
import org.apache.geode.cache.control.RebalanceOperation;
import org.apache.geode.cache.server.CacheServer;
import org.apache.geode.distributed.DistributedMember;

import java.util.*;
import java.util.stream.Stream;

public class CacheApplication {

    private final Properties cacheConfiguration;
    private Cache cache;


    private Region<Long, Role> roleRegion;
    public static final String ROLE_REGION = "Role";

    private PersonService personService;

    private UserService userService;

    public CacheApplication(Properties cacheConfiguration) {
        this.cacheConfiguration = cacheConfiguration;

    }

    public void init(){
        CacheFactory cacheFactory = new CacheFactory(this.cacheConfiguration);
        cache = cacheFactory.create();

        personService = new PersonServiceImpl(cache);
        personService.createPersonRegion();

        userService = new UserServiceImpl(cache);
        userService.createUserRegion();

        createRoleRegion();
    }

    private void createRoleRegion() {
        RegionFactory<Long, Role> regionFactory = this.cache.createRegionFactory(RegionShortcut.PARTITION);
        roleRegion = regionFactory.create(ROLE_REGION);
    }

    public Stream<CacheServer> getCacheServersStream() {
        List<CacheServer> cacheServers = cache.getCacheServers();
        return cacheServers.stream();
    }

    public void insertPersonRecord(String firstName, String lastName) {
        personService.insertPersonRecord(firstName,lastName);
    }


    public Stream<Person> showPersonDataForCurrentDistributedMember() {
        return personService.showPersonDataForCurrentDistributedMember();
    }

    public RebalanceOperation performRebalanceOperation(String... regionNames) {
        Set<String> regions = new HashSet<>(Arrays.asList(regionNames));
        RebalanceFactory rebalanceFactory = cache.getResourceManager()
                .createRebalanceFactory()
                .includeRegions(regions);

        return rebalanceFactory.start();
    }

    public Stream<DistributedMember> getDistributedMembers() {
        return cache.getDistributedSystem().
                getAllOtherMembers().stream();
    }

    public Object getRecord(String regionName, Object key) {
        Region<Object, Object> region1 = cache.getRegion(regionName);
        return region1.get(key);
    }

    public void executePersonTransactions(String start) {
        personService.executePersonTransactions(start);
    }

    public void executeUserTransaction(String start) {
        userService.executeUserTransaction(start);
    }

    public void updatingUserBatch(String start) {
        userService.updatingUserBatch(start);
    }

    public Stream<User> showUserDataForCurrentDistributedMember() {
        return userService.showUserDataForCurrentDistributedMember();
    }
}
