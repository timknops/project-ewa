import {mount} from "@vue/test-utils";
import AddTeamModal from "@/components/modal/team/AddTeamModal";

const mockedWarehouses = [
    {
        "id": 1000,
        "name": "Solar Sedum",
        "location": "H.J.E. Wenckebachweg 47D, 1096AK Amsterdam"
    },
    {
        "id": 1001,
        "name": "Superzon",
        "location": "Marconistraat 4A, 1704RG Heerhugowaard"
    },
    {
        "id": 1002,
        "name": "The switch",
        "location": "Barndegat 8, 1505HN Zaandam"
    },
    {
        "id": 1003,
        "name": "Induct",
        "location": "Philippusweg 2, 3125AS Schiedam"
    },
    {
        "id": 1004,
        "name": "EHES",
        "location": "Bolwerk 5, 3905NH Veenendaal"
    }
];
/**
 * @author Nashon Woldai
 *
 * Test the AddTeamModal component with a mock item.
 */
describe("AddTeamModal", () => {
    let wrapper;

    beforeEach(() => {
        // Mocking the warehouseService for testing
        const warehouseService = {
            findAll: jest.fn().mockResolvedValue(mockedWarehouses),
        };

        // Mounting the AddTeamModal component
        wrapper = mount(AddTeamModal, {
            global: {
                provide: {
                    warehouseService,
                },
            },
        });
    });

    // Test case to ensure the component initializes with the correct data
    it("initializes with correct data", () => {
        expect(wrapper.vm.modalItem.team).toBe("");
        expect(wrapper.vm.modalItem.warehouse.id).toBe("");
        expect(wrapper.vm.teamEmpty).toBe(false);
        expect(wrapper.vm.warehouseEmpty).toBe(false);
        expect(wrapper.vm.warehouses).toEqual([
            mockedWarehouses[0],
            mockedWarehouses[1],
            mockedWarehouses[2],
            mockedWarehouses[3],
            mockedWarehouses[4],
        ]);
    });

    // Test case to verify that warehouse options are fetched correctly
    it("fetches warehouse options correctly", async () => {
        wrapper.vm.warehouseService.findAll = jest.fn().mockResolvedValue(mockedWarehouses);
        await wrapper.vm.fetchWarehouseOptions();
        expect(wrapper.vm.warehouses).toEqual(mockedWarehouses);
    });


    // Test case to validate team name correctly
    it("validates team name correctly", async () => {
        // Test case where the team name is not empty
        wrapper.vm.modalItem.team = "Test Team";
        wrapper.vm.validateName();
        expect(wrapper.vm.teamEmpty).toBe(false);

        // Test case where the team name is empty
        wrapper.vm.modalItem.team = "";
        wrapper.vm.validateName();
        expect(wrapper.vm.teamEmpty).toBe(true);
    });
});
