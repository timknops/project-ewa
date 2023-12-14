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
       const data = await this.fetchJSON(this.resourceURL);
        return data.map(item => ({
            warehouseName: item.warehouseName,
            itemName: item.itemName,
            quantity: item.quantity,
            deliverDate: item.deliverDate,
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