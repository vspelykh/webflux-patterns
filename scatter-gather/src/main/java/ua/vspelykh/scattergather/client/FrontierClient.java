package ua.vspelykh.scattergather.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vspelykh.scattergather.dto.FlightResult;

@Service
public class FrontierClient {

    private final WebClient webClient;

    public FrontierClient(@Value("${frontier.baseUrl}") String baseUrl) {
        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<FlightResult> getFlights(String from, String to) {
        return webClient
                .post()
                .bodyValue(FrontierRequest.create(from, to))
                .retrieve()
                .bodyToFlux(FlightResult.class)
                .onErrorResume(throwable -> Mono.empty());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "create")
    private static class FrontierRequest {

        private String from;
        private String to;
    }
}
