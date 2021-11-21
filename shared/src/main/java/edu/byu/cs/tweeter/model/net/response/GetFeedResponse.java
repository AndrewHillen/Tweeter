package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class GetFeedResponse extends PagedResponse<Status>
{
    public GetFeedResponse(List<Status> items, boolean hasMorePages)
    {
        super(true, items, hasMorePages);
    }

    public GetFeedResponse(String message)
    {
        super(message);
    }
}
