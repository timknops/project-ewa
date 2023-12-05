export class InventoryAdaptor {
  resourceUrl;

  constructor() {
    this.resourceUrl = process.env.VUE_APP_API_URL;
    console.log(this.resourceUrl);
  }

  async fetchJSON(url, options = null) {
    let response = await fetch(url, options);
    if (response.ok) {
      return await response.json();
    } else {
      const error = await response.json();
      //Log the error if there is an error provided by the http response body.
      return Promise.reject({code: error.status, reason: error.message});
    }
  }

  async findAll() {
    return await this.fetchJSON(`${this.resourceUrl}/inventory`);
  }

  async findAllForWarehouse(id){
    return await this.fetchJSON(`${this.resourceUrl}/warehouses/${id}/inventory`)
  }

  async findByIds(wId, pId) {
    return await this.fetchJSON(`${this.resourceUrl}/warehouses/${wId}/products/${pId}`)
  }

  async addInventory(inventory) {
    return await this.fetchJSON(`${this.resourceUrl}/inventory`, {
      method: "POST",
      headers: {"content-type": "application/json"},
      body: JSON.stringify({
        warehouse: {
          id: inventory.warehouse.id
        },
        product: {
          id: inventory.product.id
        },
        quantity: inventory.quantity
      })
    })
  }

  async getProductWithoutInventory(wId) {
    return await this.fetchJSON(`${this.resourceUrl}/warehouses/${wId}/products/without-inventory`)
  }

  async updateInventory(inventory) {
    return await this.fetchJSON(`${this.resourceUrl}/warehouses/${inventory.warehouse.id}/products/${inventory.product.id}`, {
      method: "PATCH",
      headers: {"content-type": "application/json"},
      body: JSON.stringify({quantity: inventory.quantity})
    })
  }
}
