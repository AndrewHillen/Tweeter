package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowRequest extends ChangeFollowRequest
{
    public FollowRequest()
    {
    }

    public FollowRequest(AuthToken authToken, String userHandle, String targetHandle)
    {
        super(authToken, userHandle, targetHandle);
    }
}
