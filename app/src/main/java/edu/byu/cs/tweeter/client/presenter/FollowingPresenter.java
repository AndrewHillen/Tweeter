package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.views.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<FollowingPresenter.View, User>
{

    @Override
    public void getItems(PagedObserver observer)
    {
        new FollowService().getFollowing(authToken, user, PAGE_SIZE, lastItem, observer);
    }

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
//            //Do Exception stuff
//        }
//    }

//    private class GetFollowingObserver extends PagedObserver implements FollowService.GetFollowingObserver
//    {
//    }
//
//    private GetUserObserver getUserObserver = new GetUserObserver();
//    private GetFollowingObserver getFollowingObserver = new GetFollowingObserver();
    public interface View extends PagedView
    {
    }

//    private View view;
//    private AuthToken authToken;
//    private User user;
//    private User lastFollowee;
//    private boolean isLoading = false;
//
//    private boolean hasMorePages = true;
//
//    private static final int PAGE_SIZE = 10;

    public FollowingPresenter(View view, AuthToken authToken, User user)
    {
        super(view, authToken, user);
    }


//    public FollowingPresenter(View view, AuthToken authToken, User user)
//    {
//        super(view);
//        this.view = view;
//        this.authToken = authToken;
//        this.user = user;
//    }

//    public void loadMoreItems()
//    {
//        if(!isLoading && hasMorePages)
//        {
//            isLoading = true;
//            view.setLoading(true);
//
//        }
//    }

//    public void goToUser(String alias)
//    {
//        view.displayInfoMessage("Getting user's profile...");
//        new UserService().getUser(authToken, alias, getUserObserver);
//    }

    // FollowService overrides--------------------------------------



    // UserService overrides ----------------------------------
}
