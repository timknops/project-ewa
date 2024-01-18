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

describe("AddTeamModal", () => {
    let wrapper;

    beforeEach(() => {
        const warehouseService = {
            findAll: jest.fn().mockResolvedValue(mockedWarehouses),
        };

        wrapper = mount(AddTeamModal, {
            global: {
                provide: {
                    warehouseService,
                },
            },
        });
    });

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

    it("fetches warehouse options correctly", async () => {
        wrapper.vm.warehouseService.findAll = jest.fn().mockResolvedValue(mockedWarehouses);
        await wrapper.vm.fetchWarehouseOptions();
        console.log(wrapper.vm.warehouses);
        expect(wrapper.vm.warehouses).toEqual(mockedWarehouses);
    });


    it("validates team name correctly", async () => {
        wrapper.vm.modalItem.team = "Test Team";
        wrapper.vm.validateName();
        expect(wrapper.vm.teamEmpty).toBe(false);

        wrapper.vm.modalItem.team = "";
        wrapper.vm.validateName();
        expect(wrapper.vm.teamEmpty).toBe(true);
    });
});
