package co.uk.negura.workshop_customer_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WorkshopCustomerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkshopCustomerApiApplication.class, args);
    }

}
