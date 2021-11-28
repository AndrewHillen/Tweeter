package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest extends ChangeFollowRequest
{
    public FollowRequest()
    {
    }

    public FollowRequest(AuthToken authToken, String userHandle, User targetUser, User follower)
    {
        super(authToken, userHandle, targetUser, follower);
    }
}
