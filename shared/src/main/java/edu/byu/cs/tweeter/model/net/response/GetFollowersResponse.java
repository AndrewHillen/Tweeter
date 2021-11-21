package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersResponse extends PagedResponse<User>
{
    public GetFollowersResponse(List<User> items, boolean hasMorePages)
    {
        super(true, items, hasMorePages);
    }

    public GetFollowersResponse(String message)
    {
        super(message);
    }
}
