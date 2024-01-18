import {UserAdaptor} from "@/service/userAdaptor";

const teams = {
    id: 1,
    team: "Solar Sedum",
    warehouse: "Warehouse 1",
    type: "INTERNAL"
}

const users = [
    {
        id: 5,
        name: "admin2",
        email: "admin2@admin.com",
        password: "admin2",
        type: "ADMIN",
        team: teams
    },
    {
        id: 6,
        name: "testUser",
        email: "testUser@admin.com",
        password: "testUser",
        type: "VIEWER",
        team: teams
    }
]

const usersAdmin = [
    {
        id: 5,
        name: "admin2",
        email: "admin2@admin.com",
        type: "ADMIN",
        team: teams
    },
    {
        id: 6,
        name: "testUser",
        email: "testUser@admin.com",
        type: "VIEWER",
        team: teams
    }
]

/**
 * A unit test for the userAdaptor
 *
 * @author Noa de Greef
 */
describe("UserAdaptor", () => {
    let mockAdaptor;
    process.env.VUE_APP_API_URL = "http://localhost:8083"

    beforeEach(() => {
        mockAdaptor = new UserAdaptor();
    });

    it("resourceUrl used for getting information from back-end is setup correctly", function () {
        expect(mockAdaptor.resourceUrl, "resourceUrl is not correct").toMatch("http://localhost:8083/users");
    })

    it("asyncFindAll gets all users", async function () {
        //mock the back-end for all users
        mockAdaptor.fetchJson = jest.fn().mockResolvedValue(users)

        const fetchResult = await mockAdaptor.asyncFindAll();
        expect(fetchResult, "findAll did not return any users").toBeDefined();
        expect(fetchResult, "asyncFindAll did not match the expected users").toEqual(users);
    })

    it("asyncFindAdmin gets all users from the admin JsonView", async function () {

        //mock the back-end fetch results for the admin jsonview
        mockAdaptor.fetchJson = jest.fn().mockResolvedValue(usersAdmin)

        const fetchAdminResult = await mockAdaptor.asyncFindAdmin();
        expect(fetchAdminResult, "Fetch did not return any us").toBeDefined();
        expect(fetchAdminResult, "asyncFindAdmin did not match the expected admin version of users").toEqual(usersAdmin)
    })

    it("asyncFindById gets a single user", async function () {

        //mock the back-end fetch results
        mockAdaptor.fetchJson = jest.fn().mockResolvedValue(users[0])

        const fetchSingleUser = await mockAdaptor.asyncFindById(users[0].id);
        expect(fetchSingleUser, "Fetch did not return anything").toBeDefined();
        expect(fetchSingleUser, "asyncFindById did not match the expected user").toEqual(users[0])
    })

    it("add a user", async () => {
        //mock the back-end fetch results
        mockAdaptor.fetchJson = jest.fn().mockResolvedValue(users[0])

        //mock the add method
        const addedUser = await mockAdaptor.asyncAdd(users[0]);

        expect(addedUser, "added user not found").toBeDefined();
        expect(addedUser, "added user changed when adding").toEqual(users[0]);
        expect(mockAdaptor.fetchJson, "fetchJson was not called").toHaveBeenCalled();
    })

    it("properly deal with an error from the back-end", async () => {
        const error400 = {code: 400, reason: "400 Bad Request"};
        const error406 = {code: 406, reason: "406 Not Acceptable"};

        //mock the back-end error 400 code from the back-end
        mockAdaptor.fetchJson = jest.fn().mockRejectedValue(error400);
        //call the asyncdelete function which should be rejected because of the mock.fetchJson and look if the results match
        await expect(mockAdaptor.asyncDelete(users[0].id),
            "fetchJson does not properly reject the invalid 400 response from the back-end").rejects.toEqual(error400);

        //mock the back-end error 406 code from the back-end
        mockAdaptor.fetchJson = jest.fn().mockRejectedValue(error406);
        //call the asyncdelete function which should be rejected because of the mock.fetchJson and look if the results match
        await expect(mockAdaptor.asyncDelete(users[0].id),
            "fetchJson does not properly reject the invalid 406 response from the back-end").rejects.toEqual(error406);
    })
})
