package ua.vspelykh.orchestrator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.vspelykh.orchestrator.client.UserClient;
import ua.vspelykh.orchestrator.dto.OrchestrationRequestContext;
import ua.vspelykh.orchestrator.dto.Status;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class PaymentOrchestrator extends Orchestrator {

    private final UserClient userClient;

    @Override
    public Mono<OrchestrationRequestContext> create(OrchestrationRequestContext ctx) {
        return userClient.deduct(ctx.getPaymentRequest())
                .doOnNext(ctx::setPaymentResponse)
                .thenReturn(ctx);
    }

    @Override
    public Predicate<OrchestrationRequestContext> isSuccess() {
        return ctx -> Status.SUCCESS.equals(ctx.getPaymentResponse().getStatus());
    }

    @Override
    public Consumer<OrchestrationRequestContext> cancel() {
        return ctx -> Mono.just(ctx)
                .filter(isSuccess())
                .map(OrchestrationRequestContext::getPaymentRequest)
                .flatMap(userClient::refund)
                .subscribe();
    }
}
