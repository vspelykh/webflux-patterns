package ua.vspelykh.orchestrator.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ua.vspelykh.orchestrator.dto.InventoryRequest;
import ua.vspelykh.orchestrator.dto.OrchestrationRequestContext;
import ua.vspelykh.orchestrator.dto.PaymentRequest;
import ua.vspelykh.orchestrator.dto.ShippingRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrchestrationUtil {

    public static void buildRequestContext(OrchestrationRequestContext ctx) {
        buildPaymentRequest(ctx);
        buildInventoryRequest(ctx);
        buildShippingRequest(ctx);
    }

    private static void buildPaymentRequest(OrchestrationRequestContext ctx) {
        PaymentRequest paymentRequest =  PaymentRequest.create(
                ctx.getOrderRequest().getUserId(),
                ctx.getProductPrice() * ctx.getOrderRequest().getQuantity(),
                ctx.getOrderId()
        );
        ctx.setPaymentRequest(paymentRequest);
    }

    private static void buildInventoryRequest(OrchestrationRequestContext ctx) {
        InventoryRequest inventoryRequest =  InventoryRequest.create(
                ctx.getOrderId(),
                ctx.getOrderRequest().getProductId(),
                ctx.getOrderRequest().getQuantity()
        );
        ctx.setInventoryRequest(inventoryRequest);
    }

    private static void buildShippingRequest(OrchestrationRequestContext ctx) {
        ShippingRequest shippingRequest = ShippingRequest.create(
                ctx.getOrderRequest().getQuantity(),
                ctx.getOrderRequest().getUserId(),
                ctx.getOrderId()
        );
        ctx.setShippingRequest(shippingRequest);
    }
}
