package ua.vspelykh.orchestrator.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import ua.vspelykh.orchestrator.dto.OrchestrationRequestContext;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCancellationService {

    private Sinks.Many<OrchestrationRequestContext> sink;
    private Flux<OrchestrationRequestContext> flux;

    private final List<Orchestrator> orchestrators;

    @PostConstruct
    public void init() {
        sink = Sinks.many().multicast().onBackpressureBuffer();
        flux = sink.asFlux().publishOn(Schedulers.boundedElastic());
        orchestrators.forEach(o -> flux.subscribe(o.cancel()));
    }

    public void cancelOrder(OrchestrationRequestContext ctx) {
        sink.tryEmitNext(ctx);
    }
}
