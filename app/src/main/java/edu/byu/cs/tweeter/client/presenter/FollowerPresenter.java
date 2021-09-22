package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerPresenter implements FollowService.GetFollowerObserver, UserService.GetUserObserver
{

    public interface View
    {
        void addItems(List<User> followers);
        void setLoading(boolean isLoading);
        void navigateToUser(User user);

        void displayErrorMessage(String message);
        void displayInfoMessage(String message);

    }

    private View view;
    private AuthToken authToken;
    private User user;
    private User lastFollower;
    private boolean isLoading = false;

    private boolean hasMorePages = true;

    private static final int PAGE_SIZE = 10;

    public FollowerPresenter(View view, AuthToken authToken, User user)
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
            new FollowService().getFollowers(authToken, user, PAGE_SIZE, lastFollower, this);
        }
    }

    public void goToUser(String alias)
    {
        view.displayInfoMessage("Getting user's profile...");
        new UserService().getUser(authToken, alias, this);
    }

    @Override
    public void getFollowerSuccess(List<User> followers, boolean hasMorePages, User lastFollower)
    {
        this.lastFollower = lastFollower;
        isLoading = false;
        view.setLoading(false);
        view.addItems(followers);
        this.hasMorePages = hasMorePages;
    }

    @Override
    public void getFollowerFailure(String message)
    {
        isLoading = false;
        view.setLoading(false);
        view.displayErrorMessage(message);
    }

    @Override
    public void getFollowerException(Exception ex)
    {
        isLoading = false;
        view.setLoading(false);
        // Do exception stuff
    }

    // UserService overrides ----------------------------------

    @Override
    public void getUserSuccess(User user)
    {
        view.navigateToUser(user);
    }

    @Override
    public void getUserFailure(String message)
    {
        view.displayErrorMessage(message);
    }

    @Override
    public void getUserException(Exception ex)
    {
        //Do Exception stuff
    }
}
