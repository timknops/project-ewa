package nl.solar.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.UnknownHostException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Enable CORS for all origins
     * @param registry  CORS registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // all endpoints
                .allowedOriginPatterns("*") // all origins
                .allowedMethods("GET", "POST", "PUT", "DELETE"); // all methods
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


