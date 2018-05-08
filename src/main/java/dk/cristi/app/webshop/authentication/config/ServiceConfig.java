package dk.cristi.app.webshop.authentication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig {

    @Value("${signing.key}")
    private String key;

    public String getJwtSigningKey() {
        return key;
    }
}
