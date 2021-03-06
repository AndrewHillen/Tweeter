package edu.byu.cs.tweeter.client.presenter;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.views.PresenterView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends BasePresenter<MainPresenter.View>
{

    private class FollowObserver implements FollowService.FollowObserver
    {
        @Override
        public void handleSuccess()
        {
            view.enableFollowButton(true);
            view.onFollow();
        }

        @Override
        public void handleFailure(String message)
        {
            view.enableFollowButton(true);
            view.displayErrorMessage(message);
        }

        @Override
        public void handleException(Exception ex)
        {
            view.enableFollowButton(true);
            view.displayErrorMessage("Failed to follow because of exception: " + ex.getMessage());
        }

    }

    private class UnFollowObserver implements FollowService.UnFollowObserver
    {
        @Override
        public void handleSuccess()
        {
            view.enableFollowButton(true);
            view.onUnfollow();
        }

        @Override
        public void handleFailure(String message)
        {
            view.enableFollowButton(true);
            view.displayErrorMessage("Failed to unfollow: " + message);
        }

        @Override
        public void handleException(Exception ex)
        {
            view.enableFollowButton(true);
            view.displayErrorMessage("Failed to unfollow because of exception: " + ex.getMessage());
        }
    }

    private class LogoutObserver implements UserService.LogoutObserver
    {
        @Override
        public void handleSuccess()
        {
            view.logout();
        }

        @Override
        public void handleFailure(String message)
        {
            view.displayErrorMessage(message);
        }

        @Override
        public void handleException(Exception ex)
        {
            view.displayErrorMessage("Failed to logout because of exception: " + ex.getMessage());
        }
    }

    private class PostStatusObserver implements StatusService.PostStatusObserver
    {
        @Override
        public void handleSuccess()
        {
            view.onStatusPost("Successfully Posted!");
        }

        @Override
        public void handleFailure(String message)
        {
            view.displayErrorMessage(message);
        }

        @Override
        public void handleException(Exception ex)
        {
            view.displayErrorMessage("Failed to post status because of exception: " + ex.getMessage());
        }
    }

    private class FollowingCountObserver implements FollowService.FollowingCountObserver
    {
        @Override
        public void handleSuccess(int count)
        {
            view.setFollowingCount(count);
        }

        @Override
        public void handleFailure(String message)
        {
            view.displayErrorMessage(message);
        }

        @Override
        public void handleException(Exception ex)
        {
            view.displayErrorMessage("Failed to get following count because of exception: " + ex.getMessage());
        }
    }

    private class FollowerCountObserver implements FollowService.FollowerCountObserver
    {
        @Override
        public void handleSuccess(int count)
        {
            view.setFollowerCount(count);
        }

        @Override
        public void handleFailure(String message)
        {
            view.displayErrorMessage(message);
        }

        @Override
        public void handleException(Exception ex)
        {
            view.displayErrorMessage("Failed to get follower count because of exception: " + ex.getMessage());
        }
    }

    private class CheckFollowObserver implements FollowService.CheckFollowObserver
    {
        @Override
        public void handleSuccess(boolean isFollower)
        {
            view.setIsFollower(isFollower);
        }

        @Override
        public void handleFailure(String message)
        {
            view.displayErrorMessage(message);
        }

        @Override
        public void handleException(Exception ex)
        {
            view.displayErrorMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }

    private FollowObserver followObserver = new FollowObserver();
    private UnFollowObserver unFollowObserver= new UnFollowObserver();
    private LogoutObserver logoutObserver = new LogoutObserver();
    private PostStatusObserver postStatusObserver = new PostStatusObserver();
    private FollowingCountObserver followingCountObserver = new FollowingCountObserver();
    private FollowerCountObserver followerCountObserver = new FollowerCountObserver();
    private CheckFollowObserver checkFollowObserver = new CheckFollowObserver();

    public interface View extends PresenterView
    {
        void logout();

        void setFollowingCount(int count);
        void setFollowerCount(int count);
        void setIsFollower(boolean isFollower);

        void hideFollowButton(boolean hide);
        void enableFollowButton(boolean enabled);
        void onFollow();
        void onUnfollow();

        void onStatusPost(String message);
    }

    private AuthToken authToken;
    private User targetUser;
    private User loggedInUser;

    private boolean isUser;

    public MainPresenter(View view, AuthToken authToken, User targetUser, User loggedInUser)
    {
        super(view);
        this.authToken = authToken;
        this.targetUser = targetUser;
        this.loggedInUser = loggedInUser;
        checkFollowVisibility();
    }

    public void logout()
    {
        view.displayInfoMessage("Logging Out...");
        new UserService().logout(authToken, loggedInUser.getAlias(), logoutObserver);
    }

    // Follow User

    public void checkFollowVisibility()
    {
        boolean hideButton = targetUser.compareTo(loggedInUser) == 0;
        view.hideFollowButton(hideButton);
    }



    public void followUser()
    {
        view.enableFollowButton(false);
        view.displayInfoMessage("Adding " + targetUser.getName() + "...");
        new FollowService().followUser(loggedInUser, targetUser, authToken, followObserver);
    }

    public void unFollowUser()
    {
        view.enableFollowButton(false);
        view.displayInfoMessage("Removing " + targetUser.getName() + "...");
        new FollowService().unFollowUser(loggedInUser, targetUser, authToken,  unFollowObserver);
    }

    //Check following ----------------------------------------------------

    public void checkFollower()
    {
        isUser = targetUser.compareTo(loggedInUser) == 0;
        if(!isUser)
        {
            new FollowService().checkFollower(authToken, loggedInUser, targetUser, checkFollowObserver);
        }
    }

    public void getFollowingCount()
    {
        new FollowService().getFollowingCount(authToken, targetUser, followingCountObserver);
    }

    public void getFollowerCount()
    {
        new FollowService().getFollowerCount(authToken, targetUser, followerCountObserver);
    }

    //Status
    //Post status

    public void postStatus(String post)
    {
        try
        {
            view.displayInfoMessage("Posting Status...");
            Status newStatus = new Status(post, loggedInUser, getFormattedDateTime(), parseURLs(post), parseMentions(post));
            getStatusService().postStatus(authToken, newStatus, postStatusObserver);
        }
        catch(Exception ex)
        {
            view.displayErrorMessage("Failed to post status because of exception: " + ex.getMessage());
        }
    }

    public StatusService getStatusService()
    {
        return new StatusService();
    }

    private String getFormattedDateTime() throws ParseException
    {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    private List<String> parseURLs(String post) throws MalformedURLException
    {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    private List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    private int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }
}
