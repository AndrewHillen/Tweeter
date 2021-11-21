package edu.byu.cs.tweeter.client.model.net;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;

class ServerFacadeTest
{
    ServerFacade serverFacade;
    @BeforeEach
    void setup()
    {
        serverFacade = new ServerFacade();
    }
    @Test
    void registerTest() throws Exception
    {
        RegisterRequest request = new RegisterRequest("", "", "", "", "");
        AuthenticateResponse response = serverFacade.register(request);

        assertNotNull(response.getAuthToken());
        assertNotNull(response.getUser());
        assertTrue(response.isSuccess());

    }

    @Test
    void getFollowerCountTest() throws Exception
    {
        GetFollowerCountRequest request = new GetFollowerCountRequest(new AuthToken(), new User());
        GetFollowerCountResponse response = serverFacade.getFollowerCount(request);

        assertNotNull(response.getCount());
        assertTrue(response.isSuccess());

    }

    @Test
    void getFollowersTest() throws Exception
    {
        GetFollowersRequest request = new GetFollowersRequest(new AuthToken(), new User(), null, 10);
        GetFollowersResponse response = serverFacade.getFollowers(request);

        assertNotNull(response.getItems());
        assertTrue(response.isSuccess());
    }
}