import {EmailAdaptor} from "@/service/emailAdaptor";

/**
 * A unit test for the emailAdaptor
 *
 * @author Noa de Greef
 */
describe("emailAdaptor", () => {
    let mockEmailAdaptor;
    process.env.VUE_APP_API_URL = "http://localhost:8083"

    beforeEach(function () {
        mockEmailAdaptor = new EmailAdaptor;
    })

    it("resourceUrl used for getting information from back-end is setup correctly", function () {
        expect(mockEmailAdaptor.resourceUrl, "resourceUrl is not correct").toMatch("http://localhost:8083");
    })

    it("asyncSendEmail sends a POST properly", async () => {
        const emailString = "greefnhva@gmail.com_Julian_1"
        const emailString2 = "Mail has been sent"
        mockEmailAdaptor.fetchJson = jest.fn().mockResolvedValue(emailString2);

        const result = await mockEmailAdaptor.asyncSendMail(emailString)
        expect(result, "result of successful asyncSendMail not found").toBeDefined();
        expect(result, "email response is like expected").toBe(emailString2);
        expect(mockEmailAdaptor.fetchJson, "fetch").toHaveBeenCalled()
    })

    it("asyncSendEmail properly deals with a bad back-end response", async () => {
        const emailString = "greefnhva@gmail.com_Julian_1"
        const emailString2 = "Something went wrong while sending mail"
        mockEmailAdaptor.fetchJson = jest.fn().mockResolvedValue(emailString2);

        const result = await mockEmailAdaptor.asyncSendMail(emailString)
        expect(result, "result of wrong asyncSendMail not found").toBeDefined();
        expect(result, "email response does not wrong return message").toBe(emailString2);
        expect(mockEmailAdaptor.fetchJson, "fetchJson was not called for asyncSendMail").toHaveBeenCalled()
    })
})
