package ua.vspelykh.aggregator.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionResponse {

    private Integer id;
    private String type;
    private Double discount;
    private LocalDate endDate;
}
