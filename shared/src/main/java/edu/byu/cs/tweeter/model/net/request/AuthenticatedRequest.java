package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class AuthenticatedRequest extends Request
{
    public AuthToken authToken;
    public String userAlias;

    public AuthenticatedRequest()
    {
    }

    public AuthenticatedRequest(AuthToken authToken, String userAlias)
    {
        this.authToken = authToken;
        this.userAlias = userAlias;
    }

    /**
     * Returns the auth token of the user who is making the request.
     *
     * @return the auth token.
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Sets the auth token.
     *
     * @param authToken the auth token.
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getUserAlias()
    {
        return userAlias;
    }

    public void setUserAlias(String userAlias)
    {
        this.userAlias = userAlias;
    }
}
