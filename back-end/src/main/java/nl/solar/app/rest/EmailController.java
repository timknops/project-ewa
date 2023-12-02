package nl.solar.app.rest;

import nl.solar.app.models.Email;
import nl.solar.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    private static String randomCodeGen() {
        StringBuilder stringBuilder = new StringBuilder(10);
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 10; i++) {
            stringBuilder.append(chars.charAt((int) Math.floor(Math.random() * chars.length())));
        }
        return stringBuilder.toString();
    }

    @PostMapping("")
    public String sendEmail(@RequestBody String givenEmail

    ){
        Email fullEmail = new Email();

        String resetLink = "http://localhost:8080/#/loginReset/" + givenEmail;

        fullEmail.setReceiver(givenEmail);
        fullEmail.setEmailBody("A login request for this email address was requested on " + resetLink);
        fullEmail.setSubject("Password reset request");

        return emailService.sendMail(fullEmail);
    }

}
