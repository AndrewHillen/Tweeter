package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.views.PagedView;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<FeedPresenter.View, Status>
{

    public FeedPresenter(View view, AuthToken authToken, User user)
    {
        super(view, authToken, user);
    }

    @Override
    public void getItems(PagedObserver observer)
    {
        new StatusService().getFeed(user, authToken, PAGE_SIZE, lastItem, observer);
    }
//
//    private class GetUserObserver implements UserService.GetUserObserver
//    {
//        @Override
//        public void handleSuccess(User user)
//        {
//            view.navigateToUser(user);
//        }
//
//        @Override
//        public void handleFailure(String message)
//        {
//            view.displayErrorMessage(message);
//        }
//
//        @Override
//        public void handleException(Exception ex)
//        {
//            view.displayErrorMessage("Failed to get user's profile because of exception: " + ex.getMessage());
//        }
//    }

//    private GetUserObserver getUserObserver = new GetUserObserver();

    public interface View extends PagedView
    {
        void navigateToWebpage(String clickable);
    }

//    private View view;
//    private AuthToken authToken;
//    private User user;
//    private Status lastStatus;
//
//    private boolean hasMorePages = true;
//    private boolean isLoading = false;
//    private static final int PAGE_SIZE = 10;

//
//    public void loadMoreItems()
//    {
//        if(!isLoading && hasMorePages)
//        {
//            isLoading = true;
//            view.setLoading(true);
//            new StatusService().getFeed(user, authToken, PAGE_SIZE, lastStatus, this);
//        }
//    }

    public void parseLink(String clickable)
    {
        if (clickable.contains("http")) {
            view.navigateToWebpage(clickable);
        } else {
            //view.displayInfoMessage("Getting user's profile...");
            goToUser(clickable);
//            new UserService().getUser(authToken, clickable, getUserObserver);
        }
    }
//
//    @Override
//    public void getFeedSuccess(List<Status> feed, boolean hasMorePages, Status lastStatus)
//    {
//        this.hasMorePages = hasMorePages;
//        this.lastStatus = lastStatus;
//        isLoading = false;
//        view.setLoading(false);
//        view.addItems(feed);
//    }
//
//    @Override
//    public void getFeedFailure(String message)
//    {
//        isLoading = false;
//        view.setLoading(false);
//        view.displayErrorMessage(message);
//    }
//
//    @Override
//    public void getFeedException(Exception ex)
//    {
//        isLoading = false;
//        view.setLoading(false);
//        view.displayErrorMessage(ex.getMessage());
//    }
}
