package nl.solar.app.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.solar.app.WebConfig;
import nl.solar.app.exceptions.NotAcceptableException;
import nl.solar.app.models.User;
import nl.solar.app.repositories.EntityRepository;
import nl.solar.app.security.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    WebConfig webConfig;

    @Autowired
    EntityRepository<User> userEntityRepository;

    /**
     * Log in POST, to specifically log in the back-end
     *
     * @param loginInfo contains the username and password that was given
     * @return returns the logged in yser and puts the token in the http header
     */
    @PostMapping(path = "/login")
    public ResponseEntity<User> userLogin(@RequestBody ObjectNode loginInfo) throws NotAcceptableException {
        String username = loginInfo.get("username").asText();
        String password = loginInfo.get("password").asText();

        //Get all users, then search the list to find the user with the same email as the email provided
        List<User> userList = userEntityRepository.findAll();
        User foundUser = userList.stream().filter(user -> user.getName().equals(username)).findAny().orElse(null);

        //if no user was found or the password isn't correct then throw a not acceptable exception
        if (foundUser == null || !foundUser.checkPassword(password)) {
            throw new NotAcceptableException("Login was unsuccessful for account: " + username);
        }
        //make a new token, encode it and then put it in the AUTHORIZATION httpheader
        JWToken jwToken = new JWToken(foundUser.getName(), foundUser.getId(), foundUser.getType());
        String token = jwToken.encode(
                this.webConfig.getIssuer(),
                this.webConfig.getPassphrase(),
                this.webConfig.getTokenValidity());

        return ResponseEntity.accepted().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).body(foundUser);
    }
}
