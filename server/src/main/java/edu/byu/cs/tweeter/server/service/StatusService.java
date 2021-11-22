package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class StatusService extends BaseService
{
    BaseService.FeedDAO feedDAO;
    BaseService.StoryDAO storyDAO;

    public StatusService(DatabaseFactory databaseFactory)
    {
        super(databaseFactory);
        feedDAO = databaseFactory.getFeedDAO();
        storyDAO = databaseFactory.getStoryDAO();
    }

    // Needs to add status to user story, get all followers, add status to their feeds
    public PostStatusResponse postStatus(PostStatusRequest request)
    {
        return getStatusDAO().postStatus(request);
    }

    public GetStoryResponse getStory(GetStoryRequest request)
    {
        return getStoryDAO().getStory(request);
    }

    public GetFeedResponse getFeed(GetFeedRequest request)
    {
        return getFeedDao().getFeed(request);
    }

    public StatusDAO getStatusDAO()
    {
        return new StatusDAO();
    }

    public BaseService.FeedDAO getFeedDao()
    {
        return feedDAO;
    }

    public BaseService.StoryDAO getStoryDAO()
    {
        return storyDAO;
    }
}
