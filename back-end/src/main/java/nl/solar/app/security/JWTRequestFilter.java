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

        //options request and paths that aren't secured should pass through
        if (HttpMethod.OPTIONS.matches(request.getMethod()) ||
                this.webConfig.SECURED_PATHS.stream().noneMatch(path::startsWith)){
            filterChain.doFilter(request, response);
            return;
        }

        //encrypted token from the authorization request header
        String encryptedToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        //if the token is null, block the request and send an error message
        if (encryptedToken == null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided, login first!");
            return;
        }

        //try decoding the token
        JWToken jwToken = null;
        try {
            jwToken = JWToken.decode(encryptedToken.replace("Bearer ", ""), this.webConfig.getIssuer(), this.webConfig.getPassphrase());
        } catch (RuntimeException e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage() + "You need to login first");
        }

        //set the token as an attribute of the request
        request.setAttribute(JWToken.JWT_ATTRIBUTE_NAME, jwToken);
        filterChain.doFilter(request, response);
    }
}
