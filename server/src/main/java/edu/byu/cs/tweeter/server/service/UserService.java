package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.UserDAODynamo;

public class UserService extends BaseService {

    UserDAO userDao;
    AuthTokenDAO authTokenDAO;

    public UserService(DatabaseFactory databaseFactory)
    {
        super(databaseFactory);
        userDao = databaseFactory.getUserDAO();
        authTokenDAO = databaseFactory.getAuthTokenDAO();
    }

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

    public GetUserResponse getUser(GetUserRequest request)
    {
        return getUserDAO().getUser(request);
    }



    public UserDAODynamo getUserDAO() {return new UserDAODynamo();}
}
