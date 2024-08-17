package ua.vspelykh.aggregator.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
public class ProductAggregate {

    private Integer id;
    private String category;
    private String description;
    private Price price;
    private List<Review> reviews;
}
