export class ProjectAdaptor {
  resourceUrl;

  constructor(resourceUrl) {
    this.resourceUrl = resourceUrl;
  }

  async fetchJSON(url, options = null) {
    let response = await fetch(url, options);

    if (response.ok) {
      return await response.json();
    } else {
      const error = await response.json();

      //Log the error if there is an error provided by the http response body.
      return Promise.reject({ code: error.status, reason: error.message });
    }
  }

  async getAll() {
    return await this.fetchJSON(`${this.resourceUrl}`);
  }
}
