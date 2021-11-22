package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class LogoutRequest extends AuthenticatedRequest
{
    String userHandle;

    public LogoutRequest()
    {
    }

    public LogoutRequest(AuthToken authToken, String userHandle)
    {
        super(authToken);
        this.userHandle = userHandle;
    }

    public String getUserHandle()
    {
        return userHandle;
    }

    public void setUserHandle(String userHandle)
    {
        this.userHandle = userHandle;
    }
}
