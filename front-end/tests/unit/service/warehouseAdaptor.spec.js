import { WarehouseAdaptor } from "@/service/warehouseAdaptor";

const mockWarehouses = [
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
]

/**
 * Tests for the WarehouseAdaptor service
 * @author Wilco van de Pol
 */
describe("WarehouseAdaptor", () => {
    let warehouseAdaptor;

    beforeEach(() => {
       warehouseAdaptor = new WarehouseAdaptor();
    });

    it('findAll should return all warehouses', async () => {

        warehouseAdaptor.fetchJSON = jest.fn().mockResolvedValue(mockWarehouses);

        const result = await warehouseAdaptor.findAll();

        expect(result, "findAll result should be te same").toEqual(mockWarehouses);
        expect(warehouseAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
        expect(warehouseAdaptor.fetchJSON, "fetchJSON calls the correct endpoint")
            .toHaveBeenCalledWith(`${process.env.VUE_APP_API_URL}/warehouses`);

    });

    it('findById should return one warehouse of a specific id', async () => {
        warehouseAdaptor.fetchJSON = jest.fn().mockResolvedValue(mockWarehouses[0]);

        const result = await warehouseAdaptor.findById(1000);

        expect(result, "findById result should be te same").toEqual(mockWarehouses[0]);
        expect(warehouseAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
        expect(warehouseAdaptor.fetchJSON, "fetchJSON calls the correct endpoint")
            .toHaveBeenCalledWith(`${process.env.VUE_APP_API_URL}/warehouses/1000`);
    });

    it('add should add a new warehouse', async () => {
        const newWarehouse = {
            "id": 1005,
            "name": "Test warehouse",
            "location": "Zuideinde 123, 1234AB Amsterdam"
        }
        warehouseAdaptor.fetchJSON = jest.fn().mockResolvedValue(newWarehouse);

        const result = await warehouseAdaptor.add(newWarehouse);

        expect(result, "data should not change").toEqual(newWarehouse);
        expect(warehouseAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
        expect(warehouseAdaptor.fetchJSON, "fetchJSON calls the correct endpoint with the correct options")
            .toHaveBeenCalledWith(`${process.env.VUE_APP_API_URL}/warehouses`, {
                method: "POST",
                headers: {"content-type": "application/json"},
                body: JSON.stringify(newWarehouse)
            });
    });
});