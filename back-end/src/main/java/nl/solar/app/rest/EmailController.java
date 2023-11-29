package nl.solar.app.rest;

import nl.solar.app.models.Email;
import nl.solar.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("")
    public String sendEmail(@RequestBody String givenEmail){
        Email fullEmail = new Email();


        fullEmail.setReceiver(givenEmail);
        fullEmail.setEmailBody("A login request for this email address was requested on solarium.com");
        fullEmail.setSubject("Password reset request");

        return emailService.sendMail(fullEmail);
    }
}
