package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UnFollowRequest extends ChangeFollowRequest
{
    public UnFollowRequest()
    {
    }

    public UnFollowRequest(AuthToken authToken, String userHandle, String targetHandle)
    {
        super(authToken, userHandle, targetHandle);
    }
}
