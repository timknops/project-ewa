export class SessionSbService {
  browserItemStorage;
  resourcesURL;
  currentToken;
  _currentUser;

  constructor(resourcesURL, browserItemStorage) {
    this.browserItemStorage = browserItemStorage;
    this.resourcesURL = resourcesURL;
    this._currentUser = null;
    this.currentToken = null;
    this.getTokenFromBrowserStorage();
  }

  /**
   * if no token is found, get the token and user from the localstorage
   * @returns the currenttoken
   */
  getTokenFromBrowserStorage() {
    if (this.currentToken != null) {
      return this.currentToken;
    }

    this.currentToken = window.localStorage.getItem(this.browserItemStorage);
    this._currentUser = JSON.parse(
      window.localStorage.getItem(this.browserItemStorage + "_USER")
    );

    return this.currentToken;
  }

  /**
   * Set the token and user in the localstorage. If the token provided was empty, remove token and user from localstorage
   * @param token token of the logged-in user
   * @param account user that was logged-in
   */
  saveTokenInBrowserStorage(token, account) {
    this.currentToken = token;
    this._currentUser = account;

    if (token == null) {
      this.currentToken = null;
      window.localStorage.removeItem(this.browserItemStorage);
      window.localStorage.removeItem(this.browserItemStorage + "_USER");
    } else {
      window.localStorage.setItem(this.browserItemStorage, token);
      window.localStorage.setItem(
        this.browserItemStorage + "_USER",
        JSON.stringify(account)
      );
    }
  }

  /**
   * Async method that tries to log the user into the back-end.
   * If the response from the back-end is ok, save the token in the localstorage.
   * Else log the response and return nothing
   * @param username the entered name of the user
   * @param password the entered password of the user
   * @returns {Promise<any|null>}
   */
  async asyncLogin(username, password) {
    const body = JSON.stringify({ username: username, password: password });
    let response = await fetch(this.resourcesURL + "/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: body,
      credentials: "include",
    });
    if (response.ok) {
      let account = await response.json();
      this.saveTokenInBrowserStorage(
        response.headers.get("Authorization"),
        account
      );
      return account;
    } else {
      console.log(response);
      return null;
    }
  }
  //Logs the user out, by setting the browser storage to null for both account and token
  logOut() {
    this.saveTokenInBrowserStorage(null, null);
  }

  //returns true or false based on whether the user is an admin or not
  isAdmin() {
    return this._currentUser?.type?.toUpperCase().includes("ADMIN");
  }
  //returns true or false based on whether the user is logged in or not
  isAuthenticated() {
    return this._currentUser != null;
  }

  get currentUser() {
    return {
      name: this._currentUser.name,
      type: this._currentUser.type,
      team: this._currentUser.team,
    };
  }
}
