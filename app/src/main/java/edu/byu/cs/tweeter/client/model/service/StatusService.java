package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.client.view.main.story.StoryFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService
{
    public interface GetFeedObserver
    {
        void getFeedSuccess(List<Status> feed, boolean hasMorePages, Status lastStatus);
        void getFeedFailure(String message);
        void getFeedException(Exception ex);
    }

    public void getFeed(User user, AuthToken authToken, int limit, Status lastStatus, GetFeedObserver observer)
    {
        GetFeedTask getFeedTask = new GetFeedTask(authToken,
                user, limit, lastStatus, new GetFeedHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends Handler
    {
        private GetFeedObserver observer;

        public GetFeedHandler(GetFeedObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);

                Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;

                observer.getFeedSuccess(statuses, hasMorePages, lastStatus);
            }
            else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
                observer.getFeedFailure(message);
            }
            else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                observer.getFeedException(ex);
            }
        }
    }

    public interface GetStoryObserver
    {
        void getStorySuccess(List<Status> story, boolean hasMorePages, Status lastStatus);
        void getStoryFailure(String message);
        void getStoryException(Exception ex);
    }

    public void getStory(User user, AuthToken authToken, int limit, Status lastStatus, GetStoryObserver observer)
    {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, limit, lastStatus, new GetStoryHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends Handler {

        private GetStoryObserver observer;

        public GetStoryHandler(GetStoryObserver observer)
        {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);
                Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;

                observer.getStorySuccess(statuses, hasMorePages, lastStatus);

            }
            else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY))
            {
                String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
                observer.getStoryFailure(message);
            }
            else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY))
            {
                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
                observer.getStoryException(ex);
            }
        }
    }
}
