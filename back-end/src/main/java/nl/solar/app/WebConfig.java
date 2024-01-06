package nl.solar.app;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.UnknownHostException;
import java.util.Set;

@Data
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //TODO fix /users and loginReset
    //All paths that are protected with authorization
    public Set<String> SECURED_PATHS = Set.of("/products", "/projects", "/teams", "/warehouses");

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
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")// all methods
                //Allow and expose Auth and content_type httpheaders
                .allowedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE)
                .exposedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE)
                //allows credentials to be included
                .allowCredentials(true);

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


