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

    /**
     * Sends an email to the user that request a password reset
     *
     * @param givenEmail email received from the front-end
     * @return an email with the link to reset the password of the user
     */
    @PostMapping("")
    public String sendResetEmail(@RequestBody String givenEmail

    ) {
        Email fullEmail = new Email();

        //split the url into it's individual params
        String[] bodySplitter = givenEmail.split("_", 3);
        String email = bodySplitter[0];
        String name = bodySplitter[1];
        String id = bodySplitter[2];

        //link for the password reset
        String resetLink = "https://front-end-8cvv.onrender.com/#/loginReset/" + email + "_" + name + "_" + id;

        //set email properties
        fullEmail.setReceiver(email);
        fullEmail.setEmailBody("A login request for this email address was requested on " + resetLink);
        fullEmail.setSubject("Password reset request");

        return emailService.sendMail(fullEmail);
    }

}
