package ua.vspelykh.aggregator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${sec01.baseUrl}")
    private String baseUrl;

    @Bean
    public WebClient productWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
