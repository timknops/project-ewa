export class DashboardAdaptor{
    resourceURL;

    constructor(resourceURL) {
        this.resourceURL = resourceURL;
    }

    async fetchJSON(url, options = null) {
        let response = await fetch(url, options);
        if (response.ok) {
            return await response.json();
        } else {
            const error = await response.json();
            return Promise.reject({code: error.status, reason: error.reason});
        }
    }

    async getForecastingData() {
        return await this.fetchJSON(this.resourceURL);
    }
}