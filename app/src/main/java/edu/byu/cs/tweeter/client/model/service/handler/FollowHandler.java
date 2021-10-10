package edu.byu.cs.tweeter.client.model.service.handler;

import edu.byu.cs.tweeter.client.model.service.observer.FollowObserver;

public class FollowHandler extends SimpleNotificationHandler<FollowObserver>
{
    protected FollowHandler(FollowObserver observer)
    {
        super(observer);
    }

    @Override
    protected String getFailurePrefix()
    {
        return "Failed to follow";
    }
}
