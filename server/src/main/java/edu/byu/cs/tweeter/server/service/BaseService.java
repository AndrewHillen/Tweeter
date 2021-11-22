package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.CheckFollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UnFollowRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.CheckFollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.UnFollowResponse;

public class BaseService
{
    protected DatabaseFactory databaseFactory;
    protected AuthTokenDAO authTokenDAO;

    public BaseService(DatabaseFactory databaseFactory)
    {
        this.databaseFactory = databaseFactory;
        authTokenDAO = databaseFactory.getAuthTokenDAO();
    }

    public interface DatabaseFactory
    {
        AuthTokenDAO getAuthTokenDAO();
        FollowDAO getFollowDAO();
        FeedDAO getFeedDAO();
        StoryDAO getStoryDAO();
        UserDAO getUserDAO();
    }

    public boolean checkAuthorization(AuthToken authToken, String userHandle)
    {
        return authTokenDAO.checkAuthToken(authToken, userHandle);
    }

    public interface AuthTokenDAO
    {
        AuthToken generateAuthToken(String userHandle);
        boolean checkAuthToken(AuthToken authToken, String userHandle);
        LogoutResponse logout(LogoutRequest request);
    }

    public interface FollowDAO
    {
        GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request);
        GetFollowerCountResponse getFollowerCount(GetFollowerCountRequest request);
        FollowResponse follow(FollowRequest request);
        UnFollowResponse unfollow(UnFollowRequest request);
        CheckFollowResponse checkFollow(CheckFollowRequest request);
        GetFollowersResponse getFollowers(GetFollowersRequest request);
        GetFollowingResponse getFollowing(GetFollowingRequest request);
    }

    public interface FeedDAO
    {
        PostStatusResponse addToFeed(PostStatusRequest request, String userHandle);
        GetFeedResponse getFeed(GetFeedRequest request);
    }

    public interface StoryDAO
    {
        PostStatusResponse addToStory(PostStatusRequest request, String userHandle);
        GetStoryResponse getStory(GetStoryRequest request);
    }

    public interface UserDAO
    {
        AuthenticateResponse login(LoginRequest request);
        AuthenticateResponse register(RegisterRequest request);
    }
}
