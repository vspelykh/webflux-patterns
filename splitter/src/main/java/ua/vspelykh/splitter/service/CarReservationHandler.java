package ua.vspelykh.splitter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ua.vspelykh.splitter.client.CarClient;
import ua.vspelykh.splitter.dto.*;

@Service
@RequiredArgsConstructor
public class CarReservationHandler extends ReservationHandler {

    private final CarClient client;

    @Override
    protected ReservationType getType() {
        return ReservationType.CAR;
    }

    @Override
    protected Flux<ReservationItemResponse> reserve(Flux<ReservationItemRequest> flux) {
        return flux.map(this::toCarReservationRequest)
                .transform(client::reserve)
                .map(this::toResponse);
    }

    private CarReservationRequest toCarReservationRequest(ReservationItemRequest request) {

        return CarReservationRequest.create(
                request.getCity(),
                request.getFrom(),
                request.getTo(),
                request.getCategory()
        );
    }

    private ReservationItemResponse toResponse(CarReservationResponse response) {

        return ReservationItemResponse.create(
                response.getReservationId(),
                this.getType(),
                response.getCategory(),
                response.getCity(),
                response.getPickup(),
                response.getDrop(),
                response.getPrice()
        );
    }
}
