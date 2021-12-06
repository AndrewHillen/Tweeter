package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.presenter.views.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T extends PagedView, U> extends BasePresenter<T>
{
    public PagedPresenter(T view, AuthToken authToken, User user)
    {
        super(view);
        this.authToken = authToken;
        this.user = user;
    }

    protected static final int PAGE_SIZE = 10;
    protected AuthToken authToken;
    protected User user;
    protected U lastItem;
    protected boolean hasMorePages = true;
    protected boolean isLoading = false;
    private GetUserObserver getUserObserver = new GetUserObserver();
    protected PagedObserver pagedObserver = new PagedObserver();




    public void loadMoreItems()
    {
        if(!isLoading && hasMorePages)
        {
            isLoading = true;
            view.setLoading(true);
            getItems(pagedObserver);
        }
    }

    public void goToUser(String alias)
    {
        view.displayInfoMessage("Getting user's profile...");
        new UserService().getUser(authToken, alias, getUserObserver);
    }

    public abstract void getItems(PagedObserver observer);

    private class GetUserObserver implements UserService.GetUserObserver
    {
        @Override
        public void handleSuccess(User user)
        {
            view.navigateToUser(user);
        }

        @Override
        public void handleFailure(String message)
        {
            view.displayErrorMessage(message);
        }

        @Override
        public void handleException(Exception ex)
        {
            view.displayErrorMessage("Failed to get user because of exception: " + ex.getMessage());
            //Do Exception stuff
        }
    }

    protected class PagedObserver implements edu.byu.cs.tweeter.client.model.service.observer.PagedObserver<U>
    {

        @Override
        public void handleSuccess(List<U> items, boolean fHasMorePages, U fLastItem)
        {
            lastItem = fLastItem;
            isLoading = false;
            view.setLoading(false);
            view.addItems(items);
            hasMorePages = fHasMorePages;
        }

        @Override
        public void handleFailure(String message)
        {
            isLoading = false;
            view.setLoading(false);
            view.displayErrorMessage(message);
        }

        @Override
        public void handleException(Exception ex)
        {
            isLoading = false;
            view.setLoading(false);
            // Do exception stuff
        }
    }



}
