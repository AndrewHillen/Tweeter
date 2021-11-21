package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetStoryRequest extends PagedRequest<Status>
{
    public GetStoryRequest()
    {
    }

    public GetStoryRequest(AuthToken authToken, User targetUser, Status lastItem, int limit)
    {
        super(authToken, targetUser, lastItem, limit);
    }
}
