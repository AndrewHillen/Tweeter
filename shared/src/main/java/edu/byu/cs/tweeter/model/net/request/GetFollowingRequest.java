package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingRequest extends PagedRequest<User>
{
    public GetFollowingRequest()
    {
    }

    public GetFollowingRequest(AuthToken authToken, String userAlias, User targetUser, User lastItem, int limit)
    {
        super(authToken, userAlias, targetUser, lastItem, limit);
    }
}
