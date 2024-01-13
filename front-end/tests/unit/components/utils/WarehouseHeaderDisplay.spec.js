import {shallowMount} from "@vue/test-utils";
import WarehouseHeaderDisplay from "@/components/util/WarehouseHeaderDisplay.vue";
import {WarehouseAdaptor} from "@/service/warehouseAdaptor";
import {createRouter, createWebHashHistory} from "vue-router";
import inventoryOverview from "@/components/InventoryOverview.vue";
import {SessionSbService} from "@/service/SessionSbService";

  let wrapper;
  jest.mock('@/service/warehouseAdaptor');
  const routes = [
    {
      path: '/inventory',
      component: {default : inventoryOverview},
      children: [
        {
          path: ':warehouse',
          component: inventoryOverview,
        }
      ]
    },
    {
      path: '/',
      redirect: '/inventory'
    }

  ]

/**
 * @test {WarehouseHeaderDisplay}
 *
 * Test the WarehouseHeaderDisplay component with a mock router and route and a mock warehouseAdaptor.
 *
 * @autor Julian Kruithof
 */
describe('WarehouseHeaderDisplay.vue',  () => {

  beforeEach(async () => {
    const router = createRouter({
      history: createWebHashHistory(),
      routes,
    })
    const mockWarehouseAdaptor = new WarehouseAdaptor();
    const sessionSbService = new SessionSbService();
    sessionSbService.saveTokenInBrowserStorage('token', {
      id: 1,
      name: 'name',
      type: 'admin'
    })

    jest.spyOn(mockWarehouseAdaptor, 'findAll').mockResolvedValue([
      {
        name: 'Solar',
        id: 1
      },
      {
        name: 'EHES',
        id: 2
      }
    ])

    //set props to not break the component on mount, props are changed in the tests
    wrapper = shallowMount(WarehouseHeaderDisplay,
      {
        global: {
          plugins: [router],
          provide: {
            warehouseService: mockWarehouseAdaptor,
            sessionService: sessionSbService
          },
        },
        props: {
          activeWarehouse : {},
          hasNoTotalOption: false,
          totalText: 'Total'
        }
      })

    await wrapper.vm.$router.isReady();
    await wrapper.vm.$nextTick();
  })

  it('WarehouseHeaderDisplay should render properly', async () => {
    expect(wrapper.element.children.length, "WarehouseHeaderDisplay is not rendered properly").toBeGreaterThan(0);
  })

  it('active warehouse should have class active', async () => {
    await wrapper.setProps({activeWarehouse: "Total"})
    const warehouse = wrapper.find('.active');
    expect(warehouse.text(), "Active warehouse should be displayed").toMatch('Total');

    await wrapper.setProps({activeWarehouse: {id: 1, name: 'Solar'}})
    const warehouse2 = wrapper.find('.active');
    expect(warehouse2.text(), "Active warehouse should be displayed").toMatch('Solar');
  })

  it('Admin should have a list of warehouse', async () => {
    wrapper.vm.sessionService.saveTokenInBrowserStorage('token', {
      id: 1,
      name: 'name',
      type: 'admin'
    })
    await wrapper.setProps({hasNoTotalOption: true})

    const warehouses = wrapper.findAll('.warehouse-select');
    expect(warehouses.length, "Admin should have a list of warehouse").toBe(2);
    expect(warehouses.includes('Total'), "List should not have Total option if hasNoTotalOption is true")
      .toBeFalsy();
  });

  it('admin should have list of warehouse and total if hasNoTotalOption is false', async () => {
    wrapper.vm.sessionService.saveTokenInBrowserStorage('token', {
      id: 1,
      name: 'name',
      type: 'admin'
    })
    await wrapper.setProps({hasNoTotalOption: false})

    const warehouses = wrapper.findAll('.warehouse-select');
    expect(warehouses.length, "Admin should have a list of warehouse and total").toBe(3);
    expect(warehouses[0].text(), "First warehouse should be Total").toMatch('Total');
  });

  it('findWarehouseByName returns correct warehouse', () => {
    expect(wrapper.vm.findWarehouseByName('Solar'), "findWarehouseByName should return correct warehouse").toMatchObject({name: 'Solar', id: 1});
    expect(wrapper.vm.findWarehouseByName('Total'), "findWarehouseByName should return correct warehouse").toMatch('Total');
  });

  it("Warehouse should emit proper event in created hook",  async () => {
    expect(wrapper.emitted(), "Display should emit the initial warehouse").toHaveProperty('setActiveWarehouse');
    //since the route is not set in the test and hasNoTotal is false, the active warehouse should be Total
    expect(wrapper.emitted('setActiveWarehouse')[0][0], "Display should emit the first warehouse in the list")
      .toMatch('Total');

    //rerun the created hook when total is not an option
    await wrapper.setProps({hasNoTotalOption: true})
    await wrapper.vm.$options.created.call(wrapper.vm);
    expect(wrapper.emitted('setActiveWarehouse')[1][0], "Display should emit the initial warehouse").toMatchObject({name: 'Solar', id: 1});

    //Rerun the created hook with a different route
    await wrapper.vm.$router.push(wrapper.vm.$route.matched[0].path + '/EHES')
    await wrapper.vm.$options.created.call(wrapper.vm);
    expect(wrapper.emitted('setActiveWarehouse')[2][0], "Display should emit the warehouse from the route param")
      .toMatchObject({name: 'EHES', id: 2});
  })

  it('When a warehouse is clicked setActiveWarehouse should fire', async () => {
    const warehouse = wrapper.findAll('.warehouse-select')[2];

    await warehouse.trigger('click');
    //The created already fired an emitted event, so the second emitted event should be the one from the click
    expect(wrapper.emitted('setActiveWarehouse')[1][0], "setActiveWarehouse should fire when a warehouse is clicked")
      .toMatchObject({name: 'EHES', id: 2});
  });

  afterEach(() => {
    wrapper.unmount()
  })
})

describe('WarehouseHeaderDisplay.vue errors',  () => {
  it('If warehouse service fails, the diplay is not broken', async () => {
    const mockWarehouseAdaptor = new WarehouseAdaptor();
    const router = createRouter({
      history: createWebHashHistory(),
      routes,
    })

    const sessionSbService = new SessionSbService();
    sessionSbService.saveTokenInBrowserStorage('token', {
      id: 1,
      name: 'name',
      type: 'admin'
    })

    wrapper = shallowMount(WarehouseHeaderDisplay, {
      global: {
        provide: {
          warehouseService: mockWarehouseAdaptor,
          sessionService: sessionSbService
        },
        plugins: [router]
      },
      props: {
        activeWarehouse : {},
        hasNoTotalOption: false,
        totalText: 'Total'
      }
    })

    jest.spyOn(mockWarehouseAdaptor, 'findAll').mockRejectedValue({code: 500, reason: 'Internal server error'});
    expect(wrapper.element.children.length, "WarehouseHeaderDisplay is not rendered properly").toBeGreaterThan(0);
    expect(wrapper.vm.warehouses.length, "WarehouseHeaderDisplay should have an empty list of warehouses")
      .toBe(0);
  });
})

describe('WarehouseHeaderDisplay.vue Viewer Flow',  () => {
  it('Viewer should only see one warehouse', async () => {
    const router = createRouter({
      history: createWebHashHistory(),
      routes,
    })
    const mockWarehouseAdaptor = new WarehouseAdaptor();
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
    })

    jest.spyOn(mockWarehouseAdaptor, 'findAll').mockResolvedValue([
      {
        name: 'Solar',
        id: 1
      },
      {
        name: 'EHES',
        id: 2
      }
    ])

    //set props to not break the component on mount, props are changed in the tests
    wrapper = shallowMount(WarehouseHeaderDisplay,
      {
        global: {
          plugins: [router],
          provide: {
            warehouseService: mockWarehouseAdaptor,
            sessionService: sessionSbService
          },
        },
        props: {
          activeWarehouse : {},
          hasNoTotalOption: false,
          totalText: 'Total'
        }
      })

    await wrapper.vm.$router.isReady();
    await wrapper.vm.$nextTick();


    const warehouses = wrapper.findAll('.warehouse-select');
    expect(warehouses.length, "Viewer should have one warehouse").toBe(1);
    expect(warehouses[0].text(), "First warehouse should be Solar").toMatch('Solar');
  });
})