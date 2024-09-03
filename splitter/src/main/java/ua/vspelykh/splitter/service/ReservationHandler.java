package ua.vspelykh.splitter.service;

import reactor.core.publisher.Flux;
import ua.vspelykh.splitter.dto.ReservationItemRequest;
import ua.vspelykh.splitter.dto.ReservationItemResponse;
import ua.vspelykh.splitter.dto.ReservationType;

public abstract class ReservationHandler {

    protected abstract ReservationType getType();

    protected abstract Flux<ReservationItemResponse> reserve(Flux<ReservationItemRequest> flux);
}
