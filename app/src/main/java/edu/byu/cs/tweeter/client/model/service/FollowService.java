package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends BaseService
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

    public interface FollowObserver extends SimpleNotificationObserver
    {
    }

    public void followUser(User user, AuthToken authToken, FollowObserver observer)
    {
        FollowTask followTask = new FollowTask(authToken,
                user, new FollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    // FollowHandler
    public class FollowHandler extends SimpleNotificationHandler<FollowObserver>
    {
        protected FollowHandler(FollowObserver observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to follow";
        }
    }

    public interface UnFollowObserver extends SimpleNotificationObserver
    {
    }

    public void unFollowUser(User user, AuthToken authToken, UnFollowObserver observer)
    {
        UnfollowTask unfollowTask = new UnfollowTask(authToken,
                user, new UnfollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }

    // UnfollowHandler

    public class UnfollowHandler extends SimpleNotificationHandler<UnFollowObserver>
    {
        protected UnfollowHandler(UnFollowObserver observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to follow";
        }
    }


    public interface CheckFollowObserver
    {
        void checkFollowSuccess(boolean isFollower);
        void checkFollowFailure(String message);
        void checkFollowException(Exception ex);
    }

    public void checkFollower(AuthToken authToken, User loggedInUser, User targetUser, CheckFollowObserver observer)
    {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
                loggedInUser, targetUser, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    // IsFollowerHandler

    private class IsFollowerHandler extends Handler {

        private CheckFollowObserver observer;

        public IsFollowerHandler(CheckFollowObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);

                observer.checkFollowSuccess(isFollower);
            }
            else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                observer.checkFollowFailure(message);
            }
            else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                observer.checkFollowException(ex);
            }
        }
    }

    public interface FollowingCountObserver
    {
        void followingCountSuccess(int count);
        void followingCountFailure(String message);
        void followingCountException(Exception ex);
    }

    public void getFollowingCount(AuthToken authToken, User user, FollowingCountObserver observer)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                user, new GetFollowingCountHandler(observer));
        executor.execute(followingCountTask);
    }

    // GetFollowingCountHandler

    private class GetFollowingCountHandler extends Handler {

        private FollowingCountObserver observer;

        public GetFollowingCountHandler(FollowingCountObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
                observer.followingCountSuccess(count);
            }
            else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
                observer.followingCountFailure(message);
            }
            else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
                observer.followingCountException(ex);
            }
        }
    }

    public interface FollowerCountObserver
    {
        void followerCountSuccess(int count);
        void followerCountFailure(String message);
        void followerCountException(Exception ex);
    }

    public void getFollowerCount(AuthToken authToken, User user, FollowerCountObserver observer)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                user, new GetFollowerCountHandler(observer));
        executor.execute(followersCountTask);
    }

    // GetFollowingCountHandler

    private class GetFollowerCountHandler extends Handler {

        private FollowerCountObserver observer;

        public GetFollowerCountHandler(FollowerCountObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
                observer.followerCountSuccess(count);
            }
            else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
                observer.followerCountFailure(message);
            }
            else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
                observer.followerCountException(ex);
            }
        }
    }
}
