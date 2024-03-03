package co.uk.negura.workshop_customer_api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
public class ValidateTokenUtil {

    @Value("${auth.validateTokenApiUrl}")
    private String validateTokenApiUrl;

    /*
    Old way of creating WebClient, with java 11.
     */
//    public ResponseEntity<?> validateToken(String bearerToken) {
//        WebClient webClient = WebClient.create();
//        Mono<ResponseEntity<String>> responseEntityMono = webClient.get()
//                .uri(validateTokenApiUrl)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
//                .exchangeToMono(response -> response.toEntity(String.class));
//        return responseEntityMono.block();
//    }

    public ResponseEntity<?> validateToken(String bearerToken) {
        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .build();
        Mono<ResponseEntity<String>> responseEntityMono = webClient.get()
                .uri(validateTokenApiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .exchangeToMono(response -> response.toEntity(String.class));
        return responseEntityMono.block();
    }
}
