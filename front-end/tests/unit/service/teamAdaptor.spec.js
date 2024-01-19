import {TeamAdaptor} from "@/service/teamAdaptor";

describe("TeamAdaptor", () => {
    let teamAdaptor;

    beforeEach(() => {
        teamAdaptor = new TeamAdaptor();
    });

    it("fetches JSON data successfully", async () => {
        const mockData = { id: 1, team: "Test Team" };
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve(mockData),
                ok: true,
            })
        );

        const result = await teamAdaptor.fetchJSON("/test-url");
        expect(result).toEqual(mockData);
    });

    it("handles errors when fetching JSON data", async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve({ message: "Error message" }),
                ok: false,
            })
        );

        await expect(teamAdaptor.fetchJSON("/test-url")).rejects.toEqual({
            code: undefined,
            reason: "Error message",
        });
    });

    it("fetches all teams successfully", async () => {
        const mockTeams = [{ id: 1, team: "Team 1" }, { id: 2, team: "Team 2" }];
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve(mockTeams),
                ok: true,
            })
        );

        const result = await teamAdaptor.findAll();
        expect(result).toEqual(mockTeams);
    });
});