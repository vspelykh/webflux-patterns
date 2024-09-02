package ua.vspelykh.orchestrator.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.vspelykh.orchestrator.dto.Product;

@Service
public class ProductClient {

    private final WebClient productWebClient;

    public ProductClient(@Value("${product.baseUrl}") String baseUrl) {
        productWebClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    public Mono<Product> getProduct(Integer productId) {

        return productWebClient.get()
                .uri("{id}", productId)
                .retrieve()
                .bodyToMono(Product.class);
    }
}