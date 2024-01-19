package nl.solar.app.rest;

import nl.solar.app.exceptions.ResourceNotFoundException;
import nl.solar.app.models.Email;
import nl.solar.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * Sends an email to the user that request a password reset
     *
     * @param givenEmail email received from the front-end
     */
    @PostMapping("/loginReset")
    public void sendResetEmail(@RequestBody String givenEmail
    ) throws ResourceNotFoundException {
        Email fullEmail = new Email();

        if (givenEmail.isEmpty()){
            throw new ResourceNotFoundException("Email String not found.");
        }

        //split the url into it's individual params
        String[] bodySplitter = givenEmail.split("_", 3);
        String email = bodySplitter[0];
        String name = bodySplitter[1];
        String id = bodySplitter[2];

        //link for the password reset
        String resetLink = "http://localhost:8080/#/loginReset/" + email + "_" + name + "_" + id;

        //set email properties
        fullEmail.setReceiver(email);
        fullEmail.setEmailBody("A login request for this email address was requested on " + resetLink);
        fullEmail.setSubject("Password reset request");

        emailService.sendMail(fullEmail);
    }

}
