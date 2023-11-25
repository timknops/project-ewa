/** This class is responsible for fetching data from the backend API.
 *
 * @author Tim Knops
 */
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

      // Log the error if there is an error provided by the http response body.
      return Promise.reject({ code: error.status, reason: error.message });
    }
  }

  async getAll() {
    return await this.fetchJSON(`${this.resourceUrl}`);
  }

  async get(id) {
    return await this.fetchJSON(`${this.resourceUrl}/${id}`);
  }

  async add(project) {
    return await this.fetchJSON(`${this.resourceUrl}`, {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify(project),
    });
  }

  async delete(id) {
    try {
      return await this.fetchJSON(`${this.resourceUrl}/${id}`, {
        method: "DELETE",
      });
    } catch (e) {
      return Promise.reject(e);
    }
  }

  async update(project) {
    return await this.fetchJSON(`${this.resourceUrl}/${project.id}`, {
      method: "PUT",
      headers: { "content-type": "application/json" },
      body: JSON.stringify(project),
    });
  }
}
