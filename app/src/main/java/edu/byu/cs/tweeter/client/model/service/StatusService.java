package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.client.view.main.story.StoryFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends BaseService
{
    public interface GetFeedObserver
    {
        void getFeedSuccess(List<Status> feed, boolean hasMorePages, Status lastStatus);
        void getFeedFailure(String message);
        void getFeedException(Exception ex);
    }

    public void getFeed(User user, AuthToken authToken, int limit, Status lastStatus, PagedObserver observer)
    {
        GetFeedTask getFeedTask = new GetFeedTask(authToken,
                user, limit, lastStatus, new GetFeedHandler(observer));
        executeTask(getFeedTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends PagedHandler<PagedObserver<Status>, Status>
    {
        public GetFeedHandler(PagedObserver<Status> observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to get Feed";
        }

        @Override
        protected List<Status> fetchItems(Message msg)
        {
            List<Status> feed = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
            return feed;
        }
    }

    public interface GetStoryObserver
    {
        void getStorySuccess(List<Status> story, boolean hasMorePages, Status lastStatus);
        void getStoryFailure(String message);
        void getStoryException(Exception ex);
    }

    public void getStory(User user, AuthToken authToken, int limit, Status lastStatus, PagedObserver observer)
    {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, limit, lastStatus, new GetStoryHandler(observer));
        executeTask(getStoryTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends PagedHandler<PagedObserver<Status>, Status> {

        public GetStoryHandler(PagedObserver<Status> observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to get Story";
        }

        @Override
        protected List<Status> fetchItems(Message msg)
        {
            List<Status> story = (List<Status>) msg.getData().getSerializable(GetStoryTask.STATUSES_KEY);
            return story;
        }
    }

    public interface PostStatusObserver extends SimpleNotificationObserver
    {
    }

    public void postStatus(AuthToken authToken, Status status, PostStatusObserver observer)
    {
        PostStatusTask statusTask = new PostStatusTask(authToken,
                status, new PostStatusHandler(observer));
        executeTask(statusTask);
    }

    // PostStatusHandler

    private class PostStatusHandler extends SimpleNotificationHandler<PostStatusObserver>
    {

        public PostStatusHandler(PostStatusObserver observer)
        {
            super(observer);
        }

        @Override
        protected String getFailurePrefix()
        {
            return "Failed to post status";
        }
    }
}
