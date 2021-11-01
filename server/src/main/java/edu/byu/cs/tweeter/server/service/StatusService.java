package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class StatusService
{
    public PostStatusResponse postStatus(PostStatusRequest request)
    {
        return getStatusDAO().postStatus(request);
    }

    public StatusDAO getStatusDAO()
    {
        return new StatusDAO();
    }
}
