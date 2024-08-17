package ua.vspelykh.aggregator.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.vspelykh.aggregator.dto.ProductResponse;

@Service
@RequiredArgsConstructor
public class ProductClient {

    private final WebClient productWebClient;

    public Mono<ProductResponse> getProduct(Integer productId) {

        return productWebClient.get()
                .uri("/sec01/product/{id}", productId)
                .retrieve()
                .bodyToMono(ProductResponse.class);
    }
}
