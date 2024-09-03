package ua.vspelykh.splitter.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vspelykh.splitter.dto.CarReservationRequest;
import ua.vspelykh.splitter.dto.CarReservationResponse;

@Service
public class CarClient {

    private final WebClient client;

    public CarClient(@Value("${car.baseUrl}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<CarReservationResponse> reserve(Flux<CarReservationRequest> flux) {
        return client
                .post()
                .body(flux, CarReservationRequest.class)
                .retrieve()
                .bodyToFlux(CarReservationResponse.class)
                .onErrorResume(throwable -> Mono.empty());
    }
}
