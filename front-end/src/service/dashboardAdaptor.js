export class DashboardAdaptor {
    resourceURL;

    constructor() {
        this.resourceURL = process.env.VUE_APP_API_URL + '/dashboard-items';
    }

    async fetchJSON(url, options = null) {
        let response = await fetch(url, options);
        if (response.ok) {
            return await response.json();
        } else {
            const error = await response.json();
            return Promise.reject({code: error.status, reason: error.message});
        }
    }

    async findAll() {
       const data = await this.fetchJSON(this.resourceURL + '/inventory');
        // console.log('Fetched data:', data);
        return data.map(item => ({
            warehouseName: item.warehouseName,
            productName: item.productName,
            quantity: item.quantity,
            inventoryQuantity: item.inventoryQuantity,
            deliverDate: item.deliverDate,
        }));
    }

    async findAllProjects() {
        const data = await this.fetchJSON(this.resourceURL + '/project');
        // console.log('Fetched project data:', data);
        return data.map(item => ({
            dueDate: item.dueDate,
            projectName: item.projectName,
            warehouseName: item.warehouseName,
            productName: item.productName,
            amountOfProduct: item.amountOfProduct,
        }));
    }

    async findAllWarehouses() {
        const data = await this.fetchJSON(this.resourceURL);
        const uniqueWarehouseNames = Array.from(new Set(data.map(item => item.warehouseName)));
        return uniqueWarehouseNames.map(warehouseName => ({
            warehouseName: warehouseName,
        }));
    }


}