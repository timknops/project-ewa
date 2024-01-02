import {shallowMount} from "@vue/test-utils";
import WarehouseHeaderDisplay from "@/components/util/WarehouseHeaderDisplay.vue";
import {WarehouseAdaptor} from "@/service/warehouseAdaptor";
import {createRouter, createWebHashHistory} from "vue-router";
import inventoryOverview from "@/components/InventoryOverview.vue";

  let wrapper;
  jest.mock('@/service/warehouseAdaptor');
  const routes = [
    {
      path: '/',
      component: {default : inventoryOverview},
    },
    {
      path: '/:warehouse',
      component: WarehouseHeaderDisplay
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
            warehouseService: mockWarehouseAdaptor
          },
        },
        props: {
          activeUser: {id: 1, name: 'test', role: 'admin', team: {id: 1, name: 'test', warehouse: {id: 1, name: 'Solar'}}},
          activeWarehouse : "Total",
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
    await wrapper.setProps({activeUser: {id: 1, name: 'test', role: 'admin', team: {id: 1, name: 'test', warehouse: {id: 1, name: 'Solar'}}}})
    await wrapper.setProps({hasNoTotalOption: true})

    const warehouses = wrapper.findAll('.warehouse-select');
    expect(warehouses.length, "Admin should have a list of warehouse").toBe(2);
    expect(warehouses.includes('Total'), "List should not have Total option if hasNoTotalOption is true")
      .toBeFalsy();
  });

  it('admin should have list of warehouse and total if hasNoTotalOption is false', async () => {
    await wrapper.setProps({activeUser: {id: 1, name: 'test', role: 'admin', team: {id: 1, name: 'test', warehouse: {id: 1, name: 'Solar'}}}})
    await wrapper.setProps({hasNoTotalOption: false})

    const warehouses = wrapper.findAll('.warehouse-select');
    expect(warehouses.length, "Admin should have a list of warehouse and total").toBe(3);
    expect(warehouses[0].text(), "First warehouse should be Total").toMatch('Total');
  });

  it('findWarehouseByName returns correct warehouse', () => {
    expect(wrapper.vm.findWarehouseByName('Solar'), "findWarehouseByName should return correct warehouse").toMatchObject({name: 'Solar', id: 1});
    expect(wrapper.vm.findWarehouseByName('Total'), "findWarehouseByName should return correct warehouse").toMatch('Total');
  });

  it('viewer should only have one warehouse', async () => {
    await wrapper.setProps({activeUser: {id: 1, name: 'test', role: 'viewer', team: {id: 1, name: 'test', warehouse: {id: 1, name: 'Solar'}}}})

    const warehouses = wrapper.findAll('.warehouse-select');
    expect(warehouses.length, "Viewer should have one warehouse").toBe(1);
    expect(warehouses[0].text(), "First warehouse should be Solar").toMatch('Solar');
  });

  it("Warehouse should emit proper event in created hook",  () => {
    console.log(wrapper.emitted())
  })

  afterEach(() => {
    wrapper.unmount()
  })
})