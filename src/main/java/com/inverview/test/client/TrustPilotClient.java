package com.inverview.test.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class TrustPilotClient {
    @Autowired
    private WebClient client;

    public Mono<String> getDomainPageHtml(String domain) {
        return client.get()
                .uri("/{domain}", domain)
                .retrieve()
                .bodyToMono(String.class)
                .log(domain + " domain fetched.");
    }
}
