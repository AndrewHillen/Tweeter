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
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.CountHandler;
import edu.byu.cs.tweeter.client.model.service.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public class FollowService extends BaseService
{

    // GetFollowing portions ------------------------------------------------
//    public interface GetFollowingObserver extends PagedObserver<User>
//    {
//    }

    public void getFollowing(AuthToken authToken, User target, int limit, User lastFollowee, PagedObserver observer)
    {
        GetFollowingTask getFollowingTask = new GetFollowingTask(authToken,
                target, limit, lastFollowee, new GetFollowingHandler(observer));
        executeTask(getFollowingTask);
    }


    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends PagedHandler<PagedObserver<User>, User>
    {
        public GetFollowingHandler(PagedObserver observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to get Following";
        }

        @Override
        protected List<User> fetchItems(Message msg)
        {
            List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
            return followees;
        }
    }

    // GetFollower portions --------------------------------------------------

//    public interface GetFollowerObserver
//    {
//        void getFollowerSuccess(List<User> user, boolean hasMorePages, User lastFollower);
//        void getFollowerFailure(String message);
//        void getFollowerException(Exception ex);
//    }

    public void getFollowers(AuthToken authToken, User target, int limit, User lastFollower, PagedObserver observer)
    {
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, target, limit, lastFollower,
                new GetFollowersHandler(observer));
        executeTask(getFollowersTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends PagedHandler<PagedObserver<User>, User> {

        public GetFollowersHandler(PagedObserver<User> observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to get Followers";
        }

        @Override
        protected List<User> fetchItems(Message msg)
        {
            List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
            return followers;
        }
    }

    public interface FollowObserver extends SimpleNotificationObserver
    {
    }

    public void followUser(User user, AuthToken authToken, FollowObserver observer)
    {
        FollowTask followTask = new FollowTask(authToken,
                user, new FollowHandler(observer));
        executeTask(followTask);
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
        executeTask(unfollowTask);
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


    public interface CheckFollowObserver extends ServiceObserver
    {
        void handleSuccess(boolean isFollower);
    }

    public void checkFollower(AuthToken authToken, User loggedInUser, User targetUser, CheckFollowObserver observer)
    {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
                loggedInUser, targetUser, new IsFollowerHandler(observer));
        executeTask(isFollowerTask);
    }

    // IsFollowerHandler

    private class IsFollowerHandler extends BackgroundTaskHandler<CheckFollowObserver>
    {

        public IsFollowerHandler(CheckFollowObserver observer)
        {
            super(observer);
        }

        @Override
        protected void handleSuccessMessage(CheckFollowObserver observer, Message msg)
        {
            boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
            observer.handleSuccess(isFollower);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to determine Follow relationship";
        }
    }

    public interface FollowingCountObserver extends CountObserver
    {
    }

    public void getFollowingCount(AuthToken authToken, User user, FollowingCountObserver observer)
    {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                user, new GetFollowingCountHandler(observer));
        executeTask(followingCountTask);
    }

    // GetFollowingCountHandler

    private class GetFollowingCountHandler extends CountHandler<FollowingCountObserver>
    {

        public GetFollowingCountHandler(FollowingCountObserver observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to get Following count";
        }
    }

    public interface FollowerCountObserver extends CountObserver
    {
    }

    public void getFollowerCount(AuthToken authToken, User user, FollowerCountObserver observer)
    {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                user, new GetFollowerCountHandler(observer));
        executeTask(followersCountTask);
    }

    // GetFollowingCountHandler

    private class GetFollowerCountHandler extends CountHandler<FollowerCountObserver> {

        public GetFollowerCountHandler(FollowerCountObserver observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to get Follower Count";
        }
    }
}
