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
}
