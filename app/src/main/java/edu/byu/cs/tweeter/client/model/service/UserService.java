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


    public interface LoginObserver
    {
        void loginSuccess(User user, AuthToken authToken);
        void loginFailure(String message);
        void loginException(Exception ex);
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
    private class LoginHandler extends Handler {
        private LoginObserver observer;

        public LoginHandler(LoginObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
            if (success) {
                User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);
                observer.loginSuccess(loggedInUser, authToken);
            }
            else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
                observer.loginFailure("Failed to login: " + message);
            }
            else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
                observer.loginException(ex);
            }
        }
    }


    public interface RegisterObserver
    {
        void registerSuccess(User user, AuthToken authToken);
        void registerFailure(String message);
        void registerException(Exception ex);
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

    private class RegisterHandler extends Handler {

        private RegisterObserver observer;

        public RegisterHandler(RegisterObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
            if (success) {
                User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);

                observer.registerSuccess(registeredUser, authToken);
            }
            else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
                observer.registerFailure(message);
            }
            else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
                observer.registerException(ex);
            }
        }
    }


    public interface LogoutObserver
    {
        void logoutSuccess();
        void logoutFailure(String message);
        void logoutException(Exception ex);
    }

    public void logout(AuthToken authToken, LogoutObserver observer)
    {
        LogoutTask logoutTask = new LogoutTask(authToken, new LogoutHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }

    // LogoutHandler

    private class LogoutHandler extends Handler {
        private LogoutObserver observer;

        public LogoutHandler(LogoutObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
            if (success) {
                observer.logoutSuccess();
            }
            else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);
                observer.logoutFailure(message);
            }
            else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);
                observer.logoutException(ex);
            }
        }
    }


}
