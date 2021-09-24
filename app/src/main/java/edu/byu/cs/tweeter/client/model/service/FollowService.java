package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.followers.FollowersFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService
{

    // GetFollowing portions ------------------------------------------------
    public interface GetFollowingObserver
    {
        void getFollowingSuccess(List<User> user, boolean hasMorePages, User lastFollowee);
        void getFollowingFailure(String message);
        void getFollowingException(Exception ex);
    }

    public void getFollowing(AuthToken authToken, User target, int limit, User lastFollowee, GetFollowingObserver observer)
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
        private GetFollowingObserver observer;

        public GetFollowingHandler(GetFollowingObserver observer)
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

    // GetFollower portions --------------------------------------------------
    public interface GetFollowerObserver
    {
        void getFollowerSuccess(List<User> user, boolean hasMorePages, User lastFollower);
        void getFollowerFailure(String message);
        void getFollowerException(Exception ex);
    }

    public void getFollowers(AuthToken authToken, User target, int limit, User lastFollower, GetFollowerObserver observer)
    {
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, target, limit, lastFollower,
                new GetFollowersHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends Handler {

        private GetFollowerObserver observer;

        public GetFollowersHandler(GetFollowerObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success)
            {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
                User lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;

                observer.getFollowerSuccess(followers, hasMorePages, lastFollower);

                //followersRecyclerViewAdapter.addItems(followers);
            }
            else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.getFollowerFailure("Failed to get followers: " + message);
            }
            else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                observer.getFollowerException(ex);
            }
        }
    }


    public interface FollowObserver
    {
        void followSuccess();
        void followFailure(String message);
        void followException(Exception ex);
    }

    public void followUser(User user, AuthToken authToken, FollowObserver observer)
    {
        FollowTask followTask = new FollowTask(authToken,
                user, new FollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    // FollowHandler

    private class FollowHandler extends Handler {

        private FollowObserver observer;

        public FollowHandler(FollowObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                observer.followSuccess();
            }
            else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
                observer.followFailure(message);
            }
            else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
                observer.followException(ex);
            }
        }
    }

    public interface UnFollowObserver
    {
        void unFollowSuccess();
        void unFollowFailure(String message);
        void unFollowException(Exception ex);
    }

    public void unFollowUser(User user, AuthToken authToken, UnFollowObserver observer)
    {
        UnfollowTask unfollowTask = new UnfollowTask(authToken,
                user, new UnfollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }

    // UnfollowHandler

    private class UnfollowHandler extends Handler {
        private UnFollowObserver observer;

        public UnfollowHandler(UnFollowObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                observer.unFollowSuccess();
            }
            else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
                observer.unFollowFailure(message);
            }
            else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
                observer.unFollowException(ex);
            }
        }
    }
}
