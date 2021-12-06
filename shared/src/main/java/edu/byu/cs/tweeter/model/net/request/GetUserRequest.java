package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class GetUserRequest extends AuthenticatedRequest
{
    public String alias;

    public GetUserRequest()
    {
    }

    public GetUserRequest(AuthToken authToken, String userAlias, String alias)
    {
        super(authToken, userAlias);
        this.alias = alias;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }
}
