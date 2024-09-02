package ua.vspelykh.orchestrator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.vspelykh.orchestrator.dto.OrchestrationRequestContext;
import ua.vspelykh.orchestrator.dto.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentService {

    private final List<Orchestrator> orchestrators;

    public Mono<OrchestrationRequestContext> placeOrder(OrchestrationRequestContext ctx) {

        var list = orchestrators.stream().map(o -> o.create(ctx)).toList();
        return Mono.zip(list, a -> a[0])
                .cast(OrchestrationRequestContext.class)
                .doOnNext(this::updateStatus);
    }

    private void updateStatus(OrchestrationRequestContext ctx) {
        boolean allSuccess = orchestrators.stream().allMatch(o -> o.isSuccess().test(ctx));
        ctx.setStatus(allSuccess ? Status.SUCCESS : Status.FAILED);
    }
}
