package com.trendcore.cache.inlinecaching;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class EligibilityService {


    @Cacheable("EligibilityDecisions")
    public EligibilityDecision getEligibilityCriteria(Integer id) {
        System.out.println("getEligibilityCriteria");
        EligibilityDecision eligibilityDecision = new EligibilityDecision();
        eligibilityDecision.setId(100);
        eligibilityDecision.setEligibilityDecisionKey("100");
        return eligibilityDecision;
    }

    @Cacheable("EligibilityDecisions")
    public void setEligibilityCriteria(EligibilityDecision eligibilityDecision) {
        System.out.println("setEligibilityCriteria " +eligibilityDecision.getId());
    }
}
