/**
 * The adaptor for communicating with the orders controller
 *
 * @author Julian Kruithof
 */
export class OrderAdaptor {
  resourceUrl;

  constructor() {
    this.resourceUrl = process.env.VUE_APP_API_URL + '/orders';
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
    return await this.fetchJSON(this.resourceUrl);
  }

  async findById(id) {
    return await this.fetchJSON(`${this.resourceUrl}/${id}`);
  }

  async add(order) {
    return await this.fetchJSON(this.resourceUrl, {
      method: "POST",
      headers: {"content-type": "application/json"},
      body: JSON.stringify(order)
    })
  }

  async update(order) {
    try {
      return await this.fetchJSON(`${this.resourceUrl}/${order.id}`, {
        method: "PUT",
        headers: {"content-type": "application/json"},
        body: JSON.stringify(order)
      })
    } catch (e) {
      return Promise.reject(e);
    }
  }

  async delete(id) {
    try {
      return await this.fetchJSON(`${this.resourceUrl}/${id}`, {
        method: "DELETE"
      })
    } catch (e) {
      return Promise.reject(e);
    }
  }

  async getItemsForOrder(id) {
      return await this.fetchJSON(`${this.resourceUrl}/${id}/items`)
  }
}