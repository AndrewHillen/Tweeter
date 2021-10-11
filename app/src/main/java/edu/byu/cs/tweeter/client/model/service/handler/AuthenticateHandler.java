package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.AuthenticateTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticateHandler<T extends AuthenticateObserver> extends BackgroundTaskHandler<T>
{
    public AuthenticateHandler(T observer)
    {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Message msg)
    {
        User user = (User) msg.getData().getSerializable(AuthenticateTask.USER_KEY);
        AuthToken authToken = (AuthToken) msg.getData().getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);
        observer.handleSuccess(user, authToken);
    }
}
