package ua.vspelykh.scattergather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ua.vspelykh.scattergather.client.DeltaClient;
import ua.vspelykh.scattergather.client.FrontierClient;
import ua.vspelykh.scattergather.client.JetBlueClient;
import ua.vspelykh.scattergather.dto.FlightResult;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class FlightSearchService {

    private final DeltaClient deltaClient;
    private final FrontierClient frontierClient;
    private final JetBlueClient jetBlueClient;

    public Flux<FlightResult> getFlights(String from, String to) {
        return Flux.merge(
                deltaClient.getFlights(from, to),
                frontierClient.getFlights(from, to),
                jetBlueClient.getFlights(from, to)
        ).take(Duration.ofSeconds(3));
    }

}
