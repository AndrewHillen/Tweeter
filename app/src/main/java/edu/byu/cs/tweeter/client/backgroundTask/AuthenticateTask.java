package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticateTask extends BackgroundTask
{
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    protected String username;
    protected String password;
    protected User user;
    protected AuthToken authToken;

    public AuthenticateTask(Handler messageHandler, String username, String password)
    {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle)
    {
        msgBundle.putSerializable(USER_KEY, user);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }

    protected Pair<User, AuthToken> doAuthenticate() throws IOException
    {
        User registeredUser = getFakeData().getFirstUser();
        BackgroundTaskUtils.loadImage(registeredUser);
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(registeredUser, authToken);
    }
}
