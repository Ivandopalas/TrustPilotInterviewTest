package com.inverview.test.service;

import com.inverview.test.client.TrustPilotClient;
import com.inverview.test.config.CacheConfiguration;
import com.inverview.test.config.TrustPilotServiceConfiguration;
import com.inverview.test.dto.DomainInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class TrustPilotReviewService {
    @Autowired
    private TrustPilotClient trustPilotClient;
    @Autowired
    private CacheConfiguration cacheConfiguration;
    @Autowired
    private TrustPilotServiceConfiguration trustPilotServiceConfiguration;

    @Cacheable("trust-pilot-domain-cache")
    public Mono<DomainInfo> getTrustPilotDomainReviewInfo(String domain) {
        return trustPilotClient
                .getDomainPageHtml(domain)
                .flatMap(this::parseTrustDomainInfo)
                .cache(Duration.ofMillis(cacheConfiguration.getCacheTtl()));
    }

    private Mono<DomainInfo> parseTrustDomainInfo(String html) {
        Document document = Jsoup.parse(html);
        String reviewsCount = document.select(trustPilotServiceConfiguration.getReviewParseQuery())
                .text()
                .replaceAll("[^\\d]", "");
        String rating = document.select(trustPilotServiceConfiguration.getRatingParseQuery())
                .text()
                .replaceAll("[^\\d.]", "");
        if (StringUtils.isEmpty(reviewsCount) && StringUtils.isEmpty(rating)) {
            return Mono.empty();
        }
        return Mono.just(new DomainInfo(reviewsCount, rating));
    }

    @CacheEvict(value = "trust-pilot-domain-cache", allEntries = true)
    @Scheduled(fixedRateString = CacheConfiguration.CACHE_TTL_CONFIG_KEY)
    public void emptyHotelsCache() {
    }
}
