package com.trendcore.cache.peertopeer;

import com.trendcore.core.domain.Person;
import org.apache.geode.cache.EntryOperation;
import org.apache.geode.cache.PartitionResolver;

public class StandardPartitionResolver implements PartitionResolver<String, Person> {

    @Override
    public Object getRoutingObject(EntryOperation<String, Person> opDetails) {
        return "1";
    }

    @Override
    public String getName() {
        return getClass().getName();
    }
}
