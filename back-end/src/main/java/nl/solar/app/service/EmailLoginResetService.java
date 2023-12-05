package nl.solar.app.service;

import nl.solar.app.models.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//Service is similar to repository, but repository is more for database CRUD actions.
// While Service is meant for business logic
@Service
public class EmailLoginResetService implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    //Email of the account that is sending emails, can be found in application.properties
    @Value("${spring.mail.username}")
    private String sendEmailer;

    @Override
    public String sendMail(Email email) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sendEmailer);
            mailMessage.setTo(email.getReceiver());
            mailMessage.setText(email.getEmailBody());
            mailMessage.setSubject(email.getSubject());

            javaMailSender.send(mailMessage);
            return "Mail has been send";
        } catch (Exception e){
            return "Something went wrong while sending mail";
        }
    }
}
