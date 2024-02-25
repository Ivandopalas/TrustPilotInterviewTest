package com.inverview.test.controller;

import com.inverview.test.dto.DomainInfo;
import com.inverview.test.service.TrustPilotReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerErrorException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {
    @Autowired
    private TrustPilotReviewService userService;

    @GetMapping("/{domain}")
    public Mono<ResponseEntity<DomainInfo>> getTrustPilotDomainReviewInfo(@PathVariable String domain) {
        Mono<DomainInfo> domainInfo = userService.getTrustPilotDomainReviewInfo(domain);
        return domainInfo.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> {
                    if (e instanceof WebClientResponseException.NotFound) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.error(e);
                });
    }
}
