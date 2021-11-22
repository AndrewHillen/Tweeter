package edu.byu.cs.tweeter.server.dao;

import java.util.Date;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.service.BaseService;

public class AuthTokenDAODynamo implements BaseService.AuthTokenDAO
{
    public AuthTokenDAODynamo()
    {
    }

    //TODO Make a real token with a UUID
    @Override
    public AuthToken generateAuthToken(String userHandle)
    {
        return new AuthToken("Token", Long.toString(new Date().getTime()));
    }

    @Override
    public boolean checkAuthToken(AuthToken authToken, String userHandle)
    {
        return true;
    }

    @Override
    public LogoutResponse logout(LogoutRequest request)

    {
        return new LogoutResponse(true);
    }
}
