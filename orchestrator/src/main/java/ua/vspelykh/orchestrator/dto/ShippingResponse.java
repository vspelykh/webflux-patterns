package ua.vspelykh.orchestrator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ShippingResponse {

    private UUID orderID;
    private Integer quantity;
    private Status status;
    private Address shippingAddress;
    private String expectedDelivery;
}
