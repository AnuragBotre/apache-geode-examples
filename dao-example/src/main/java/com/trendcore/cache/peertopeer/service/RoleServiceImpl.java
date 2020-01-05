package com.trendcore.cache.peertopeer.service;

import com.trendcore.cache.peertopeer.models.Role;
import com.trendcore.cache.peertopeer.models.User;
import com.trendcore.core.lang.IdentifierSequence;
import org.apache.geode.cache.*;
import org.apache.geode.cache.partition.PartitionRegionHelper;

import java.util.stream.Stream;

public class RoleServiceImpl implements RoleService {

    private Region<Long, Role> roleRegion;
    public static final String ROLE_REGION = "Role";


    private final Cache cache;

    public RoleServiceImpl(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void createRoleRegion() {
        RegionFactory<Long, Role> regionFactory = this.cache.createRegionFactory(RegionShortcut.PARTITION);
        roleRegion = regionFactory.create(ROLE_REGION);
    }

    @Override
    public void insertRole(Role role) {
        CacheTransactionManager cacheTransactionManager = cache.getCacheTransactionManager();
        try {
            cacheTransactionManager.setDistributed(true);
            cacheTransactionManager.begin();
            roleRegion.put(role.getId(), role);
            cacheTransactionManager.commit();
        } catch (Exception e) {
            cacheTransactionManager.rollback();
        }
    }

    @Override
    public Stream<Role> showRoleDataForCurrentDistributedMember() {
        Region<Long, Role> localData = PartitionRegionHelper.getLocalData(roleRegion);
        return localData.values().stream();
    }
}
