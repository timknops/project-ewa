import fetchIntercept from 'fetch-intercept';
export class FetchInterceptor {
    static theInstance;
    session;
    router;
    unregister;


    constructor(session, router) {
        this.session = session;
        this.router = router;

        FetchInterceptor.theInstance = this;
        this.unregister = fetchIntercept.register(this);
        console.log("Current token after register = ",
            FetchInterceptor.theInstance.session.currentToken)
    }

    /**
     * Gets the token, if the token is empty change nothing.
     * Else if the options parameter is empty, return the Authorization header as the only option with the url
     * Else if the options parameter is not empty, combine the old options with the new Authorization header
     * Note that the options can't be modified which is why they are newly recreated when they have to be changed
     * @param url url of the request
     * @param options options of the request
     * @returns {[undefined,{headers: {Authorization: (*|null)}}]|*[]}
     */
    request(url, options) {
        let token = FetchInterceptor.theInstance.session.currentToken;
        if (token == null){
            return [url, options];
        } else if (options == null){
            return [url, {headers: {Authorization: token}}]
        } else {
            let newOptions = { ...options};
            newOptions.headers = {
                ...options.headers, Authorization: token
            }
            return [url, newOptions]
        }
    }
    //If an error happens during a request, reject the promise
    requestError(error){
        return Promise.reject(error);
    }

    //when a response has a status between 400 and 600, call the handle errors method
    response(response){
        if (response.status >= 400 && response.status < 600){
            FetchInterceptor.theInstance.handleErrors(response);
        }
        return response;
    }
    //If a fetch error happens, reject the promise
    responseError(error){
        return Promise.reject(error);
    }
    //This method is called when a response has a status between 400 and 600.
    // If the status 401, navigate the user back the login page
    handleErrors(response){
        if (response.status === 401){
            this.router.push("/logout");
        }
    }
}
