package com.trendcore.cache.inlinecaching;

import com.trendcore.cache.springboot.DaoCommandLineRunner;
import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheWriter;
import org.apache.geode.cache.GemFireCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;

import java.util.Properties;

@SpringBootApplication
public class FinancialLoanApplicationServer implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FinancialLoanApplicationServer.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }



    @Bean
    public Properties gemfireProperties() {
        Properties gemfireProperties = new Properties();

        gemfireProperties.setProperty("name", FinancialLoanApplicationServer.class.getSimpleName());
        gemfireProperties.setProperty("mcast-port", "0");
        gemfireProperties.setProperty("log-level", "warning");

        return gemfireProperties;
    }

    @Bean
    public CacheFactoryBean gemfireCache() {
        CacheFactoryBean cacheFactoryBean = new CacheFactoryBean();

        cacheFactoryBean.setProperties(gemfireProperties());

        return cacheFactoryBean;
    }

    @Bean("EligibilityDecisions")
    ReplicatedRegionFactoryBean<Object, EligibilityDecision> eligibilityDecisionsRegion(
            GemFireCache gemfireCache, CacheLoader decisionManagementSystemLoader,
            CacheWriter decisionManagementSystemWriter) {

        ReplicatedRegionFactoryBean<Object, EligibilityDecision> eligibilityDecisionsRegion =
                new ReplicatedRegionFactoryBean<>();

        eligibilityDecisionsRegion.setCache(gemfireCache);
        eligibilityDecisionsRegion.setCacheLoader(decisionManagementSystemLoader);
        eligibilityDecisionsRegion.setCacheWriter(decisionManagementSystemWriter);
        eligibilityDecisionsRegion.setClose(false);
        eligibilityDecisionsRegion.setPersistent(false);
        eligibilityDecisionsRegion.setName("EligibilityDecisions");

        return eligibilityDecisionsRegion;
    }


    @Bean
    CacheLoader<?, EligibilityDecision> decisionManagementSystemLoader() {
        return new DecisionManagementSystemLoader();
    }

    @Bean
    CacheWriter<?, EligibilityDecision> decisionManagementSystemWriter() {
        return new DecisionManagementSystemWriter();
    }

    @Autowired
    private EligibilityService eligibilityService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Run");
        System.out.println(" Eligibility Service" + eligibilityService);

        eligibilityService.getEligibilityCriteria(1);


        EligibilityDecision eligibilityDecision = new EligibilityDecision();
        eligibilityDecision.setId(11);
        eligibilityDecision.setEligibilityDecisionKey("20");
        eligibilityService.setEligibilityCriteria(eligibilityDecision);
    }
}
