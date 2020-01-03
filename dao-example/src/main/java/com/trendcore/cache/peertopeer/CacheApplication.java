package com.trendcore.cache.peertopeer;

import com.trendcore.cache.peertopeer.models.Role;
import com.trendcore.cache.peertopeer.models.User;
import com.trendcore.cache.peertopeer.service.*;
import com.trendcore.core.domain.Person;
import com.trendcore.core.lang.IdentifierSequence;
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

    public static Object EMPTY_OBJECT = new Object();

    private PersonService personService;

    private UserService userService;

    private RoleService roleService;

    public CacheApplication(Properties cacheConfiguration) {
        this.cacheConfiguration = cacheConfiguration;

    }

    public void init() {
        CacheFactory cacheFactory = new CacheFactory(this.cacheConfiguration);
        cache = cacheFactory.create();

        personService = new PersonServiceImpl(cache);
        personService.createPersonRegion();

        userService = new UserServiceImpl(cache);
        userService.createUserRegion();

        roleService = new RoleServiceImpl(cache);
        roleService.createRoleRegion();
    }

    public Stream<CacheServer> getCacheServersStream() {
        List<CacheServer> cacheServers = cache.getCacheServers();
        return cacheServers.stream();
    }

    public void insertPersonRecord(String firstName, String lastName) {
        personService.insertPersonRecord(firstName, lastName);
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

    public User insertUser(String userName, String firstName, String lastName) {
        User user = new User();
        IdentifierSequence.INSTANCE.setSequentialLongId(user);
        user.setUsername(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userService.insertUser(user);
        return user;
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

    public Role insertRole(String roleName) {
        Role role = new Role();
        IdentifierSequence.INSTANCE.setSequentialLongId(role);
        role.setRoleName(roleName);
        roleService.insertRole(role);
        return role;
    }

    public Stream<Role> showRoleDataForCurrentDistributedMember() {
        return roleService.showRoleDataForCurrentDistributedMember();
    }

    public void attachRoleToUser(Long userId, Long roleId) {
        this.userService.attachRoleToUser(userId,roleId);
    }

    public Stream<User> getAllUsers() {
        return this.userService.getAllUsers();
    }
}
