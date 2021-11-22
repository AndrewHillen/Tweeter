package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
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
import edu.byu.cs.tweeter.model.net.response.CheckFollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UnFollowResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://c92aiaa2zd.execute-api.us-west-2.amazonaws.com/dev";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @return the login response.
     */

    private <T extends Response> T handleResponse(T response)
    {
        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    // User stuff------------------------------------------------------------------------

    public AuthenticateResponse login(LoginRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/login";
        AuthenticateResponse response = clientCommunicator.doPost(urlPath, request, null, AuthenticateResponse.class);

        return handleResponse(response);
    }

    public AuthenticateResponse register(RegisterRequest request) throws IOException, TweeterRemoteException {
        String urlPath = "/register";
        AuthenticateResponse response = clientCommunicator.doPost(urlPath, request, null, AuthenticateResponse.class);

        return handleResponse(response);
    }

    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/logout";
        LogoutResponse response = clientCommunicator.doPost(urlPath, request, null, LogoutResponse.class);

        return handleResponse(response);
    }

    public GetUserResponse getUser(GetUserRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/getUser";
        GetUserResponse response = clientCommunicator.doPost(urlPath, request, null, GetUserResponse.class);

        return handleResponse(response);
    }


    //Status stuff ---------------------------------------------------------------------------------

    public PostStatusResponse postStatus(PostStatusRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/poststatus";
        PostStatusResponse response = clientCommunicator.doPost(urlPath, request, null, PostStatusResponse.class);

        return handleResponse(response);
    }

    public GetStoryResponse getStory(GetStoryRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/getStory";
        GetStoryResponse response = clientCommunicator.doPost(urlPath, request, null, GetStoryResponse.class);

        return handleResponse(response);
    }

    public GetFeedResponse getFeed(GetFeedRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/getFeed";
        GetFeedResponse response = clientCommunicator.doPost(urlPath, request, null, GetFeedResponse.class);

        return handleResponse(response);
    }

    // Follows stuff -------------------------------------------------------------------------------------------

//    /**
//     * Returns the users that the user specified in the request is following. Uses information in
//     * the request object to limit the number of followees returned and to return the next set of
//     * followees after any that were returned in a previous request.
//     *
//     * @param request contains information about the user whose followees are to be returned and any
//     *                other information required to satisfy the request.
//     * @return the followees.
//     */
//    public FollowingResponse getFollowees(FollowingRequest request, String urlPath)
//            throws IOException, TweeterRemoteException {
//
//        FollowingResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);
//
//        return handleResponse(response);
//    }

    public GetFollowerCountResponse getFollowerCount(GetFollowerCountRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/getFollowerCount";
        GetFollowerCountResponse response = clientCommunicator.doPost(urlPath, request, null, GetFollowerCountResponse.class);

        return handleResponse(response);
    }

    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/getFollowingCount";
        GetFollowingCountResponse response = clientCommunicator.doPost(urlPath, request, null, GetFollowingCountResponse.class);

        return handleResponse(response);
    }

    public FollowResponse follow(FollowRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/follow";
        FollowResponse response = clientCommunicator.doPost(urlPath, request, null, FollowResponse.class);

        return handleResponse(response);
    }

    public UnFollowResponse unfollow(UnFollowRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/unfollow";
        UnFollowResponse response = clientCommunicator.doPost(urlPath, request, null, UnFollowResponse.class);

        return handleResponse(response);
    }

    public CheckFollowResponse checkFollow(CheckFollowRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/checkFollow";
        CheckFollowResponse response = clientCommunicator.doPost(urlPath, request, null, CheckFollowResponse.class);

        return handleResponse(response);
    }

    public GetFollowersResponse getFollowers(GetFollowersRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/getFollowers";
        GetFollowersResponse response = clientCommunicator.doPost(urlPath, request, null, GetFollowersResponse.class);

        return handleResponse(response);
    }

    public GetFollowingResponse getFollowing(GetFollowingRequest request) throws IOException, TweeterRemoteException
    {
        String urlPath = "/getFollowing";
        GetFollowingResponse response = clientCommunicator.doPost(urlPath, request, null, GetFollowingResponse.class);

        return handleResponse(response);
    }
}