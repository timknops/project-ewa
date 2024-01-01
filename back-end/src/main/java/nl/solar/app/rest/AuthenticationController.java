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

    @PostMapping(path = "/login")
    public ResponseEntity<User> userLogin(@RequestBody ObjectNode loginInfo) throws NotAcceptableException {
        String email = loginInfo.get("email").asText();
        String password = loginInfo.get("password").asText();

        //Get all users, then search the list to find the user with the same email as the email provided
        List<User> userList = userEntityRepository.findAll();
        User foundUser = userList.stream().filter(user -> user.getEmail().equals(email)).findAny().orElse(null);

        //if no user was found or the password isn't correct then throw a not acceptable exception
        if (foundUser == null || !foundUser.checkPassword(password)) {
            System.out.println(userList.get(0));
            System.out.println(password);
            throw new NotAcceptableException("Login was unsuccessful for account: " + email);
        }
        JWToken jwToken = new JWToken(foundUser.getName(), foundUser.getId(), foundUser.getType());
        String token = jwToken.encode(
                this.webConfig.getIssuer(),
                this.webConfig.getPassphrase(),
                this.webConfig.getTokenValidity());

        return ResponseEntity.accepted().header(HttpHeaders.AUTHORIZATION, token).body(foundUser);
    }
}
