export class EmailAdaptor{
    resourceUrl;

    constructor(resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    async fetchJson(url, options = null) {
        let response = await fetch(url, options);
        if (response.ok) {
            return response;
        } else {
            // the response body provided the http-error information
            const error = response;
            //Log the error if there is an error provided by the http response body.
            return Promise.reject({code: error.status, reason: error.message});
        }
    }

    async asyncSendMail(email){
        return await this.fetchJson(this.resourceUrl, {
            method: "POST",
            body: JSON.stringify(email)
        })
    }
}
