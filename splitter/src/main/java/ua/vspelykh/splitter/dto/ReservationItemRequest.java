package ua.vspelykh.splitter.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationItemRequest {

    private ReservationType type;
    private String category;
    private String city;
    private LocalDate from;
    private LocalDate to;
}
