package ua.vspelykh.scattergather.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vspelykh.scattergather.dto.FlightResult;

@Service
public class JetBlueClient {

    private static final String JETBLUE_AIRLINE = "Jet Blue";

    private final WebClient webClient;

    public JetBlueClient(@Value("${jetblue.baseUrl}") String baseUrl) {
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
                .doOnNext(flight -> normalizeResult(flight, from, to))
                .onErrorResume(throwable -> Mono.empty());
    }

    private void normalizeResult(FlightResult result, String from, String to) {
        result.setFrom(from);
        result.setTo(to);
        result.setAirline(JETBLUE_AIRLINE);
    }
}
