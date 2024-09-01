package ua.vspelykh.scattergather.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vspelykh.scattergather.dto.FlightResult;

@Service
public class DeltaClient {

    private final WebClient webClient;

    public DeltaClient(@Value("${delta.baseUrl}") String baseUrl) {
        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<FlightResult> getFlights(String from, String to) {
        return webClient
                .get()
                .uri("{from}/{to}", from, to)
                .retrieve()
                .bodyToFlux(FlightResult.class)
                .onErrorResume(throwable -> Mono.empty());
    }
}
