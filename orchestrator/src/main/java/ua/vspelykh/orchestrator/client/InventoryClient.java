package ua.vspelykh.orchestrator.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.vspelykh.orchestrator.dto.InventoryRequest;
import ua.vspelykh.orchestrator.dto.InventoryResponse;
import ua.vspelykh.orchestrator.dto.Status;

@Service
@Slf4j
public class InventoryClient {

    private static final String DEDUCT = "deduct";
    private static final String RESTORE = "restore";

    private final WebClient inventoryWebClient;

    public InventoryClient(@Value("${inventory.baseUrl}") String baseUrl) {
        inventoryWebClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<InventoryResponse> deduct(InventoryRequest inventoryRequest) {
        return callService(DEDUCT, inventoryRequest);
    }

    public Mono<InventoryResponse> restore(InventoryRequest inventoryRequest) {
        return callService(RESTORE, inventoryRequest);
    }

    private Mono<InventoryResponse> callService(String endpoint, InventoryRequest inventoryRequest) {

        return inventoryWebClient
                .post()
                .uri(endpoint)
                .bodyValue(inventoryRequest)
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .doOnNext(response -> log.info("Received inventory response: {}", response))
                .onErrorReturn(buildErrorResponse(inventoryRequest));
    }

    private InventoryResponse buildErrorResponse(InventoryRequest request) {
        return InventoryResponse.create(
                request.getProductId(),
                request.getQuantity(),
                null,
                Status.FAILED
        );
    }
}
