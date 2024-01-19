/**
 * DashboardAdaptor class is responsible for fetching data related to dashboard items
 * from the API endpoints.
 *
 * @author Hanan Ouardi
 */
export class DashboardAdaptor {
    resourceURL;

    /**
     * Constructs a new DashboardAdaptor instance.
     * Initializes the resource URL for the dashboard API based on the environment.
     */
    constructor() {
        this.resourceURL = process.env.VUE_APP_API_URL + '/dashboard-items';
    }

    /**
     * Asynchronously fetches JSON data from the URL
     *
     * @param url
     * @param options
     * @returns {Promise<any>}
     */
    async fetchJSON(url, options = null) {
        let response = await fetch(url, options);
        if (response.ok) {
            return await response.json();
        } else {
            const error = await response.json();
            return Promise.reject({code: error.status, reason: error.message});
        }
    }

    /**
     * Asynchronously fetches inventory data from the dashboard API.
     *
     * @returns {Promise<*>}  A promise that resolves to an array of adapted project data.
     */
    async findAll() {
        const data = await this.fetchJSON(this.resourceURL + '/inventory');
        return data.map(item => ({
            warehouseName: item.warehouseName,
            productName: item.productName,
            quantity: item.quantity,
            inventoryQuantity: item.inventoryQuantity,
            deliverDate: item.deliverDate,
        }));
    }

    /**
     * Asynchronously fetches project data from the dashboard API.
     *
     * @returns {Promise<*>}  A promise that resolves to an array of adapted project data.
     */
    async findAllProjects() {
        const data = await this.fetchJSON(this.resourceURL + '/project');
        return data.map(item => ({
            dueDate: item.dueDate,
            projectName: item.projectName,
            warehouseName: item.warehouseName,
            productName: item.productName,
            amountOfProduct: item.amountOfProduct,
        }));
    }

    /**
     * Asynchronously fetches all inventory quantity data from the dashboard API.
     *
     * @returns {Promise<*>} A promise that resolves to an array of adapted project data.
     */
    async findAllInventoryQuantity() {
        const data = await this.fetchJSON(this.resourceURL + '/inventory-quantity');
        return data.map(item => ({
            warehouseName: item.warehouseName,
            productName: item.productName,
            inventoryQuantity: item.inventoryQuantity,
        }));
    }
}