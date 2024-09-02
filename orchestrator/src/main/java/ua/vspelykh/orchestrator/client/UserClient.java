package ua.vspelykh.orchestrator.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.vspelykh.orchestrator.dto.PaymentRequest;
import ua.vspelykh.orchestrator.dto.PaymentResponse;
import ua.vspelykh.orchestrator.dto.Status;

@Service
public class UserClient {

    private static final String DEDUCT = "deduct";
    private static final String REFUND = "refund";
    private static final Logger log = LoggerFactory.getLogger(UserClient.class);

    private final WebClient userWebClient;

    public UserClient(@Value("${user.baseUrl}") String baseUrl) {
        userWebClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    public Mono<PaymentResponse> deduct(PaymentRequest paymentRequest) {
        return callUserService(DEDUCT, paymentRequest);
    }

    public Mono<PaymentResponse> refund(PaymentRequest paymentRequest) {
        return callUserService(REFUND, paymentRequest);
    }

    private Mono<PaymentResponse> callUserService(String endpoint, PaymentRequest paymentRequest) {

        return userWebClient
                .post()
                .uri(endpoint)
                .bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .doOnNext(response -> log.info("Received user response: {}", response))
                .onErrorReturn(buildErrorResponse(paymentRequest));
    }

    private PaymentResponse buildErrorResponse(PaymentRequest paymentRequest) {
        return PaymentResponse.create(
                paymentRequest.getUserId(),
                null,
                paymentRequest.getAmount(),
                Status.FAILED);
    }
}
