package edu.byu.cs.tweeter.client.presenter.views;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticateView extends PresenterView
{
    void authenticate(User user, AuthToken authToken);
    void clearErrorMessage();
}
