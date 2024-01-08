package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.solar.app.models.views.UserView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class for the users of Solar Sedum
 *
 * @author Noa de Greef
 */
@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @SequenceGenerator(name = "user_id_generator", initialValue = 5000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @JsonView(UserView.userAdmin.class)
    private long id;
    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIncludeProperties({ "id", "team" })
    @JsonView(UserView.userAdmin.class)
    private Team team;
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

    public User(long id, Team team, String name, String email, String password, String Type) {
        this.id = id;
        this.team = team;
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = Type;
    }

    public boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    /**
     * Creates dummy users to populate the table. The data for the dummy users is
     * random generated
     *
     * @param userId id of the user
     * @param team   team that is going to be attached to the user
     * @return a dummy user
     */
    public static User creatyDummyUser(long userId, Team team) {
        final String[] FIRST_NAME_ARRAY = { "Paola", "Drew", "Adrianna", "Evan", "Henk", "Abel", "Muhammad",
                "Mara", "Clair", "Amar", "Braden", "Anwar", "Amelia", "Dax", "Ayan", "Ava", "Kim" };
        final String[] SURNAME_ARRAY = { "de Groot", "Smits", "Mulder", "Visser", "Pacheco", "Dalal", "Ahssini", "Imen",
                "Ferguson", "Costa", "Herrera", "Dalal", "Case", "de Vries", "Santana", "Nguyen" };
        String randomFirstName = FIRST_NAME_ARRAY[(int) Math.floor(Math.random() * FIRST_NAME_ARRAY.length)];
        String randomSurname = SURNAME_ARRAY[(int) Math.floor(Math.random() * SURNAME_ARRAY.length)];
        String fullRandomName = randomFirstName + " " + randomSurname;
        String randomEmailPre = randomFirstName + randomSurname + userId + "@gmail.com";
        // remove any whitespace in the random email
        String randomEmail = randomEmailPre.replaceAll("\\s", "");
        String randomPassword = randomPasswordGen((int) (8 + Math.floor(Math.random() * 20)));
        final userType[] USER_TYPE_ARRAY = userType.values();
        userType randomUserType = USER_TYPE_ARRAY[(int) Math.floor(Math.random() * USER_TYPE_ARRAY.length)];

        return new User(userId, team, fullRandomName, randomEmail, randomPassword, randomUserType.toString());
    }

    public static List<User> createStaticAdmin() {
        List<User> userList = new ArrayList<>();
        User user1 = new User(1, null, "Julian", "nashonwoldai@gmail.com", "password", "ADMIN");
        User user2 = new User(2, null, "admin", "admin@admin.com", "admin", "ADMIN");
        userList.add(user1);
        userList.add(user2);
        return userList;
    }

    public static User createStaticUser() {
        return new User(3, null, "user", "user@user.com", "user", "VIEWER");
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

}
