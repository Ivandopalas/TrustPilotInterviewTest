package com.inverview.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrustPilotServiceConfiguration {
    @Value("${app.trustpilotservice.reviewparsequery}")
    private String reviewParseQuery;
    @Value("${app.trustpilotservice.ratingparsequery}")
    private String ratingParseQuery;

    public String getReviewParseQuery() {
        return reviewParseQuery;
    }

    public String getRatingParseQuery() {
        return ratingParseQuery;
    }
}
