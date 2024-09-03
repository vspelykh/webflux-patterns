package ua.vspelykh.splitter.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vspelykh.splitter.dto.RoomReservationRequest;
import ua.vspelykh.splitter.dto.RoomReservationResponse;

@Service
public class RoomClient {

    private final WebClient client;

    public RoomClient(@Value("${room.baseUrl}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<RoomReservationResponse> reserve(Flux<RoomReservationRequest> flux) {
        return client
                .post()
                .body(flux, RoomReservationRequest.class)
                .retrieve()
                .bodyToFlux(RoomReservationResponse.class)
                .onErrorResume(throwable -> Mono.empty());
    }
}
