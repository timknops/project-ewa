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

    //All paths that are protected with authorization
    public Set<String> SECURED_PATHS = Set.of("/users");

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
                .allowedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE) //Auth http header
                .exposedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE) //Auth http header
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


