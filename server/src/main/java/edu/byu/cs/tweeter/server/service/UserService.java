package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.util.FakeData;

public class UserService {

    public AuthenticateResponse login(LoginRequest request)
    {
        return getUserDAO().login(request);
    }

    public AuthenticateResponse register(RegisterRequest request)
    {
        return getUserDAO().register(request);
    }

    public LogoutResponse logout(LogoutRequest request)
    {
        return getUserDAO().logout(request);
    }

    public boolean checkAuthorization(AuthToken authToken)
    {
        return getUserDAO().checkAuthorization(authToken);
    }



    public UserDAO getUserDAO() {return new UserDAO();}
}
