package edu.byu.cs.tweeter.client.presenter.views;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public interface PagedView extends PresenterView
{
    <T> void addItems(List<T> items);
    void setLoading(boolean isLoading);
    void navigateToUser(User user);
}
