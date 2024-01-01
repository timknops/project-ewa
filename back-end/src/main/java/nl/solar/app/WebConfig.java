package nl.solar.app;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.UnknownHostException;

@Data
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.passphrase}")
    private String passphrase;

    @Value("${jwt.token-validity}")
    private int tokenValidity;


    /**
     * Enable CORS for all origins
     * @param registry  CORS registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // all endpoints
                .allowedOriginPatterns("*") // all origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH"); // all methods

    }

    /**
     * Get the host IP address pattern
     * @return  host IP address pattern
     */
    private String getHostIPAddressPattern() {
        try {
            return "http://" + java.net.InetAddress.getLocalHost().getHostAddress() + ":*";
        } catch (UnknownHostException e) {
            return "";
        }
    }
}


