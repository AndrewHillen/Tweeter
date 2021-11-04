package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersRequest extends PagedRequest<User>
{
    public GetFollowersRequest()
    {
    }

    public GetFollowersRequest(AuthToken authToken, User targetUser, User lastItem, int limit)
    {
        super(authToken, targetUser, lastItem, limit);
    }
}
