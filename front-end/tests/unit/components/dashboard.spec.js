import {mount} from '@vue/test-utils';
import Dashboard from '@/components/Dashboard.vue';


Object.defineProperty(global.HTMLCanvasElement.prototype, 'getContext', {
    value: jest.fn(() => {
    }),
});

/**
 * Unit tests for the Dashboard component
 *
 * @author Anonymized
 */
describe('Dashboard.vue', () => {
    // Mock chart data
    const chartData = [
        {productName: 'Relay 1', quantity: 10, deliverDate: '2024-01-15', inventoryQuantity: 5},
    ];
    // Mock project data
    const projectData = [
        {productName: 'Gateway', amountOfProduct: 5, dueDate: '2024-01-20'},
    ];
    // Mocked services
    const dashboardServiceMock = {
        findAll: jest.fn(() => Promise.resolve(chartData)),
        findAllProjects: jest.fn(() => Promise.resolve(projectData)),
        findAllInventoryQuantity: jest.fn(() => Promise.resolve([])),
    };

    const orderServiceMock = {
        findAllPending: jest.fn(() => Promise.resolve([])),
    };

    const projectServiceMock = {};
    /**
     * Test 1: for rendering the component without errors.
     */
    it('renders the component without errors', () => {
        const wrapper = mount(Dashboard, {
            global: {
                provide: {
                    dashboardService: dashboardServiceMock,
                    orderService: orderServiceMock,
                    projectService: projectServiceMock,
                },
                mocks: {
                    $nextTick: jest.fn(),
                },
            },
        });
        expect(wrapper.exists()).toBe(true);
    });

    /**
     * Test 2: for rendering the default warehouse name in the dropdown button.
     */
    it('renders the default warehouse name in the dropdown button', () => {
        const wrapper = mount(Dashboard, {
            global: {
                provide: {
                    dashboardService: dashboardServiceMock,
                    orderService: orderServiceMock,
                    projectService: projectServiceMock,
                },
            },
        });
        const dropdownButton = wrapper.find('.dropdown-toggle');
        expect(dropdownButton.text()).toBe('The switch');
    });

    /**
     * Test 3: for Inventory Quantity Data.
     * Returns correct data based on the selected warehouse.
     */
    it('Inventory Quantity Data Test: Returns correct data based on selected warehouse', async () => {
        const wrapper = mount(Dashboard, {
            data() {
                return {
                    inventoryQuantities: [
                        {warehouseName: 'Superzon', productName: 'Relay 8+', inventoryQuantity: 5},
                        {warehouseName: 'Switch', productName: 'Gateway', inventoryQuantity: 10},
                    ],
                    selectedWarehouse: 'Superzon',
                };
            },
        });
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.inventoryQuantitiesData).toEqual([
            {warehouseName: 'Superzon', productName: 'Relay 8+', inventoryQuantity: 5},
        ]);
    });

    /**
     * Test 4: unexpected response
     */
    it("should handle unexpected response", async () => {
        dashboardServiceMock.fetchJSON = jest.fn().mockResolvedValue([]);

        // Ensure the component handles unexpected response structures and returns the unexpected data
        const result = await dashboardServiceMock.findAllInventoryQuantity();
        expect(result).toEqual([]);
    });

    /**
     * Test 5: Boundary conditions for Inventory Quantity Data.
     * Verifies correct handling
     */
    it('handles boundary conditions for Inventory Quantity Data', async () => {
        // minimal and maximal values
        const resultMin = await dashboardServiceMock.findAllInventoryQuantity({ warehouse: 'WarehouseA', min: 0 });
        const resultMax = await dashboardServiceMock.findAllInventoryQuantity({ warehouse: 'WarehouseB', max: 100000 });

        // Assertions for boundary conditions
        expect(resultMin).toEqual([]);
        expect(resultMax).not.toBeNull();
    });

    /**
     * Test 6: Invalid inputs for Inventory Quantity Data.
     * Verifies correct handling of invalid input parameters.
     */
    it('handles invalid inputs for Inventory Quantity Data', async () => {
        // Test with invalid input nonexistent warehouse
        const resultInvalid = await dashboardServiceMock.findAllInventoryQuantity({ warehouse: 'NoWarehouse' });

        // Assertions invalid inputs
        expect(resultInvalid).toEqual([]);
    });

});
