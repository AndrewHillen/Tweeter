package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserDAO
{
    public boolean checkAuthorization(AuthToken authToken)
    {
        return true;
    }
}
