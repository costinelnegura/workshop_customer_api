package co.uk.negura.workshop_customer_api.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseHandlerUtil {

        public ResponseEntity<?> generateResponse(HttpStatus status, String message) {
            return ResponseEntity.status(status).header(
                    "Message", message
            ).build();
        }

}
