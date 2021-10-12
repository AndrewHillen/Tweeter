package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.views.PresenterView;

public class BasePresenter<T extends PresenterView>
{
    protected T view;

    protected BasePresenter(T view)
    {
        this.view = view;
    }
}
