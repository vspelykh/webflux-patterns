package ua.vspelykh.aggregator.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.vspelykh.aggregator.dto.PromotionResponse;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PromotionClient {

    private final PromotionResponse noPromotionResponse = PromotionResponse.builder()
            .id(-1)
            .type("no type")
            .discount(0.0)
            .endDate(LocalDate.now())
            .build();

    private final WebClient productWebClient;

    public Mono<PromotionResponse> getPromotion(Integer id) {
        return productWebClient.get()
                .uri("/sec01/promotion/{id}", id)
                .retrieve()
                .bodyToMono(PromotionResponse.class)
                .onErrorReturn(noPromotionResponse);
    }
}
