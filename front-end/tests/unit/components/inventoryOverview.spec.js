import {shallowMount} from "@vue/test-utils";
import InventoryOverview from "@/components/InventoryOverview.vue";
import {InventoryAdaptor} from "@/service/inventoryAdaptor.js";
import {createRouter, createWebHashHistory} from "vue-router";
import {SessionSbService} from "@/service/SessionSbService";


//mock adaptor
jest.mock('@/service/inventoryAdaptor');

//mock router
const routes = [
  {
    path: "/inventory",
    component: InventoryOverview,
    meta: {icon: "fa-solid fa-boxes-stacked", requiresLogin: true},
    children: [{path: ":warehouse", component: InventoryOverview}],
  },
  {
    path: "/",
    redirect: "/inventory",
  }
]
let wrapper;
/**
 *
 * @test {InventoryOverview}
 *
 * test the InventoryOverview component with a mock router and adaptor.
 *
 * @Author Julian Kruithof
 */
describe('InventoryOverview.vue', () => {

  beforeEach(async () => {
    const router = createRouter({
      history: createWebHashHistory(),
      routes: routes
    })

    const sessionSbService = new SessionSbService();
    sessionSbService.saveTokenInBrowserStorage('token', {
      id: 1,
      name: 'name',
      type: 'admin'
    });
    const mockInventoryAdaptor = new InventoryAdaptor();
    //mock the findAll method
    jest.spyOn(mockInventoryAdaptor, 'findAll').mockResolvedValue([
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
    ])

    wrapper = shallowMount(InventoryOverview, {
      global: {
        provide: {
          inventoryService: mockInventoryAdaptor,
          sessionService: sessionSbService
        },
        plugins: [router]
      }
    })

    //wait for router to be ready
    wrapper.vm.$router.push('/inventory');
    await wrapper.vm.$router.isReady()
    //wait for data to be loaded
    await wrapper.vm.$nextTick();
  })

  it('overview is rendered', () => {
    expect(wrapper.element.children.length, "Overview not rendered").toBeGreaterThan(0);
  });

  it('table is displayed', () => {
    const table = wrapper.findComponent({name: "TableComponent"}); //find the table
    expect(table.exists(), "Table not rendered").toBeTruthy();
  });

  it('Router changes according to active warehouse', () => {
    const routerPushSpy = jest.spyOn(wrapper.vm.$router, 'push');
    wrapper.vm.setActiveWarehouse({id: 1000, name: "Solar Sedum"});

    expect(routerPushSpy, "Router doesn't change to the active warehouse")
      .toHaveBeenCalledWith('/inventory/Solar Sedum');

    wrapper.vm.setActiveWarehouse("Total");

    expect(routerPushSpy, "Router doesn't change to the total overview")
      .toHaveBeenCalledWith('/inventory');
  });

  it('total product list formats correctly', () => {
    const totalInventory = wrapper.vm.getTotalProductInfo() //uses totalProduct which is equal to the mocked findAll method
    expect(totalInventory.length, "Total product list is not formatted correctly").toBe(2);
    expect(totalInventory).toEqual([
      {
        productName: "Enphase IQ8+ omvormer",
        quantity: 19,
      },
      {
        productName: "Enphase Q kabel 1 fase",
        quantity: 13,
      }
    ])
  });

  it('product list is correctly formatted, for a specific warehouse', () => {
    const products = wrapper.vm.getWarehouseProductInfo({id: 1000, name: "Solar Sedum"});

    expect(products.length, "length aren't the same").toBe(2);
    expect(products, "the product is not in the correct format").toEqual([
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
      }
    ])
  });

  it('modals are handle correctly', async () => {
    //set active warehouse to a specific warehouse
    wrapper.vm.setActiveWarehouse({id: 1000, name: "Solar Sedum"});

    const mockedFunction = jest.spyOn(wrapper.vm.inventoryService, 'getProductWithoutInventory')

    mockedFunction.mockResolvedValue([{id: 3, productName: "Enphase Q kabel 3 fase"}]);
    await wrapper.vm.showAddModal();

    await wrapper.vm.$nextTick();

    //check if the modal is rendered and the correct modal is rendered with the correct data
    const modal = wrapper.findComponent({name: "ModalComponent"});
    expect(modal.exists(), "Add modal is not rendered").toBeTruthy();
    expect(modal.vm.title, "Title is not correct for add modal").toBe("Add inventory");
    expect(modal.vm.activeModal, "incorrect modal child component is rendered").toBe("add-inventory-modal");

    wrapper.setData({showModal: false});

    mockedFunction.mockResolvedValue([])
    await wrapper.vm.showAddModal();
    await wrapper.vm.$nextTick();
    expect(modal.exists(), "Modal should not be rendered if there are no products").toBeFalsy();
    const toast = wrapper.findComponent({name: "ToastComponent"}); //toast should be rendered if there are no products
    expect(toast.exists(), "Toast is not rendered").toBeTruthy();

    //check the update modal
    wrapper.vm.showUpdateModal({id: 1, productName: "Enphase IQ8+ omvormer", quantity: 1, minimum: 1});
    await wrapper.vm.$nextTick();

    const updateModal = wrapper.findComponent({name: "ModalComponent"});
    expect(updateModal.exists(), "Update modal is not rendered").toBeTruthy();
    expect(updateModal.vm.title, "Title is not correct for update modal").toBe("Update inventory");
    expect(updateModal.vm.activeModal, "incorrect modal child component is rendered").toBe("update-inventory-modal");
  });

  it('add inventory', async () => {
    const mockedFunction = jest.spyOn(wrapper.vm.inventoryService, 'addInventory');
    mockedFunction.mockResolvedValue(
      {warehouse: {id: 1000}, product: {id: 3, productName: "enphase q kabel 3 fase"}, quantity: 30, minimum: 10});

    wrapper.vm.setActiveWarehouse({id: 1000, name: "Solar Sedum"});

    await wrapper.vm.handleAdd(
      {
        warehouse: {id: 1000},
        product: {
          id: 3,
        },
        quantity: 30,
        minimum: 10
      })

    await wrapper.vm.$nextTick();
    expect(mockedFunction, "service should be called").toHaveBeenCalled();
    expect(mockedFunction, "service should be called with the correct format").toHaveBeenCalledWith({
      warehouse: {id: 1000},
      product: {id: 3},
      quantity: 30,
      minimum: 10
    });

    const products = wrapper.vm.getWarehouseProductInfo(wrapper.vm.activeWarehouse);
    expect(products.length, "product should be added to the list").toBe(3);
    expect(products[products.length - 1], "product should be added to the end of the list").toEqual({
      id: 3,
      productName: "enphase q kabel 3 fase",
      quantity: 30,
      minimum: 10
    });
    expect(wrapper.vm.getWarehouseProductInfo({
      id: 1001,
      name: "Superzon"
    }).length, "product should not be added to the list of not active warehouses").toBe(2);

    //toast should message should be displayed
    const toast = wrapper.findComponent({name: "ToastComponent"});
    expect(toast.exists(), "Toast is not rendered").toBeTruthy();
    expect(toast.vm.toastTitle, "Toast title is not correct").toBe("Inventory added!");
    expect(toast.vm.toastMessage, "Toast message is not correct")
      .toBe("Successfully added inventory for Product: enphase q kabel 3 fase and warehouse: " + wrapper.vm.activeWarehouse.name);
  });

  it('update inventory', async () => {
    const mockedFunction = jest.spyOn(wrapper.vm.inventoryService, 'updateInventory');
    mockedFunction.mockResolvedValue(
      {warehouse: {id: 1000}, product: {id: 1, productName: "Enphase IQ8+ omvormer"}, quantity: 30, minimum: 10});

    wrapper.vm.setActiveWarehouse({id: 1000, name: "Solar Sedum"});

    await wrapper.vm.handleUpdate(
      {
        warehouse: {id: 1000},
        product: {
          id: 1,
        },
        quantity: 30,
        minimum: 10
      })

    await wrapper.vm.$nextTick();

    expect(mockedFunction, "service should be called").toHaveBeenCalled();
    expect(mockedFunction, "service should be called with the correct format").toHaveBeenCalledWith({
      warehouse: {id: 1000},
      product: {id: 1},
      quantity: 30,
      minimum: 10
    });

    const products = wrapper.vm.getWarehouseProductInfo(wrapper.vm.activeWarehouse);
    expect(products.length, "updating product should not change product length").toBe(2);
    expect(products.find((product) => product.id === 1), "product should be updated").toEqual({
      id: 1,
      productName: "Enphase IQ8+ omvormer",
      quantity: 30,
      minimum: 10
    });

    //other products should not be changed
    expect(products.find((product) => product.id === 2), "other product should not be changed").not.toEqual({
      id: 2,
      productName: "Enphase IQ8+ omvormer",
      quantity: 30,
      minimum: 10
    });

    //check if same product is not updated in other warehouses.
    //Mock data is different from the data that is updated, so the product should be different
    expect(wrapper.vm.getWarehouseProductInfo({id: 1001, name: "Superzon"}).find((product) => product.id === 1),
      "product should not be updated in other warehouses").not.toEqual({
      id: 1,
      productName: "Enphase IQ8+ omvormer",
      quantity: 30,
      minimum: 10
    });

    //toast should message should be displayed
    const toast = wrapper.findComponent({name: "ToastComponent"});
    expect(toast.exists(), "Toast is not rendered").toBeTruthy();
    expect(toast.vm.toastTitle, "Toast title is not correct").toBe("Inventory updated!");
    expect(toast.vm.toastMessage, "Toast message is not correct").toBe("Successfully updated inventory for Product: Enphase IQ8+ omvormer and warehouse: " + wrapper.vm.activeWarehouse.name);
  });

  it('wrong input is handled correctly', async () => {
    const mockedFunction = jest.spyOn(wrapper.vm.inventoryService, 'addInventory');
    mockedFunction.mockRejectedValue({code: 400, reason: "Bad request"});

    wrapper.vm.setActiveWarehouse({id: 1000, name: "Solar Sedum"});

    await wrapper.vm.handleAdd(
      {
        warehouse: {id: 1000},
        product: {
          id: 3,
        },
        quantity: 30,
        minimum: 10
      })

    await wrapper.vm.$nextTick();

    const toast = wrapper.findComponent({name: "ToastComponent"});
    expect(toast.exists(), "Toast is not rendered").toBeTruthy();
    expect(toast.vm.toastTitle, "Toast title is not correct").toBe("Failed to add Inventory");
    expect(toast.vm.toastMessage, "Toast message is not correct").toBe("Bad request");
  })

  it("empty inventory is handled correctly", async () => {
    wrapper.vm.products = [];
    await wrapper.vm.$nextTick();
    const table = wrapper.findComponent({name: "TableComponent"});
    expect(table.exists(), "Table should be rendered if there are no products").toBeTruthy();
    expect(table.vm.tableData.length, "Table should be empty if there are no products").toBe(0);
  })

  it('table should not have buttons when active warehouse is total', async () => {
    wrapper.vm.setActiveWarehouse("Total");
    await wrapper.vm.$nextTick();
    const table = wrapper.findComponent({name: "TableComponent"});

    expect(table.exists(), "Table should be rendered").toBeTruthy();
    expect(table.vm.hasAddButton, "Table should not have add button when active warehouse is total").toBeFalsy();
    expect(table.vm.hasEditButton, "Table should not have update button when active warehouse is total").toBeFalsy();
    expect(table.vm.hasDeleteButton, "Table should not have delete button when active warehouse is total").toBeFalsy();
  });

  it('table should have buttons when active warehouse is a specific warehouse', async () => {
    wrapper.vm.setActiveWarehouse({id: 1000, name: "Solar Sedum"});
    await wrapper.vm.$nextTick();
    const table = wrapper.findComponent({name: "TableComponent"});
    console.log(table.html())
    expect(table.exists(), "Table should be rendered").toBeTruthy();
    expect(table.vm.hasAddButton, "Table should have add button when active warehouse is a specific warehouse").toBeTruthy();
    expect(table.vm.hasEditButton, "Table should have update button when active warehouse is a specific warehouse").toBeTruthy();
    expect(table.vm.hasDeleteButton, "Table should not have delete button when active warehouse is a specific warehouse").toBeFalsy();

  });
})

describe('InventoryOverview.vue Viewer Flow', () => {

  beforeEach(async () => {
    const router = createRouter({
      history: createWebHashHistory(),
      routes: routes
    })

    const sessionSbService = new SessionSbService();
    sessionSbService.saveTokenInBrowserStorage('token', {
      id: 1,
      name: 'name',
      type: 'viewer',
      team: {
        id: 1,
        name: 'Solar',
        warehouse: {
          id: 1,
          name: 'Solar'
        }
      }
    });

    const mockInventoryAdaptor = new InventoryAdaptor();
    //mock the findAll method
    jest.spyOn(mockInventoryAdaptor, 'findAllForWarehouse').mockResolvedValue([
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
    ])

    wrapper = shallowMount(InventoryOverview, {
      global: {
        provide: {
          inventoryService: mockInventoryAdaptor,
          sessionService: sessionSbService
        },
        plugins: [router]
      }
    })

    //wait for router to be ready
    wrapper.vm.$router.push('/inventory');
    await wrapper.vm.$router.isReady()
    //wait for data to be loaded
    await wrapper.vm.$nextTick();
    wrapper.vm.setActiveWarehouse({id: 1, name: "Solar"});
  });
  it('Viewer data should only be inventory for one warehouse', () => {
      expect(wrapper.vm.activeWarehouse, "Active warehouse should be null for a viewer").toBeNull()
      expect(wrapper.vm.products.length, "Products should only be for the user warehouse").toBe(2);
      expect(wrapper.vm.totalProducts.length, "Total products should be empty").toBe(0);
  });

  it('Router should not contain warehouse name', () => {
      expect(wrapper.vm.$route.path, "Route path should not contain warehouse name").toBe('/inventory');
  });

  it('Crud buttons should not be displayed', () => {
    console.log(wrapper.html())
    const table = wrapper.findComponent({name: "TableComponent"});

    expect(table.exists(), "Table should be rendered").toBeTruthy();
    expect(table.vm.hasAddButton, "Table should not have add button when the user is a viewer").toBeFalsy();
    expect(table.vm.hasEditButton, "Table should not have update button when the user is a viewer").toBeFalsy();
    expect(table.vm.hasDeleteButton, "Table should not have delete button when the user is a viewer").toBeFalsy();
  });
})
