package edu.byu.cs.tweeter.server.dao;

import java.util.Date;
import java.util.UUID;

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
        String token = UUID.randomUUID().toString();
        long timestamp = new Date().getTime();



        String timestampString = Long.toString(timestamp);
        return new AuthToken(token, timestampString);
        //Need to stuff token in here as well
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
