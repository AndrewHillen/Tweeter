package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter implements StatusService.GetFeedObserver
{
    public interface View
    {
        void addItems(List<Status> feed);
        void setLoading(boolean isLoading);
        void navigateToUser(User user);

        void displayErrorMessage(String message);
        void displayInfoMessage(String message);

    }

    private View view;
    private AuthToken authToken;
    private User user;
    private Status lastStatus;

    private boolean hasMorePages = true;
    private boolean isLoading = false;
    private static final int PAGE_SIZE = 10;

    public FeedPresenter(View view, AuthToken authToken, User user)
    {
        this.view = view;
        this.authToken = authToken;
        this.user = user;
    }

    public void loadMoreItems()
    {
        if(!isLoading && hasMorePages)
        {
            isLoading = true;
            view.setLoading(true);
            new StatusService().getFeed(user, authToken, PAGE_SIZE, lastStatus, this);
        }
    }

    @Override
    public void getFeedSuccess(List<Status> feed, boolean hasMorePages, Status lastStatus)
    {
        this.hasMorePages = hasMorePages;
        this.lastStatus = lastStatus;
        isLoading = false;
        view.setLoading(false);
        view.addItems(feed);
    }

    @Override
    public void getFeedFailure(String message)
    {
        isLoading = false;
        view.setLoading(false);
        view.displayErrorMessage(message);
    }

    @Override
    public void getFeedException(Exception ex)
    {
        isLoading = false;
        view.setLoading(false);
        view.displayErrorMessage(ex.getMessage());
    }
}
