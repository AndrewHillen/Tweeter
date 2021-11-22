package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.service.BaseService;
import edu.byu.cs.tweeter.server.util.FakeData;
import edu.byu.cs.tweeter.server.util.Pair;

public class FeedDAODynamo implements BaseService.FeedDAO
{
    @Override
    public PostStatusResponse addToFeed(PostStatusRequest request, String userHandle)
    {
        return new PostStatusResponse(true);
    }

    public GetFeedResponse getFeed(GetFeedRequest request)
    {
        Pair<List<Status>, Boolean> pageOfStatus = getFakeData().getPageOfStatus(request.getLastItem(), request.getLimit());
        return new GetFeedResponse(pageOfStatus.getFirst(), pageOfStatus.getSecond());
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}
