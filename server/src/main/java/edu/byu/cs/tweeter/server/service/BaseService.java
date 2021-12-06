package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.CheckFollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
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
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
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
        FollowResponse follow(FollowRequest request);
        UnFollowResponse unfollow(UnFollowRequest request);
        CheckFollowResponse checkFollow(CheckFollowRequest request);
        GetFollowersResponse getFollowers(String followee, User lastUser, int limit);
        GetFollowingResponse getFollowing(String follower, User lastUser, int limit);
    }

    public interface FeedDAO
    {
        PostStatusResponse addToFeed(Status status, String userHandle, long timestamp);
        GetFeedResponse getFeed(String alias, Status lastStatus, int limit);
    }

    public interface StoryDAO
    {
        PostStatusResponse addToStory(Status status, String userHandle, long timestamp);
        GetStoryResponse getStory(String alias, Status lastStatus, int limit);
    }

    public interface UserDAO
    {
        User login(LoginRequest request);
        User register(RegisterRequest request);
        LogoutResponse logout(LogoutRequest request);
        GetUserResponse getUser(GetUserRequest request);
        void incrementFollowerCount(String alias);
        void incrementFollowingCount(String alias);
        void decrementFollowerCount(String alias);
        void decrementFollowingCount(String alias);
        int getFollowerCount(String alias);
        int getFollowingCount(String alias);
    }
}
