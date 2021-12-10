package edu.byu.cs.tweeter.server.service;

import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.services.dynamodbv2.xspec.S;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.dao.StoryDAODynamo;

class StatusServiceTest
{
    StatusService statusServiceSpy;
    StoryDAODynamo mockStoryDAO;

    @BeforeEach
    void setUp()
    {
        statusServiceSpy = Mockito.spy(StatusService.class);
        mockStoryDAO = Mockito.mock(StoryDAODynamo.class);

    }

    @AfterEach
    void tearDown()
    {
    }

    @Test
    void getStory()
    {
        String alias = "Alias";
        User targetUser = new User("First", "Last", alias, "url");
        int limit = 10;
        GetStoryRequest getStoryRequest = new GetStoryRequest(null, "User", targetUser, null, 10);

        GetStoryResponse getStoryResponse = buildStoryResponse();
        Mockito.when(mockStoryDAO.getStory(alias, null, limit)).thenReturn(getStoryResponse);
        Mockito.when(statusServiceSpy.getStoryDAO()).thenReturn(mockStoryDAO);

        GetStoryResponse response = statusServiceSpy.getStory(getStoryRequest);


        Assertions.assertTrue(response.equals(getStoryResponse));

    }

    GetStoryResponse buildStoryResponse()
    {
        ArrayList<Status> statuses = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            User user = new User("first" + i, "last" + i, "alias" + i, "url" + i);
            Status status = new Status("post" + i, user, "datetime" + i, null, null);
            statuses.add(status);
        }
        return new GetStoryResponse(statuses, false);
    }
}