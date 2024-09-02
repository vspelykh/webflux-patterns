package ua.vspelykh.orchestrator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.vspelykh.orchestrator.client.ShippingClient;
import ua.vspelykh.orchestrator.dto.OrchestrationRequestContext;
import ua.vspelykh.orchestrator.dto.Status;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class ShippingOrchestrator extends Orchestrator {

    private final ShippingClient client;

    @Override
    public Mono<OrchestrationRequestContext> create(OrchestrationRequestContext ctx) {
        return client.schedule(ctx.getShippingRequest())
                .doOnNext(ctx::setShippingResponse)
                .thenReturn(ctx);
    }

    @Override
    public Predicate<OrchestrationRequestContext> isSuccess() {
        return ctx -> Status.SUCCESS.equals(ctx.getShippingResponse().getStatus());
    }

    @Override
    public Consumer<OrchestrationRequestContext> cancel() {
        return ctx -> Mono.just(ctx)
                .filter(isSuccess())
                .map(OrchestrationRequestContext::getShippingRequest)
                .flatMap(client::cancel)
                .subscribe();
    }
}
