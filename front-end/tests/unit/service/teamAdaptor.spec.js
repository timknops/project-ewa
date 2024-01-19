import { TeamAdaptor } from "@/service/teamAdaptor";

// Test suite for the TeamAdaptor class
describe("TeamAdaptor", () => {
    let teamAdaptor;

    // Setup function to create a new instance of TeamAdaptor before each test
    beforeEach(() => {
        teamAdaptor = new TeamAdaptor();
    });

    // Test case to ensure the fetchJSON method handles successful JSON data retrieval
    it("fetches JSON data successfully", async () => {
        // Mocking fetch with successful response and JSON data
        const mockData = { id: 1, team: "Test Team" };
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve(mockData),
                ok: true,
            })
        );

        // Calling the fetchJSON method and asserting the result
        const result = await teamAdaptor.fetchJSON("/test-url");
        expect(result).toEqual(mockData);
    });

    // Test case to ensure the fetchJSON method handles errors during JSON data retrieval
    it("handles errors when fetching JSON data", async () => {
        // Mocking fetch with an error response and JSON error message
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve({ message: "Error message" }),
                ok: false,
            })
        );

        // Calling the fetchJSON method and expecting it to reject with the appropriate error object
        await expect(teamAdaptor.fetchJSON("/test-url")).rejects.toEqual({
            code: undefined,
            reason: "Error message",
        });
    });

    // Test case to ensure the findAll method retrieves all teams successfully
    it("fetches all teams successfully", async () => {
        // Mocking fetch with successful response and JSON array of teams
        const mockTeams = [{ id: 1, team: "Team 1" }, { id: 2, team: "Team 2" }];
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve(mockTeams),
                ok: true,
            })
        );

        // Calling the findAll method and asserting the result
        const result = await teamAdaptor.findAll();
        expect(result).toEqual(mockTeams);
    });
});
