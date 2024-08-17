package ua.vspelykh.aggregator.client;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.vspelykh.aggregator.dto.Review;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewClient {

    private final WebClient productWebClient;

    public Mono<List<Review>> getReviews(Integer productId) {

        return productWebClient.get()
                .uri("/sec01/review/{id}", productId)
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .onErrorReturn(Collections.emptyList());
    }
}
