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
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.view.login.LoginFragment;
import edu.byu.cs.tweeter.client.view.login.RegisterFragment;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService
{
    public interface GetUserObserver
    {
        void getUserSuccess(User user);
        void getUserFailure(String message);
        void getUserException(Exception ex);
    }
    public void getUser(AuthToken authToken, String alias, GetUserObserver observer)
    {
        GetUserTask getUserTask = new GetUserTask(authToken, alias, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandler extends Handler
    {
        private GetUserObserver observer;

        public GetUserHandler(GetUserObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success)
            {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.getUserSuccess(user);

            }
            else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                observer.getUserFailure(message);
            }
            else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                observer.getUserException(ex);
            }
        }
    }


    public interface LoginObserver extends AuthenticateObserver
    {
    }

    public void login(String alias, String password, LoginObserver observer)
    {
        // Send the login request.
        LoginTask loginTask = new LoginTask(alias, password,
                new LoginHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends AuthenticateHandler<LoginObserver>
    {

        public LoginHandler(LoginObserver observer)
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
                         String imageBytesBase64, RegisterObserver observer)
    {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new RegisterHandler(observer));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }

    // RegisterHandler

    private class RegisterHandler extends AuthenticateHandler<RegisterObserver>{

        public RegisterHandler(RegisterObserver observer)
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
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
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
