package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter implements StatusService.GetStoryObserver, UserService.GetUserObserver
{

    public interface View
    {
        void addItems(List<Status> feed);
        void setLoading(boolean isLoading);
        void navigateToUser(User user);
        void navigateToWebpage(String clickable);

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

    public StoryPresenter(View view, AuthToken authToken, User user)
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
            new StatusService().getStory(user, authToken, PAGE_SIZE, lastStatus, this);
        }
    }

    public void parseLink(String clickable)
    {
        if (clickable.contains("http")) {
            view.navigateToWebpage(clickable);
        } else {
            view.displayInfoMessage("Getting user's profile...");
            new UserService().getUser(authToken, clickable, this);
        }
    }


    @Override
    public void getStorySuccess(List<Status> story, boolean hasMorePages, Status lastStatus)
    {
        this.hasMorePages = hasMorePages;
        this.lastStatus = lastStatus;
        isLoading = false;
        view.setLoading(false);
        view.addItems(story);
    }

    @Override
    public void getStoryFailure(String message)
    {
        isLoading = false;
        view.setLoading(false);
        view.displayErrorMessage(message);
    }

    @Override
    public void getStoryException(Exception ex)
    {
        isLoading = false;
        view.setLoading(false);
        view.displayErrorMessage(ex.getMessage());
    }

    @Override
    public void getUserSuccess(User user)
    {
        view.navigateToUser(user);
    }

    @Override
    public void getUserFailure(String message)
    {
        view.displayErrorMessage("Failed to get user's profile: " + message);
    }

    @Override
    public void getUserException(Exception ex)
    {
        view.displayErrorMessage("Failed to get user's profile because of exception: " + ex.getMessage());
    }
}
