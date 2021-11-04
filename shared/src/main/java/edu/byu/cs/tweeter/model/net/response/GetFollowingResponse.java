package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingResponse extends PagedResponse<User>
{
    public GetFollowingResponse(List<User> items, boolean hasMorePages)
    {
        super(true, items, hasMorePages);
    }

    public GetFollowingResponse(String message)
    {
        super(message);
    }
}
