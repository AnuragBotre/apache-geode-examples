package com.trendcore.cache.peertopeer;

import com.trendcore.core.domain.Person;
import com.trendcore.core.lang.IdentifierSequence;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionFactory;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.cache.server.CacheServer;

import java.util.List;
import java.util.Scanner;

public class CacheFeatures {


    public void utility(Cache cache) {

        Scanner scanner = new Scanner(System.in);

        RegionFactory<String, Person> regionFactory = cache.createRegionFactory(RegionShortcut.REPLICATE_PROXY);
        Region<String, Person> region = regionFactory.create("Person");

        boolean flag = true;
        while (flag) {
            int options = scanner.nextInt();

            switch (options) {
                case 1:
                    System.out.println("Cache Servers");
                    List<CacheServer> cacheServers = cache.getCacheServers();
                    cacheServers.stream().forEach(System.out::println);
                    break;
                case 2: {
                    String firstName = scanner.next();
                    String lastName = scanner.next();
                    Person person = createPerson(firstName, lastName);
                    region.put(person.getFirstName(), person);
                }
                break;
                case 3: {
                    String firstName = scanner.next();
                    Person person = region.get(firstName);
                    System.out.println(person);
                }
                break;
                case 0:
                    flag = false;
                    break;
            }
        }
    }

    private Person createPerson(String firstname, String lastname) {
        Person person = new Person(firstname, lastname);
        IdentifierSequence.INSTANCE.setSequentialLongId(person);
        return person;
    }

}
