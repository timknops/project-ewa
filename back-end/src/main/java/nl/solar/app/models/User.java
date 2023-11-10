package nl.solar.app.models;

import java.util.Objects;

/**
 * A class for the users of Solar Sedum
 *
 * @author Noa de Greef
 */
public class User {
    private long id;
    private long teamId;
    private String firstName;
    private String surName;
    private String email;
    private String password;
    private enum userType {
        ADMIN, VIEWER
    }
    private userType type;

    public User(long id, long teamId, String firstName, String surName, String email, String password, userType type) {
        this.id = id;
        this.teamId = teamId;
        this.firstName = firstName;
        this.surName = surName;
        this.email = email;
        this.password = password;
        this.type = type;
    }
    //todo make stuff final like the arrays

    public static User creatyDummyUser(long userId, long teamId){
        String[] firstNameArray = {"Paola", "Drew", "Adrianna", "Evan", "Henk", "Abel", "Muhammad", "Mara", "Clair",
                "Amar"};
        String[] surnameArray = {"de Groot", "Smits", "Mulder", "Visser", "Pacheco", "Dalal", "Ahssini", "Imen",
        "Ferguson", "Costa"};
        String randomFirstName = firstNameArray[(int) Math.floor(Math.random() * firstNameArray.length)];
        String randomSurname = surnameArray[(int) Math.floor(Math.random() * surnameArray.length)];
        String randomEmail = randomFirstName + randomSurname + "@gmail.com";
        String randomPassword = randomPasswordGen((int) (8 + Math.floor(Math.random() * 20)));
        userType[] userTypeArray = userType.values();
        userType randomUserType = userTypeArray[(int) Math.floor(Math.random() * userTypeArray.length)];

        return new User(userId, teamId, randomFirstName, randomSurname, randomEmail, randomPassword, randomUserType);
    }

    private static String randomPasswordGen(int length){
        StringBuilder stringBuilder = new StringBuilder(length);
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < length; i++) {
            stringBuilder.append(chars.charAt((int) Math.floor(Math.random() * chars.length())));
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj instanceof User user){
            return this.getId() == user.id;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public userType getType() {
        return type;
    }

    public void setType(userType type) {
        this.type = type;
    }
}
