package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.util.FakeData;

public class StatusDAO
{


    public PostStatusResponse postStatus(PostStatusRequest request)
    {
        return new PostStatusResponse(true);
    }
    FakeData getFakeData() {
        return new FakeData();
    }
}
