/**
 * class for the users of Solar Sedum
 *
 * @author Noa de Greef
 */

export class userLogin {

    /**
     * Constructor of a user
     *
     * @constructor
     * @param {number} id        id of the user
     * @param {number} teamId    id of the team
     * @param {String} username  name of the user
     * @param {String} email     email of the user
     * @param {String} password  password of the user
     * @param {String} type      type of user, either admin or viewer
     */
    constructor(id, teamId, username, email, password, type) {
        this.id = id;
        this.teamId = teamId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = type;
    }

    static copyConstructor(user){
        if (user == null || user.isUndefined()) return null;
        return Object.assign(new userLogin(), user);
    }

    static dummyData() {
        return new userLogin(
            1,
            1,
            "Julian",
            "kaasbaas43@gmail.com",
            "Konijn",
            "admin"
        );
    }
}
