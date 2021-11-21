package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class StatusService
{
    public PostStatusResponse postStatus(PostStatusRequest request)
    {
        return getStatusDAO().postStatus(request);
    }

    public GetStoryResponse getStory(GetStoryRequest request)
    {
        return getStatusDAO().getStory(request);
    }

    public GetFeedResponse getFeed(GetFeedRequest request)
    {
        return getStatusDAO().getFeed(request);
    }

    public StatusDAO getStatusDAO()
    {
        return new StatusDAO();
    }
}
