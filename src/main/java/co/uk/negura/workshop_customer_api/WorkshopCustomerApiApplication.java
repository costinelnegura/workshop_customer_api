package co.uk.negura.workshop_customer_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class WorkshopCustomerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkshopCustomerApiApplication.class, args);
    }

}
