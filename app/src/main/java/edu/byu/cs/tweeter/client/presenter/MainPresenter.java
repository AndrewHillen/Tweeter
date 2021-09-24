package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter implements UserService.LogoutObserver, FollowService.FollowObserver,
        FollowService.UnFollowObserver
{
    public interface View
    {
        void logout();

        void setFollowingCount(int count);
        void setFollowerCount(int count);
        void setIsFollowing(boolean isFollowing);

        void hideFollowButton(boolean hide);
        void enableFollowButton(boolean enabled);
        void onFollow();
        void onUnfollow();

        void onStatusPost();

        void displayErrorMessage(String message);
        void displayInfoMessage(String message);
    }

    private View view;
    private AuthToken authToken;
    private User user;

    public MainPresenter(View view, AuthToken authToken, User user)
    {
        this.view = view;
        this.authToken = authToken;
        this.user = user;
    }

    @Override
    public void logoutSuccess()
    {
        view.logout();
    }

    @Override
    public void logoutFailure(String message)
    {
        view.displayErrorMessage("Failed to logout: " + message);
    }

    @Override
    public void logoutException(Exception ex)
    {
        view.displayErrorMessage("Failed to logout because of exception: " + ex.getMessage());
    }

    public void logout()
    {
        view.displayInfoMessage("Logging Out...");
        new UserService().logout(authToken, this);
    }

    // Follow User

    public void checkFollowVisibility(User loggedInUser)
    {
        boolean hideButton = user.compareTo(loggedInUser) == 0;
        view.hideFollowButton(hideButton);
    }

    @Override
    public void followSuccess()
    {
        view.enableFollowButton(true);
        view.onFollow();
    }

    @Override
    public void followFailure(String message)
    {
        view.enableFollowButton(true);
        view.displayErrorMessage("Failed to follow: " + message);
    }

    @Override
    public void followException(Exception ex)
    {
        view.enableFollowButton(true);
        view.displayErrorMessage("Failed to follow because of exception: " + ex.getMessage());
    }

    public void followUser()
    {
        view.enableFollowButton(false);
        view.displayInfoMessage("Adding " + user.getName() + "...");
        new FollowService().followUser(user, authToken, this);
    }

    @Override
    public void unFollowSuccess()
    {
        view.enableFollowButton(true);
        view.onUnfollow();
    }

    @Override
    public void unFollowFailure(String message)
    {
        view.enableFollowButton(true);
        view.displayErrorMessage("Failed to unfollow: " + message);
    }

    @Override
    public void unFollowException(Exception ex)
    {
        view.enableFollowButton(true);
        view.displayErrorMessage("Failed to unfollow because of exception: " + ex.getMessage());
    }

    public void unFollowUser()
    {
        view.enableFollowButton(false);
        view.displayInfoMessage("Removing " + user.getName() + "...");
        new FollowService().unFollowUser(user, authToken, this);
    }


    //Follow
    //Check following
    // Follower + following count

    //Status
    //Post status
}
