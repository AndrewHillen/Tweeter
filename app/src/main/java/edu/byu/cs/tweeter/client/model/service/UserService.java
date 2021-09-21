package edu.byu.cs.tweeter.client.model.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService
{
    public interface Observer
    {
        void getUserSuccess(User user);
        void getUserFailure(String message);
        void getUserException(Exception ex);
    }

    public void getUser(AuthToken authToken, String alias, Observer observer)
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
        private Observer observer;

        public GetUserHandler(Observer observer)
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
}
