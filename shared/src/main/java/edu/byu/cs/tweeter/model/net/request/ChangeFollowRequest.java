package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class ChangeFollowRequest extends AuthenticatedRequest
{
    protected User targetUser;
    protected User follower;

    public ChangeFollowRequest()
    {
    }

    public ChangeFollowRequest(AuthToken authToken, String userHandle, User targetUser, User follower)
    {
        super(authToken, userHandle);
        this.targetUser = targetUser;
        this.follower = follower;
    }


    public User getTargetUser()
    {
        return targetUser;
    }

    public void setTargetUser(User targetUser)
    {
        this.targetUser = targetUser;
    }

    public User getFollower()
    {
        return follower;
    }

    public void setFollower(User follower)
    {
        this.follower = follower;
    }
}
