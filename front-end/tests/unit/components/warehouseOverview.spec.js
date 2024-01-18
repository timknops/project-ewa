import { shallowMount } from "@vue/test-utils";
import { WarehouseAdaptor } from "@/service/warehouseAdaptor";
import WarehouseOverview from "@/components/WarehouseOverview.vue";
import {createRouter, createWebHashHistory} from "vue-router";

// Mock the adaptor
jest.mock('@/service/warehouseAdaptor')

// Mock router
const routes = [
    {
        path: "/warehouses",
        component: WarehouseOverview,
        meta: { icon: "fa-solid fa-warehouse", requiresLogin: true },
    },
    {
        path: "/",
        redirect: "/warehouses",
    }
]

let wrapper;

/**
 * Tests for WarehouseOverview component
 * @author Wilco van de Pol
 */
describe("WarehouseOverview", () => {
    beforeEach(async () => {
        const router = createRouter({
            history: createWebHashHistory(),
            routes: routes,
        })

        const mockWarehouseAdaptor = new WarehouseAdaptor();

        //mock the findAll method for the warehouses
        jest.spyOn(mockWarehouseAdaptor, 'findAll').mockResolvedValue(mockWarehouses)

        wrapper = shallowMount(WarehouseOverview, {
            global: {
                provide: {
                    warehouseService: mockWarehouseAdaptor,
                },
                plugins: [router]
            }
        })

        //wait for router to be ready
        wrapper.vm.$router.push('/warehouses');
        await wrapper.vm.$router.isReady()
        //wait for data to be loaded
        await wrapper.vm.$nextTick();
    });

    it('renders warehouse component correctly', () => {
        // Assert that the warehouse component is displayed
        expect(wrapper.exists()).toBe(true);
    });

    it('renders table component correctly', () => {
        // Assert that the table component is displayed
        expect(wrapper.findComponent({ name: 'TableComponent' }).exists()).toBe(true);
    });

    it('displays spinner when warehouses are loading', async () => {
         // Update the data property to simulate loading
        wrapper.setData({ wareHousesAreLoading: true });

        // Wait for action to be completed
        await wrapper.vm.$nextTick();

        // Assert that the spinner component is rendered when loading
        expect(wrapper.findComponent({ name: 'SpinnerComponent' }).exists()).toBe(true);
    });
});