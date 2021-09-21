package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService
{

    public interface Observer
    {
        void getFollowingSuccess(List<User> user, boolean hasMorePages, User lastFollowee);
        void getFollowingFailure(String message);
        void getFollowingException(Exception ex);
    }

    public void getFollowing(AuthToken authToken, User target, int limit, User lastFollowee, Observer observer)
    {
        GetFollowingTask getFollowingTask = new GetFollowingTask(authToken,
                target, limit, lastFollowee, new GetFollowingHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }


    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends Handler
    {
        private Observer observer;

        public GetFollowingHandler(Observer observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success)
            {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                User lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;

                observer.getFollowingSuccess(followees, hasMorePages, lastFollowee);
            }
            else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                observer.getFollowingFailure(message);
            }
            else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.getFollowingException(ex);
            }
        }
    }
}
