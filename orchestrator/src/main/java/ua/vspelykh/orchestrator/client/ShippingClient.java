package ua.vspelykh.orchestrator.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.vspelykh.orchestrator.dto.*;

@Service
@Slf4j
public class ShippingClient {

    private static final String SCHEDULE = "schedule";
    private static final String CANCEL = "cancel";

    private final WebClient shippingWebClient;

    public ShippingClient(@Value("${shipping.baseUrl}") String baseUrl) {
        shippingWebClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<ShippingResponse> schedule(ShippingRequest request) {
        return callShippingService(SCHEDULE, request);
    }

    public Mono<ShippingResponse> cancel(ShippingRequest request) {
        return callShippingService(CANCEL, request);
    }

    private Mono<ShippingResponse> callShippingService(String endpoint, ShippingRequest request) {

        return shippingWebClient
                .post()
                .uri(endpoint)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShippingResponse.class)
                .doOnNext(response -> log.info("Received shipping response: {}", response))
                .onErrorReturn(buildErrorResponse(request));
    }

    private ShippingResponse buildErrorResponse(ShippingRequest request) {
        return ShippingResponse.create(
                request.getOrderId(),
                request.getQuantity(),
                Status.FAILED,
                null,
                null
        );
    }
}
