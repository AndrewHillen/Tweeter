package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter implements FollowService.Observer, UserService.Observer
{
    public interface View
    {
        void addItems(List<User> followees);
        void setLoading(boolean isLoading);
        void navigateToUser(User user);

        void displayErrorMessage(String message);
        void displayInfoMessage(String message);

    }

    private View view;
    private AuthToken authToken;
    private User user;
    private User lastFollowee;
    private boolean isLoading = false;

    private boolean hasMorePages = true;

    private static final int PAGE_SIZE = 10;

    public FollowingPresenter(View view, AuthToken authToken, User user)
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
            new FollowService().getFollowing(authToken, user, PAGE_SIZE, lastFollowee, this);
        }
    }

    public void goToUser(String alias)
    {
        view.displayInfoMessage("Getting user's profile...");
        new UserService().getUser(authToken, alias, this);
    }

    // FollowService overrides--------------------------------------

    @Override
    public void getFollowingSuccess(List<User> followees, boolean hasMorePages, User lastFollowee)
    {
        this.lastFollowee = lastFollowee;
        isLoading = false;
        view.setLoading(false);
        view.addItems(followees);
        this.hasMorePages = hasMorePages;
    }

    @Override
    public void getFollowingFailure(String message)
    {
        isLoading = false;
        view.setLoading(false);
        view.displayErrorMessage(message);
    }

    @Override
    public void getFollowingException(Exception ex)
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
