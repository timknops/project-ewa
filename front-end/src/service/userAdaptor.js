export class UserAdaptor {

    resourceUrl;

    constructor(resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    async fetchJson(url, options = null) {
        let response = await fetch(url, options);
        if (response.ok) {
            return await response.json();
        } else {
            // the response body provided the http-error information
            const error = await response.json();
            //Log the error if there is an error provided by the http response body.
            return Promise.reject({code: error.status, reason: error.message});
        }
    }

    /**
     * Finds the full view of users
     * @returns {Promise<any>}
     */
    async asyncFindAll(){
        return await this.fetchJson(this.resourceUrl);
    }

    /**
     * Finds the admin view of users, this will exclude passwords
     * @returns {Promise<any>}
     */
    async asyncFindAdmin(){
        return await this.fetchJson(this.resourceUrl + "/admin")
    }

    async asyncFindById(id){
        return await this.fetchJson(`${this.resourceUrl}/${id}`)
    }

    async asyncAdd(user){
        return await this.fetchJson(this.resourceUrl, {
            method: "POST",
            headers: {"content-type": "application/json",},
            body: JSON.stringify(user)
        })
    }

    async asyncSave(user){
        try {
            return await this.fetchJson(`${this.resourceUrl}/${user.id}`, {
                method: "PUT",
                headers: {"content-type": "application/json"},
                body: JSON.stringify(user)
            })
        } catch (e){
            Promise.reject(e)
        }
    }

    async asyncDelete(id){
        try {
            return await this.fetchJson(`${this.resourceUrl}/${id}`,{
                method: "DELETE"
            })
        } catch (e){
            Promise.reject(e)
        }
    }
}
