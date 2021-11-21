package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetCountRequest extends AuthenticatedRequest
{
    private User targetUser;

    public GetCountRequest()
    {
    }

    public GetCountRequest(AuthToken authToken, User targetUser)
    {
        super(authToken);
        this.targetUser = targetUser;
    }

    public User getTargetUser()
    {
        return targetUser;
    }

    public void setTargetUser(User targetUser)
    {
        this.targetUser = targetUser;
    }
}
