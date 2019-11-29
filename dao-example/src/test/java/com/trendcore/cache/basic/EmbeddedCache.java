package com.trendcore.cache.basic;

import com.trendcore.core.domain.Person;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionFactory;
import org.apache.geode.cache.server.CacheServer;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedCache {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void createCacheWithRegion() {
        CacheFactory cacheFactory = new CacheFactory();
        Cache cache = cacheFactory.create();

        RegionFactory<Object, Object> regionFactory = cache.createRegionFactory();
        Region<Object, Object> region = regionFactory.create("People");

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("Anurag");
        person.setLastName("B");
        region.put("1",person);

        Object o = region.get("1");
        System.out.println(" Value from cache :- " + o);

    }
}
