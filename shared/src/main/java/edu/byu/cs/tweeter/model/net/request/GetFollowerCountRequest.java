package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowerCountRequest extends GetCountRequest
{
    public GetFollowerCountRequest()
    {
    }

    public GetFollowerCountRequest(AuthToken authToken, String userAlias, User targetUser)
    {
        super(authToken, userAlias, targetUser);
    }
}
