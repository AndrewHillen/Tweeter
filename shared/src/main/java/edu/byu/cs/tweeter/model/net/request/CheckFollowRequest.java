package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class CheckFollowRequest extends ChangeFollowRequest
{
    public CheckFollowRequest()
    {
    }

    public CheckFollowRequest(AuthToken authToken, String userHandle, String targetHandle)
    {
        super(authToken, userHandle, targetHandle);
    }
}
