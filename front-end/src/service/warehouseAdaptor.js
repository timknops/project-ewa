export class WarehouseAdaptor{
    resourceURL;

    constructor() {
        this.resourceURL = process.env.VUE_APP_API_URL + '/warehouses';
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
        return await this.fetchJSON(this.resourceURL+ "/");
    }

    async findById(id) {
        return await this.fetchJSON(`${this.resourceURL}/${id}`);
    }

    async findOrdersForWarehouse(id) {
        return await this.fetchJSON(`${this.resourceURL}/${id}/orders`);
    }

    async delete(id) {
        try {
            return await this.fetchJSON(`${this.resourceURL}/${id}`, {
                method: "DELETE"
            })
        } catch (e) {
            return Promise.reject(e);
        }
    }

    async add(warehouse) {
        return await this.fetchJSON(this.resourceURL,{
            method: "POST",
            headers: {"content-type": "application/json"},
            body: JSON.stringify(warehouse)
        })
    }

    async update(warehouse) {
        try {
            return await this.fetchJSON(`${this.resourceURL}/${warehouse.id}`, {
                method: "PUT",
                headers: {"content-type": "application/json"},
                body: JSON.stringify(warehouse)
            })
        } catch (e) {
            return Promise.reject(e);
        }
    }
}