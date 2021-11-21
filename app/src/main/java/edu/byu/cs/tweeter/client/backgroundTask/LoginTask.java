package edu.byu.cs.tweeter.client.backgroundTask;


import android.os.Handler;


import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask{

    private static final String LOG_TAG = "LoginTask";


    public LoginTask(String username, String password, Handler messageHandler)
    {
        super(messageHandler, username, password);
    }

    @Override
    public boolean runTask() throws Exception
    {
        LoginRequest loginRequest = new LoginRequest(username, password);

        AuthenticateResponse authenticateResponse = getServerFacade().login(loginRequest);

        user = authenticateResponse.getUser();
        authToken = authenticateResponse.getAuthToken();
        if(user == null || authToken == null)
        {
            return false;
        }
        return true;

    }
}
