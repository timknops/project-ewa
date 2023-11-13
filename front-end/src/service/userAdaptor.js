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

    /**
     * Finds the user based on the id
     * @param id id of the user being searched for
     * @returns {Promise<any>}
     */
    async asyncFindById(id){
        return await this.fetchJson(`${this.resourceUrl}/${id}`)
    }

    /**
     * Adds the given user to the back-end
     * @param user the given user that needs to be added
     * @returns {Promise<any>}
     */
    async asyncAdd(user){
        return await this.fetchJson(this.resourceUrl, {
            method: "POST",
            headers: {"content-type": "application/json",},
            body: JSON.stringify(user)
        })
    }

    /**
     * Updates the given user to the back-end
     * @param user the given user that needs to be updated
     * @returns {Promise<any>}
     */
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

    /**
     * Deletes the given user from the user list in the back-end
     * @param id the given user that needs to be deleted
     * @returns {Promise<any>}
     */
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
