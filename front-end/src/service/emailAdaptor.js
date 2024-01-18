export class EmailAdaptor{
    resourceUrl;

    constructor() {
        this.resourceUrl = process.env.VUE_APP_API_URL + "/emails";
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
        return await this.fetchJson(this.resourceUrl + "/loginReset", {
            method: "POST",
            body: (email)
        })
    }
}
