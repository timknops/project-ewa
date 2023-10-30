export class ProductAdaptor {
  resourceUrl;

  constructor(resourceUrl) {
    this.resourceUrl = resourceUrl;
  }

  async fetchJSON(url, options = null) {
    let response = await fetch(url, options);
    if (response.ok) {
      return await response.json();
    } else {
      //Log the error if there is an error provided by the http response body.
      console.log(response, !response.bodyUsed ? await response.text() : "");
      return null;
    }
  }
  async findAll() {
    return await this.fetchJSON(this.resourceUrl);
  }
}