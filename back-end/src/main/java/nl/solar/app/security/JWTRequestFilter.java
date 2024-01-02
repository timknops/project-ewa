package nl.solar.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.solar.app.WebConfig;
import nl.solar.app.security.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    WebConfig webConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //servlet path
        String path = request.getServletPath();

        if (HttpMethod.OPTIONS.matches(request.getMethod()) ||
                this.webConfig.SECURED_PATHS.stream().noneMatch(path::startsWith)){
            filterChain.doFilter(request, response);
            return;
        }

        //encoded
        String encryptedToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (encryptedToken == null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided, login first!");
            return;
        }

        JWToken jwToken = null;
        try {
            jwToken = JWToken.decode(encryptedToken, this.webConfig.getIssuer(), this.webConfig.getPassphrase());
        } catch (RuntimeException e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage() + "You need to login first");
        }

        request.setAttribute(JWToken.JWT_ATTRIBUTE_NAME, jwToken);
        filterChain.doFilter(request, response);
    }
}
