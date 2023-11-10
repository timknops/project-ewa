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

    async asyncFindAll(){
        return await this.fetchJson(this.resourceUrl);
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
            return await this.fetchJson(this.resourceUrl, {
                method: "PUT",
                headers: {"content-type": "application/json"},
                body: JSON.stringify(user)
            })
        } catch (e){
            Promise.reject(e)
        }
    }

    async asyncUpdate(id){
        try {
            return await this.fetchJson(`${this.resourceUrl}/${id}`,{
                method: "DELETE"
            })
        } catch (e){
            Promise.reject(e)
        }
    }
}
