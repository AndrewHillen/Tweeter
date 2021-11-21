package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingRequest extends PagedRequest<User>
{
    public GetFollowingRequest()
    {
    }

    public GetFollowingRequest(AuthToken authToken, User targetUser, User lastItem, int limit)
    {
        super(authToken, targetUser, lastItem, limit);
    }
}
