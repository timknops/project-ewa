/** Responsible for fetching data from the backend API.
 *
 * @author Tim Knops
 */
export class ProjectAdaptor {
  resourceUrl;

  /**
   * Constructs a new ProjectAdaptor instance.
   */
  constructor() {
    this.resourceUrl = process.env.VUE_APP_API_URL + '/projects';
  }

  /**
   * Fetches JSON data from the specified URL.
   * @param {string} url The URL to fetch JSON data from.
   * @param {object} [options=null] Additional options for the fetch request.
   * @returns {Promise<object>} A promise that resolves to the fetched JSON data.
   * @throws {object} An object containing the error code and reason if the fetch request fails.
   */
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

  /**
   * Retrieves all projects.
   * @returns {Promise<object>} A promise that resolves to the fetched JSON data representing all projects.
   */
  async getAll() {
    return await this.fetchJSON(`${this.resourceUrl}`);
  }

  /**
   * Retrieves a project by its ID.
   * @param {string} id The ID of the project to retrieve.
   * @returns {Promise<object>} A promise that resolves to the fetched JSON data representing the project.
   */
  async get(id) {
    return await this.fetchJSON(`${this.resourceUrl}/${id}`);
  }

  /**
   * Adds a new project.
   * @param {object} project The project object to add.
   * @returns {Promise<object>} A promise that resolves to the fetched JSON data representing the added project.
   */
  async add(project) {
    return await this.fetchJSON(`${this.resourceUrl}`, {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify(project),
    });
  }

  /**
   * Deletes a project by its ID.
   * @param {string} id The ID of the project to delete.
   * @returns {Promise<object>} A promise that resolves to the fetched JSON data representing the deleted project.
   * @throws {object} An object containing the error code and reason if the delete request fails.
   */
  async delete(id) {
    try {
      return await this.fetchJSON(`${this.resourceUrl}/${id}`, {
        method: "DELETE",
      });
    } catch (e) {
      return Promise.reject(e);
    }
  }

  /**
   * Updates a project.
   * @param {object} project The updated project object.
   * @returns {Promise<object>} A promise that resolves to the fetched JSON data representing the updated project.
   */
  async update(project) {
    return await this.fetchJSON(`${this.resourceUrl}/${project.project.id}`, {
      method: "PUT",
      headers: { "content-type": "application/json" },
      body: JSON.stringify(project),
    });
  }

  /**
   * Retrieves data for the project add/update modal.
   * @returns {Promise<object>} A promise that resolves to the fetched JSON data representing the project add modal data.
   */
  async getProjectModalData() {
    return await this.fetchJSON(`${this.resourceUrl}/modal`);
  }
}
