package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.presenter.views.AuthenticateView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticatePresenter<T extends AuthenticateView> extends BasePresenter<T>
{
    public AuthenticatePresenter(T view)
    {
        super(view);
    }

    private class AuthenticateObserver implements edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver
    {
        @Override
        public void handleSuccess(User user, AuthToken authToken)
        {
            view.displayInfoMessage("Hello " + user.getName());
            view.authenticate(user, authToken);
        }

        @Override
        public void handleFailure(String message)
        {
            view.displayErrorMessage(message);
        }

        @Override
        public void handleException(Exception ex)
        {
            //Do exception stuff
            view.displayErrorMessage("Failed to authenticate because of exception: " +ex.getMessage());
        }
    }

    protected AuthenticateObserver authenticateObserver = new AuthenticateObserver();
}
