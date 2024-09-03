package ua.vspelykh.splitter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ua.vspelykh.splitter.client.RoomClient;
import ua.vspelykh.splitter.dto.*;

@Service
@RequiredArgsConstructor
public class RoomReservationHandler extends ReservationHandler {

    private final RoomClient client;

    @Override
    protected ReservationType getType() {
        return ReservationType.ROOM;
    }

    @Override
    protected Flux<ReservationItemResponse> reserve(Flux<ReservationItemRequest> flux) {
        return flux.map(this::toRoomReservationRequest)
                .transform(client::reserve)
                .map(this::toResponse);
    }

    private RoomReservationRequest toRoomReservationRequest(ReservationItemRequest request) {

        return RoomReservationRequest.create(
                request.getCity(),
                request.getFrom(),
                request.getTo(),
                request.getCategory()
        );
    }

    private ReservationItemResponse toResponse(RoomReservationResponse response) {

        return ReservationItemResponse.create(
                response.getReservationId(),
                this.getType(),
                response.getCategory(),
                response.getCity(),
                response.getCheckIn(),
                response.getCheckOut(),
                response.getPrice()
        );
    }
}
