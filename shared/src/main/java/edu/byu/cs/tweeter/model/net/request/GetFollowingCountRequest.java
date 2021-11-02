package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingCountRequest extends GetCountRequest
{
    public GetFollowingCountRequest()
    {
    }

    public GetFollowingCountRequest(AuthToken authToken, User targetUser)
    {
        super(authToken, targetUser);
    }
}
