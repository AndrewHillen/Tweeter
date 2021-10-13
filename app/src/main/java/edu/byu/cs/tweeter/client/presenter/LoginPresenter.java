package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.views.AuthenticateView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends AuthenticatePresenter<LoginPresenter.View>
{

//    private class LoginObserver implements UserService.LoginObserver
//    {
//        @Override
//        public void handleSuccess(User user, AuthToken authToken)
//        {
//            view.displayInfoMessage("Hello " + user.getName());
//            view.authenticate(user, authToken);
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
//            //Do exception stuff
//        }
//    }
//
//    private LoginObserver loginObserver = new LoginObserver();

    public interface View extends AuthenticateView
    {
    }

    public LoginPresenter(View view)
    {
        super(view);
    }

    public void login(String alias, String password)
    {
        view.clearErrorMessage();
        view.displayInfoMessage("Logging In...");
        try
        {
            validateLogin(alias, password);
            new UserService().login(alias, password, authenticateObserver);
        }
        catch(Exception ex)
        {
            view.displayErrorMessage(ex.getMessage());
        }
    }


    public void validateLogin(String alias, String password) {
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }
}
