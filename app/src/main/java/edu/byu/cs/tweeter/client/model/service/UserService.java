package edu.byu.cs.tweeter.client.model.service;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;

import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handler.AuthenticateHandler;
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.view.login.LoginFragment;
import edu.byu.cs.tweeter.client.view.login.RegisterFragment;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends BaseService
{
    public interface GetUserObserver extends ServiceObserver
    {
        void handleSuccess(User user);
    }
    public void getUser(AuthToken authToken, String alias, GetUserObserver observer)
    {
        GetUserTask getUserTask = new GetUserTask(authToken, alias, new GetUserHandler(observer));
        executeTask(getUserTask);
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandler extends BackgroundTaskHandler<GetUserObserver>
    {
        public GetUserHandler(GetUserObserver observer)
        {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(GetUserObserver observer, Message msg)
        {
            User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
            observer.handleSuccess(user);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to retrieve user";
        }
    }


    public interface LoginObserver extends AuthenticateObserver
    {
    }

    public void login(String alias, String password, AuthenticateObserver observer)
    {
        // Send the login request.
        LoginTask loginTask = new LoginTask(alias, password,
                new LoginHandler(observer));
        executeTask(loginTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends AuthenticateHandler<AuthenticateObserver>
    {

        public LoginHandler(AuthenticateObserver observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to Login";
        }
    }


    public interface RegisterObserver extends AuthenticateObserver
    {
    }

    public void register(String firstName, String lastName,
                         String alias, String password,
                         String imageBytesBase64, AuthenticateObserver observer)
    {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new RegisterHandler(observer));

        executeTask(registerTask);
    }

    // RegisterHandler

    private class RegisterHandler extends AuthenticateHandler<AuthenticateObserver>{

        public RegisterHandler(AuthenticateObserver observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to Register";
        }
    }


    public interface LogoutObserver extends SimpleNotificationObserver
    {
    }

    public void logout(AuthToken authToken, LogoutObserver observer)
    {
        LogoutTask logoutTask = new LogoutTask(authToken, new LogoutHandler(observer));
        executeTask(logoutTask);
    }

    // LogoutHandler

    private class LogoutHandler extends SimpleNotificationHandler<LogoutObserver>
    {

        public LogoutHandler(LogoutObserver observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to logout";
        }
    }


}
