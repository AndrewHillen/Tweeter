package edu.byu.cs.tweeter.server.service;

import java.util.Date;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class StatusService extends BaseService
{
    BaseService.FeedDAO feedDAO;
    BaseService.StoryDAO storyDAO;
    FollowDAO followDAO;

    public StatusService(DatabaseFactory databaseFactory)
    {
        super(databaseFactory);
        feedDAO = databaseFactory.getFeedDAO();
        storyDAO = databaseFactory.getStoryDAO();
        followDAO = databaseFactory.getFollowDAO();
    }

    // Needs to add status to user story, get all followers, add status to their feeds
    public PostStatusResponse postStatus(PostStatusRequest request)
    {
        int limit = 10;
        User lastUser = null;
        boolean keepGoing = true;
        String userHandle = request.getUserAlias();
        Status status = request.getStatus();
        status.getUser().setImageBytes(null);
        long timestamp = new Date().getTime();

        status.setTimestamp(timestamp);

        getStoryDAO().addToStory(status, userHandle, timestamp);
        while(keepGoing)
        {
            GetFollowersResponse getFollowersResponse = getFollowingDAO().getFollowers(userHandle, lastUser, limit);
            List<User> users = getFollowersResponse.getItems();
            lastUser = users.get(users.size() - 1);
            keepGoing = getFollowersResponse.getHasMorePages();
            for(User user : users)
            {
                getFeedDao().addToFeed(status, user.getAlias(), timestamp);
            }
        }

        return new PostStatusResponse(true);
    }

    public GetStoryResponse getStory(GetStoryRequest request)
    {
        if(request.getLastItem() != null)
        {
            request.getLastItem().getUser().setImageBytes(null);
        }
        return getStoryDAO().getStory(request.getTargetUser().getAlias(), request.getLastItem(), request.getLimit());
    }

    public GetFeedResponse getFeed(GetFeedRequest request)
    {
        if(request.getLastItem() != null)
        {
            request.getLastItem().getUser().setImageBytes(null);
        }
        return getFeedDao().getFeed(request.getTargetUser().getAlias(), request.getLastItem(), request.getLimit());
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

    FollowDAO getFollowingDAO() {
        return followDAO;
    }
}
