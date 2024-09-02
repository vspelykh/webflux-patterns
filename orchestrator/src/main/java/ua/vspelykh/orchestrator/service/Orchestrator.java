package ua.vspelykh.orchestrator.service;

import reactor.core.publisher.Mono;
import ua.vspelykh.orchestrator.dto.OrchestrationRequestContext;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class Orchestrator {

    public abstract Mono<OrchestrationRequestContext> create(OrchestrationRequestContext ctx);

    public abstract Predicate<OrchestrationRequestContext> isSuccess();

    public abstract Consumer<OrchestrationRequestContext> cancel();
}
