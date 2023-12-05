export class TeamAdaptor {
    resourceUrl;

    constructor() {
        this.resourceUrl = process.env.VUE_APP_API_URL + '/teams';
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
        return await this.fetchJSON(this.resourceUrl);
    }

    async findById(id) {
        return await this.fetchJSON(`${this.resourceUrl}/${id}`);
    }

    async add(team) {
        return await this.fetchJSON(this.resourceUrl, {
            method: "POST",
            headers: {"content-type": "application/json"},
            body: JSON.stringify(team)
        })
    }

    async update(team) {
        try {
            return await this.fetchJSON(`${this.resourceUrl}/${team.id}`, {
                method: "PUT",
                headers: {"content-type": "application/json"},
                body: JSON.stringify(team)
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
}