package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class AuthenticatedRequest extends Request
{
    public AuthToken authToken;

    public AuthenticatedRequest()
    {
    }

    public AuthenticatedRequest(AuthToken authToken)
    {
        this.authToken = authToken;
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
}
