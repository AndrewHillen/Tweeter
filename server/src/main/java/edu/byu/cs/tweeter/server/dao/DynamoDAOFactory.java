package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.service.BaseService;

public class DynamoDAOFactory implements BaseService.DatabaseFactory
{
    @Override
    public BaseService.AuthTokenDAO getAuthTokenDAO()
    {
        return new AuthTokenDAODynamo();
    }

    @Override
    public BaseService.FollowDAO getFollowDAO()
    {
        return new FollowDAODynamo();
    }

    @Override
    public BaseService.FeedDAO getFeedDAO()
    {
        return new FeedDAODynamo();
    }

    @Override
    public BaseService.StoryDAO getStoryDAO()
    {
        return new StoryDAODynamo();
    }

    @Override
    public BaseService.UserDAO getUserDAO()
    {
        return new UserDAODynamo();
    }
}
