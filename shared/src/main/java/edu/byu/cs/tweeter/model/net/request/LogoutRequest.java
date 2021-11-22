package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class LogoutRequest extends AuthenticatedRequest
{

    public LogoutRequest()
    {
    }

    public LogoutRequest(AuthToken authToken, String userHandle)
    {
        super(authToken, userHandle);
    }
}
