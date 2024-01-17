import {InventoryAdaptor} from "@/service/inventoryAdaptor";

/**
 * This is a unit test for the InventoryAdaptor class.
 * It tests the findAll, findAllForWarehouse, findByIds, addInventory, getProductWithoutInventory and updateInventory methods.
 * The fetchJSON method is mocked to return the expected data.
 * The fetchJSON method is also tested to throw an error when the response is not ok.
 *
 * @Author Julian Kruithof
 */
describe('InventoryAdaptor', () => {
  let inventoryAdaptor;


  beforeEach(() => {
    inventoryAdaptor = new InventoryAdaptor();
  });


  it('findAll should return all inventory', async () => {
    const expected = [
      {
        warehouse: {
          id: 1000,
          name: "Solar Sedum",
        },
        products: [
          {
            id: 1,
            productName: "Enphase IQ8+ omvormer",
            quantity: 1,
            minimum: 1,
          },
          {
            id: 2,
            productName: "Enphase Q kabel 1 fase",
            quantity: 2,
            minimum: 2,
          },
        ],
      },
      {
        warehouse: {
          id: 1001,
          name: "Superzon"
        },
        products: [
          {
            id: 1,
            productName: "Enphase IQ8+ omvormer",
            minimum: 11,
            quantity: 18
          },
          {
            id: 2,
            productName: "Enphase Q kabel 1 fase",
            minimum: 13,
            quantity: 11
          }
        ]
      }
    ]
    inventoryAdaptor.fetchJSON = jest.fn().mockResolvedValue(expected);

    const result = await inventoryAdaptor.findAll();

    expect(result, "FindAll shouldn't change data").toEqual(expected);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON calls the correct endpoint")
      .toHaveBeenCalledWith(`${process.env.VUE_APP_API_URL}/inventory`);
  })

  it('findAll return one item ', async () => {
    const warehouseId = 1;
    const productId = 1;
    const expected = {id: productId, productName: "Enphase IQ8+ omvormer", quantity: 1, minimum: 1}

    inventoryAdaptor.fetchJSON = jest.fn().mockResolvedValue(expected);

    const result = await inventoryAdaptor.findByIds(warehouseId, productId);

    expect(result, "data should not change").toEqual(expected);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON calls the correct endpoint")
      .toHaveBeenCalledWith(`${process.env.VUE_APP_API_URL}/warehouses/${warehouseId}/products/${productId}`);
  });

  it('findAllForWarehouse should return all inventory for a warehouse', async () => {
    const warehouseId = 1;
    const expected = [
      {
        id: 1,
        productName: "Enphase IQ8+ omvormer",
        quantity: 1,
        minimum: 1
      },
      {
        id: 2,
        productName: "Enphase Q kabel 1 fase",
        quantity: 2,
        minimum: 2
      }
    ]

    inventoryAdaptor.fetchJSON = jest.fn().mockResolvedValue(expected);

    const result = await inventoryAdaptor.findAllForWarehouse(warehouseId);

    expect(result, "data should not change").toEqual(expected);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON calls the correct endpoint")
      .toHaveBeenCalledWith(`${process.env.VUE_APP_API_URL}/warehouses/${warehouseId}/inventory`);
  });

  it('addInventory should add inventory', async () => {
    const inventory = {
      warehouse: {
        id: 1,
        name: "Solar Sedum",
      },
      product: {
        id: 1,
        productName: "Enphase IQ8+ omvormer",
      },
      quantity: 1,
      minimum: 1
    }

    inventoryAdaptor.fetchJSON = jest.fn().mockResolvedValue(inventory);

    const result = await inventoryAdaptor.addInventory(inventory);

    expect(result, "data should not change").toEqual(inventory);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON calls the correct endpoint with the correct options")
      .toHaveBeenCalledWith(`${process.env.VUE_APP_API_URL}/inventory`, {
        method: "POST",
        headers: {"content-type": "application/json"},
        body: JSON.stringify(inventory)
      });
  })

  it('getProductWithoutInventory should return all products without inventory', async () => {
    const warehouseId = 1;
    const expected = [
      {
        id: 1,
        productName: "Enphase IQ8+ omvormer"
      },
      {
        id: 2,
        productName: "Enphase Q kabel 1 fase"
      }
    ]

    inventoryAdaptor.fetchJSON = jest.fn().mockResolvedValue(expected);

    const result = await inventoryAdaptor.getProductWithoutInventory(warehouseId);

    expect(result, "data should not change").toEqual(expected);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON calls the correct endpoint")
      .toHaveBeenCalledWith(`${process.env.VUE_APP_API_URL}/warehouses/${warehouseId}/products/without-inventory`);
  })

  it('updateInventory should update inventory', async () => {
    const inventory = {
      warehouse: {
        id: 1,
        name: "Solar Sedum",
      },
      product: {
        id: 1,
        productName: "Enphase IQ8+ omvormer",
      },
      quantity: 1,
      minimum: 1
    }

    inventoryAdaptor.fetchJSON = jest.fn().mockResolvedValue(inventory);

    const result = await inventoryAdaptor.updateInventory(inventory);

    expect(result, "data should not change").toEqual(inventory);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON should be called").toHaveBeenCalledTimes(1);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON calls the correct endpoint with the correct options")
      .toHaveBeenCalledWith(`${process.env.VUE_APP_API_URL}/warehouses/${inventory.warehouse.id}/products/${inventory.product.id}`, {
        method: "PATCH",
        headers: {"content-type": "application/json"},
        body: JSON.stringify({
          minimum: inventory.minimum,
          quantity: inventory.quantity
        })
      });
  })

  it('fetchJSON to throw error when response is not ok', async () => {
    const error = {status: 400, message: "Bad request"};
    inventoryAdaptor.fetchJSON = jest.fn().mockRejectedValue(error);


    await expect(inventoryAdaptor.findAll(), "findAll should return error if something went wrong")
      .rejects.toEqual(error);
    await expect(inventoryAdaptor.fetchJSON, "fetchJSON should reject the error").rejects.toEqual(error);
    expect(inventoryAdaptor.fetchJSON, "fetchJSON calls the correct endpoint").toHaveBeenCalledWith(`${process.env.VUE_APP_API_URL}/inventory`);
  })
})