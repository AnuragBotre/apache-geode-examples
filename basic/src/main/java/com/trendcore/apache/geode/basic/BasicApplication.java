package com.trendcore.apache.geode.basic;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BasicApplication {

    public static void main(String[] args) {
        ClientCacheFactory clientCacheFactory = new ClientCacheFactory();
        clientCacheFactory.addPoolLocator("localhost",10334);

        ClientCache clientCache = clientCacheFactory.create();

        Region<String, Object> basicRegion = clientCache.<String, Object>createClientRegionFactory(ClientRegionShortcut.PROXY).create("basic");

        basicRegion.create("fe", "iron");
        Object fe = basicRegion.get("fe");

        System.out.println("Value for iron is " + fe);
    }

}
