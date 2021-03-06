package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.util.FakeData;
import edu.byu.cs.tweeter.server.util.Pair;

public class StatusDAO
{


    public PostStatusResponse postStatus(PostStatusRequest request)
    {
        return new PostStatusResponse(true);
    }

    public GetStoryResponse getStory(GetStoryRequest request)
    {
        Pair<List<Status>, Boolean> pageOfStatus = getFakeData().getPageOfStatus(request.getLastItem(), request.getLimit());
        return new GetStoryResponse(pageOfStatus.getFirst(), pageOfStatus.getSecond());
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
