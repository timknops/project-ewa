package nl.solar.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//don't need things like getters, setters and to string with data
//make a constructor with all field
//makes a constructor with no fields, because JPA requires it
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    private String receiver;
    private String emailBody;
    private String subject;

}
