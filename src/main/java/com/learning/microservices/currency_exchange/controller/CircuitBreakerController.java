package com.learning.microservices.currency_exchange.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

    private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api")
    // @Retry(name="sample-api", fallbackMethod = "sendHardCodedResponse")
    @CircuitBreaker(name = "default", fallbackMethod = "sendHardCodedResponse")
    @RateLimiter(name="default")
    //10s => 10000calls to sample api (example)
    @Bulkhead(name = "sample-api")
    public String sampleApi(){
        logger.info("Sample API call receieved");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-notworking/",String.class);
        return forEntity.getBody();
    }

    public String sendHardCodedResponse(Exception ex){
        return "Hard coded response";
    }
}
