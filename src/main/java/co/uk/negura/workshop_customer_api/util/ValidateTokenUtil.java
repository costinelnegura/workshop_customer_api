package co.uk.negura.workshop_customer_api.util;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Component
public class ValidateTokenUtil {

    @Value("${auth.validateTokenApiUrl}")
    private String validateTokenApiUrl;

    private final WebClient.Builder webClientBuilder;
    private final DiscoveryClient discoveryClient;

    public ValidateTokenUtil(WebClient.Builder webClientBuilder,
                             DiscoveryClient discoveryClient) {
        this.webClientBuilder = webClientBuilder;
        this.discoveryClient = discoveryClient;
    }


    public ResponseEntity<?> validateToken(String bearerToken) {
        List<ServiceInstance> instances = discoveryClient.getInstances("workshop-users-api");
        if (instances.isEmpty()) {
            throw new RuntimeException("No instances found for service workshop-users-api");
        }
        ServiceInstance instance = instances.getFirst();
        URI uri = instance.getUri().resolve(validateTokenApiUrl);
//        URI uri = URI.create("http://localhost:8080/api/v1/auth/validate");

        Mono<ResponseEntity<String>> responseEntityMono = webClientBuilder.build()
                .get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .retrieve()
                .toEntity(String.class);
        return responseEntityMono.block();
    }
}
