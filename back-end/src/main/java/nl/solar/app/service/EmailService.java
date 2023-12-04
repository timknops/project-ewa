package nl.solar.app.service;

import nl.solar.app.models.Email;

public interface EmailService {

    String sendMail(Email email);
}
