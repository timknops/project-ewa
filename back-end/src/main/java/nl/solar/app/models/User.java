package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonView;
import nl.solar.app.models.views.UserView;

import java.util.Objects;

/**
 * A class for the users of Solar Sedum
 *
 * @author Noa de Greef
 */
public class User {
    @JsonView(UserView.userAdmin.class)
    private long id;
    @JsonView(UserView.userAdmin.class)
    private long teamId;
    @JsonView(UserView.userAdmin.class)
    private String name;
    @JsonView(UserView.userAdmin.class)
    private String email;
    @JsonView(UserView.userFull.class)
    private String password;

    private enum userType {
        ADMIN, VIEWER
    }

    @JsonView(UserView.userAdmin.class)
    private String type;

    public User(long id, long teamId, String name, String email, String password, String Type) {
        this.id = id;
        this.teamId = teamId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = Type;
    }

    /**
     * Creates dummy users to populate the table. The data for the dummy users is random generated
     *
     * @param userId id of the user
     * @param teamId id of the team
     * @return a dummy user
     */
    public static User creatyDummyUser(long userId, long teamId) {
        final String[] FIRST_NAME_ARRAY = {"Paola", "Drew", "Adrianna", "Evan", "Henk", "Abel", "Muhammad",
                "Mara", "Clair", "Amar", "Braden", "Anwar", "Amelia", "Dax", "Ayan", "Ava", "Kim"};
        final String[] SURNAME_ARRAY = {"de Groot", "Smits", "Mulder", "Visser", "Pacheco", "Dalal", "Ahssini", "Imen",
                "Ferguson", "Costa", "Herrera", "Dalal", "Case", "de Vries", "Santana", "Nguyen"};
        String randomFirstName = FIRST_NAME_ARRAY[(int) Math.floor(Math.random() * FIRST_NAME_ARRAY.length)];
        String randomSurname = SURNAME_ARRAY[(int) Math.floor(Math.random() * SURNAME_ARRAY.length)];
        String fullRandomName = randomFirstName + " " + randomSurname;
        String randomEmail = randomFirstName + randomSurname + "@gmail.com";
        String randomPassword = randomPasswordGen((int) (8 + Math.floor(Math.random() * 20)));
        final userType[] USER_TYPE_ARRAY = userType.values();
        userType randomUserType = USER_TYPE_ARRAY[(int) Math.floor(Math.random() * USER_TYPE_ARRAY.length)];

        return new User(userId, teamId, fullRandomName, randomEmail, randomPassword, randomUserType.toString());
    }

    /**
     * Creates a random generated password
     *
     * @param length length of the password
     * @return a random password
     */
    private static String randomPasswordGen(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < length; i++) {
            stringBuilder.append(chars.charAt((int) Math.floor(Math.random() * chars.length())));
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof User user) {
            return this.getId() == user.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
