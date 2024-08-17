package ua.vspelykh.aggregator.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    private Integer id;
    private String user;
    private Integer rating;
    private String comment;
}
