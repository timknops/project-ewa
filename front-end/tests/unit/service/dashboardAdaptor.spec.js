import {DashboardAdaptor} from "@/service/dashboardAdaptor";

/**
 * Unit tests for the DashboardAdaptor class
 *
 * @author Hanan Ouardi
 */
//Right-Bicep as the base
describe("DashboardAdaptor", () => {
    let dashboardAdaptor;

    /**
     * Set up a new DashboardAdaptor instance before each test
     */
    beforeEach(() => {
        dashboardAdaptor = new DashboardAdaptor();
    });

    /**
     * Test 1: for fetching inventory data
     */
    it("should fetch inventory data", async () => {
        //expected data for the inventory
        const expected = [
            {
                warehouseName: "Solar",
                productName: "gateway",
                quantity: 10,
                inventoryQuantity: 20,
                deliverDate: "2024-01-20",
            },
        ];
        //Mock the fetchJson method and resolve with expected data
        dashboardAdaptor.fetchJSON = jest.fn().mockResolvedValue(expected);

        const result = await dashboardAdaptor.findAll();
        //Assertions
        expect(result, "FindAll shouldn't change data").toEqual(expected);
        expect(dashboardAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
        expect(dashboardAdaptor.fetchJSON, "fetchJSON calls the correct endpoint")
            .toHaveBeenCalledWith(
                `${process.env.VUE_APP_API_URL}/dashboard-items/inventory`
            );
    });
    /**
     * Test 2: for fetching project data
     */
    it("should fetch project data", async () => {
        const projectDataExpected = [
            {
                dueDate: "2024-01-20",
                projectName: "project 78",
                warehouseName: "Superzon",
                productName: "Relay omvormer",
                amountOfProduct: 15,
            },
        ];
        //Mock the fetchJson method and resolve with expected data
        dashboardAdaptor.fetchJSON = jest.fn().mockResolvedValue(projectDataExpected);

        const result = await dashboardAdaptor.findAllProjects();

        //Assertions
        expect(result, "FindAllProject shouldn't change data").toEqual(projectDataExpected);
        expect(dashboardAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
        expect(dashboardAdaptor.fetchJSON, "fetchJSON calls the correct endpoint")
            .toHaveBeenCalledWith(
                `${process.env.VUE_APP_API_URL}/dashboard-items/project`
            );
    });

    /**
     * Test 3: for fetching the inventory quantity data
     */
    it("should fetch all inventory quantity data", async () => {
        const allInventoryDataExpected = [
            {
                warehouseName: "EHES",
                productName: "gateway",
                inventoryQuantity: 15,
            },
        ];
        //Mock the fetchJson method and resolve with expected data
        dashboardAdaptor.fetchJSON = jest.fn().mockResolvedValue(allInventoryDataExpected);
        const result = await dashboardAdaptor.findAllInventoryQuantity();
        //Assertions
        expect(result, "FindAllInventoryQuantity shouldn't change data").toEqual(allInventoryDataExpected);
        expect(dashboardAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
        expect(dashboardAdaptor.fetchJSON, "fetchJSON calls the correct endpoint")
            .toHaveBeenCalledWith(
                `${process.env.VUE_APP_API_URL}/dashboard-items/inventory-quantity`
            );
    });
    /**
     * Test 4: incorrect API endpoint
     */
    it("handles incorrect API endpoint for project data", async () => {
        process.env.VUE_APP_API_URL = "http://incorrect-url";
        dashboardAdaptor.fetchJSON = jest.fn().mockResolvedValue([]);
        const result = await dashboardAdaptor.findAllProjects();
        expect(result).toEqual([]);
    });
});
